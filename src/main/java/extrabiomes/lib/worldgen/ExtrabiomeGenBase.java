package extrabiomes.lib.worldgen;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary.Type;
import extrabiomes.lib.settings.BiomeSettings;

public abstract class ExtrabiomeGenBase extends BiomeGenBase {
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
