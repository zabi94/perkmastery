package zabi.minecraft.perkmastery.items.special;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.Config;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.handlers.TickHandler;
import zabi.minecraft.perkmastery.items.ItemBase;

public class ReturnTome extends ItemBase {

	public ReturnTome(String modName, CreativeTabs tab) {
		super(modName, tab);
	}
	
	public ItemStack onEaten(ItemStack stack, World w, EntityPlayer p) {
        return stack;
    }
	
	public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }
	
	public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 4)) {
			
			if (player.experienceTotal<Config.teleportationCost) {
				if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.notenoughexperience")));
				return stack;
			}
			
			
			player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		} else {
			if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
		}
		return stack;
	}
	
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int ticksUsing) {
		if (!world.isRemote && getMaxItemUseDuration(stack)-ticksUsing>40) {
			ChunkCoordinates bed=player.getBedLocation(world.provider.dimensionId);
			if (bed!=null && askExperience(player,Config.teleportationCost)) 
				player.setPositionAndUpdate(bed.posX+0.5, bed.posY+0.8, bed.posZ+0.5);
		}
	}

	private boolean askExperience(EntityPlayer player, int quantity) {
		if (player.capabilities.isCreativeMode) return true;
		if (player.experienceTotal>=quantity) {
			TickHandler.addExperience(player,-quantity);
			return true;
		}		
		return false;
	}
	
}
