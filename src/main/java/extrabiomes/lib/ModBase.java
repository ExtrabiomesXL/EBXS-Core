package extrabiomes.lib;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

public abstract class ModBase {
    public static final Minecraft MC	= Minecraft.getMinecraft();
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
}
