package extrabiomes.core.stuff;

import java.util.List;

import com.google.common.collect.Lists;

import extrabiomes.lib.settings.BlockSettings;

public enum BlockCollection {
	EELGRASS;
	
	public final BlockSettings settings;
	
	private BlockCollection() {
		settings = new BlockSettings(this.name().toLowerCase());
	}
	
	public static final List<BlockSettings> allSettings = Lists.newArrayList();
	public static void init() {
		if( allSettings.isEmpty() ) {
			for( BlockCollection coll : BlockCollection.values() ) {
				allSettings.add(coll.settings);
			}
		}
	}
}
