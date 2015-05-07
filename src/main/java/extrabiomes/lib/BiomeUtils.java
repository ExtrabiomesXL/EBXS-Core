package extrabiomes.lib;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import extrabiomes.lib.event.EBXSBus;
import extrabiomes.lib.event.RegisterEvent;
import extrabiomes.lib.settings.BiomeSettings;

public abstract class BiomeUtils {
	
	private static final Set<WorldType>                        worldTypes   = new HashSet<WorldType>();
	
	// create a new instance of the biome class specified in the settings object
	public static void createBiome(BiomeSettings settings) throws Exception {
		if( settings.getBiomeClass().isPresent() && !settings.getBiome().isPresent() ) {
			if( BiomeGenBase.getBiomeGenArray()[settings.getID()] != null ) {
				throw new IllegalArgumentException(
						String.format("Biome id %d is already in use by %s when adding %s. "+
								"Please review the config file.", 
								settings.getID(), 
								BiomeGenBase.getBiomeGenArray()[settings.getID()].biomeName, 
								settings.toString()
						));
			}
			final Optional<?extends ExtrabiomeGenBase> biome = Optional.of(settings.getBiomeClass().get().newInstance());
			settings.setBiome(biome);
		}
	}
	
	// register a child mod
	public static void register(IEBXSMod mod, String apiVersion) {
		EBXSBus.raise(new RegisterEvent(mod, apiVersion));
	}

	public static Set<WorldType> discoverWorldTypes() {
		if( worldTypes.isEmpty() ) {
			worldTypes.add(WorldType.DEFAULT);
			worldTypes.add(WorldType.LARGE_BIOMES);
			// TODO: raise api discovery event
		}
		return ImmutableSet.copyOf(worldTypes);
	}
}
