//@noformat
package zabi.minecraft.perkmastery.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import zabi.minecraft.perkmastery.blocks.BlockList;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.items.ItemList;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class Recipes {
	public static void registerRecipes() {
		RecipeSorter.register(LibGeneral.MOD_ID+":lockedRecipe", LockedRecipe.class, Category.UNKNOWN, "after:minecraft:shaped");
		registerVanilla();
	}

	private static void registerVanilla() {
		GameRegistry.addShapedRecipe(new ItemStack(ItemList.boneAmulet), "BBB","B B","NDN", 'B', Items.bone, 'N', Items.gold_nugget, 'D', Items.diamond);
		ItemStack chainmailStack=new ItemStack(ItemList.chainmailCloth,1,0);
		GameRegistry.addShapedRecipe(new ItemStack(ItemList.chainmailCloth,3), " i ","i i"," i ",'i', Items.iron_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_helmet), "iii", "i i", 'i', chainmailStack);
		GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_leggings), "iii", "i i","i i", 'i', chainmailStack);
		GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_boots), "i i", "i i", 'i', chainmailStack);
		GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_chestplate), "i i", "iii", "iii", 'i', chainmailStack);
		GameRegistry.addShapedRecipe(new ItemStack(ItemList.goldenBow), "SG ","S D", "SG ", 'G', Items.gold_ingot, 'D', Items.diamond, 'S', Items.string);
		GameRegistry.addShapedRecipe(new ItemStack(ItemList.goldenBow), " GS","D S", " GS", 'G', Items.gold_ingot, 'D', Items.diamond, 'S', Items.string);
		GameRegistry.addShapedRecipe(new ItemStack(ItemList.hook), " B ","BIB", "LIL", 'B', new ItemStack(Blocks.iron_bars), 'I', Items.iron_ingot, 'L', Items.lead);
		GameRegistry.addShapedRecipe(new ItemStack(ItemList.scaffoldBuilder,1,0), "IWI","WPW","IWI", 'I', Items.iron_ingot, 'W', Items.stick, 'P', Items.diamond_pickaxe);
		GameRegistry.addShapedRecipe(new ItemStack(BlockList.decanter), " B ", "GCG","III", 'B', Items.brewing_stand, 'G', Items.gold_ingot, 'C', Items.cauldron, 'I', Items.iron_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(BlockList.enchanter), " E ","BLB","GGG", 'E', Blocks.enchanting_table, 'G', Items.gold_ingot, 'L', Blocks.lapis_block, 'B', Items.book);
		GameRegistry.addShapedRecipe(new ItemStack(BlockList.disenchanter), " E ","SLS","CCC", 'E', Blocks.enchanting_table, 'C', Blocks.cobblestone, 'L', Blocks.lapis_block, 'S', Items.emerald);
		
		GameRegistry.addShapelessRecipe(new ItemStack(ItemList.tomeEvocation), Items.book, Items.fermented_spider_eye, Items.name_tag);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemList.tomeExperience), Items.book, Items.poisonous_potato, Items.snowball);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemList.tomeReturn), Items.book, Items.map, Items.ender_pearl);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemList.scaffoldBuilder,1,0),new ItemStack(ItemList.scaffoldBuilder,1,1));
		GameRegistry.addShapelessRecipe(new ItemStack(ItemList.scaffoldBuilder,1,1),new ItemStack(ItemList.scaffoldBuilder,1,0));
		
		//Designer
		for (int i=0;i<6;i++)  GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.planks,1,i)}, 					new ItemStack(Blocks.planks,1,(i+1)%6), PlayerClass.BUILDER, 3));
		for (int i=0;i<6;i++)  GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.wooden_slab,1,i)}, 				new ItemStack(Blocks.wooden_slab,1,(i+1)%6), PlayerClass.BUILDER, 3));
		for (int i=0;i<3;i++)  GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.quartz_block,1,i)}, 				new ItemStack(Blocks.quartz_block,1,(i+1)%3), PlayerClass.BUILDER, 3));
		for (int i=0;i<3;i++)  GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.sandstone,1,i)}, 				new ItemStack(Blocks.sandstone,1,(i+1)%3), PlayerClass.BUILDER, 3));
		for (int i=0;i<4;i++)  GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.stonebrick,1,i)}, 				new ItemStack(Blocks.stonebrick,1,(i+1)%4), PlayerClass.BUILDER, 3));
		for (int i=0;i<2;i++)  GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.sand,1,i)}, 						new ItemStack(Blocks.sand,1,(i+1)%2), PlayerClass.BUILDER, 3));
		for (int i=0;i<16;i++) GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.wool,1,i)}, 						new ItemStack(Blocks.wool,1,(i+1)%16), PlayerClass.BUILDER, 3));
		for (int i=0;i<16;i++) GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.carpet,1,i)}, 					new ItemStack(Blocks.carpet,1,(i+1)%16), PlayerClass.BUILDER, 3));
		for (int i=0;i<16;i++) GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.stained_hardened_clay,1,i)}, 	new ItemStack(Blocks.stained_hardened_clay,1,(i+1)%16), PlayerClass.BUILDER, 3));
		for (int i=0;i<16;i++) GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.stained_glass,1,i)}, 			new ItemStack(Blocks.stained_glass,1,(i+1)%16), PlayerClass.BUILDER, 3));
		for (int i=0;i<16;i++) GameRegistry.addRecipe(new LockedRecipe(1, 1, new ItemStack[] {new ItemStack(Blocks.stained_glass_pane,1,i)},		new ItemStack(Blocks.stained_glass_pane,1,(i+1)%16), PlayerClass.BUILDER, 3));
		
		
	}


}
