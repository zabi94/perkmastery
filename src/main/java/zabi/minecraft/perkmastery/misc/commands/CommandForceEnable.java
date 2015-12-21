package zabi.minecraft.perkmastery.misc.commands;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.misc.Log;
import zabi.minecraft.perkmastery.network.packets.SyncPlayer;


public class CommandForceEnable extends CommandBase {

	@SuppressWarnings("rawtypes")
	private final List aliases, numbers, classes, bools;

	@SuppressWarnings("unchecked")
	public CommandForceEnable() {
		aliases = new ArrayList<String>();
		aliases.add("perkmastery-force-enable");
		aliases.add("pema-fe");
		numbers = new ArrayList<String>(6);
		classes = new ArrayList<String>(6);
		for (int i = 1; i < 7; i++) {
			numbers.add("" + i);
			classes.add(PlayerClass.values()[i - 1].name().toLowerCase());
		}
		bools = new ArrayList<String>(2);
		bools.add("true");
		bools.add("false");

	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "perkmastery-force-enable";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "perkmastery-force-enable <username> <Class> <Level> <true|false>";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		try {
			for (Object oplayer : MinecraftServer.getServer().getConfigurationManager().playerEntityList.toArray()) {
				EntityPlayerMP player = (EntityPlayerMP) oplayer;
				if (player.getDisplayName().equals(args[0])) {
					for (int classPlayer = 0; classPlayer < 6; classPlayer++) {
						if (PlayerClass.values()[classPlayer].name().toLowerCase().equals(args[1])) {
							int level = Integer.parseInt(args[2]);
							boolean active = Boolean.parseBoolean(args[3]);
							ExtendedPlayer.forceToggle(player, active, PlayerClass.values()[classPlayer], level);
							PerkMastery.network.sendTo(new SyncPlayer(player), player);
							return;
						}
					}
					return;
				}
			}
		} catch (Exception e) {
			Log.e("Cannot execute command");
		}

	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender user) {
		return (!(user instanceof EntityPlayer) || ((user instanceof EntityPlayer) && (((EntityPlayer) user).capabilities.isCreativeMode)));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender user, String[] args) { // args non contiene il nome
		try {
			if (args.length == 1 || args.length == 0) return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
			if (args.length == 2) return classes;
			if (args.length == 3) return numbers;
			if (args.length == 4) return bools;
		} catch (Exception e) {
			Log.e("Error completing, catching silently");
			return null;
		}

		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}

}
