package extrabiomes.lib.blocks;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extrabiomes.lib.items.IExtraItemType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;

public abstract class BlockExtraSapling extends BlockFlower implements IExtraBlock {

	private static final int	MIN_GROW_LIGHT			= 9;
	private static final float	DEFAULT_REPLANT_CHANCE	= 15F;	// 15% chance of an expiring sapling trying to plant
	private static int			saplingLifespan			= 5000;
	
	protected final IExtraItemType[]	types;
	protected IIcon[]					textures;
	protected WorldGenerator[]			generators;
	
	protected BlockExtraSapling(IExtraItemType[] types) {
		super(0);
		this.types = types;
		textures = new IIcon[this.types.length];
		generators = new WorldGenerator[this.types.length];
		
		setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.8F, 0.9F);
		setHardness(0.0F);
		setStepSound(Block.soundTypeGrass);
		setCreativeTab(CreativeTabs.tabDecorations);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public String getUnlocalizedName(int metadata) {
		if( metadata < 0 || metadata > types.length )
			return null;
		final IExtraItemType type = types[metadata];
		if( type != null ) {
			return this.getUnlocalizedName() + "." + type.name().toLowerCase(Locale.ENGLISH);
		} else {
			return "";
		}
	}
	
	private static final int	METADATA_BITMASK = 0x7;
	private static final int	METADATA_MARKBIT = 0x8;

	private static boolean isMarkedMetadata(int metadata) {
		return (metadata & METADATA_MARKBIT) != 0;
	}
	private static int markedMetadata(int metadata) {
		return metadata | METADATA_MARKBIT;
	}
	private static int unmarkedMetadata(int metadata) {
		return metadata & METADATA_BITMASK;
	}
	
	public int damageDropped(int metadata) {
		return unmarkedMetadata(metadata);
	}
	
	protected void registerGenerator(int metadata, WorldGenerator generator) {
		generators[metadata] = generator;
	}
	
	public IIcon getIcon(int side, int metadata) {
		metadata = unmarkedMetadata(metadata);
		if( metadata >= textures.length ) metadata = 0;
		return textures[metadata];
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List itemList) {
		for( IExtraItemType type : types ) {
			itemList.add(new ItemStack(this, 1, type.metadata()));
		}
	}
	
	// TODO: make this more configurable, add a soil registry 
	protected boolean canThisPlantGrowOnThisBlock(Block block) {
		return  block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland || block.isFlowerPot();
	}
	
	private static boolean isEnoughLightToGrow(World world, int x, int y, int z) {
		return world.getBlockLightValue(x, y, z) >= MIN_GROW_LIGHT;
	}
	
	private void attemptGrowTree(World world, int x, int y, int z, Random rand) {
		if( isEnoughLightToGrow(world, x, y+1, z) && rand.nextInt(7) == 0 ) {
			final int metadata = world.getBlockMetadata(x, y, z);
			if( !isMarkedMetadata(metadata) ) {
				world.setBlockMetadataWithNotify(x, y, z, markedMetadata(metadata), 3);
			} else {
				growTree(world, x, y, z, rand);
			}
		}
	}
	
	public void markOrGrowMarked(World world, int x, int y, int z, Random rand) {
		int metadata = world.getBlockMetadata(x, y, z);
		if( (metadata & METADATA_MARKBIT) == 0) {
			world.setBlockMetadataWithNotify(x,y,z, metadata | METADATA_MARKBIT, 4);
		} else {
			growTree(world, x, y, z, rand);
		}
	}
	
	public void growTree(World world, int x, int y, int z, Random rand) {
		final int metadata = unmarkedMetadata(world.getBlockMetadata(x, y, z));
		WorldGenerator tree = generators[metadata];
		
		// TODO: add support for detection of and generation of 2x2 (huge) trees
		// TODO: add support for Forestry
		
		if( tree != null ) {
			world.setBlock(x, y, z, Blocks.air);
			if( !tree.generate(world,  rand, x, y, z) ) {
				world.setBlock(x, y, z, this, metadata, 3);
			}
		}
	}
	
	public boolean isSameSapling(World world, int x, int y, int z, int metadata) {
		return world.getBlock(x, y, z) == this && unmarkedMetadata(world.getBlockMetadata(x, y, z)) == metadata;
	}
	
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if( !world.isRemote ) {
			super.updateTick(world, x, y, z, rand);
			attemptGrowTree(world, x, y, z, rand);
		}
	}
	
	public static int getSaplingLifespan() {
		return saplingLifespan;
	}
	public static void setSaplingLifespan( int life ) {
		saplingLifespan = (life > 0) ? life : 0;
	}
	
	@SubscribeEvent
	public void itemExpiring(ItemExpireEvent event) {
		final ItemStack itemstack = event.entityItem.getEntityItem();
		if( itemstack.getItem() == ItemBlock.getItemFromBlock((this)) ) {
			int count = itemstack.stackSize;
			for( int k = 0; k < count; ++k ) {
				final int metadata = unmarkedMetadata(itemstack.getItemDamage());
				final int posX = (int)Math.floor(event.entityItem.lastTickPosX);
				final int posY = (int)Math.floor(event.entityItem.lastTickPosY);
				final int posZ = (int)Math.floor(event.entityItem.lastTickPosZ);

				final World world = event.entityItem.worldObj;
				if( canThisPlantGrowOnThisBlock(world.getBlock(posX, posY-1, posZ)) ) {
					double chance = world.rand.nextDouble() * 100.0;
					// TODO: make chance of autoplanting configurable
					if( chance < DEFAULT_REPLANT_CHANCE ) {
						// TODO: support huge trees
						world.setBlock(posX, posY, posZ, this, metadata, 2);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void itemEntering(EntityJoinWorldEvent event) {
		if( event.entity instanceof EntityItem && !event.world.isRemote ) {
			if( ((EntityItem)event.entity).getEntityItem().getItem() == ItemBlock.getItemFromBlock(this) ) {
				((EntityItem)event.entity).lifespan = saplingLifespan;
			}
		}
	}
	
	// TODO: add support for tooltips
}
