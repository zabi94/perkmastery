package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.PlayerExtraInventory;


public class ContainerChainmail extends ContainerBase {
	
	//61,17
	//61,53
	//97,17
	//97,53
	
	private EntityPlayer player;
	
	public ContainerChainmail(EntityPlayer player) {
		this.player=player;
		PlayerExtraInventory inv=new PlayerExtraInventory(player);
		addSlotToContainer(new Slot(inv, 19, 61, 13) {
			@Override
			public boolean isItemValid(ItemStack is)  {
		        return is.getItem()==Items.chainmail_helmet;
		    }
		});
		addSlotToContainer(new Slot(inv, 20, 97, 13) {
			@Override
			public boolean isItemValid(ItemStack is)  {
		        return is.getItem()==Items.chainmail_chestplate;
		    }
		});
		addSlotToContainer(new Slot(inv, 21, 61, 49) {
			@Override
			public boolean isItemValid(ItemStack is)  {
		        return is.getItem()==Items.chainmail_leggings;
		    }
		});
		addSlotToContainer(new Slot(inv, 22, 97, 49) {
			@Override
			public boolean isItemValid(ItemStack is)  {
		        return is.getItem()==Items.chainmail_boots;
		    }
		});
		addPlayerSlots(this.player.inventory);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
