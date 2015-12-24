package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.tileentity.TileEntityDecanter;


public class ContainerDecanter extends ContainerBase {

	private TileEntityDecanter	tile;

	private static final int[]	xCb	= new int[] { 10, 33, 56, 56, 56, 79, 79, 102, 102, 102, 125, 148 };
	private static final int[]	yCb	= new int[] { 91, 98, 91, 71, 112, 78, 119, 71, 112, 91, 98, 91 };

	public ContainerDecanter(EntityPlayer player, TileEntityDecanter te) {
		tile = te;
		addSlotToContainer(new SlotBucket(tile, 0, 8, 16)); // 0: bucket
		addSlotToContainer(new SlotBottle(tile, 1, 79, 51)); // 1: bottles
		for (int i = 0; i < 7; i++)
			addSlotToContainer(new SlotIngredient(tile, i + 2, 32 + (i * 20), 16)); // 2->8: ingredienti
		for (int i = 0; i < 12; i++)
			addSlotToContainer(new SlotResult(tile, i + 9, xCb[i], yCb[i])); // 9->20: risultati
		addPlayerSlots(player.inventory, 8, 140); // 21->56: player
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 1);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int _slot) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(_slot);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (_slot < 21) {
				if (!this.mergeItemStack(itemstack1, 21, 56, true)) return null;
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				if (itemstack.getItem().equals(Items.water_bucket)) {
					if (!this.mergeItemStack(itemstack1, 0, 0, true)) return null;
					slot.onSlotChange(itemstack1, itemstack);
				} else if (itemstack.getItem().equals(Items.glass_bottle)) {
					if (!this.mergeItemStack(itemstack1, 1, 1, true)) return null;
					slot.onSlotChange(itemstack1, itemstack);
				} else if (itemstack.getItem().isPotionIngredient(itemstack) && !itemstack.getItem().equals(Items.glass_bottle)) {
					if (!this.mergeItemStack(itemstack1, 2, 8, false)) return null;
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

	public class SlotBucket extends Slot {

		public SlotBucket(IInventory inventory, int slot, int x, int y) {
			super(inventory, slot, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack is) {
			return is.getItem() == Items.water_bucket;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}

	}

	public class SlotBottle extends Slot {

		public SlotBottle(IInventory inventory, int slot, int x, int y) {
			super(inventory, slot, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack is) {
			return is.getItem() == Items.glass_bottle;
		}

	}

	public class SlotIngredient extends Slot {

		public SlotIngredient(IInventory inventory, int slot, int x, int y) {
			super(inventory, slot, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack is) {
			return is.getItem().isPotionIngredient(is);
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}
	}

	public class SlotResult extends Slot {

		public SlotResult(IInventory inventory, int slot, int x, int y) {
			super(inventory, slot, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack is) {
			return false;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}

	}

}
