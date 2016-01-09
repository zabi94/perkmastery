package zabi.minecraft.perkmastery.handlers;

import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import zabi.minecraft.perkmastery.libs.LibGameRules;
import zabi.minecraft.perkmastery.proxy.ServerProxy;


public class DigHandler {

	private static final int										VEIN_MAX_SIZE	= 40;
	private static final ConcurrentLinkedQueue<ChunkCoordinates>	visited			= new ConcurrentLinkedQueue<ChunkCoordinates>();

	public static boolean containsGlass(String unlocalizedName) {
		return unlocalizedName.contains("glass") || unlocalizedName.contains("pane") || unlocalizedName.contains("glowstone");
	}

	public static void applyFortune(BreakEvent evt) {
		try {
			if (!evt.block.getItemDropped(evt.blockMetadata, evt.world.rand, 0).equals(Item.getItemFromBlock(evt.block)) && evt.world.getGameRules().getGameRuleBooleanValue(LibGameRules.doTileDrops.name())) evt.world.spawnEntityInWorld(new EntityItem(evt.world, evt.x, evt.y, evt.z, new ItemStack(evt.block.getItemDropped(evt.blockMetadata, evt.world.rand, 0), 1, evt.block.damageDropped(evt.blockMetadata))));
		} catch (NullPointerException e) {
		}
	}

	public static boolean isToolDelicate(BreakEvent evt) {
		if (!(evt.block.canSilkHarvest(evt.world, evt.getPlayer(), evt.x, evt.y, evt.z, evt.blockMetadata) && containsGlass(evt.block.getUnlocalizedName().toLowerCase()))) return false;
		ItemStack held = evt.getPlayer().getHeldItem();
		if (held == null || !(IntegrationHelper.isPickaxe(held))) return true;
		return false;
	}

	public static void applyVeinminer(BreakEvent evt, int x, int y, int z, boolean baseRun) {
		if (evt.world.isRemote) return;
		if (!IntegrationHelper.isPickaxe(evt.getPlayer().getHeldItem())) return;
		if (visited.size() > VEIN_MAX_SIZE) return;
		visited.offer(new ChunkCoordinates(x, y, z));
		for (int dx = -1; dx <= 1; dx++)
			for (int dy = -1; dy <= 1; dy++)
				for (int dz = -1; dz <= 1; dz++) {
					ChunkCoordinates coords = new ChunkCoordinates(x + dx, y + dy, z + dz);
					boolean isAlreadyVisited = visited.contains(coords);
					boolean isEquivalentToMined = checkVeinminer(evt, x + dx, y + dy, z + dz);
					boolean isValidCandidate = isOre(evt.world, x + dx, y + dy, z + dz);
					if (!isAlreadyVisited && isEquivalentToMined && isValidCandidate) {
						applyVeinminer(evt, x + dx, y + dy, z + dz, false);
					}
				}
		if (baseRun) {
			for (ChunkCoordinates cc : visited) {
				boolean stop = ServerProxy.harvestBlockAt(cc.posX, cc.posY, cc.posZ, evt.getPlayer(), evt);
				visited.remove(cc);
				if (stop) {
					visited.clear();
					break;
				}
			}
		}
	}

	private static boolean isOre(World world, int x, int y, int z) {
		String blockName = world.getBlock(x, y, z).getUnlocalizedName().toLowerCase();
		if (blockName.contains("ore")) return true;
		if (blockName.contains("netherquartz")) return true;
		if (world.getTileEntity(x, y, z) != null) return false;
		return false;
	}

	private static boolean checkVeinminer(BreakEvent evt, int x, int y, int z) {
		return areEquivalent(evt.block, evt.blockMetadata, evt.world.getBlock(x, y, z), evt.world.getBlockMetadata(x, y, z));
	}

	public static void applyCrumbling(int x, int y, int z, World world, EntityPlayer player) {
		if (!world.isRemote && world.getBlock(x, y, z).equals(Blocks.gravel)) {
			ServerProxy.harvestBlockAt(x, y, z, null, null);
			applyCrumbling(x, y + 1, z, world, player);
		}
	}

	public static boolean areEquivalent(Block a, int ma, Block b, int mb) {
		if (b instanceof BlockRedstoneOre && a instanceof BlockRedstoneOre) return true;
		return (a.equals(b) && ma == mb);
	}

}
