package extrabiomes.lib;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;
import extrabiomes.lib.settings.BiomeSettings;

public class ExtraWorldGenerator implements IWorldGenerator {

	protected Set<ExtrabiomeGenBase> biomes = Sets.newHashSet();
	protected Map<String,WorldGenerator> worldGens = Maps.newHashMap();
	
	public final int maxDecorations;
	
	public ExtraWorldGenerator(int max) {
		this.maxDecorations = max;
	}
	
	public void registerGenerator(String type, WorldGenerator gen) {
		worldGens.put(type, gen);
	}
	
	public void registerBiome(ExtrabiomeGenBase biome) {
		biomes.add(biome);
	}
	public void registerBiome(BiomeSettings settings) {
		if( settings.getBiome().isPresent() ) {
			registerBiome( settings.getBiome().get() );
		}
	}
	
	protected int applyGenerator(String type, World world, int chunkX, int chunkZ, Random rand, int times) {
		final WorldGenerator gen = worldGens.get(type);
		int count = 0;
		if (gen != null) {
			for (int i = 0; i < times; ++i) {
				final int x = chunkX + rand.nextInt(16) + 8;
				final int y = rand.nextInt(128);
				final int z = chunkZ + rand.nextInt(16) + 8;
				if (gen.generate(world, rand, x, y, z)) ++count;
			}
		}
		return count;
	}

	protected boolean applyGenerator(String type, World world, int chunkX, int chunkZ, Random rand) {
		final WorldGenerator gen = worldGens.get(type);
		if (gen != null) {
			return applyGenerator(gen, world, chunkX, chunkZ, rand);
		}
		return false;
	}

	protected boolean applyGenerator(WorldGenerator gen, World world, int chunkX, int chunkZ, Random rand) {
		if (gen != null) {
			final int x = chunkX + rand.nextInt(16) + 8;
			final int y = rand.nextInt(128);
			final int z = chunkZ + rand.nextInt(16) + 8;
			return gen.generate(world, rand, x, y, z);
		}
		return false;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX <<= 4;
		chunkZ <<= 4;
		final BiomeGenBase biome = world.getBiomeGenForCoords(chunkX, chunkZ);
		
		if( biomeCheck(biome) ) {
			final ExtrabiomeGenBase eBiome = (ExtrabiomeGenBase)biome;
			for( int k = 0; k < maxDecorations; ++k ) {
				final int idx = random.nextInt( worldGens.size() );
				applyGenerator( (String)worldGens.keySet().toArray()[idx], world, chunkX, chunkZ, random );
			}
		}
	}

	protected boolean biomeCheck(BiomeSettings settings, BiomeGenBase biome) {
		return settings.getBiome().isPresent()
				&& biome == settings.getBiome().get();
	}
	
	protected boolean biomeCheck(BiomeGenBase biome) {
		return biomes.contains(biome);
	}

}
