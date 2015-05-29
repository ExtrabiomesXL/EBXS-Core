package extrabiomes.core;

import net.minecraftforge.common.config.Configuration;
import extrabiomes.core.stuff.BlockCollection;
import extrabiomes.core.stuff.ItemCollection;
import extrabiomes.lib.settings.BlockSettings;
import extrabiomes.lib.settings.ItemSettings;

public class ConfigurationHandler {

	// Load all of our config settings
	public static void init(Configuration config) {
		
		for( final ItemSettings settings : ItemCollection.allSettings ) {
			settings.load(config);
		}
		
		for( final BlockSettings settings : BlockCollection.allSettings ) {
			settings.load(config);
		}
		
		config.save();
	}
	
}
