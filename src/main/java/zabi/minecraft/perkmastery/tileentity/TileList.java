package zabi.minecraft.perkmastery.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class TileList {

	public static void register() {
		GameRegistry.registerTileEntity(TileEntityDecanter.class, LibGeneral.MOD_ID + ":decanter");
		GameRegistry.registerTileEntity(TileEntityEnchanter.class, LibGeneral.MOD_ID + ":enchanter");
	}
}
