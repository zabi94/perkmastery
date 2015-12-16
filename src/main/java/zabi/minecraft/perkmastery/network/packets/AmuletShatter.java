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
	

	
	public AmuletShatter() {}


	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
	
	
	public static class Handler implements IMessageHandler<AmuletShatter, IMessage> {

		@Override
		public IMessage onMessage(AmuletShatter message, MessageContext ctx) {
			ExtendedPlayer.destroyAmulet(PerkMastery.proxy.getSinglePlayer());
			PerkMastery.proxy.getSinglePlayer().addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.amuletShatter")));
			return null;
		}
		
	}
	
	

}
