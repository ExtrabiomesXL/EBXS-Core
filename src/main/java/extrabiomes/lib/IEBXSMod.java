package extrabiomes.lib;

import java.util.List;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.IWorldGenerator;
import extrabiomes.lib.settings.BiomeSettings;

public interface IEBXSMod {
	// Init hooks to be called by core on registered mods
	public void ebxsPreInit();
	public void ebxsInit();
	public void ebxsPostInit();
	
	// Return a list of all BiomeSettings provided by this mod
	public List<BiomeSettings> getBiomeSettings();
	
	// Return a list of all IWorldGenerators provided by this mod
	public List<Class<? extends IWorldGenerator>> getWorldGenerators();
}
