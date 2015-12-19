package zabi.minecraft.perkmastery.items.special;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.items.ItemBase;
import zabi.minecraft.perkmastery.items.ItemList;


public class UndeadSoul extends ItemBase {

	private static final String TAG_TYPE = "mobType";

	public UndeadSoul(String modName, CreativeTabs tab) {
		super(modName, tab);
		this.setMaxStackSize(1);
	}

	public static ItemStack getNewSoul(UndeadType type) {
		ItemStack res = new ItemStack(ItemList.soul);
		if (res.stackTagCompound == null) res.stackTagCompound = new NBTTagCompound();
		res.stackTagCompound.setInteger(TAG_TYPE, type.ordinal());
		return res;
	}

	public static UndeadType getType(ItemStack is) {
		try {
			return UndeadType.values()[is.stackTagCompound.getInteger(TAG_TYPE)];
		} catch (NullPointerException exc) {
			return null;
		}
	}

	public void onUpdate(ItemStack is, World world, Entity player, int stackSlot, boolean isSelected) {
		if (player != null && player instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) player;

			if (ExtendedPlayer.isEnabled(p, PlayerClass.MAGE, 4)) {
				for (int i = 0; i < p.inventory.getSizeInventory(); i++) {
					ItemStack ip = p.inventory.getStackInSlot(i);
					if (ip != null && ip.getItem().equals(ItemList.tomeEvocation)) {
						EvocationTome.addSoulToStack(ip, getType(is));
						p.inventory.setInventorySlotContents(stackSlot, null);
						return;
					}
				}

				if (world.isRemote) p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.soul.destroyed.nobook")));

			} else {
				if (world.isRemote) p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.soul.destroyed.unable")));
			}
			p.inventory.setInventorySlotContents(stackSlot, null);
		}

	}

	public enum UndeadType {
		ZOMBIE, SKELETON
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List lista) {
		super.getSubItems(item, tab, lista);
		for (UndeadType t : UndeadType.values())
			lista.add(getNewSoul(t));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer player, List lista, boolean p_77624_4_) {
		UndeadType t = null;
		try {
			t = getType(is);
			if (t == null) return;
		} catch (Exception e) {
			return;
		}
		lista.add(t.name());
	}

}
