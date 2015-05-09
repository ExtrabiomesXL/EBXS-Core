package extrabiomes.lib;

import java.util.List;

import cpw.mods.fml.common.IWorldGenerator;
import extrabiomes.lib.settings.BiomeSettings;

public interface IEBXSSubMod extends IEBXSMod {	
	// Return a list of all BiomeSettings provided by this mod
	public List<BiomeSettings> getBiomeSettings();
	
	// Return a list of all IWorldGenerators provided by this mod
	public List<Class<? extends IWorldGenerator>> getWorldGenerators();

}
