package zabi.minecraft.perkmastery.network.packets;

import java.io.IOException;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.InventoryType;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.handlers.ToggleHandler;
import zabi.minecraft.perkmastery.misc.Log;


public class SyncPlayer implements IMessage {

	protected EntityPlayer	p;
	protected int[]			abs			= new int[6];
	protected int[]			furn		= new int[3];
	protected byte[]		ens			= new byte[6];
	protected ItemStack[]	filter		= new ItemStack[5];
	protected ItemStack[]	inventory	= new ItemStack[26];

	// 0-17=Extra inventory
	// 18=bone talisman
	// 19,20,21,22=Chainmail
	// 23,24,25=Furnace in, out, coal

	public SyncPlayer() {
	}

	public SyncPlayer(EntityPlayer p) {
		this.p = p;
		filter = ExtendedPlayer.getExtraInventory(p, InventoryType.FILTER);
		inventory = ExtendedPlayer.getExtraInventory(p, InventoryType.REAL);
		furn = ExtendedPlayer.getFurnaceData(p);
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		for (int i = 0; i < 6; i++)
			abs[i] = buf.readInt();
		for (int i = 0; i < 6; i++)
			ens[i] = buf.readByte();

		PacketBuffer pb = new PacketBuffer(buf);
		for (int i = 0; i < filter.length; i++) {
			if (buf.readBoolean()) {
				try {
					filter[i] = pb.readItemStackFromBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < inventory.length; i++) {
			if (buf.readBoolean()) {
				try {
					inventory[i] = pb.readItemStackFromBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			for (int i = 0; i < 3; i++)
				furn[i] = buf.readInt();
		} catch (Exception e) {
			Log.e("Cannot read furnace data");
		}

	}

	@Override
	public void toBytes(ByteBuf buf) {
		for (int in : ExtendedPlayer.getAbilities(p))
			buf.writeInt(in);
		for (byte bt : ExtendedPlayer.getEnabledAbilities(p))
			buf.writeByte(bt);

		PacketBuffer pb = new PacketBuffer(buf);
		for (ItemStack is : filter) {
			buf.writeBoolean(is != null);
			if (is != null) try {
				pb.writeItemStackToBuffer(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (ItemStack is : inventory) {
			buf.writeBoolean(is != null);
			if (is != null) try {
				pb.writeItemStackToBuffer(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (furn != null) for (int i : furn)
			buf.writeInt(i);
	}

	public static class Handler implements IMessageHandler<SyncPlayer, IMessage> {

		@Override
		public IMessage onMessage(SyncPlayer message, MessageContext ctx) {
			EntityPlayer p = PerkMastery.proxy.getSinglePlayer();
			for (int i = 0; i < 6; i++)
				ExtendedPlayer.setAbilityLevel(p, i, message.abs[i]);
			for (int i = 0; i < 6; i++)
				ExtendedPlayer.setEnabledAbilities(p, i, message.ens[i]);
			ExtendedPlayer.setInventory(p, message.inventory);
			ExtendedPlayer.setFilter(p, message.filter);
			ExtendedPlayer.setFurnaceData(p, message.furn);
			ToggleHandler.toggleReachDistance(p, ExtendedPlayer.isEnabled(p, PlayerClass.BUILDER, 1));
			ToggleHandler.toggleWellTrained(p, ExtendedPlayer.isEnabled(p, PlayerClass.EXPLORER, 4));
			ToggleHandler.toggleExpertEye(p, ExtendedPlayer.isEnabled(p, PlayerClass.MINER, 3));
			return null;
		}

	}

}
