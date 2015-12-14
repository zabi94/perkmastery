package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import zabi.minecraft.perkmastery.entity.PlayerExtraInventory;

public class ContainerExtendedInventory extends ContainerBase {

	private EntityPlayer player;
	
	public ContainerExtendedInventory(EntityPlayer player) {
		this.player=player;
		addPlayerSlots(this.player.inventory,8,51);
		PlayerExtraInventory inv=new PlayerExtraInventory(player);
		int x=8;
		int y=15;
		for (int i=0;i<9;i++) for (int j=0;j<2;j++) addSlotToContainer(new Slot(inv, i + 9*j, x + i * 18, y + j * 18));
	}
	
}
