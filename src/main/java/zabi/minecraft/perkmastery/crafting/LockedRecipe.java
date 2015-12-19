package zabi.minecraft.perkmastery.crafting;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;


public class LockedRecipe extends ShapedRecipes {

	protected PlayerClass	classe;
	protected int			level;

	public LockedRecipe(int w, int h, ItemStack[] in, ItemStack out, PlayerClass classe, int level) {
		super(w, h, in, out);
		this.classe = classe;
		this.level = level;
	}

	public boolean matches(InventoryCrafting i, World w) {
		boolean r = super.matches(i, w);
		Container cont = ReflectionHelper.getPrivateValue(InventoryCrafting.class, i, "eventHandler", "field_70465_c", "ain", "aid", "d");
		EntityPlayer p = null;
		if (cont instanceof ContainerPlayer) {
			p = ReflectionHelper.getPrivateValue(ContainerPlayer.class, (ContainerPlayer) cont, "thePlayer", "field_82862_h", "ajb", "ajh", "h");
		}
		if (p == null || !ExtendedPlayer.isEnabled(p, classe, level)) return false;
		return r;
	}

}
