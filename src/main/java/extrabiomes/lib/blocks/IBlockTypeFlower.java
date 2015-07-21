package extrabiomes.lib.blocks;

import net.minecraft.util.IIcon;

public interface IBlockTypeFlower {
	public int getMeta();
	public int getWeight();
	public int getColor();
	public String getTexture();
		
	public String name();
			
	public void setIcon(IIcon icon);
	public IIcon getIcon();
}
