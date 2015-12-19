package zabi.minecraft.perkmastery.items.special;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.blocks.BlockList;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.items.ItemBase;


public class ScaffoldBuilder extends ItemBase {

	private static final String NBT_TAG = "scaffold_data";

	public ScaffoldBuilder(String modName, CreativeTabs tab) {
		super(modName, tab);
		this.setMaxStackSize(1);
	}

	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float ux, float uy, float uz) {
		if (ExtendedPlayer.isEnabled(player, PlayerClass.BUILDER, 6)) {
			if (player.isSneaking()) {
				prepareNbt(is);
				if (is.stackTagCompound.hasKey(NBT_TAG)) is.stackTagCompound.removeTag(NBT_TAG);
				return true;
			}
			prepareNbt(is);
			ChunkCoordinates coords = getBoundBlock(is);
			if (coords == null) {
				is.stackTagCompound.setIntArray(NBT_TAG, new int[] { x, y, z });
			} else {
				if (x == coords.posX || y == coords.posY || z == coords.posZ) {
					int sx = Math.min(x, coords.posX);
					int ex = Math.max(x, coords.posX);
					int sy = Math.min(y, coords.posY);
					int ey = Math.max(y, coords.posY);
					int sz = Math.min(z, coords.posZ);
					int ez = Math.max(z, coords.posZ);
					for (int dx = sx; dx <= ex; dx++)
						for (int dy = sy; dy <= ey; dy++)
							for (int dz = sz; dz <= ez; dz++)
								if (world.isAirBlock(dx, dy, dz)) world.setBlock(dx, dy, dz, BlockList.scaffold);
				} else {
					int sx = Math.min(x, coords.posX);
					int ex = Math.max(x, coords.posX);
					int sy = Math.min(y, coords.posY);
					int ey = Math.max(y, coords.posY);
					int sz = Math.min(z, coords.posZ);
					int ez = Math.max(z, coords.posZ);
					for (int dx = sx; dx <= ex; dx++)
						for (int dy = sy; dy <= ey; dy++)
							for (int dz = sz; dz <= ez; dz++)
								if ((is.getItemDamage() != 0 || dz == sz || dz == ez || dx == sx || dx == ex || dy == sy || dy == ey) && world.isAirBlock(dx, dy, dz)) world.setBlock(dx, dy, dz, BlockList.scaffold);
				}
				is.stackTagCompound.removeTag(NBT_TAG);
			}
			return true;
		} else {
			if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
		}
		return false;
	}

	private static void prepareNbt(ItemStack is) {
		if (is.stackTagCompound == null) is.stackTagCompound = new NBTTagCompound();
	}

	private static ChunkCoordinates getBoundBlock(ItemStack is) {
		if (!is.stackTagCompound.hasKey(NBT_TAG)) return null;
		int[] coords = is.stackTagCompound.getIntArray(NBT_TAG);
		return new ChunkCoordinates(coords[0], coords[1], coords[2]);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer player, List lista, boolean p_77624_4_) {

		lista.add(EnumChatFormatting.ITALIC + EnumChatFormatting.AQUA.toString() + StatCollector.translateToLocal("general.scaffoldbuilder.mode." + (is.getItemDamage() == 0 ? "empty" : "full")));

		prepareNbt(is);
		if (is.stackTagCompound.hasKey(NBT_TAG)) {
			lista.add(StatCollector.translateToLocal("general.scaffoldbuilder.boundto"));
			ChunkCoordinates cd = getBoundBlock(is);
			lista.add(cd.posX + " - " + cd.posY + " - " + cd.posZ);
			lista.add(EnumChatFormatting.DARK_GRAY + EnumChatFormatting.ITALIC.toString() + StatCollector.translateToLocal("general.scaffoldbuilder.cleanInstructions"));
		}
	}
}
