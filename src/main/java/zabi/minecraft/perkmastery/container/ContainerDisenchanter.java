package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.tileentity.TileEntityDisenchanter;


public class ContainerDisenchanter extends ContainerBase {

	private EntityPlayer player;

	public ContainerDisenchanter(EntityPlayer player, TileEntityDisenchanter inv) {
		this.player = player;
		int x = 16, y = 35;
		this.addSlotToContainer(new Slot(inv, 0, x, y) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.isItemEnchanted() && !is.getItem().equals(Items.enchanted_book);
			}

			@Override
			public int getSlotStackLimit() {
				return 1;
			}
		});

		this.addSlotToContainer(new Slot(inv, 1, x + 64, y - 1) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem().equals(Items.book);
			}

			@Override
			public int getSlotStackLimit() {
				return 1;
			}
		});

		this.addSlotToContainer(new Slot(inv, 2, x + 135, y) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return false;
			}

			@Override
			public int getSlotStackLimit() {
				return 1;
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
			if (_slot < 3) {
				if (!this.mergeItemStack(itemstack1, 3, 38, false)) return null;
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				if (itemstack.getItem().equals(Items.book)) {
					if (!this.mergeItemStack(itemstack1, 1, 1, true)) return null;
					slot.onSlotChange(itemstack1, itemstack);
				} else if (itemstack.isItemEnchanted() && !itemstack.getItem().equals(Items.enchanted_book)) {
					if (!this.mergeItemStack(itemstack1, 0, 0, true)) return null;
					slot.onSlotChange(itemstack1, itemstack);
				}
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
