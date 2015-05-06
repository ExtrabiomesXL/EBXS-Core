package extrabiomes.lib;

import org.apache.logging.log4j.Logger;

public interface IEBXSMod {
	// Init hooks to be called by core on registered mods
	public void ebxsPreInit();
	public void ebxsInit();
	public void ebxsPostInit();
}
