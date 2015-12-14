package zabi.minecraft.perkmastery.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.MathHelper;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;

public class TileEntityDecanter extends TileBase implements IInventory {
	
	private ItemStack bucket,bottles;
	private ItemStack[] recipe=new ItemStack[7];
	private ItemStack[] result=new ItemStack[12];
	
	private static final String TAG_BUCKET="bucket";
	private static final String TAG_BOTTLE="bottle";
	private static final String TAG_PREFIX_IN="in_";
	private static final String TAG_PREFIX_OUT="out_";
	private static final String TAG_TIME="brewTime";
	
	private int progress=0;
	
	private static final int REQUIRED_TICKS=300;
	
	public TileEntityDecanter() {
		super();
	}

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		if (tag.hasKey(TAG_BUCKET)) bucket=ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_BUCKET));
		if (tag.hasKey(TAG_BOTTLE)) bottles=ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_BOTTLE));
		for (int i=0;i<recipe.length;i++) if (tag.hasKey(TAG_PREFIX_IN+i)) recipe[i]=ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_PREFIX_IN+i));
		for (int i=0;i<result.length;i++) if (tag.hasKey(TAG_PREFIX_OUT+i)) result[i]=ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_PREFIX_OUT+i));
		progress=tag.getInteger(TAG_TIME);
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		
		NBTTagCompound btag=new NBTTagCompound();
		if (bucket!=null) bucket.writeToNBT(btag);
		tag.setTag(TAG_BUCKET, btag);
		
		NBTTagCompound ctag=new NBTTagCompound();
		if (bottles!=null) bottles.writeToNBT(ctag);
		tag.setTag(TAG_BOTTLE, ctag);
		
		for (int i=0;i<recipe.length;i++) {
			NBTTagCompound dtag=new NBTTagCompound();
			if (recipe[i]!=null) recipe[i].writeToNBT(dtag);
			tag.setTag(TAG_PREFIX_IN+i, dtag);
		}
		
		for (int i=0;i<result.length;i++) {
			NBTTagCompound dtag=new NBTTagCompound();
			if (result[i]!=null) result[i].writeToNBT(dtag);
			tag.setTag(TAG_PREFIX_OUT+i, dtag);
		}
		tag.setInteger(TAG_TIME, progress);
	}

	@Override
	protected void tick() {
		if (isRunning()) {
			progress++;
			if (progress>=REQUIRED_TICKS) {
				craft();
				progress=0;
			}
		} else {
			progress=0;
		}
	}
	
	public boolean isRunning() {
		return bucket!=null && bucket.getItem().equals(Items.water_bucket) && bottles!=null && ingredientsInserted() && !isPowered();
	}
	
	private boolean isPowered() {
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	private boolean ingredientsInserted() {
		for (ItemStack is:recipe) if (is!=null) return true;
		return false;
	}

	private void craft() {
		ItemStack stack=getResultCrafting();
		int resNum=MathHelper.clamp_int(bottles.stackSize, 0, 12);
		bottles.stackSize-=resNum;
		if (bottles.stackSize<1) bottles=null;
		for (int i=0;i<resNum;i++) result[i]=stack.copy();
		recipe=new ItemStack[7];
		bucket=new ItemStack(Items.bucket);
		
	}

	
	private ItemStack getResultCrafting() {
		ItemStack res=new ItemStack(Items.potionitem);
		for (ItemStack is:recipe) res=getPartialResult(res, is);
		return res;
	}
	
	@SuppressWarnings("rawtypes")
	private ItemStack getPartialResult(ItemStack base, ItemStack ingredient) {

		if (ingredient==null) return base;

		if (base != null && base.getItem() instanceof ItemPotion) {
			base=base.copy();
			ingredient=ingredient.copy();
			int j = base.getItemDamage();
			int k = this.func_145936_c(j, ingredient);
			List list = Items.potionitem.getEffects(j);
			List list1 = Items.potionitem.getEffects(k);
			
			if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null)) {
				if (j != k) {
					base.setItemDamage(k);
				}
			} else if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
				base.setItemDamage(k);
			}
			return base;
		}
		return null;
		
	}
	
	

	public boolean isFull() {
		for (ItemStack res:result) if (res!=null) return true;
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 21;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot>21) return null;
		if (slot==0) return bucket;
		if (slot==1) return bottles;
		if (slot<9) return recipe[slot-2];
		return result[slot-9];
	}

	@Override
	public ItemStack decrStackSize(int slot, int qt) {
		ItemStack from=getStackInSlot(slot);
		ItemStack res=null;
		if (from==null) return null;
		if (from.stackSize>qt) return from.splitStack(qt);
		else {
			res=from.copy();
			setInventorySlotContents(slot, null);
			return res;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		if (slot==0) bucket=is;
		else if (slot==1) bottles=is;
		else if (slot<9) recipe[slot-2]=is;
		else result[slot-9]=is;
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
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 1);
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot==0) return is.getItem().equals(Items.water_bucket);
		if (slot==1) return is.getItem().equals(Items.glass_bottle);
		if (slot<9 && slot>1) return is.getItem().isPotionIngredient(is);
		return false;
	}
	
	public void dropContents() {
		for (int i=0;i<getSizeInventory();i++) {
			if (getStackInSlot(i)!=null) worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, this.getStackInSlot(i)));
		}
	}
	
	public double getProgress() {
		return ((double)progress/(double)REQUIRED_TICKS);
	}

	private int func_145936_c(int intero, ItemStack stack) {
        return stack == null ? intero : (stack.getItem().isPotionIngredient(stack) ? PotionHelper.applyIngredient(intero, stack.getItem().getPotionEffect(stack)) : intero);
    }
	
}
