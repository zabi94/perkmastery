package zabi.minecraft.perkmastery.handlers;

import java.util.ArrayList;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import zabi.minecraft.perkmastery.libs.LibModIDs;
import zabi.minecraft.perkmastery.misc.Log;


public class IntegrationHelper {

	private static boolean[]		loadedMods	= new boolean[LibModIDs.values().length];
	private static ArrayList<Item>	IMCpickaxes	= new ArrayList<Item>();

	public static boolean isPickaxe(ItemStack stack) {
		if (stack == null) return false;
		if (stack.getItem() instanceof ItemPickaxe) return true;
		if (isModLoaded(LibModIDs.Thaumcraft)) {
			if (testFor(stack, LibModIDs.Thaumcraft.name(), "ItemPickVoid")) return true;
			if (testFor(stack, LibModIDs.Thaumcraft.name(), "ItemPickThaumium")) return true;
			if (testFor(stack, LibModIDs.Thaumcraft.name(), "ItemPickaxeElemental")) return true;
		}
		if (isModLoaded(LibModIDs.TConstruct)) {
			if (testFor(stack, LibModIDs.TConstruct.name(), "pickaxe")) return true;
		}
		for (Item item : IMCpickaxes)
			if (item.equals(stack.getItem())) return true;
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

	public static void addPickaxe(ItemStack stack) {
		IMCpickaxes.add(stack.getItem());
	}

}
