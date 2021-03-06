package extrabiomes.core;

import java.util.Iterator;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extrabiomes.core.handlers.BlockHandler;
import extrabiomes.core.handlers.ConfigurationHandler;
import extrabiomes.core.handlers.ItemHandler;
import extrabiomes.core.handlers.RecipeHandler;
import extrabiomes.core.stuff.BlockCollection;
import extrabiomes.core.stuff.ItemCollection;
import extrabiomes.lib.IEBXSMod;
import extrabiomes.lib.ModBase;
import extrabiomes.lib.event.EBXSBus;
import extrabiomes.lib.event.EBXSEvent;
import extrabiomes.lib.event.IEBXSHandler;
import extrabiomes.lib.event.RegisterEvent;

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
        
        BlockCollection.init();
        ItemCollection.init();
        ConfigurationHandler.init(Config);
        
        Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
        while( mods.hasNext() ) {
        	mods.next().ebxsPreInit();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	// block/item handlers
    	ItemHandler.init();
    	BlockHandler.init();
    	
    	// spin up our biome handler
        try {
			BiomeHandler.init();
	    	BiomeHandler.registerWorldGenerators();
	    	BiomeHandler.enableBiomes();
		} catch (Exception e) {
			LOGGER.catching(e);
		}

        LOGGER.info("Loaded version %s", Version.VERSION);
        
        Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
        while( mods.hasNext() ) {
        	mods.next().ebxsInit();
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	// TODO: initialize plugins

    	RecipeHandler.init();
    	
        Iterator<IEBXSMod> mods = BiomeRegistry.iterator();
        while( mods.hasNext() ) {
        	mods.next().ebxsPostInit();
        }
        
        // TODO: handle crossgen requests
        
        if( DEV ) {
	        // dump the full list of registered biomes for debug purposes
	        BiomeRegistry.dump();
        }
    }
    
	@SideOnly(Side.CLIENT)
	public IIcon registerIcon(IIconRegister iconRegister, String texture) {
		IIcon icon = iconRegister.registerIcon(Version.TEXTURE_PATH + texture);
		return icon;
	}
    
    // Wrapper for EBXS Event handling
    // TODO: Break this horrible subclass out, possibly into multiple parts
    private class Handler implements IEBXSHandler {
	    public Logger log() {
	    	return LOGGER;
	    }
	    
	    // Semi-terrible stub handler implementation for now
	    public void receive(EBXSEvent event) {
    		if( event.type.equals("register") ) {
    			BiomeRegistry.register((IEBXSMod)event.data, ((RegisterEvent)event).apiVersion);
    		} else {
    			log().error("Got unsupported event of type %s.", event.type);
    		}
	    }

		// return the requested integer
		public int queryInt(String query_type, Object param) {
			if( query_type.equals(EBXSBus.QUERY_BIOME_ID) ) {
				if( param instanceof Integer ) {
					return BiomeRegistry.getBiomeID(((Integer)param).intValue());
				} else {
					log().warn("%s requires Integer parameter.", query_type);
				}
			} else {
				log().error("Got unsupported queryInt of type %s.", query_type);
			}
			return -1;
		}
    }

}
