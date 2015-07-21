package extrabiomes.lib.blocks;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IBlockTypeCrop {

	public String name();
	
	public IIcon getStageIIcon(int stage);
	public void setStageIIcons(ArrayList<IIcon> IIcons);
	
	public ItemStack getSeedItem();
	public Item getCropItem();

	public void setSeedItem(ItemStack seed);
	public void setCropItem(Item crop);
    
	public int getRenderType();
	
}
