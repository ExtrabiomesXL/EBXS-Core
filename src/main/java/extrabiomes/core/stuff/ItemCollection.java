package extrabiomes.core.stuff;

import java.util.List;

import com.google.common.collect.Lists;

import extrabiomes.lib.settings.BlockSettings;
import extrabiomes.lib.settings.ItemSettings;

public enum ItemCollection {
	DYES;
	
	public final ItemSettings settings;
	
	private ItemCollection() {
		settings = new ItemSettings(this.name().toLowerCase());
	}
	
	public static final List<ItemSettings> allSettings = Lists.newArrayList();
	public static void init() {
		if( allSettings.isEmpty() ) {
			for( ItemCollection coll : ItemCollection.values() ) {
				allSettings.add(coll.settings);
			}
		}
	}
}
