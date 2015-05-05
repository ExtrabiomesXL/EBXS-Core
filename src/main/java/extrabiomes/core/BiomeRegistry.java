package extrabiomes.core;

import java.util.HashSet;

import com.google.common.collect.Sets;

import extrabiomes.lib.IEBXSMod;

public enum BiomeRegistry {
	INSTANCE;
	
	private HashSet<IEBXSMod> mods;
	
	private BiomeRegistry() {
		mods = Sets.newHashSet();
	}
	
	public static boolean register(IEBXSMod mod) {
		return INSTANCE.mods.add(mod);
	}
}
