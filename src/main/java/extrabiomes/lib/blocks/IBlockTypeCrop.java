package extrabiomes.lib.blocks;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IBlockTypeCrop {

	public enum CropType {
		NORMAL, REGROW;
	}
	
	public String name();
	
	public IIcon getStageIcon(int stage);
	public void setStageIcons(ArrayList<IIcon> icons);
	
	public ItemStack getSeedItem();
	public Item getCropItem();

	public void setSeedItem(ItemStack seed);
	public void setCropItem(Item crop);
    
	public int getRenderType();
	
	public CropType getCropType();
	
}
