package zabi.minecraft.perkmastery.misc;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.network.packets.ReloadConfig;


public class CommandControl implements ICommand {

	@SuppressWarnings("rawtypes")
	private final List aliases;

	@SuppressWarnings("unchecked")
	public CommandControl() {
		aliases = new ArrayList<String>();
		aliases.add("perkmastery");
		aliases.add("pema");
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "perkmastery";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "perkmastery <reset|all|reload-config>";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] args) {
		EntityPlayer p = null;
		if (var1 instanceof EntityPlayer) p = (EntityPlayer) var1;
		if (p == null) {
			Log.e("ICommandSender error");
			return;
		}

		if (args.length != 1 || (!args[0].equals("reset") && !args[0].equals("all") && !args[0].equals("reload-config"))) {
			var1.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(var1)));
			return;
		}

		if (args[0].equals("reset")) {
			for (int i = 0; i < 6; i++) {
				ExtendedPlayer.setAbilityLevel(p, i, 0);
				ExtendedPlayer.setEnabledAbilities(p, i, (byte) 0);

				if (var1.getEntityWorld().isRemote && PerkMastery.proxy.getSinglePlayer().getDisplayName().equals(p.getDisplayName())) {
					ExtendedPlayer.setAbilityLevel(PerkMastery.proxy.getSinglePlayer(), i, 0);
					ExtendedPlayer.setEnabledAbilities(PerkMastery.proxy.getSinglePlayer(), i, (byte) 0);
				}
			}
			p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.abilitiesReset")));
		} else if (args[0].equals("all")) {

			if (var1.getEntityWorld().isRemote && PerkMastery.proxy.getSinglePlayer().getDisplayName().equals(p.getDisplayName())) {
				for (int i = 0; i < 6; i++)
					ExtendedPlayer.setAbilityLevel(PerkMastery.proxy.getSinglePlayer(), i, 6);
			}

			for (int i = 0; i < 6; i++)
				ExtendedPlayer.setAbilityLevel(p, i, 6);
			p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.allAbilitiesUnlocked")));
		} else if (args[0].equals("reload-config")) {
			PerkMastery.network.sendTo(new ReloadConfig(), (EntityPlayerMP) p);
		}

		ExtendedPlayer.syncToClient(p);

	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		EntityPlayer p = null;
		if (var1 instanceof EntityPlayer) p = (EntityPlayer) var1;
		if (p == null || !p.capabilities.isCreativeMode) {
			var1.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.cannotProcess")));
			return false;
		}
		return true;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
