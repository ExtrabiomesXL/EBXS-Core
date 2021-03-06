package extrabiomes.lib;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ModBase implements IEBXSMod {
    public static final Boolean   DEV   = Boolean.parseBoolean( System.getProperty("development", "false") );

    public static Logger          LOGGER;
    public static File            BaseDir;
    public static Configuration   Config;
    
    protected ModBase(String modId) {
    	LOGGER = LogManager.getFormatterLogger(modId);
    	LOGGER.info("ModBase initialized for "+modId);
    }
    
    protected void basePreInit(FMLPreInitializationEvent event) {
        BaseDir = new File(event.getModConfigurationDirectory(), Const.PREFIX_LONG);
        if ( !BaseDir.exists() )
            BaseDir.mkdir();

        Config  = new Configuration( new File(BaseDir, getClass().getSimpleName().toLowerCase() + ".cfg") );
    }
    
    // terrible init hook race condition management
    private boolean ranPreInit = false;
	public void ebxsPreInit() {
		ranPreInit = true;
	}
	public void ebxsInit() {
		if( !ranPreInit ) ebxsPreInit();
	}
	public void ebxsPostInit() {}
	
	@SideOnly(Side.CLIENT)
	public abstract IIcon registerIcon(IIconRegister iconRegister, String texture);
}
