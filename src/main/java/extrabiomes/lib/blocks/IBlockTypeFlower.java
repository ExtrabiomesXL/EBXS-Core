package extrabiomes.lib.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockTypeFlower {
	public final int	metadata	= 0;
	public final int	weight		= 1;
	public final int	color		= 0;
	
	public final String	texture		= "";
	
	public String name();
			
	@SideOnly(Side.CLIENT)
	public IIcon registerIcon(IIconRegister iconRegister);
	
	public IIcon getIcon();
}
