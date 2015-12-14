package zabi.minecraft.perkmastery.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.InventoryType;

public class PlayerFilterInventory implements IInventory {

	EntityPlayer player;
	
	public PlayerFilterInventory(EntityPlayer p) {
		player=p;
	}
	
	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ExtendedPlayer.getExtraInventory(player, InventoryType.FILTER)[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int qt) {
		ItemStack res=getStackInSlot(slot);
		ExtendedPlayer.setFilterSlot(player, slot, null);		
		return res;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		ExtendedPlayer.setFilterSlot(player, slot, is);

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
		return 1;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer user) {
		return user.equals(player);
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

}
