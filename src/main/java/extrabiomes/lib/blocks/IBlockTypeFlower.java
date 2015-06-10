package extrabiomes.lib.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockTypeFlower {
	public int getMeta();
	public int getWeight();
	public int getColor();
		
	public String name();
			
	@SideOnly(Side.CLIENT)
	public IIcon registerIcon(IIconRegister iconRegister);
	
	public IIcon getIcon();
}
