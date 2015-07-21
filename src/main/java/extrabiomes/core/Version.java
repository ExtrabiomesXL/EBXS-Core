package extrabiomes.core;

import java.util.Locale;

import extrabiomes.lib.Const;

public abstract class Version {
	public static final String	MOD_ID			= Const.API_MOD_ID;
	public static final String	MOD_NAME		= "Extrabiomes Core";
	public static final String	VERSION			= "@VERSION@";
	public static final String	API_VERSION		= Const.API_VERSION;

	public static final String	CHANNEL			= MOD_ID;
	public static final String	TEXTURE_PATH	= MOD_ID.toLowerCase(Locale.ENGLISH) + ":";
}
