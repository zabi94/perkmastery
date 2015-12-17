package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.tileentity.TileEntityEnchanter;

public class ContainerEnchanter extends ContainerBase {

	private EntityPlayer player;
	
	public ContainerEnchanter(EntityPlayer player, TileEntityEnchanter inv) {
		this.player=player;
		addPlayerSlots(this.player.inventory);
		int x=0,y=0;
		this.addSlotToContainer(new Slot(inv, 0, x, y) {
			@Override
			public boolean isItemValid(ItemStack is)  {
		        return is.isItemEnchantable();
		    }
		});
	}
	
	

}
