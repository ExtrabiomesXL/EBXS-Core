package extrabiomes.lib.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenExtraTree extends WorldGenAbstractTree {

	protected int		base_height			= 4;
	protected int		canopy_height		= 3;
	protected int		max_variance_height	= 2;
	protected int		canopy_extra_radius	= 0;
	
	protected Block		trunkBlock			= Blocks.log;
	protected int		trunkMetadata		= 0;
	
	protected ItemStack	leaves				= new ItemStack(Blocks.leaves);
	
	public WorldGenExtraTree(boolean doBlockNotify) {
		super(doBlockNotify);
	}

	// TODO: add a soil registry
	protected static boolean isBlockSuitableForGrowing(final World world, final int x, final int y, final int z) {
		final Block soil = world.getBlock(x, y, z);
		return soil.equals(Blocks.grass) || soil.equals(Blocks.dirt) || soil.equals(Blocks.farmland);
	}
	
	protected static boolean isRoomToGrow(final World world, final int x, final int y, final int z, final int height) {
		if( y < 0 ) return false;
		final int max_height = world.getActualHeight();
		if( (y + height + 1) >= max_height )
			return false;
		
		for( int _y = y; _y <= y + 1 + height; ++_y ) {
			int radius = 1;
			if( _y == y )
				radius = 0;
			else if( _y >= y + 1 + height - 2 )
				radius = 2;
			
			for( int _x = x - radius; _x <= x + radius; ++_x ) {
				for( int _z = z - radius; _z <= z + radius; ++_z ) {
					final Block block = world.getBlock(_x, _y, _z);
					if( block == null || block.equals(Blocks.grass) )
						return false;
					if( !block.isLeaves(world, _x, _y, _z) 
							&& !block.isWood(world, _x, _y, _z)
							&& !block.isReplaceable(world, _x, _y, _z) )
						return false;
				}
			}
		}
				
		return true;
	}
	
	public void setTrunkBlock(Block block, int metadata) {
		this.trunkBlock = block;
		this.trunkMetadata = metadata;
	}
	
	public void setLeaves(ItemStack itemstack) {
		this.leaves = itemstack;
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		return generateTree(world, rand, x, y, z);
	}

	private boolean generateTree(World world, Random rand, int x, int y, int z) {
		final int height = rand.nextInt(max_variance_height+1) + base_height;
		
		if( y < 1 || y + height + 1 > world.getActualHeight() )
			return false;
		if( !isBlockSuitableForGrowing(world, x, y-1, z) )
			return false;
		if( !isRoomToGrow(world, x, y, z, height) )
			return false;
		
		// TODO: do this in memory instead of in world
		world.setBlock(x, y-1, z, Blocks.dirt);
		growLeaves(world, rand, x, y, z, height);
		growTrunk(world, x, y, z, height);
		
		return true;
	}

	protected void growLeaves(World world, Random rand, int x, int y, int z, int height) {
		final Block leaf = Block.getBlockFromItem(leaves.getItem());
		final int leafMeta = leaves.getItemDamage();
		
		for( int _y = y - canopy_height + height; _y <= y + height; ++_y ) {
			final int canopyRow = _y - (y+height);
			final int radius = canopy_extra_radius + 1 - (canopyRow>>1);
			
			for( int _x = x - radius; _x <= x + radius; ++_x ) {
				final int xFromTrunk = _x - x;
				for( int _z = z - radius; _z <= z + radius; ++_z ) {
					final int zFromTrunk = _z - z;
					final Block block = world.getBlock(_x, _y, _z);
					// NB: the rand here is weird and easy to miss
					if( (Math.abs(xFromTrunk) != radius || Math.abs(zFromTrunk) != radius || rand.nextInt(2) != 0 && canopyRow != 0)
							&& (block == null || block.canBeReplacedByLeaves(world, _x, _y, _z)) ) {
						setBlockAndNotifyAdequately(world, _x, _y, _z, leaf, leafMeta);
					}
				}
			}
		}
	}

	protected void growTrunk(World world, int x, int y, int z, int height) {
		for( int dy = height-1; dy >= 0; --dy ) {
			final int _y = y + dy;
			final Block block = world.getBlock(x, _y, z);
			if( block.isReplaceable(world, x, _y, z) || block.isLeaves(world, x, _y, z) ) {
				setBlockAndNotifyAdequately(world, x, _y, z, trunkBlock, trunkMetadata);
			}
		}
	}

}
