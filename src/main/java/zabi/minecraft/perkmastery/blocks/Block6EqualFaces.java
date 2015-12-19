package zabi.minecraft.perkmastery.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class Block6EqualFaces extends ModBlockBase {

	@SideOnly(Side.CLIENT)
	protected IIcon blockTexture;

	public Block6EqualFaces(String string, float hardness, Material material) {
		super(string, hardness, material);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockTexture;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iir) {
		this.blockTexture = iir.registerIcon(LibGeneral.MOD_ID + ":" + textureName);
	}

}
