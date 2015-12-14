package zabi.minecraft.perkmastery.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import zabi.minecraft.perkmastery.blocks.special.BlockDecanter;
import zabi.minecraft.perkmastery.blocks.special.BlockScaffold;


public class BlockList {

//	public static final BlockSleepingBag sleepingBag=new BlockSleepingBag("sleepingBag", CreativeTabs.tabMisc);
	public static final BlockDecanter decanter=new BlockDecanter("decanter");
	public static final BlockScaffold scaffold=new BlockScaffold("scaffold", 0F, Material.wood, CreativeTabs.tabBlock);
	
	public static void register() {
//		sleepingBag.register();
		decanter.register();
		scaffold.register();
	}
	
}
