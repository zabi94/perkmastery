package zabi.minecraft.perkmastery.tileentity;

import zabi.minecraft.perkmastery.libs.LibGeneral;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileList {
	public static void register() {
		GameRegistry.registerTileEntity(TileEntityDecanter.class, LibGeneral.MOD_ID+":decanter");
		GameRegistry.registerTileEntity(TileEntityEnchanter.class, LibGeneral.MOD_ID+":enchanter");
		
	}
}
