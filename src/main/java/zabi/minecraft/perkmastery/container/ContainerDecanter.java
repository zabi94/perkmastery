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
		addPlayerSlots(player.inventory, 8, 140);
		addSlotToContainer(new SlotBucket(tile, 0, 8, 16));
		addSlotToContainer(new SlotBottle(tile, 1, 79, 51));
		for (int i = 0; i < 7; i++)
			addSlotToContainer(new SlotIngredient(tile, i + 2, 32 + (i * 20), 16));
		for (int i = 0; i < 12; i++)
			addSlotToContainer(new SlotResult(tile, i + 9, xCb[i], yCb[i]));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 1);
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
