package zabi.minecraft.perkmastery.network.packets;

import java.io.IOException;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.misc.Log;


public class SyncInventoryToClient implements IMessage {

	private String		player;
	private int			slot;
	private ItemStack	is;

	public SyncInventoryToClient() {
	}

	public SyncInventoryToClient(String player, int slot, ItemStack is) {
		Log.d("Starting sync to client for " + player);
		this.player = player;
		this.slot = slot;
		this.is = is;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int nameLenght = buf.readInt();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nameLenght; i++)
			sb.append(buf.readChar());
		player = sb.toString();

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
		int lenght = player.length();
		buf.writeInt(lenght);
		for (int i = 0; i < player.length(); i++)
			buf.writeChar(player.charAt(i));

		buf.writeInt(slot);

		PacketBuffer pb = new PacketBuffer(buf);
		try {
			pb.writeItemStackToBuffer(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static class Handler implements IMessageHandler<SyncInventoryToClient, IMessage> {

		@Override
		public IMessage onMessage(SyncInventoryToClient message, MessageContext ctx) {
			if (PerkMastery.proxy.getSinglePlayer().getDisplayName().equals(message.player)) ExtendedPlayer.setInventorySlot(PerkMastery.proxy.getSinglePlayer(), message.slot, message.is);
			return null;
		}

	}

}
