package zabi.minecraft.perkmastery.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlockBase extends Block {

	protected ModBlockBase(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	public ModBlockBase(String name, float hardness, Material material) {
		super(material);
		this.setBlockName(name);
		this.setBlockTextureName(name);
		this.setHardness(hardness);
		onCreation();
	}
	
	public void register() {
		GameRegistry.registerBlock(this, getUnlocalizedName().substring(5));
		
	}
	
	public void onCreation() {}

	public void onWrench(ItemStack is, World w, int x, int y, int z, EntityPlayer p) {}
	
	
}
