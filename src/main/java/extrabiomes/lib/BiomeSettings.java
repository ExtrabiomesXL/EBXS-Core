package extrabiomes.lib;

import java.util.Locale;

import com.google.common.base.Optional;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeSettings {
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
	
	public BiomeSettings(int defaultID, Class<? extends BiomeGenBase> biomeClass) {
		this.defaultID = defaultID;
		this.biomeID = this.defaultID;
		this.biomeClass = Optional.fromNullable(biomeClass);
	}

	@Override
	public String toString() {
		// TODO: embed a sensible name
		return super.toString().toLowerCase(Locale.ENGLISH);
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
}
