package zabi.minecraft.perkmastery.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.InventoryType;
import zabi.minecraft.perkmastery.items.ItemList;


public class PlayerExtraInventory implements IInventory {

	EntityPlayer player;

	public PlayerExtraInventory(EntityPlayer p) {
		player = p;
	}

	@Override
	public int getSizeInventory() {
		return 26;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ExtendedPlayer.getExtraInventory(player, InventoryType.REAL)[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int qt) {
		// Log.i("Decr ss");
		ItemStack s = getStackInSlot(slot);
		if (s == null) return null;
		ItemStack res;
		if (s.stackSize > qt) res = s.splitStack(qt);
		else {
			res = s.copy();
			s = null;
		}
		ExtendedPlayer.setInventorySlot(player, slot, s);

		return res;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		ExtendedPlayer.setInventorySlot(player, slot, is);

	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer user) {
		return user.equals(player);
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 18) return stack.getItem().equals(ItemList.boneAmulet);
		return true;
	}

}
