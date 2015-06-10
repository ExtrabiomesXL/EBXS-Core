package extrabiomes.core.handlers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import extrabiomes.lib.blocks.BlockExtraFlower;
import extrabiomes.lib.blocks.IBlockTypeFlower;
import extrabiomes.lib.items.ItemExtraDye;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeHandler {

	public static void init() {
		writeFlowerRecipes();
	}
		
	private static void addRecipe(IRecipe recipe) {
		CraftingManager.getInstance().getRecipeList().add(0, recipe);
	}
	
	private static void writeFlowerRecipes() {
		Map<Integer, ItemStack> vanillaDyes = Maps.newHashMap();
		
		for( BlockExtraFlower block : BlockExtraFlower.getAllFlowers() ) {
			for( IBlockTypeFlower type : block.blockTypes ) {
				ItemStack dye;
				int color = type.getColor();
				switch( color ) {
					case -1:
						continue;
					case 0:
						dye = ItemExtraDye.getDye(ItemExtraDye.Color.BLACK);
						break;
					case 3:
						dye = ItemExtraDye.getDye(ItemExtraDye.Color.BROWN);
						break;
					case 4:
						dye = ItemExtraDye.getDye(ItemExtraDye.Color.BLUE);
						break;
					case 15:
						dye = ItemExtraDye.getDye(ItemExtraDye.Color.WHITE);
						break;
					default:
						if( vanillaDyes.containsKey(color) ) {
							dye = vanillaDyes.get(color);
						} else {
							dye = new ItemStack(Items.dye, 1, color);
							vanillaDyes.put(color, dye);
						}
				}
				ItemStack item = new ItemStack(block, 1, type.getMeta());
				IRecipe recipe = new ShapelessOreRecipe( dye, item );
				addRecipe(recipe);
			}
		}
	}

}
