package extrabiomes.lib.worldgen;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.Height;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary.Type;
import extrabiomes.lib.settings.BiomeSettings;

public abstract class ExtrabiomeGenBase extends BiomeGenBase {
	/*
	 * Vanilla provides the following height definitions, which we should use where appropriate
	 * 
					    height_Default				= new Height(0.1F, 0.2F);
					    height_ShallowWaters		= new Height(-0.5F, 0.0F);
					    height_Oceans				= new Height(-1.0F, 0.1F);
					    height_DeepOceans			= new Height(-1.8F, 0.1F);
					    height_LowPlains			= new Height(0.125F, 0.05F);
					    height_MidPlains			= new Height(0.2F, 0.2F);
					    height_LowHills				= new Height(0.45F, 0.3F);
					    height_HighPlateaus			= new Height(1.5F, 0.025F);
					    height_MidHills				= new Height(1.0F, 0.5F);
					    height_Shores				= new Height(0.0F, 0.025F);
					    height_RockyWaters			= new Height(0.1F, 0.8F);
					    height_LowIslands			= new Height(0.2F, 0.3F);
					    height_PartiallySubmerged	= new Height(-0.2F, 0.1F);
    */
	protected final static Height height_FlatPlains = new Height(0.1F, 0.03125F);	// between Shores and LowPlains
	
	protected BiomeSettings settings;
	protected Type[] typeFlags;
	
	// TODO: make these lists weighted
	private final List<WorldGenAbstractTree> treeGenerators = Lists.newArrayList();
	private final List<WorldGenerator> miscGenerators = Lists.newArrayList();
	
	protected ExtrabiomeGenBase(BiomeSettings settings, Type... typeFlags) {
		super(settings.getID(), true);
		this.settings = settings;
		this.typeFlags = typeFlags;
	}
	
	public BiomeSettings getBiomeSettings() {
		return settings;
	}
	public Type[] getTypeFlags() {
		return typeFlags;
	}
	
	public void registerTreeGenerator(WorldGenAbstractTree gen) {
		treeGenerators.add(gen);
	}
	public void registerMiscGenerator(WorldGenerator gen) {
		miscGenerators.add(gen);
	}
	
	private WorldGenerator getRandomGenerator(List<?extends WorldGenerator> list, Random rng) {
		if( list.isEmpty() ) return null;
		final int idx = rng.nextInt(list.size());
		return list.get(idx);
	}
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand) {
		final WorldGenerator gen = getRandomGenerator(miscGenerators, rand);
		if( gen != null )
			return gen;
		return super.getRandomWorldGenForGrass(rand);
	}
	@Override
	public WorldGenAbstractTree func_150567_a(Random rand) {
		// public WorldGenerator getRandomWorldGenForTrees(Random rand)
		final WorldGenAbstractTree gen = (WorldGenAbstractTree) getRandomGenerator(treeGenerators, rand);
		if( gen != null )
			return gen;
		return super.func_150567_a(rand);
	}
}
