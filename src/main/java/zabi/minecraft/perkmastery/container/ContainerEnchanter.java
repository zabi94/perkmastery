package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.tileentity.TileEntityEnchanter;


public class ContainerEnchanter extends ContainerBase {

	private EntityPlayer player;

	public ContainerEnchanter(EntityPlayer player, TileEntityEnchanter inv) {
		this.player = player;

		int x = 80, y = 34;
		this.addSlotToContainer(new Slot(inv, 0, x, y) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.isItemEnchantable();
			}
		});
		addPlayerSlots(this.player.inventory);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int _slot) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(_slot);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (_slot == 0) {
				if (!this.mergeItemStack(itemstack1, 1, 36, true)) return null;
				slot.onSlotChange(itemstack1, itemstack);
			} else if (itemstack.isItemEnchantable()) {
				if (!this.mergeItemStack(itemstack1, 0, 0, true)) return null;
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
