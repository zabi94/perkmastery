package zabi.minecraft.perkmastery.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.visual.effects.EffectRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;

public class ToggleHandler {

	public static void toggleWellTrained(EntityPlayer player, boolean enable) {
		if (!ExtendedPlayer.hasPlayerAquired(player, PlayerClass.EXPLORER,4)) return;
		player.stepHeight=enable?1F:0.5F;
		try {
			if (PerkMastery.proxy.getSinglePlayer().getDisplayName().equals(player.getDisplayName())) {
				PerkMastery.proxy.getSinglePlayer().stepHeight=enable?1F:0.5F;
				player.stepHeight=enable?1F:0.5F;
			}
		} catch (NullPointerException e) {} //Cerca di caricare m.gm.theplayer quando non è ancora loggato
		
		ReflectionHelper.setPrivateValue(PlayerCapabilities.class, player.capabilities, enable?0.25F:0.1F, "walkSpeed","field_75097_g");
		
		
	}

	public static void toggleReachDistance(EntityPlayer player, boolean enable) {
				
		if (ExtendedPlayer.getAbilityLevelFor(player, PlayerClass.BUILDER)==0) return;
		
		if (FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT && player.getDisplayName().equals(PerkMastery.proxy.getSinglePlayer().getDisplayName())) {
			PerkMastery.proxy.setupHackyController(enable);
		} else {
			if (enable) {
				((EntityPlayerMP)player).theItemInWorldManager.setBlockReachDistance(9.0D);
			} else {
				((EntityPlayerMP)player).theItemInWorldManager.setBlockReachDistance(player.capabilities.isCreativeMode?5D:4.5D);
			}
		}
		
		
	}

	public static void toggleExpertEye(EntityPlayer player, boolean toggle) {
		
		if (!ExtendedPlayer.hasPlayerAquired(player, PlayerClass.MINER, 3)) return;
		
		if (player.worldObj.isRemote) {
			if (toggle) {
				EffectRegistry.addEffect(new OreDetectionHandler(player, player.worldObj));
			} else {
				EffectRegistry.purge();
			}
		}
	}

}
