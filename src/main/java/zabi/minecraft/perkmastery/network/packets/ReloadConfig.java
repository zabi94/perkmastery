package zabi.minecraft.perkmastery.network.packets;

import zabi.minecraft.perkmastery.Config;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ReloadConfig implements IMessage {
	
	public ReloadConfig() {}

	@Override
	public void fromBytes(ByteBuf buf) {
		
	}

	@Override
	public void toBytes(ByteBuf buf) {

	}
	
	
	public static class Handler implements IMessageHandler<ReloadConfig, IMessage> {

		@Override
		public IMessage onMessage(ReloadConfig message, MessageContext ctx) {
			Config.reload();
			return null;
		}
		
	}
	
	

}
