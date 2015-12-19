package zabi.minecraft.perkmastery.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;


public class ModBlockBase extends Block {

	protected ModBlockBase(Material p_i45394_1_) {
		super(p_i45394_1_);
	}

	public ModBlockBase(String name, float hardness, Material material) {
		super(material);
		this.setBlockName(name);
		this.setBlockTextureName(name);
		this.setHardness(hardness);
	}

	public void register() {
		GameRegistry.registerBlock(this, getUnlocalizedName().substring(5));

	}

}
