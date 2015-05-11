package extrabiomes.lib;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ExtraItem extends ItemBlock {
	public ExtraItem(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
	private Block getBlock() {
		return this.field_150939_a;
	}
	
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int junk) {
		return getBlock().getRenderColor(itemStack.getItemDamage());
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata) {
		return getBlock().getIcon(0, metadata);
	}
	
	@SideOnly(Side.CLIENT)
	public String getUnlocalizedName(ItemStack itemstack) {
		final String blockName = ((IExtraBlock)getBlock()).getUnlocalizedName(itemstack.getItemDamage());
		return super.getUnlocalizedName(itemstack) + "." + itemstack.getItemDamage();
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
