package extrabiomes.lib.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extrabiomes.lib.ModBase;
import extrabiomes.lib.blocks.IBlockTypeFlower;
import extrabiomes.lib.blocks.IExtraBlock;

public class BlockExtraCrop extends BlockFlower implements IExtraBlock {
	private static final List<BlockExtraCrop>	allCrops = Lists.newArrayList();
	
	private final ModBase 			parentMod;
	public final IBlockTypeCrop		cropType;
	
	public static final int		MAX_GROWTH_STAGE	= 7;
	protected static final int	MIN_LIGHT_LEVEL		= 9;
	protected static final int	MIN_FERTILIZER		= 2;
	protected static final int	MAX_FERTILIZER		= 5;
	
	protected static final int	RENDER_TYPE_CROP	= 6;
	protected static final int	RENDER_TYPE_FLOWER	= 1;
	protected static final int	DEFAULT_RENDER_TYPE	= RENDER_TYPE_CROP;

	public BlockExtraCrop(ModBase parentMod, String locPrefix, IBlockTypeCrop blockType) {
		super(0);
		
		this.parentMod = parentMod;
		this.cropType = blockType;
		
		setBlockName(locPrefix+".crop."+blockType.name());
		setTickRandomly(true);
		setHardness(0.0f);
		setStepSound(Block.soundTypeGrass);
		// TODO: add our own tab again
		setCreativeTab(CreativeTabs.tabDecorations);
		
		final float offset = 0.2F;
		setBlockBounds(0.5F - offset, 0.0F, 0.5F - offset, 0.5F + offset, offset * 3.0F, 0.5F + offset);
		
		allCrops.add(this);
	}
	
	public static List<BlockExtraCrop> getAllCrops() {
		return allCrops;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List itemList) {
		for( int k = 0; k <= MAX_GROWTH_STAGE; ++k ) {
			itemList.add(new ItemStack(item, 1, k));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		
		ArrayList<IIcon> IIcons = Lists.newArrayListWithCapacity(MAX_GROWTH_STAGE+1);
		final String name = cropType.name().toLowerCase();
		IIcon lastIIcon = null;
		for (int k = 0; k <= MAX_GROWTH_STAGE; ++k) {
			int l = (k != (MAX_GROWTH_STAGE - 1)) ? k : k-1;
			
			final String texture = "plant_" + name + l;
			parentMod.LOGGER.debug(this.getClass().getSimpleName() + ": registerIcons");
			final IIcon icon = parentMod.registerIcon(iconRegister, texture);
			if( icon == null ) {
				parentMod.LOGGER.warn("No icon found for %s (%d)", cropType, k);
			} else {
				parentMod.LOGGER.debug("%s: %s = %s", this.getClass().getSimpleName(), cropType, icon);
			}

			IIcons.add(k, icon);
		}
		cropType.setStageIIcons(IIcons);

	}

	public IBlockTypeCrop getType() {
		return cropType;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		if (metadata > MAX_GROWTH_STAGE) metadata = MAX_GROWTH_STAGE;
		try {
			return cropType.getStageIIcon(metadata);
		} catch (Exception e) {
			parentMod.LOGGER.warn("Unable to get stage IIcon for " + cropType.name()
					+ " @ " + metadata);
			return null;
		}
	}
	
	public String getUnlocalizedName(int metadata) {
		if( cropType != null ) {
			return this.getUnlocalizedName() + "." + cropType.name().toLowerCase(Locale.ENGLISH);
		} else {
			return "";
		}
	}
	
	//////////
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockCrops#updateTick(net.minecraft.world.World, int, int, int, java.util.Random)
	 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		super.updateTick(world, x, y, z, rand);
		
		if( world.getBlockLightValue(x, y, z) >= MIN_LIGHT_LEVEL ) {
			int meta = world.getBlockMetadata(x, y, z);

			if( meta < MAX_GROWTH_STAGE ) {
				float rate = this.getGrowthRate(world, x, y, z);
				
				if( rand.nextInt((int)( 25F / rate) + 1) == 0 ) {
					++meta;
					world.setBlockMetadataWithNotify(x, y, z, meta, 2);
				}
			}
		}
	}
	
	/**
	 * Apply bonemeal to the crops.
	 */
	public void fertilize(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z)
				+ MathHelper.getRandomIntegerInRange(world.rand, MIN_FERTILIZER, MAX_FERTILIZER);
		
		if( meta > MAX_GROWTH_STAGE ) {
			meta = MAX_GROWTH_STAGE;
		}
		
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}
	
	public void markOrGrowMarked(World world, int x, int y, int z, Random rand) {
		fertilize(world, x, y, z);
	}
	
	/**
	 * Gets the growth rate for the crop. Setup to encourage rows by halving growth rate if there is diagonals, crops on
	 * different sides that aren't opposing, and by adding growth for every crop next to this one (and for crop below
	 * this one). Args: x, y, z
	 */
	private float getGrowthRate(World world, int x, int y, int z)
	{
		float rate = 1.0F;
		final Block id_nZ = world.getBlock(x, y, z - 1);
		final Block id_pZ = world.getBlock(x, y, z + 1);
		final Block id_nX = world.getBlock(x - 1, y, z);
		final Block id_pX = world.getBlock(x + 1, y, z);
		final Block id_nXnZ = world.getBlock(x - 1, y, z - 1);
		final Block id_pXnZ = world.getBlock(x + 1, y, z - 1);
		final Block id_pXpZ = world.getBlock(x + 1, y, z + 1);
		final Block id_nXpZ = world.getBlock(x - 1, y, z + 1);
		final boolean flagX = id_nX == this || id_pX == this;
		final boolean flagZ = id_nZ == this || id_pZ == this;
		final boolean flagD = id_nXnZ == this || id_pXnZ == this || id_pXpZ == this || id_nXpZ == this;
		
		// bonus for nearby soil
		for (int i = x - 1; i <= x + 1; ++i) {
			for (int j = z - 1; j <= z + 1; ++j) {
				final Block id_ground = world.getBlock(i, y - 1, j);
				float bonus = 0.0F;
				
				if (id_ground != null && id_ground.canSustainPlant(world, i, y - 1, j, ForgeDirection.UP, this)) {
					bonus = 1.0F;
					
					if (id_ground.isFertile(world, i, y - 1, j)) {
						bonus = 3.0F;
					}
				}
					
				if (i != x || j != z) {
					bonus /= 4.0F;
				}
					
				rate += bonus;
			}
		}
		
		// penalty for adjacent similar crops
		if (flagD || flagX && flagZ) {
			rate /= 2.0F;
		}
		
		return rate;
	}
	
	/**
	 * Currently borrowed directly from vanilla crops, will improve.
	 */
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);

		if (metadata >= MAX_GROWTH_STAGE) {
			for (int n = 0; n < 3 + fortune; n++) {
				if (world.rand.nextInt(15) <= metadata) {
					ret.add(this.getSeedItem());
				}
			}
		}

		return ret;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return meta >= MAX_GROWTH_STAGE ? this.getCropItem() : this.getSeedItem().getItem();
	}
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
		
	@Override
	public int getRenderType() {
		return cropType.getRenderType();
	}

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }
	
	public ItemStack getSeedItem() {
		return cropType.getSeedItem();
	}
	public Item getCropItem() {
		return cropType.getCropItem();
	}
	public void setSeedItem(ItemStack seed) {
		cropType.setSeedItem(seed);
	}
	public void setCropItem(Item crop) {
		cropType.setCropItem(crop);
	}
	
}

