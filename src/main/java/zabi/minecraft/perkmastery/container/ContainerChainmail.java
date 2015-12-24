package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.PlayerExtraInventory;


public class ContainerChainmail extends ContainerBase {

	// 61,17
	// 61,53
	// 97,17
	// 97,53

	private EntityPlayer player;

	public ContainerChainmail(EntityPlayer player) {
		this.player = player;
		PlayerExtraInventory inv = new PlayerExtraInventory(player);
		addSlotToContainer(new Slot(inv, 19, 61, 13) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem() == Items.chainmail_helmet;
			}
		});
		addSlotToContainer(new Slot(inv, 20, 97, 13) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem() == Items.chainmail_chestplate;
			}
		});
		addSlotToContainer(new Slot(inv, 21, 61, 49) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem() == Items.chainmail_leggings;
			}
		});
		addSlotToContainer(new Slot(inv, 22, 97, 49) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem() == Items.chainmail_boots;
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
			if (_slot < 4) {
				if (!this.mergeItemStack(itemstack1, 4, 39, false)) return null;
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				if (itemstack.getItem().equals(Items.chainmail_chestplate)) {
					if (!this.mergeItemStack(itemstack1, 1, 1, true)) return null;
					slot.onSlotChange(itemstack1, itemstack);
				} else if (itemstack.getItem().equals(Items.chainmail_helmet)) {
					if (!this.mergeItemStack(itemstack1, 0, 0, true)) return null;
					slot.onSlotChange(itemstack1, itemstack);
				} else if (itemstack.getItem().equals(Items.chainmail_leggings)) {
					if (!this.mergeItemStack(itemstack1, 2, 2, true)) return null;
					slot.onSlotChange(itemstack1, itemstack);
				} else if (itemstack.getItem().equals(Items.chainmail_boots)) {
					if (!this.mergeItemStack(itemstack1, 3, 3, true)) return null;
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

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
