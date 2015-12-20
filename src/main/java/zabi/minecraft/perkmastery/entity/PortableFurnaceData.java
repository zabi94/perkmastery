package zabi.minecraft.perkmastery.entity;

import java.util.Hashtable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.MinecraftServer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.InventoryType;


public class PortableFurnaceData {

	EntityPlayer												player;

	public int													furnaceCookTime, furnaceBurnTime, currentItemBurnTime;
	public long													time	= -1;
	private static Hashtable<EntityPlayer, PortableFurnaceData>	table	= new Hashtable<EntityPlayer, PortableFurnaceData>();

	private PortableFurnaceData(EntityPlayer p) {
		player = p;
		int[] fdata = ExtendedPlayer.getFurnaceData(player);
		if (fdata != null) {
			if (fdata.length != 3) fdata = new int[] { 0, 0, 0 };
			furnaceCookTime = fdata[0];
			furnaceBurnTime = fdata[1];
			currentItemBurnTime = fdata[2];
		} else {
			furnaceBurnTime = furnaceCookTime = currentItemBurnTime = 0;
		}
		write();
	}

	public void write() {
		ExtendedPlayer.setFurnaceData(player, new int[] { furnaceCookTime, furnaceBurnTime, currentItemBurnTime });
	}

	public static PortableFurnaceData getDataFor(EntityPlayer player) {
		if (table.containsKey(player)) return table.get(player);
		table.put(player, new PortableFurnaceData(player));
		return table.get(player);
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int scale) {
		if (this.currentItemBurnTime == 0) this.currentItemBurnTime = 200;
		return this.furnaceBurnTime * scale / this.currentItemBurnTime;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int scale) {
		return this.furnaceCookTime * scale / 200;
	}

	public boolean canSmelt() {
		ItemStack[] inv = ExtendedPlayer.getExtraInventory(player, InventoryType.REAL);

		if (inv[23] == null) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(inv[23]);
			if (itemstack == null) return false;
			if (inv[24] == null) return true;
			if (!inv[24].isItemEqual(itemstack)) return false;
			int result = inv[24].stackSize + itemstack.stackSize;
			return result <= 64 && result <= inv[24].getMaxStackSize();
		}
	}

	public static void smeltItem(ItemStack[] inv, PortableFurnaceData data, EntityPlayer player) {

		if (Math.abs(data.time - getTime()) < 200) { return; }
		data.time = getTime();

		if (data.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(inv[23]);

			if (inv[24] == null) {
				ExtendedPlayer.setInventorySlot(player, 24, itemstack.copy());
			} else if (inv[24].getItem() == itemstack.getItem()) {
				inv[24].stackSize += itemstack.stackSize;
				ExtendedPlayer.setInventorySlot(player, 24, inv[24]);
			}

			--inv[23].stackSize;

			if (inv[23].stackSize <= 0) {
				inv[23] = null;
			}
			ExtendedPlayer.setInventorySlot(player, 23, inv[23]);
		}
		data.write();
	}

	private static long getTime() {
		if (FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT)) {
			return Minecraft.getSystemTime();
		} else {
			return MinecraftServer.getSystemTimeMillis();
		}
	}

}
