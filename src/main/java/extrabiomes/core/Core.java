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
import extrabiomes.lib.ModBase;
import extrabiomes.lib.event.EBXSBus;
import extrabiomes.lib.event.EBXSEvent;
import extrabiomes.lib.event.IEBXSHandler;
import extrabiomes.lib.event.RegisterEvent;

import java.io.File;
import java.util.Iterator;

@Mod(modid = Version.MOD_ID, name = Version.MOD_NAME, version = Version.VERSION, dependencies = "")
public class Core extends ModBase
{
    public Core() {
		super(Version.MOD_ID);
	}

	@Instance(Version.MOD_ID)
    public static Core instance;

    static final Logger  LOGGER  = LogManager.getFormatterLogger(Version.MOD_ID);
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	basePreInit(event);
        EBXSBus.INSTANCE.init(new Handler());
        
        /// BiomeHandler.init();
        
        Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
        while( mods.hasNext() ) {
        	mods.next().ebxsPreInit();
        }
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
        
        Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
        while( mods.hasNext() ) {
        	mods.next().ebxsInit();
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	// TODO: activate plugins
    	// TODO: initialize recipes
    	
        Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
        while( mods.hasNext() ) {
        	mods.next().ebxsPostInit();
        }
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
