package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.entity.PlayerExtraInventory;
import zabi.minecraft.perkmastery.items.ItemList;


public class ContainerBoneAmulet extends ContainerBase {

	private EntityPlayer player;

	public ContainerBoneAmulet(EntityPlayer player) {
		this.player = player;
		addSlotToContainer(new Slot(new PlayerExtraInventory(player), 18, 79, 35) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem() == ItemList.boneAmulet;
			}
		});

		addPlayerSlots(this.player.inventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
