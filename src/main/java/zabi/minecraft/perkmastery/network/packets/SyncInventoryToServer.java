package zabi.minecraft.perkmastery.network.packets;

import java.io.IOException;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;


public class SyncInventoryToServer implements IMessage {

	private int			slot;
	private ItemStack	is;

	public SyncInventoryToServer() {
	}

	public SyncInventoryToServer(int slot, ItemStack is) {
		this.slot = slot;
		this.is = is;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		slot = buf.readInt();

		PacketBuffer pb = new PacketBuffer(buf);
		try {
			is = pb.readItemStackFromBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);

		PacketBuffer pb = new PacketBuffer(buf);
		try {
			pb.writeItemStackToBuffer(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static class Handler implements IMessageHandler<SyncInventoryToServer, IMessage> {

		@Override
		public IMessage onMessage(SyncInventoryToServer message, MessageContext ctx) {
			EntityPlayer p = ctx.getServerHandler().playerEntity;
			ExtendedPlayer.setInventorySlot(p, message.slot, message.is);

			PerkMastery.network.sendToAllAround(new SyncInventoryToClient(p.getDisplayName(), message.slot, message.is), new TargetPoint(p.dimension, p.posX, p.posY, p.posZ, 32));
			// Log.i(message.player+" received and synced");

			return null;
		}

	}

}
