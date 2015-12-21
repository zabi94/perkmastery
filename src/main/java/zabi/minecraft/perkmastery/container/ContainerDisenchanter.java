package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.tileentity.TileEntityDisenchanter;


public class ContainerDisenchanter extends ContainerBase {

	private EntityPlayer player;

	public ContainerDisenchanter(EntityPlayer player, TileEntityDisenchanter inv) {
		this.player = player;
		addPlayerSlots(this.player.inventory);
		int x = 16, y = 35;
		this.addSlotToContainer(new Slot(inv, 0, x, y) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.isItemEnchanted() && !is.getItem().equals(Items.enchanted_book);
			}
		});

		this.addSlotToContainer(new Slot(inv, 1, x + 64, y - 1) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return is.getItem().equals(Items.book);
			}
		});

		this.addSlotToContainer(new Slot(inv, 2, x + 135, y) {

			@Override
			public boolean isItemValid(ItemStack is) {
				return false;
			}
		});

	}

}
