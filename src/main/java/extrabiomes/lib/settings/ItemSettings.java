package extrabiomes.lib.settings;

import extrabiomes.lib.Const;
import extrabiomes.lib.event.EBXSBus;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ItemSettings {
	public final String name;
	
	private boolean enabled;
	
	public ItemSettings(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
		
	@Override
	public String toString() {
		return name;
	}
	
	private String keyEnabled() {
		return toString() + ".enabled";
	}
	
	public void load(Configuration config) {
		Property property;
		
		property = config.get(Const.CATEGORY_ITEM, keyEnabled(), enabled);
		enabled = property.getBoolean(false);
	}
}
