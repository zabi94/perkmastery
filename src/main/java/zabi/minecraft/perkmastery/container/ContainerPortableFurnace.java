package zabi.minecraft.perkmastery.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import zabi.minecraft.perkmastery.entity.PlayerExtraInventory;
import zabi.minecraft.perkmastery.entity.PortableFurnaceData;


public class ContainerPortableFurnace extends ContainerBase {

	private int					lastCookTime;
	private int					lastBurnTime;
	private int					lastItemBurnTime;
	public PortableFurnaceData	tileFurnace;

	public ContainerPortableFurnace(EntityPlayer player) {
		PlayerExtraInventory pei = new PlayerExtraInventory(player);
		this.addSlotToContainer(new Slot(pei, 23, 56, 17));
		this.addSlotToContainer(new Slot(pei, 25, 56, 53));
		this.addSlotToContainer(new SlotFurnace(player, pei, 24, 116, 35));
		addPlayerSlots(player.inventory);
		tileFurnace = PortableFurnaceData.getDataFor(player);

	}

	public void addCraftingToCrafters(ICrafting crft) {
		super.addCraftingToCrafters(crft);
		crft.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
		crft.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
		crft.sendProgressBarUpdate(this, 2, this.tileFurnace.currentItemBurnTime);
		tileFurnace.write();
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (this.lastCookTime != this.tileFurnace.furnaceCookTime) {
				icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
			}

			if (this.lastBurnTime != this.tileFurnace.furnaceBurnTime) {
				icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
			}

			if (this.lastItemBurnTime != this.tileFurnace.currentItemBurnTime) {
				icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.currentItemBurnTime);
			}
		}

		this.lastCookTime = this.tileFurnace.furnaceCookTime;
		this.lastBurnTime = this.tileFurnace.furnaceBurnTime;
		this.lastItemBurnTime = this.tileFurnace.currentItemBurnTime;
		tileFurnace.write();
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
		if (p_75137_1_ == 0) {
			this.tileFurnace.furnaceCookTime = p_75137_2_;
		}

		if (p_75137_1_ == 1) {
			this.tileFurnace.furnaceBurnTime = p_75137_2_;
		}

		if (p_75137_1_ == 2) {
			this.tileFurnace.currentItemBurnTime = p_75137_2_;
		}
		tileFurnace.write();
	}

	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNum);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNum == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 38, true)) return null;
				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotNum != 1 && slotNum != 0) {
				if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
					if (!this.mergeItemStack(itemstack1, 0, 0, false)) return null;
				} else if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 1, 1, false)) return null;
				} else if (slotNum >= 3 && slotNum < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 38, false)) return null;
				} else if (slotNum >= 30 && slotNum < 39 && !this.mergeItemStack(itemstack1, 3, 29, false)) return null;
			} else if (!this.mergeItemStack(itemstack1, 3, 38, false)) return null;

			if (itemstack1.stackSize == 0) slot.putStack((ItemStack) null);
			else
				slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize) return null;
			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}
}