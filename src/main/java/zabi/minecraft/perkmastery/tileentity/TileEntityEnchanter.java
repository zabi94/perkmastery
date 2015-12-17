package zabi.minecraft.perkmastery.tileentity;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;

public class TileEntityEnchanter extends TileBase implements IInventory {

	private ItemStack content=null;
	private int ticks=0;
	private Random rnd;
	private static final String TAG="tile_data";
	private static final String T_TAG="req_ticks";
	private static final int REQUIRED_TICKS=2;
	
	public TileEntityEnchanter() {
		rnd=new Random();
	}
	
	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		if (tag.hasKey(TAG)) content=ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG));
//		content=null;
		if (tag.hasKey(T_TAG)) ticks=tag.getInteger(T_TAG);
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		if (content!=null) {
			NBTTagCompound data=new NBTTagCompound();
			content.writeToNBT(data);
			tag.setTag(TAG, data);
			if (ticks!=0) tag.setInteger(T_TAG, ticks);
		}
	}

	@Override
	protected void tick() {
		
		if (worldObj.isRemote) return;
		
		if (content!=null && !content.isItemEnchanted() && ticks<=REQUIRED_TICKS) {
			if (ticks==REQUIRED_TICKS) enchant();
			ticks++;
		} else {
			ticks=0;
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void enchant() {
		List<EnchantmentData> enchantments=EnchantmentHelper.buildEnchantmentList(rnd, content, EnchantmentHelper.calcItemStackEnchantability(rnd, 2, 3, content)*2);
		for (EnchantmentData data:enchantments) content.addEnchantment(data.enchantmentobj, data.enchantmentLevel);
	}

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
