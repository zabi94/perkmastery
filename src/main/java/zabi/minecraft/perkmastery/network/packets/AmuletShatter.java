package zabi.minecraft.perkmastery.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class AmuletShatter implements IMessage {
	
	private String player;
	
	public AmuletShatter() {}
	
	public AmuletShatter(String player) {
		this.player=player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int nameLenght=buf.readInt();
		StringBuilder sb=new StringBuilder();
		for (int i=0;i<nameLenght;i++) sb.append(buf.readChar());
		player=sb.toString();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		int lenght=player.length();
		buf.writeInt(lenght);
		for (int i=0;i<player.length();i++) buf.writeChar(player.charAt(i));
	}
	
	
	public static class Handler implements IMessageHandler<AmuletShatter, IMessage> {

		@Override
		public IMessage onMessage(AmuletShatter message, MessageContext ctx) {
			if (PerkMastery.proxy.getSinglePlayer().getDisplayName().equals(message.player)) {
				ExtendedPlayer.destroyAmulet(PerkMastery.proxy.getSinglePlayer());
				PerkMastery.proxy.getSinglePlayer().addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.amuletShatter")));
			}
			return null;
		}
		
	}
	
	

}
