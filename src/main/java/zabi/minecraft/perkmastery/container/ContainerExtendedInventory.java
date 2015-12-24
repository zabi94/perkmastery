package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.PlayerExtraInventory;


public class ContainerExtendedInventory extends ContainerBase {

	private EntityPlayer player;

	public ContainerExtendedInventory(EntityPlayer player) {
		this.player = player;

		PlayerExtraInventory inv = new PlayerExtraInventory(player);
		int x = 8;
		int y = 15;
		for (int j = 0; j < 2; j++)
			for (int i = 0; i < 9; i++)
				addSlotToContainer(new Slot(inv, i + 9 * j, x + i * 18, y + j * 18));
		addPlayerSlots(this.player.inventory, 8, 51);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int _slot) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(_slot);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (_slot > 17) {
				if (!this.mergeItemStack(itemstack1, 0, 17, false)) return null;
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				if (!this.mergeItemStack(itemstack1, 18, 53, true)) return null;
				slot.onSlotChange(itemstack1, itemstack);
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize) { return null; }
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}

}
