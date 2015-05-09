package extrabiomes.lib;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;
import extrabiomes.lib.settings.BiomeSettings;

public abstract class ExtraWorldGenerator implements IWorldGenerator {

	protected Map<String,WorldGenerator> worldGens = Maps.newHashMap();
	
	public void registerGenerator(String type, WorldGenerator gen) {
		worldGens.put(type, gen);
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


	protected boolean biomeCheck(BiomeSettings settings, BiomeGenBase biome) {
		return settings.getBiome().isPresent()
				&& biome == settings.getBiome().get();
	}

}
