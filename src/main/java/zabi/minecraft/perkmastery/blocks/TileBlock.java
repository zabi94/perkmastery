package zabi.minecraft.perkmastery.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class TileBlock extends BlockContainer {

	
	public TileBlock(String name, float hardness, Material material ,CreativeTabs tab) {
		super(material);
		this.setCreativeTab(tab);
		this.setBlockTextureName(LibGeneral.MOD_ID+":"+name);
		this.setBlockName(name);
		this.setHardness(hardness);
	}
	
	
	public void register() {
		GameRegistry.registerBlock(this, getUnlocalizedName().substring(5));
	}


	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;

	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int integ) {
		return getNewTileInstance();
	}


	protected abstract TileEntity getNewTileInstance();
	
	
}
