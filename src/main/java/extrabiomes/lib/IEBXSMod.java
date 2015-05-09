package extrabiomes.lib;

public interface IEBXSMod {
	// Init hooks to be called by core on registered mods
	public void ebxsPreInit();
	public void ebxsInit();
	public void ebxsPostInit();
}
