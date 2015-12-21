package zabi.minecraft.perkmastery.handlers;

import java.util.Random;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import zabi.minecraft.perkmastery.Config;


public class DigHandler {

	private static final int VEIN_MAX_SIZE = 40;

	public static boolean containsGlass(String unlocalizedName) {
		return unlocalizedName.contains("glass") || unlocalizedName.contains("pane") || unlocalizedName.contains("glowstone");
	}

	public static void applyFortune(BreakEvent evt) {
		if (!evt.block.getItemDropped(evt.blockMetadata, evt.world.rand, 0).equals(Item.getItemFromBlock(evt.block))) evt.world.spawnEntityInWorld(new EntityItem(evt.world, evt.x, evt.y, evt.z, new ItemStack(evt.block.getItemDropped(evt.blockMetadata, evt.world.rand, 0), 1, evt.block.damageDropped(evt.blockMetadata))));
	}

	public static boolean isToolDelicate(BreakEvent evt) {
		if (!(evt.block.canSilkHarvest(evt.world, evt.getPlayer(), evt.x, evt.y, evt.z, evt.blockMetadata) && containsGlass(evt.block.getUnlocalizedName().toLowerCase()))) return false;
		ItemStack held = evt.getPlayer().getHeldItem();
		if (held == null || !(IntegrationHandler.isPickaxe(held))) return true;
		return false;
	}

	public static void applyVeinminer(BreakEvent evt, int x, int y, int z, int[] counter, boolean firstRun, boolean silk, int fortune, int iteration) {

		iteration++;

		if (iteration > Config.maxIterations) { return; }

		if (!evt.block.getUnlocalizedName().toLowerCase().contains("ore") || evt.block instanceof ITileEntityProvider) return;

		Item drop = silk ? Item.getItemFromBlock(evt.block) : evt.block.getItemDropped(evt.blockMetadata, evt.world.rand, fortune);
		int meta = silk ? evt.blockMetadata : evt.block.damageDropped(evt.blockMetadata);
		Random random = evt.world.rand;

		if (checkVeinminer(evt, x + 1, y, z, counter[0])) {
			counter[0]++;
			counter[1] += silk ? 1 : evt.block.quantityDropped(meta, fortune, random);
			evt.world.setBlockToAir(x + 1, y, z);
			if (counter[0] < VEIN_MAX_SIZE) applyVeinminer(evt, x + 1, y, z, counter, false, silk, fortune, iteration);
		}

		if (checkVeinminer(evt, x - 1, y, z, counter[0])) {
			counter[0]++;
			counter[1] += silk ? 1 : evt.block.quantityDropped(meta, fortune, random);
			evt.world.setBlockToAir(x - 1, y, z);
			if (counter[0] < VEIN_MAX_SIZE) applyVeinminer(evt, x - 1, y, z, counter, false, silk, fortune, iteration);
		}

		if (checkVeinminer(evt, x, y + 1, z, counter[0])) {
			counter[0]++;
			counter[1] += silk ? 1 : evt.block.quantityDropped(meta, fortune, random);
			evt.world.setBlockToAir(x, y + 1, z);
			if (counter[0] < VEIN_MAX_SIZE) applyVeinminer(evt, x, y + 1, z, counter, false, silk, fortune, iteration);
		}

		if (checkVeinminer(evt, x, y - 1, z, counter[0])) {
			counter[0]++;
			counter[1] += silk ? 1 : evt.block.quantityDropped(meta, fortune, random);
			evt.world.setBlockToAir(x, y - 1, z);
			if (counter[0] < VEIN_MAX_SIZE) applyVeinminer(evt, x, y - 1, z, counter, false, silk, fortune, iteration);
		}

		if (checkVeinminer(evt, x, y, z + 1, counter[0])) {
			counter[0]++;
			counter[1] += silk ? 1 : evt.block.quantityDropped(meta, fortune, random);
			evt.world.setBlockToAir(x, y, z + 1);
			if (counter[0] < VEIN_MAX_SIZE) applyVeinminer(evt, x, y, z + 1, counter, false, silk, fortune, iteration);
		}

		if (checkVeinminer(evt, x, y, z - 1, counter[0])) {
			counter[0]++;
			counter[1] += silk ? 1 : evt.block.quantityDropped(meta, fortune, random);
			evt.world.setBlockToAir(x, y, z - 1);
			if (counter[0] < VEIN_MAX_SIZE) applyVeinminer(evt, x, y, z - 1, counter, false, silk, fortune, iteration);
		}

		if (firstRun && counter[0] > 0) {
			evt.block.dropXpOnBlockBreak(evt.world, evt.x, evt.y, evt.z, evt.getExpToDrop() * counter[0]);
			evt.world.spawnEntityInWorld(new EntityItem(evt.world, x, y, z, new ItemStack(drop, counter[1], meta)));
		}

	}

	private static boolean checkVeinminer(BreakEvent evt, int x, int y, int z, int pass) {
		return (evt.block.equals(evt.world.getBlock(x, y, z))) && (evt.blockMetadata == evt.world.getBlockMetadata(x, y, z)) && (pass <= VEIN_MAX_SIZE);
	}

	public static void applyCrumbling(int x, int y, int z, World world, int qtDestroyed) {
		if (qtDestroyed > Config.maxIterations) { return; }
		if (!world.isRemote && world.getBlock(x, y, z).equals(Blocks.gravel)) {
			world.setBlockToAir(x, y, z);
			applyCrumbling(x, y + 1, z, world, qtDestroyed + 1);
		} else {
			if (qtDestroyed > 0) world.spawnEntityInWorld(new EntityItem(world, x, y - qtDestroyed - 1, z, new ItemStack(Blocks.gravel, qtDestroyed)));
		}
	}

}
