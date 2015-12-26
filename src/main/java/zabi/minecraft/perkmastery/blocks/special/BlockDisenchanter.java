package zabi.minecraft.perkmastery.blocks.special;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.blocks.TileBlock;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.gui.GuiHandler;
import zabi.minecraft.perkmastery.tileentity.TileEntityDisenchanter;


public class BlockDisenchanter extends TileBlock {

	private static IIcon texture_top, texture_bottom;

	public BlockDisenchanter(String name, float hardness, Material material, CreativeTabs tab) {
		super(name, hardness, material, tab);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.68F, 1.0F);
	}

	@Override
	protected TileEntity getNewTileInstance() {
		return new TileEntityDisenchanter();
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 0) return texture_bottom;
		if (side == 1) return texture_top;
		return blockIcon;

	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iir) {
		texture_bottom = iir.registerIcon(textureName + "_bottom");
		texture_top = iir.registerIcon(textureName + "_top");
		blockIcon = iir.registerIcon(textureName);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int int1, float f1, float f2, float f3) {
		if (ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 3)) player.openGui(PerkMastery.instance, GuiHandler.IDs.GUI_DISENCHANTER.ordinal(), world, x, y, z);
		else if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		((TileEntityDisenchanter) world.getTileEntity(x, y, z)).dropContents();
		super.breakBlock(world, x, y, z, block, meta);
	}

	// @SideOnly(Side.CLIENT)
	// @Override
	// public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {
	// Minecraft.getMinecraft().effectRenderer.addEffect(new RuneFadeFX(world, x + 0.5, y + 0.4, z + 0.5, ((TileEntityEnchanter) world.getTileEntity(x, y, z)).isErrored()));
	// }

	public boolean renderAsNormalBlock() {
		return false;
	}
}
