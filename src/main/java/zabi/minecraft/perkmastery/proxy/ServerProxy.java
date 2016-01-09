package zabi.minecraft.perkmastery.proxy;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.network.packets.SyncFilterToClient;
import zabi.minecraft.perkmastery.network.packets.SyncInventoryToClient;


public class ServerProxy extends CommonProxy {

	@Override
	public void setPlayerExtraInventory(EntityPlayer player, int slot, ItemStack is) {
		PerkMastery.network.sendToDimension(new SyncInventoryToClient(player.getDisplayName(), slot, is), slot);
	}

	@Override
	public void setPlayerFilter(EntityPlayer player, int slot, ItemStack is) {
		PerkMastery.network.sendToDimension(new SyncFilterToClient(player.getDisplayName(), slot, is), slot);
	}

	@Override
	public void registerKeyBindings() {
	}

	@Override
	public void registerAnimationHelper() {
	}

	@Override
	public EntityPlayer getSinglePlayer() {
		return null;
	}

	@Override
	public void setupHackyController(boolean enable) {
	}

	@Override
	public void registerRenderers() {
	}

	public static boolean harvestBlockAt(int x, int y, int z, EntityPlayer player, BreakEvent event) {

		EntityPlayerMP playerMP = ((EntityPlayerMP) player);
		ItemInWorldManager IIWM = playerMP.theItemInWorldManager;

		boolean stackDestroyed = false;
		Block block = player.worldObj.getBlock(x, y, z);
		int meta = player.worldObj.getBlockMetadata(x, y, z);

		BreakEvent subEvent = new BreakEvent(x, y, z, player.worldObj, block, meta, playerMP);
		subEvent.setResult(Result.DENY); // To intercept it later in the handler and avoid StackOverflows
		PerkMastery.eventi.onBlockBreakEvent(subEvent);
		if (subEvent.isCanceled()) return false;

		boolean blockRemoved = false;

		if (IIWM.isCreative()) {
			blockRemoved = removeBlock(x, y, z, false, playerMP);
			IIWM.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, player.worldObj));
		} else {
			ItemStack itemstack = playerMP.getCurrentEquippedItem();
			boolean canHarvest = block.canHarvestBlock(playerMP, meta);

			if (itemstack != null) {
				itemstack.func_150999_a(player.worldObj, block, x, y, z, playerMP);

				if (itemstack.stackSize == 0) {
					playerMP.destroyCurrentEquippedItem();
					stackDestroyed = true;
				}
			}

			blockRemoved = removeBlock(x, y, z, canHarvest, playerMP);
			if (blockRemoved && canHarvest) {
				block.harvestBlock(player.worldObj, playerMP, x, y, z, meta);
				if (player.getHeldItem() != null) player.getHeldItem().getItem().onBlockStartBreak(player.getHeldItem(), x, y, z, player);
			}
		}
		if (!IIWM.isCreative() && blockRemoved && event != null) {
			block.dropXpOnBlockBreak(player.worldObj, x, y, z, event.getExpToDrop());
		}
		return stackDestroyed;

	}

	private static boolean removeBlock(int x, int y, int z, boolean canHarvest, EntityPlayerMP thisPlayerMP) {
		World theWorld = thisPlayerMP.worldObj;
		Block block = theWorld.getBlock(x, y, z);
		int l = theWorld.getBlockMetadata(x, y, z);
		block.onBlockHarvested(theWorld, x, y, z, l, thisPlayerMP);
		boolean flag = block.removedByPlayer(theWorld, thisPlayerMP, x, y, z, canHarvest);
		if (flag) block.onBlockDestroyedByPlayer(theWorld, x, y, z, l);
		return flag;
	}

}
