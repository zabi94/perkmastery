package zabi.minecraft.perkmastery.tileentity;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;


public class TileEntityDisenchanter extends TileBase implements IInventory {

	private ItemStack			input					= null;
	private ItemStack			books					= null;
	private ItemStack			output					= null;

	private int					ticks					= 0;
	private Random				rnd;

	private static final String	TAG_ITEMSTACK_INPUT		= "input_stack";
	private static final String	TAG_ITEMSTACK_BOOKS		= "books_stack";
	private static final String	TAG_ITEMSTACK_OUTPUT	= "output_stack";
	private static final String	TAG_PROGRESS			= "req_ticks";
	private static final int	REQUIRED_TICKS			= 200;

	public TileEntityDisenchanter() {
		rnd = new Random();
	}

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		if (tag.hasKey(TAG_ITEMSTACK_INPUT)) input = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_ITEMSTACK_INPUT));
		if (tag.hasKey(TAG_ITEMSTACK_OUTPUT)) output = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_ITEMSTACK_OUTPUT));
		if (tag.hasKey(TAG_ITEMSTACK_BOOKS)) books = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_ITEMSTACK_BOOKS));
		if (tag.hasKey(TAG_PROGRESS)) ticks = tag.getInteger(TAG_PROGRESS);
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		if (ticks != 0) tag.setInteger(TAG_PROGRESS, ticks);
		if (input != null) {
			NBTTagCompound data = new NBTTagCompound();
			input.writeToNBT(data);
			tag.setTag(TAG_ITEMSTACK_INPUT, data);
		}

		if (output != null) {
			NBTTagCompound data = new NBTTagCompound();
			output.writeToNBT(data);
			tag.setTag(TAG_ITEMSTACK_OUTPUT, data);
		}

		if (books != null) {
			NBTTagCompound data = new NBTTagCompound();
			books.writeToNBT(data);
			tag.setTag(TAG_ITEMSTACK_BOOKS, data);
		}
	}

	@Override
	protected void tick() {
		if (input != null && books != null && books.getItem().equals(Items.book)) {
			ticks++;
		} else {
			ticks = 0;
		}

		if (ticks >= REQUIRED_TICKS) {
			disenchant();
			ticks = 0;
		}
	}

	private void disenchant() {
		if (worldObj.isRemote) return;

		NBTTagList nbttaglist = input.getEnchantmentTagList();

		int[] incanti = new int[nbttaglist.tagCount()];
		int[] livelli = new int[nbttaglist.tagCount()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			incanti[i] = nbttaglist.getCompoundTagAt(i).getShort("id");
			livelli[i] = nbttaglist.getCompoundTagAt(i).getShort("lvl");
		}

		if (incanti.length > 0) {
			input.stackTagCompound.removeTag("ench");
			for (int i = 0; i < incanti.length - 1; i++) {
				input.addEnchantment(Enchantment.enchantmentsList[incanti[i]], livelli[i]);
			}

			// if (input.getEnchantmentTagList().tagCount() == 0) input.stackTagCompound.removeTag("ench");

			ItemStack ebook = new ItemStack(Items.enchanted_book);
			ebook.addEnchantment(Enchantment.enchantmentsList[incanti[incanti.length - 1]], livelli[livelli.length - 1]);
			input.attemptDamageItem(input.getMaxDamage() / 5, rnd);
			books = ebook;
			output = input.copy();
			input = null;
		} else {
			output = input.copy();
			input = null;
		}
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot == 2) return output;
		if (slot == 1) return books;
		if (slot == 0) return input;
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int qt) {
		switch (slot) {
		case 0:
			if (input == null) return null;
			if (input.stackSize > qt) return input.splitStack(qt);
			ItemStack res = input.copy();
			input = null;
			return res;
		case 1:
			if (books == null) return null;
			if (books.stackSize > qt) return books.splitStack(qt);
			ItemStack res2 = books.copy();
			books = null;
			return res2;
		case 2:
			if (output == null) return null;
			if (output.stackSize > qt) return output.splitStack(qt);
			ItemStack res3 = output.copy();
			output = null;
			return res3;
		default:
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		if (slot == 0) input = is;
		if (slot == 1) books = is;
		if (slot == 2) output = is;
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
		return ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 3);
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot > 1) return false;
		if (slot == 0) return is.isItemEnchanted() && !is.getItem().equals(Items.enchanted_book);
		if (slot == 1) return is.getItem().equals(Items.book);
		return false;
	}

	public double getProgress() {
		return (double) ticks / (double) REQUIRED_TICKS;
	}

}
