package zabi.minecraft.perkmastery.items.special;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.items.ItemBase;


public class GoldenBow extends ItemBase {

	private static final String	KEY_INIT	= "validated_goldenbow";
	private static ItemStack	bowstack;

	public GoldenBow(String modName, CreativeTabs tab) {
		super(modName, tab);
		this.setMaxStackSize(1);
		bowstack = new ItemStack(Items.bow);
	}

	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int ticksUsing) {
		int charge = this.getMaxItemUseDuration(stack) - ticksUsing;

		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, charge);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) return;
		charge = event.charge;
		boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

		if (flag || player.inventory.hasItem(Items.arrow)) {
			float f = (float) charge / 2.0F;
			f = (f * f + f * 2.0F) / 3.0F;
			if ((double) f < 0.1D) return; // Carica insufficiente
			if (f > 1.0F) f = 1.0F; // Troppa carica
			EntityArrow entityarrow = new EntityArrow(world, player, f * 2.0F);

			entityarrow.setIsCritical(true);

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			if (k > 0) entityarrow.setDamage(entityarrow.getDamage() + (double) k * 0.5D + 0.5D);

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			if (l > 0) entityarrow.setKnockbackStrength(l);

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) entityarrow.setFire(100);

			world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

			if (flag) entityarrow.canBePickedUp = 2;
			else
				player.inventory.consumeInventoryItem(Items.arrow);

			if (!world.isRemote) world.spawnEntityInWorld(entityarrow);
		}
	}

	public ItemStack onEaten(ItemStack stack, World w, EntityPlayer p) {
		return stack;
	}

	public int getMaxItemUseDuration(ItemStack stack) {
		return 15;
	}

	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.bow;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ArrowNockEvent event = new ArrowNockEvent(player, stack);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) return event.result;
		if (ExtendedPlayer.isEnabled(player, PlayerClass.ARCHER, 3)) {
			if (player.capabilities.isCreativeMode || player.inventory.hasItem(Items.arrow)) {
				player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
			}
		} else {
			if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
		}

		return stack;
	}

	public void onUsingTick(ItemStack stack, EntityPlayer player, int tickInUse) {
		if (tickInUse == 1) onPlayerStoppedUsing(stack, player.worldObj, player, 0);

	}

	@SuppressWarnings("unchecked")
	public void setRandomEnchantedStack(ItemStack res) {
		List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(itemRand, bowstack, 30);
		int rnd = (int) (Math.random() * list.size());
		EnchantmentData data = list.get(rnd);
		if (data.enchantmentobj.equals(Enchantment.unbreaking)) {
			setRandomEnchantedStack(res);
			return;
		}
		res.addEnchantment(data.enchantmentobj, data.enchantmentLevel);
	}

	public void onUpdate(ItemStack is, World world, Entity player, int intero, boolean bool) {
		if (world.isRemote) return;
		if (is.stackTagCompound == null) is.stackTagCompound = new NBTTagCompound();
		if (!is.stackTagCompound.hasKey(KEY_INIT)) {
			setRandomEnchantedStack(is);
			is.stackTagCompound.setString(KEY_INIT, "ok");
		}
	}

}
