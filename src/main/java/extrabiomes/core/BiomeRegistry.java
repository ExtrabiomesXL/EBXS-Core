package extrabiomes.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.IWorldGenerator;
import extrabiomes.lib.Const;
import extrabiomes.lib.IEBXSMod;

public enum BiomeRegistry {
	INSTANCE;
	
	// every ebxs mod registered with us
	private HashSet<IEBXSMod> mods;
	
	// every biome id requested by a config file we are responsible for
	private final boolean[]   configBiomes = new boolean[BiomeGenBase.getBiomeGenArray().length];
	
	private BiomeRegistry() {
		mods = Sets.newHashSet();
	}
	
	public static boolean register(IEBXSMod mod, String apiVersion) {
		final String modName = mod.getClass().getSimpleName();
		Core.LOGGER.info("Got registration request from %s.", modName);
		if( !apiVersion.equals(Const.API_VERSION) ) {
			Core.LOGGER.error("-- %s is running mismatched EBXS api version %s (%s expected)",
					modName, apiVersion, Const.API_VERSION);
		}
		return INSTANCE.mods.add(mod);
	}
	
	public static Iterator<IEBXSMod> iterator() {
		return INSTANCE.mods.iterator();
	}

	public static void addWorldGenerator(IWorldGenerator generator) {
		// TODO Auto-generated method stub
		
	}

	/// Request a new biome id
	public static int getBiomeID(int defaultID) {
		return INSTANCE._getBiomeID(defaultID);
	}
	private int _getBiomeID(int defaultID) {
		// set to 0 or -1 to "disable"
		if( defaultID < 1 )
			return defaultID;
		
		// check if the id requested is available
		if( BiomeGenBase.getBiomeGenArray()[defaultID] == null && !configBiomes[defaultID]) {
			configBiomes[defaultID] = true;
			return defaultID;
		}
		
		// check for warning case where we've seen this ID before
		if( configBiomes[defaultID] ) {
			Core.LOGGER.warn("CONFLICT @ Biome ID %d.", defaultID);
		}		
		// find an empty biome ID and log it
		for( int k = 0; k < configBiomes.length -1; ++k ) {
			if( BiomeGenBase.getBiomeGenArray()[k] == null && !configBiomes[k] ) {
				Core.LOGGER.debug("Found unclaimed Biome ID %d", k);
				configBiomes[k] = true;
				return k;
			}
		}
		
		throw new RuntimeException("No more Biome ID's available.");
	}
	
	public static void dump() {
		Core.LOGGER.info(":: beginning biome dump ::");
		for( BiomeGenBase biome : BiomeGenBase.getBiomeGenArray() ) {
			if( biome == null ) continue;
			if( INSTANCE.configBiomes[biome.biomeID] )
				Core.LOGGER.info("<%3d>  %s", biome.biomeID, biome.biomeName);
			else
				Core.LOGGER.info(" %3d - %s", biome.biomeID, biome.biomeName);
		}
		Core.LOGGER.info(":: end of biome dump ::");
	}
}
