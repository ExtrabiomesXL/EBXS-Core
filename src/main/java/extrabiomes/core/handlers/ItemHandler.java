package extrabiomes.core.handlers;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import extrabiomes.core.Core;
import extrabiomes.core.Version;
import extrabiomes.core.stuff.ItemCollection;
import extrabiomes.lib.items.ItemExtraDye;

public class ItemHandler {
	public static void init() {
		if( ItemCollection.DYES.settings.isEnabled() ) {
			final ItemExtraDye dye = new ItemExtraDye();
			dye.setCreativeTab(CreativeTabs.tabMaterials);
			GameRegistry.registerItem(dye, dye.getUnlocalizedName(), Version.MOD_ID);
			
			dye.init();
		}
	}
}
