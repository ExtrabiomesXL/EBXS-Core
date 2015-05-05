package extrabiomes.lib;

import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.base.Optional;

public class BiomeUtils {
	// create a new instance of the biome class specified in the settings object
	public void createBiome(BiomeSettings settings) throws Exception {
		if( settings.getBiomeClass().isPresent() && !settings.getBiome().isPresent() ) {
			final Optional<?extends BiomeGenBase> biome = Optional.of(settings.getBiomeClass().get().newInstance());
			settings.setBiome(biome);
		}
	}
}
