package extrabiomes.core;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;

import com.google.common.base.Optional;

import cpw.mods.fml.common.IWorldGenerator;
import extrabiomes.lib.BiomeUtils;
import extrabiomes.lib.IEBXSMod;
import extrabiomes.lib.IEBXSSubMod;
import extrabiomes.lib.settings.BiomeSettings;
import extrabiomes.lib.worldgen.ExtrabiomeGenBase;

public enum BiomeHandler {
	INSTANCE;
	
	public static void init() throws Exception {
		Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
		while( mods.hasNext() ) {
			final IEBXSSubMod submod = (IEBXSSubMod)(mods.next());
			final List<BiomeSettings> settingsList = submod.getBiomeSettings();
			for( final BiomeSettings settings : settingsList ) {
				if( settings.isEnabled() && settings.getID() > 0 ) {
					BiomeUtils.createBiome(settings);
					if( settings.getBiome().isPresent() ) {
						postLoad(settings);
					} else {
						Core.LOGGER.warn("Unable to create biome class for %s.", settings);
					}
				}
			}
		}
		
		// TODO: fire api event to announce biome handler initialization
	}
	
	// final cleanup on a biome once it is loaded
	public static void postLoad(BiomeSettings settings) {
		ExtrabiomeGenBase biome = settings.getBiome().get();
		
		Core.LOGGER.debug("Registering %s with dictionary.", settings);
		BiomeDictionary.registerBiomeType(biome, biome.getTypeFlags());
		
		// now add ourselves to the biome manager
		BiomeManager.BiomeEntry entry = new BiomeManager.BiomeEntry(biome, settings.getWeight());
		Core.LOGGER.info("BiomeEntry: %s, weight: %d, temp: %f, rain: %f", biome.biomeName, entry.itemWeight, biome.temperature, biome.rainfall);
		if( biome.temperature > 0.5f ) {
			if( biome.rainfall < 0.25f ) {
				BiomeManager.addBiome(BiomeType.DESERT, entry);
			} else {
				BiomeManager.addBiome(BiomeType.WARM, entry);
			}
		} else if( biome.temperature > 0.25 ) {
			BiomeManager.addBiome(BiomeType.COOL, entry);
		} else {
			BiomeManager.addBiome(BiomeType.ICY, entry);
		}
	}

	// enable the biomes for generation now that they are created
	public static void enableBiomes() {
		final Set<WorldType> worldTypes = BiomeUtils.discoverWorldTypes();
		Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
		while( mods.hasNext() ) {
			final IEBXSSubMod submod = (IEBXSSubMod)(mods.next());
			final List<BiomeSettings> settingsList = submod.getBiomeSettings();
			for( final BiomeSettings settings : settingsList ) {
				final Optional<? extends BiomeGenBase> biomeOpt = settings.getBiome();
				if( settings.isEnabled() && biomeOpt.isPresent() ) {
					final BiomeGenBase biome = biomeOpt.get();
					if( settings.allowSpawn() )
						BiomeManager.addSpawnBiome(biome);
					if( settings.allowStronghold() )
						BiomeManager.addStrongholdBiome(biome);
					if( settings.allowVillages() )
						BiomeManager.addVillageBiome(biome, true);
				} else {
					Core.LOGGER.debug("Biome %s disabled.", settings);
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
		
	public static void registerWorldGenerators() {
		Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
		while( mods.hasNext() ) {
			final IEBXSSubMod submod = (IEBXSSubMod)(mods.next());
			final List<Class <?extends IWorldGenerator>> generators = submod.getWorldGenerators();
			for( final Class<?extends IWorldGenerator> clazz : generators ) {
				try {
					final IWorldGenerator generator = clazz.newInstance();
					BiomeRegistry.addWorldGenerator( generator );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// TODO: register any worldgen defined in Core
	}
}
