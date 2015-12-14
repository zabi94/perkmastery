package zabi.minecraft.perkmastery.network.packets;

import io.netty.buffer.ByteBuf;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ToggleAbility implements IMessage {
	
	private int tree, level;
	private boolean active;
	
	public ToggleAbility() {}
	
	public ToggleAbility(boolean status, int tree, int level) {
		this.tree=tree;
		this.level=level;
		active=status;
		
//		Log.i("Toggled client");
//		NBTTagCompound tg=(NBTTagCompound) PerkMastery.proxy.getSinglePlayer().getEntityData().getTag(ExtendedPlayer.TAG_PREFIX);
//		Log.i(tg.toString());
		
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tree=buf.readInt();
		level=buf.readInt();
		active=buf.readBoolean();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(tree);
		buf.writeInt(level);
		buf.writeBoolean(active);
	}
	
	
	public static class Handler implements IMessageHandler<ToggleAbility, IMessage> {

		@Override
		public IMessage onMessage(ToggleAbility message, MessageContext ctx) {
			ExtendedPlayer.toggle(ctx.getServerHandler().playerEntity,message.active, PlayerClass.values()[message.tree],message.level);
//			Log.i("Toggled Server:");
//			NBTTagCompound tg=(NBTTagCompound) ctx.getServerHandler().playerEntity.getEntityData().getTag(ExtendedPlayer.TAG_PREFIX);
//			Log.i(tg.toString());
			return null;
		}
		
	}
	
	

}
