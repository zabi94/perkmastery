package zabi.minecraft.perkmastery.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import zabi.minecraft.perkmastery.blocks.special.BlockDecanter;
import zabi.minecraft.perkmastery.blocks.special.BlockEnchanter;
import zabi.minecraft.perkmastery.blocks.special.BlockScaffold;


public class BlockList {

	public static final BlockEnchanter	enchanter	= new BlockEnchanter("enchanter", 3.0F, Material.rock, CreativeTabs.tabBlock);
	public static final BlockDecanter	decanter	= new BlockDecanter("decanter");
	public static final BlockScaffold	scaffold	= new BlockScaffold("scaffold", 0F, Material.wood);

	public static void register() {
		enchanter.register();
		decanter.register();
		scaffold.register();
	}

}
