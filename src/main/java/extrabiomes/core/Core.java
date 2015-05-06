package extrabiomes.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extrabiomes.lib.Const;
import extrabiomes.lib.IEBXSMod;
import extrabiomes.lib.event.EBXSBus;
import extrabiomes.lib.event.EBXSEvent;
import extrabiomes.lib.event.IEBXSHandler;
import extrabiomes.lib.event.RegisterEvent;

import java.io.File;

@Mod(modid = Version.MOD_ID, name = Version.MOD_NAME, version = Version.VERSION, dependencies = "")
public class Core
{
    static final Minecraft MC = Minecraft.getMinecraft();
    
    @Instance(Version.MOD_ID)
    public static Core instance;

    static final Logger  LOGGER  = LogManager.getFormatterLogger(Version.MOD_ID);
    static final Boolean DEV     = Boolean.parseBoolean( System.getProperty("development", "false") );

    static File          BaseDir;
    static Configuration Config;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        BaseDir = new File(event.getModConfigurationDirectory(), Const.PREFIX_LONG);
        if ( !BaseDir.exists() )
            BaseDir.mkdir();

        Config  = new Configuration( new File(BaseDir, getClass().getSimpleName().toLowerCase() + ".cfg") );
        
        EBXSBus.INSTANCE.init(new Handler());
        
        /// BiomeHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	// TODO: register vanilla event handler
        //MinecraftForge.EVENT_BUS.register(this);
        //FMLCommonHandler.instance().bus().register(this);
        //LOGGER.debug("Registered events");
    	
    	/// BiomeHandler.registerWorldGenerators();
    	/// BiomeHandler.enableBiomes();

        LOGGER.info("Loaded version %s", Version.VERSION);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	// TODO: activate plugins
    	// TODO: initialize recipes
    }
    
    // Wrapper for EBXS Event handling
    private class Handler implements IEBXSHandler {
	    public Logger log() {
	    	return LOGGER;
	    }
	    
	    // Semi-terrible stub handler implementation for now
	    public void receive(EBXSEvent event) {
	    	switch( event.type ) {
	    		case "register":
	    			BiomeRegistry.register((IEBXSMod)event.data, ((RegisterEvent)event).apiVersion);
	    			break;
	    		default:
	    			log().warn("Got unsupported event of type "+event.type);
	    	}
	    }
    }

}
