package zabi.minecraft.perkmastery.handlers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.libs.LibModIDs;
import zabi.minecraft.perkmastery.misc.Log;


// Rename to helper TODO
public class IntegrationHandler {

	private static boolean[] loadedMods = new boolean[LibModIDs.values().length];

	public static boolean isPickaxe(ItemStack stack) {
		if (stack == null) return false;
		if (stack.getItem() instanceof ItemPickaxe) return true;
		try {
			if (isModLoaded(LibModIDs.Thaumcraft)) {
				if (testFor(stack, LibModIDs.Thaumcraft.name(), "ItemPickVoid")) return true;
				if (testFor(stack, LibModIDs.Thaumcraft.name(), "ItemPickThaumium")) return true;
				if (testFor(stack, LibModIDs.Thaumcraft.name(), "ItemPickaxeElemental")) return true;
			}
			if (isModLoaded(LibModIDs.TConstruct)) {
				if (testFor(stack, LibModIDs.TConstruct.name(), "pickaxe")) return true;
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
		for (LibModIDs id : LibModIDs.values())
			if (Loader.isModLoaded(id.name())) {
				loadedMods[id.ordinal()] = true;
				Log.i("Supported Mod Detected: " + id.name() + " (aka: " + Loader.instance().getIndexedModList().get(id.name()).getName() + ")");
			} else {
				Log.d("Supported Mod not detected: " + id.name());
			}
	}

	public static boolean isModLoaded(LibModIDs modId) {
		return loadedMods[modId.ordinal()];
	}

}
