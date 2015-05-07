package extrabiomes.lib;

import extrabiomes.lib.settings.BiomeSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary.Type;

public abstract class ExtrabiomeGenBase extends BiomeGenBase {
	protected BiomeSettings settings;
	protected Type[] typeFlags;
	
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
}
