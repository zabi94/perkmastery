package zabi.minecraft.perkmastery.blocks.special;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import zabi.minecraft.perkmastery.blocks.TileBlock;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.tileentity.TileEntityEnchanter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEnchanter extends TileBlock {

	private static IIcon texture_top,texture_bottom;
	
	public BlockEnchanter(String name, float hardness, Material material, CreativeTabs tab) {
		super(name, hardness, material, tab);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
	}

	@Override
	protected TileEntity getNewTileInstance() {
		return new TileEntityEnchanter();
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side==0) return texture_bottom;
		if (side==1) return texture_top;
		return blockIcon;
		
	}
	
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iir){
		texture_bottom=iir.registerIcon(LibGeneral.MOD_ID+":"+textureName+"_bottom");
		texture_top=iir.registerIcon(LibGeneral.MOD_ID+":"+textureName+"_top");
		blockIcon=iir.registerIcon(LibGeneral.MOD_ID+":"+textureName);
    }

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
