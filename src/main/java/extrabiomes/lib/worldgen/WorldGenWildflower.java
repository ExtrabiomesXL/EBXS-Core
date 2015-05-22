package extrabiomes.lib.worldgen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenWildflower extends WorldGenerator {
	private final List<ItemStack> flowers;
	
	public WorldGenWildflower(List<ItemStack> flowerStacks) {
		this.flowers = flowerStacks;
	}

	private ItemStack getFlower(Random rand) {
		final int idx = rand.nextInt(flowers.size());
		return flowers.get(idx);
	}
	
	@Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
		if( flowers.size() == 0 ) return false;
        for( int k = 0; k < 64; ++k ) {
            final int x1 = x + rand.nextInt(8) - rand.nextInt(8);
            final int y1 = y + rand.nextInt(4) - rand.nextInt(4);
            final int z1 = z + rand.nextInt(8) - rand.nextInt(8);
            
            final boolean isAir = world.isAirBlock(x1, y1, z1);
            final boolean isSnow = (world.getBlock(x1, y1, z) == Blocks.snow);
            
            final ItemStack flower = getFlower(rand);
            final Block block = Block.getBlockFromItem(flower.getItem());
            final int metadata = flower.getItemDamage();
            
            try {
	            if ((isAir || isSnow) && block.canBlockStay(world, x1, y1, z1)) {
	                world.setBlock(x1, y1, z1, block, metadata, 2);
	            }
            } catch (Exception e) {
            	/*
            	LogHelper.severe("We stopped a crash in the flower generator.");
            	LogHelper.severe("BlockName: %d, World: %s, Line: %d",
            		block.getUnlocalizedName(),
            		(world == null) ? "null" : world.toString(),
            		e.getStackTrace()[0].getLineNumber()
            	);
            	*/
            }
        }
        
        return true;
    }

}
