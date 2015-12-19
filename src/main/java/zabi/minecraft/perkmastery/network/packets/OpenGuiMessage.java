package zabi.minecraft.perkmastery.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.gui.GuiHandler;


public class OpenGuiMessage implements IMessage {

	private int guiExtra = -1;

	public OpenGuiMessage() {
	}

	public OpenGuiMessage(int guiVar) {
		guiExtra = guiVar;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		guiExtra = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(guiExtra);
	}

	public static class Handler implements IMessageHandler<OpenGuiMessage, IMessage> {

		@Override
		public IMessage onMessage(OpenGuiMessage message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			// Log.i(message.guiExtra);
			if (message.guiExtra == -1) player.openGui(PerkMastery.instance, GuiHandler.IDs.GUI_BOOK.ordinal(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			else if (message.guiExtra == 1) {
				((EntityPlayerMP) player).getNextWindowId();
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(((EntityPlayerMP) player).currentWindowId, 1, "Crafting", 9, true));
				((EntityPlayerMP) player).openContainer = new ContainerWorkbench(((EntityPlayerMP) player).inventory, ((EntityPlayerMP) player).worldObj, (int) player.posX, (int) player.posY, (int) player.posZ) {

					public boolean canInteractWith(EntityPlayer p_75145_1_) {
						return true;
					}
				};
				((EntityPlayerMP) player).openContainer.windowId = ((EntityPlayerMP) player).currentWindowId;
				((EntityPlayerMP) player).openContainer.addCraftingToCrafters(((EntityPlayerMP) player));
			} else {
				if (message.guiExtra == 0) message.guiExtra = GuiHandler.IDs.GUI_EXTENDED_INVENTORY.ordinal() + 2;
				else if (message.guiExtra == 2) message.guiExtra = GuiHandler.IDs.GUI_FURNACE.ordinal() + 2;
				else if (message.guiExtra == 4) message.guiExtra = GuiHandler.IDs.GUI_FILTER.ordinal() + 2;
				player.openGui(PerkMastery.instance, message.guiExtra - 2, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			return null;
		}

	}

}
