package extrabiomes.lib;

import java.util.Locale;

import com.google.common.base.Optional;

import extrabiomes.lib.event.EBXSBus;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class BiomeSettings {
	public final String name;
	
	private final int defaultID;

	private int biomeID = 0;
	private int weight = Weights.NORMAL.value;
	private boolean enabled = true;
	private boolean allowVillages = true;
	private boolean allowSpawn = true;
	private boolean allowStronghold = true;
	
	private final Optional<? extends Class<? extends BiomeGenBase>> biomeClass;
	private Optional<? extends BiomeGenBase> biome = Optional.absent();
	
	public enum Weights {
		NONE(0), LIGHT(5), NORMAL(10), HEAVY(20);

		public int value;

		Weights(int value) {
			this.value = value;
		}
	}
	
	public BiomeSettings(String name, int defaultID, Class<? extends BiomeGenBase> biomeClass) {
		this.name = name;
		this.defaultID = defaultID;
		this.biomeID = this.defaultID;
		this.biomeClass = Optional.fromNullable(biomeClass);
	}

	@Override
	public String toString() {
		return name;
	}
	
	public Optional<? extends Class<? extends BiomeGenBase>> getBiomeClass() {
		return biomeClass;
	}

	public Optional<? extends BiomeGenBase> getBiome() {
		return biome;
	}
	public void setBiome(Optional<? extends BiomeGenBase> biome) {
		this.biome = biome;
	}

	public int getID() {
		return biomeID;
	}
	
	public int getWeight() {
		return weight;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean allowVillages() {
		return allowVillages;
	}

	public boolean allowSpawn() {
		return allowSpawn;
	}
	
	public boolean allowStronghold() {
		return allowStronghold;
	}

	////////// ----------------------------------------------------- //////////
	
	private String keyPrefix() {
		// we may wind up with separate prefixes eventually
		return name;
	}
	private String keyID() {
		return keyPrefix() + ".id";
	}
	private String keyEnabled() {
		return keyPrefix() + ".enabled";
	}
	private String keyWeight() {
		return keyPrefix() + ".weight";
	}
	private String keyAllowVillages() {
		return keyPrefix() + ".allow_villages";
	}
	private String keyAllowSpawn() {
		return keyPrefix() + ".allow_spawn";
	}
	private String keyAllowStronghold() {
		return keyPrefix() + ".allow_stronghold";
	}
	
	// populate this settings object from the config file
	public void load(Configuration config) {
		Property property;
		
		property = config.get(Const.CATEGORY_BIOME, keyEnabled(), enabled);
		enabled = property.getBoolean(false);
		
		if( enabled ) {
			property = config.get(Const.CATEGORY_BIOME, keyID(), defaultID);
			biomeID = EBXSBus.queryInt(EBXSBus.QUERY_BIOME_ID, Integer.valueOf(property.getInt()));
			if( biomeID != defaultID ) {
				property.comment = "ID changed to avoid conflict (was "+property.getInt()+")";
				property.set(biomeID);
			}
			
			property = config.get(Const.CATEGORY_BIOME, keyWeight(), weight);
			weight = property.getInt(Weights.NONE.value);
			
			property = config.get(Const.CATEGORY_BIOME, keyAllowVillages(), allowVillages);
			allowVillages = property.getBoolean(false);
			
			property = config.get(Const.CATEGORY_BIOME, keyAllowSpawn(), allowSpawn);
			allowSpawn = property.getBoolean(false);
			
			property = config.get(Const.CATEGORY_BIOME, keyAllowStronghold(), allowStronghold);
			allowStronghold = property.getBoolean(false);
		}
	}
}
