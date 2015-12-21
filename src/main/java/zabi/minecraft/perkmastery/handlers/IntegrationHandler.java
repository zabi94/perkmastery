package zabi.minecraft.perkmastery.handlers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.libs.LibModIDs;
import zabi.minecraft.perkmastery.misc.Log;


public class IntegrationHandler {

	public static boolean	thaumcraft_loaded	= false;
	public static boolean	tconstruct_loaded	= false;

	public static boolean isPickaxe(ItemStack stack) {
		if (stack == null) return false;
		if (stack.getItem() instanceof ItemPickaxe) return true;
		try {
			if (thaumcraft_loaded) {
				if (testFor(stack, LibModIDs.THAUMCRAFT, "ItemPickVoid")) return true;
				if (testFor(stack, LibModIDs.THAUMCRAFT, "ItemPickThaumium")) return true;
				if (testFor(stack, LibModIDs.THAUMCRAFT, "ItemPickaxeElemental")) return true;
			}
			if (tconstruct_loaded) {
				if (testFor(stack, LibModIDs.TINKERS_CONSTRUCT, "InfiTool.Pickaxe")) return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private static boolean testFor(ItemStack is, String modId, String itemName) {
		try {
			Item i = GameRegistry.findItem(modId, itemName);
			if (i != null) return i.getClass().isInstance(is.getItem());
		} catch (Exception e) {
			Log.w("Error trying to find a mod item");
			return false;
		}
		return false;
	}

	public static void checkLoadedMods() {
		thaumcraft_loaded = Loader.isModLoaded(LibModIDs.THAUMCRAFT);
		if (thaumcraft_loaded) Log.i("Thaumcraft Detected");
		tconstruct_loaded = Loader.isModLoaded(LibModIDs.TINKERS_CONSTRUCT);
		if (tconstruct_loaded) Log.i("TConstruct Detected");
	}

}
