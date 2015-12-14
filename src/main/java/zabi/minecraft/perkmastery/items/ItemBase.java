package zabi.minecraft.perkmastery.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBase extends Item {

	private IIcon icona;
	
	public ItemBase(String modName, CreativeTabs tab) {
		super();
		this.setUnlocalizedName(modName);
		this.setCreativeTab(tab);
	}
	
	public ItemBase register() {
		GameRegistry.registerItem(this, getUnlocalizedName());
		return this;
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return icona;
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icona=iconRegister.registerIcon(LibGeneral.MOD_ID+":"+this.getUnlocalizedName().substring(5));
	}
	
	@Override
	public IIcon getIconIndex(ItemStack par1ItemStack) {
		return icona;
	}
	
	
}
