package zabi.minecraft.perkmastery.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.container.ContainerBase;
import zabi.minecraft.perkmastery.container.ContainerBoneAmulet;
import zabi.minecraft.perkmastery.container.ContainerChainmail;
import zabi.minecraft.perkmastery.container.ContainerDecanter;
import zabi.minecraft.perkmastery.container.ContainerEnchanter;
import zabi.minecraft.perkmastery.container.ContainerExtendedInventory;
import zabi.minecraft.perkmastery.container.ContainerFilter;
import zabi.minecraft.perkmastery.container.ContainerPortableFurnace;
import zabi.minecraft.perkmastery.gui.guis.GuiBoneAmulet;
import zabi.minecraft.perkmastery.gui.guis.GuiChainMail;
import zabi.minecraft.perkmastery.gui.guis.GuiDecanter;
import zabi.minecraft.perkmastery.gui.guis.GuiEnchanter;
import zabi.minecraft.perkmastery.gui.guis.GuiExtendedInventory;
import zabi.minecraft.perkmastery.gui.guis.GuiFilter;
import zabi.minecraft.perkmastery.gui.guis.GuiPerks;
import zabi.minecraft.perkmastery.gui.guis.GuiPortableFurnace;
import zabi.minecraft.perkmastery.misc.Log;
import zabi.minecraft.perkmastery.tileentity.TileEntityDecanter;
import zabi.minecraft.perkmastery.tileentity.TileEntityEnchanter;


public class GuiHandler implements IGuiHandler {

	public enum IDs {
		GUI_BOOK, GUI_BONE_AMULET, GUI_EXTENDED_INVENTORY, GUI_CHAINMAIL, GUI_FURNACE, GUI_DECANTER, GUI_FILTER, GUI_ENCHANTER, GUI_DISENCHANTER
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (IDs.values()[ID]) {

		case GUI_BOOK:
			return new ContainerBase();
		case GUI_BONE_AMULET:
			return new ContainerBoneAmulet(player);
		case GUI_CHAINMAIL:
			return new ContainerChainmail(player);
		case GUI_EXTENDED_INVENTORY:
			return new ContainerExtendedInventory(player);
		case GUI_FURNACE:
			return new ContainerPortableFurnace(player);
		case GUI_DECANTER:
			return new ContainerDecanter(player, (TileEntityDecanter) world.getTileEntity(x, y, z));
		case GUI_FILTER:
			return new ContainerFilter(player);
		case GUI_ENCHANTER:
			return new ContainerEnchanter(player, (TileEntityEnchanter) world.getTileEntity(x, y, z));
		default:
			Log.w("invalid GUI requested: " + ID);
			return null;

		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (IDs.values()[ID]) {
		case GUI_BOOK:
			return new GuiPerks((Container) getServerGuiElement(ID, player, world, x, y, z));
		case GUI_BONE_AMULET:
			return new GuiBoneAmulet((Container) getServerGuiElement(ID, player, world, x, y, z));
		case GUI_CHAINMAIL:
			return new GuiChainMail((Container) getServerGuiElement(ID, player, world, x, y, z));
		case GUI_EXTENDED_INVENTORY:
			return new GuiExtendedInventory((Container) getServerGuiElement(ID, player, world, x, y, z));
		case GUI_FURNACE:
			return new GuiPortableFurnace((Container) getServerGuiElement(ID, player, world, x, y, z));
		case GUI_DECANTER:
			return new GuiDecanter((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityDecanter) world.getTileEntity(x, y, z));
		case GUI_FILTER:
			return new GuiFilter((Container) getServerGuiElement(ID, player, world, x, y, z));
		case GUI_ENCHANTER:
			return new GuiEnchanter((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityEnchanter) world.getTileEntity(x, y, z));
		default:
			Log.w("invalid GUI requested");
			return null;

		}
	}

}
