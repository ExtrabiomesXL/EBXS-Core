package extrabiomes.core;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import extrabiomes.lib.BiomeSettings;
import extrabiomes.lib.BiomeUtils;
import extrabiomes.lib.IEBXSMod;

public enum BiomeHandler {
	INSTANCE;
	
	public static void registerWorldGenerators() {
		Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
		while( mods.hasNext() ) {
			final List<Class <?extends IWorldGenerator>> generators = mods.next().getWorldGenerators();
			for( final Class<?extends IWorldGenerator> clazz : generators ) {
				try {
					final IWorldGenerator generator = clazz.newInstance();
					BiomeRegistry.addWorldGenerator( generator );
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// TODO: register any worldgen defined in Core
	}
	
	public static void enableBiomes() {
		final Set<WorldType> worldTypes = BiomeUtils.discoverWorldTypes();
		Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
		while( mods.hasNext() ) {
			final List<BiomeSettings> settingsList = mods.next().getBiomeSettings();
			for( final BiomeSettings settings : settingsList ) {
				final Optional<? extends BiomeGenBase> biomeOpt = settings.getBiome();
				if( settings.isEnabled() && biomeOpt.isPresent() ) {
					final BiomeGenBase biome = biomeOpt.get();
					BiomeRegistry.addBiome(worldTypes, biome);
					if( settings.allowSpawn() )
						BiomeManager.addSpawnBiome(biome);
					if( settings.allowStronghold() )
						BiomeManager.addStrongholdBiome(biome);
					if( settings.allowVillages() )
						BiomeManager.addVillageBiome(biome, true);
				} else {
					Core.LOGGER.debug("Biome %s disabled.", settings.toString());
				}
			}
		}
		
		// TODO: add mechanic to Core for disabling vanilla biomes
		/*
		if( vanilla && biome.disabled ) {
			BiomeRegistry.removeBiome(biome);
		}
		*/
	}
	
}
