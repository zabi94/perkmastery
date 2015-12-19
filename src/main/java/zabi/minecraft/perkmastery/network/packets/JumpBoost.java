package zabi.minecraft.perkmastery.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;


public class JumpBoost implements IMessage {

	public JumpBoost() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<JumpBoost, IMessage> {

		@Override
		public IMessage onMessage(JumpBoost message, MessageContext ctx) {
			ctx.getServerHandler().playerEntity.motionY += 0.15F;
			ctx.getServerHandler().playerEntity.fallDistance -= 0.15F;
			return null;
		}

	}

}
