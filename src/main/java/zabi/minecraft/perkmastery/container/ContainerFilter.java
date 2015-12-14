package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.PlayerFilterInventory;
import zabi.minecraft.perkmastery.misc.Log;

public class ContainerFilter extends ContainerBase {

	private EntityPlayer player;
	
	public ContainerFilter(EntityPlayer player) {
		this.player=player;
		
		PlayerFilterInventory inv=new PlayerFilterInventory(player);
		int x=44;
		int y=20;
		for (int i=0;i<5;i++) addSlotToContainer(new FilterSlot(inv, i, x + i * 18, y));
		addPlayerSlots(this.player.inventory,8,51);
	}
	
	public ItemStack slotClick(int slotIndex, int unk1, int unk2, EntityPlayer player) {
		try {
			if (slotIndex==-999 || !(this.getSlot(slotIndex) instanceof FilterSlot)) return super.slotClick(slotIndex, unk1, unk2, player);
			ItemStack iso=player.inventory.getItemStack();
			if (iso!=null) {
				ItemStack is=iso.copy();
				is.stackSize=1;
				((Slot)inventorySlots.get(slotIndex)).putStack(is);
			}
			else ((Slot)inventorySlots.get(slotIndex)).putStack(null);
			return null;
		} catch (Exception e) {
			Log.i("Fallback on error");
			return super.slotClick(slotIndex, unk1, unk2, player);
		}
	}

	
	//only to intercept instances in slotclick method
	public class FilterSlot extends Slot {

		public FilterSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
			super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		}

	}

}
