package zabi.minecraft.perkmastery.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;


public class ToggleAbilityForce implements IMessage {

	private int		tree, level;
	private boolean	active;

	public ToggleAbilityForce() {
	}

	public ToggleAbilityForce(boolean status, int tree, int level) {
		this.tree = tree;
		this.level = level;
		active = status;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tree = buf.readInt();
		level = buf.readInt();
		active = buf.readBoolean();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(tree);
		buf.writeInt(level);
		buf.writeBoolean(active);
	}

	public static class Handler implements IMessageHandler<ToggleAbilityForce, IMessage> {

		@Override
		public IMessage onMessage(ToggleAbilityForce message, MessageContext ctx) {
			ExtendedPlayer.forceToggle(ctx.getServerHandler().playerEntity, message.active, PlayerClass.values()[message.tree], message.level);
			return null;
		}

	}

}
