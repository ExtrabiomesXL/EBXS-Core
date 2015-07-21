package extrabiomes.lib.settings;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import extrabiomes.lib.blocks.BlockExtraCrop;
import extrabiomes.lib.blocks.IBlockTypeCrop;

public class CropSettings implements IBlockTypeCrop {

	private final String name;
	private final CropType type;
	private final int renderType;
	
	private List<IIcon> stageIcons;
	private ItemStack seedItem;
	private Item cropItem;
	
	public CropSettings( String name, CropType cropType, int renderType ) {
		this.name = name;
		this.type = cropType;
		this.renderType = renderType;
	}
	public CropSettings( String name, CropType cropType ) {
		this( name, cropType, BlockExtraCrop.DEFAULT_RENDER_TYPE );
	}
	
	@Override
	public String name() {
		return name;
	}
	@Override
	public CropType getCropType() {
		return type;
	}

	@Override
	public IIcon getStageIcon(int stage) {
		return stageIcons.get(stage);
	}
	@Override
	public void setStageIcons(ArrayList<IIcon> icons) {
		this.stageIcons = icons;
	}

	@Override
	public ItemStack getSeedItem() {
		return seedItem;
	}
	@Override
	public void setSeedItem(ItemStack seed) {
		this.seedItem = seed;
	}
	
	@Override
	public Item getCropItem() {
		return cropItem;
	}
	@Override
	public void setCropItem(Item crop) {
		this.cropItem = crop;
	}

	@Override
	public int getRenderType() {
		return renderType;
	}

}
