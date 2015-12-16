package zabi.minecraft.perkmastery.tileentity;

import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityEnchanter extends TileBase implements IInventory {

	private ItemStack content=null;
	private static final String TAG="tile_data";
	
	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		if (tag.hasKey(TAG)) content=ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG));
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		if (content!=null) {
			NBTTagCompound data=new NBTTagCompound();
			content.writeToNBT(data);
			tag.setTag(TAG, data);
		}
	}

	@Override
	protected void tick() {} //Doesn't tick

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot!=0) return null;
		return content;
	}

	@Override
	public ItemStack decrStackSize(int slot, int qt) {
		if (slot!=0) return null;
		if (content.stackSize>qt) return content.splitStack(qt);
		ItemStack res=content.copy();
		content=null;
		return res;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		if (slot!=0) return;
		content=is;
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
	public boolean isUseableByPlayer(EntityPlayer player) {
		return ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 2);
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot!=0) return false;
		return is.isItemEnchantable();
	}

}
