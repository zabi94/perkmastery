package zabi.minecraft.perkmastery.blocks.special;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.blocks.TileBlock;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.gui.GuiHandler;
import zabi.minecraft.perkmastery.tileentity.TileEntityDecanter;

public class BlockDecanter extends TileBlock {

	public BlockDecanter(String name) {
		super(name, 2.0F, Material.iron, CreativeTabs.tabBrewing);
	}

	@Override
	protected TileEntity getNewTileInstance() {
		return new TileEntityDecanter();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int int1, float f1, float f2, float f3) {
		if (ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 1)) player.openGui(PerkMastery.instance, GuiHandler.IDs.GUI_DECANTER.ordinal(), world, x, y, z);
		else if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
		return true;
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		((TileEntityDecanter)world.getTileEntity(x, y, z)).dropContents();
		super.breakBlock(world, x, y, z, block, meta);
	}
	

}
