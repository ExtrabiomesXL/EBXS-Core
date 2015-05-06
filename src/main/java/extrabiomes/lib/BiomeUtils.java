package extrabiomes.lib;

import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.base.Optional;

import extrabiomes.lib.event.*;

public abstract class BiomeUtils {
	
	// create a new instance of the biome class specified in the settings object
	public static void createBiome(BiomeSettings settings) throws Exception {
		if( settings.getBiomeClass().isPresent() && !settings.getBiome().isPresent() ) {
			final Optional<?extends BiomeGenBase> biome = Optional.of(settings.getBiomeClass().get().newInstance());
			settings.setBiome(biome);
		}
	}
	
	// register a child mod
	public static void register(IEBXSMod mod, String apiVersion) {
		EBXSBus.raise(new RegisterEvent(mod, apiVersion));
	}
}
