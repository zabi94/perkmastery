package zabi.minecraft.perkmastery.items.special;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.Config;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.items.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ExperienceTome extends ItemBase {

	public ExperienceTome(String modName, CreativeTabs tab) {
		super(modName,tab);
	}
	
	public ItemStack onEaten(ItemStack stack, World w, EntityPlayer p) {
        return stack;
    }
	
	public int getMaxItemUseDuration(ItemStack stack) {
        return 60;
    }
	
	public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 5)) {
			player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
			
			if (world.isRemote) {
				ArrayList<ChunkCoordinates> listaBlocchi=getBlocksInRadius(player, world, 8);
				signalBlocks(world, listaBlocchi);
			}
			
		} else {
			if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
		}
		return stack;
	}
	
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int ticksUsing) {
		ArrayList<ChunkCoordinates> listaBlocchi = getBlocksInRadius(player, world, 8);
		if (!world.isRemote && ticksUsing==0) {
			for (ChunkCoordinates coo:listaBlocchi) {
				world.setBlockToAir(coo.posX, coo.posY, coo.posZ);
				world.spawnEntityInWorld(new EntityXPOrb(world, coo.posX, coo.posY, coo.posZ, 10));
			}
			player.inventory.mainInventory[player.inventory.currentItem]=null;
		}
		
	}
	
	public void onUsingTick(ItemStack stack, EntityPlayer player, int tickInUse) {
		if (tickInUse==1) {
			onPlayerStoppedUsing(stack, player.worldObj, player, 0);
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	private void signalBlocks(World world, ArrayList<ChunkCoordinates> listaBlocchi) {
		double speed=2;
		for (ChunkCoordinates coo:listaBlocchi) for (int i=0;i<10;i++) {
			double vx=speed*(Math.random()-0.5);
			double vy=speed*(Math.random()-0.5);
			double vz=speed*(Math.random()-0.5);
			world.spawnParticle("happyVillager", coo.posX+0.5+(1.2*vx/speed), coo.posY+0.5+(1.2*vy/speed), coo.posZ+0.5+(1.2*vz/speed), vx, vy, vz);
		}
	}
	
	private ArrayList<ChunkCoordinates> getBlocksInRadius(EntityPlayer player, World world, int radius) {
		ArrayList<ChunkCoordinates> listaBlocchi=new ArrayList<ChunkCoordinates>();
		for (int sx=-radius;sx<=radius;sx++) for (int sy=-radius;sy<=radius;sy++) for (int sz=-radius;sz<=radius;sz++) {
			if (world.getBlock((int)player.posX+sx,(int) player.posY+sy,(int) player.posZ+sz).equals(Blocks.bookshelf))
				listaBlocchi.add(new ChunkCoordinates((int)player.posX+sx,(int) player.posY+sy,(int) player.posZ+sz));
			if (listaBlocchi.size()>=Config.maxBookshelvesScanned && Config.maxBookshelvesScanned>0) break;
		}
		return listaBlocchi;
	}

}
