package zabi.minecraft.perkmastery.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	public void registerTESR() {
	}

}
