package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.PlayerExtraInventory;
import zabi.minecraft.perkmastery.items.ItemList;


public class ContainerBoneAmulet extends ContainerBase {

	private EntityPlayer player;

	public ContainerBoneAmulet(EntityPlayer player) {
		this.player = player;
		addSlotToContainer(new Slot(new PlayerExtraInventory(player), 18, 79, 35) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem() == ItemList.boneAmulet;
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
			} else if (itemstack.getItem().equals(ItemList.boneAmulet)) {
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

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
