package extrabiomes.lib.blocks;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extrabiomes.lib.ModBase;
import extrabiomes.lib.blocks.IBlockTypeFlower;
import extrabiomes.lib.blocks.IExtraBlock;

public class BlockExtraFlower extends BlockFlower implements IExtraBlock {
	private static final List<BlockExtraFlower>	allFlowers = Lists.newArrayList();
	
	private final ModBase 			parentMod;
	public final IBlockTypeFlower[]	blockTypes;

	// NB: may need to switch to a generic block manually implementing IPlantable
	public BlockExtraFlower(ModBase parentMod, String locPrefix, IBlockTypeFlower[] blockTypes) {
		super(0);
		
		this.parentMod = parentMod;
		this.blockTypes = blockTypes;
		
		setBlockName(locPrefix+".flower");
		setTickRandomly(true);
		setHardness(0.0f);
		setStepSound(Block.soundTypeGrass);
		// TODO: add our own tab again
		setCreativeTab(CreativeTabs.tabDecorations);
		
		allFlowers.add(this);
	}
	
	public static List<BlockExtraFlower> getAllFlowers() {
		return allFlowers;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item id, CreativeTabs tab, List itemList) {
		for( final IBlockTypeFlower type : blockTypes ) {
			itemList.add(new ItemStack(this, 1, type.getMeta()));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		parentMod.LOGGER.debug(this.getClass().getSimpleName() + ": registerIcons");
		for( final IBlockTypeFlower type : blockTypes ) {
			final IIcon icon = type.registerIcon(iconRegister);
			if( icon == null ) {
				parentMod.LOGGER.warn("No icon found for %s (%d)", type, type.getMeta());
			} else {
				parentMod.LOGGER.debug("%s: %s = %s", this.getClass().getSimpleName(), type, icon);
			}
		}
	}
	
	public IBlockTypeFlower getType(int metadata) {
		if( metadata < 0 || metadata > blockTypes.length )
			return null;
		final IBlockTypeFlower type = blockTypes[metadata];
		return type;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		final IBlockTypeFlower type = getType(metadata);
		if( type != null ) {
			return type.getIcon();
		} else {
			return null;
		}
	}

	public String getUnlocalizedName(int metadata) {
		final IBlockTypeFlower type = getType(metadata);
		if( type != null ) {
			return this.getUnlocalizedName() + "." + type.name().toLowerCase(Locale.ENGLISH);
		} else {
			return "";
		}
	}
	
}

