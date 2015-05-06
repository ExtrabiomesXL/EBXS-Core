package extrabiomes.core;

import java.util.HashSet;
import java.util.Iterator;

import com.google.common.collect.Sets;

import extrabiomes.lib.Const;
import extrabiomes.lib.IEBXSMod;

public enum BiomeRegistry {
	INSTANCE;
	
	private HashSet<IEBXSMod> mods;
	
	private BiomeRegistry() {
		mods = Sets.newHashSet();
	}
	
	public static boolean register(IEBXSMod mod, String apiVersion) {
		final String modName = mod.getClass().getSimpleName();
		Core.LOGGER.info("Got registration request from %s.", modName);
		if( !apiVersion.equals(Const.API_VERSION) ) {
			Core.LOGGER.warn("-- %s is running mismatched EBXS api version %s (%s expected)",
					modName, apiVersion, Const.API_VERSION);
		}
		return INSTANCE.mods.add(mod);
	}
	
	public static Iterator<IEBXSMod> iterator() {
		return INSTANCE.mods.iterator();
	}
}
