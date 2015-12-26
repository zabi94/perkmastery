package zabi.minecraft.perkmastery.items.special;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.EntityGrapplingHook;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.items.ItemBase;


public class GrapplingHook extends ItemBase {

	public GrapplingHook(String modName, CreativeTabs tab) {
		super(modName, tab);
		this.setMaxStackSize(1);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (ExtendedPlayer.isEnabled(player, PlayerClass.MINER, 6)) {
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				if (!world.isRemote) world.spawnEntityInWorld(new EntityGrapplingHook(world, player));
			}
			player.setCurrentItemOrArmor(0, null);
		} else {
			if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
		}
		return stack;
	}

	@Override
	public ItemBase register() {
		EntityRegistry.registerModEntity(EntityGrapplingHook.class, "grapplingHook", 0, PerkMastery.instance, 80, 1, true);
		return super.register();
	}
}
