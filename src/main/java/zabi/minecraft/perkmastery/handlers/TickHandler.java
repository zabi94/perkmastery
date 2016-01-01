package zabi.minecraft.perkmastery.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ChestGenHooks;
import zabi.minecraft.perkmastery.Config;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.InventoryType;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.entity.PortableFurnaceData;
import zabi.minecraft.perkmastery.gui.GuiHandler;
import zabi.minecraft.perkmastery.misc.Log;
import zabi.minecraft.perkmastery.network.packets.OpenGuiMessage;
import zabi.minecraft.perkmastery.proxy.ClientProxy;


public class TickHandler {

	public ArrayList<Item>	updateBlackList	= new ArrayList<Item>();
	public boolean			lock			= false;

	// Sezione eventi

	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent evt) {

		if (lock) {
			// Log.e("Tick Table Locked");
			return;
		}

		lock = true;
		EntityPlayer player = evt.player;

		if (evt.side.equals(Side.SERVER)) {
			if (ExtendedPlayer.isPlayer(player, PlayerClass.EXPLORER)) {
				try {
					handleLootfinder(player);
				} catch (Exception e) {
				} // If bed not set
				handleSaturation(player);
				handleBackpack(player);
			}

			if (ExtendedPlayer.isPlayer(player, PlayerClass.WARRIOR)) {
				handleKnight(player);
			}
			if (ExtendedPlayer.isPlayer(player, PlayerClass.MINER)) {
				handleFastMiner(player);
				handleFurnace(player);
			}
			if (ExtendedPlayer.isPlayer(player, PlayerClass.ARCHER)) {
				handleShadowForm(player);
			}
		}
		if (ExtendedPlayer.isPlayer(player, PlayerClass.BUILDER)) {
			this.handleParkour(player);
			this.handleFloorLayer(player);
		}
		lock = false;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onKeyPressedEvent(KeyInputEvent event) {
		if (ClientProxy.guiKey.isPressed()) {
			EntityPlayer player = PerkMastery.proxy.getSinglePlayer();
			PerkMastery.network.sendToServer(new OpenGuiMessage());
			PerkMastery.proxy.getSinglePlayer().openGui(PerkMastery.instance, GuiHandler.IDs.GUI_BOOK.ordinal(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
		}
	}

	// Sezione abilità

	private void handleSaturation(EntityPlayer player) { // EXPLORER
		if (!player.worldObj.isRemote && ExtendedPlayer.isEnabled(player, PlayerClass.EXPLORER, 6) && !player.isPotionActive(Potion.field_76443_y.id) && player.ticksExisted % Config.experienceCostTicksInterval == 0 && requestExperience(player)) {
			PotionEffect pfx = new PotionEffect(Potion.field_76443_y.id, Config.experienceCostTicksInterval, 0, false);// Saturation
			pfx.getCurativeItems().clear();
			player.addPotionEffect(pfx);
		}

	}

	private boolean requestExperience(EntityPlayer player) {
		if (player.capabilities.isCreativeMode || Config.experienceCost <= 0) return true;

		if (player.experienceTotal >= Config.experienceCost) {
			addExperience(player, -Config.experienceCost);
			return true;
		}

		return false;
	}

	public static void addExperience(EntityPlayer player, int amount) {
		if (player.capabilities.isCreativeMode) return;
		int experience = getPlayerXP(player) + amount;
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
	}

	private static int getPlayerXP(EntityPlayer player) {
		return (int) (getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
	}

	private static int getExperienceForLevel(int level) {
		if (level == 0) { return 0; }
		if (level > 0 && level < 16) {
			return level * 17;
		} else if (level > 15 && level < 31) {
			return (int) (1.5 * Math.pow(level, 2) - 29.5 * level + 360);
		} else {
			return (int) (3.5 * Math.pow(level, 2) - 151.5 * level + 2220);
		}
	}

	private static int getLevelForExperience(int experience) {
		int i = 0;
		while (getExperienceForLevel(i) <= experience) {
			i++;
		}
		return i - 1;
	}

	private void handleLootfinder(EntityPlayer player) { // EXPLORER
		if (!player.worldObj.isRemote && ExtendedPlayer.isEnabled(player, PlayerClass.EXPLORER, 3)) {
			ChunkCoordinates bed = player.getBedLocation(player.worldObj.provider.dimensionId);
			if (bed == null) return;
			double distanceFromBed = player.getDistance(bed.posX, bed.posY, bed.posZ);
			if (distanceFromBed > 64) {
				if (player.worldObj.rand.nextInt(2) < 2) {
					ItemStack loot = getRandomLoot(player.worldObj.rand);
					if (loot != null) player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY + 1, player.posZ, loot));
				}
			}
		}
	}

	private void handleBackpack(EntityPlayer player) {
		if (!player.worldObj.isRemote && Config.extraInventoryTicks && ExtendedPlayer.isEnabled(player, PlayerClass.EXPLORER, 2)) {
			ItemStack[] stacks = ExtendedPlayer.getExtraInventory(player, InventoryType.REAL);
			for (ItemStack is : stacks)
				if (is != null) try {
					if (!updateBlackList.contains(is.getItem())) is.getItem().onUpdate(is, player.worldObj, player, -1, false);
				} catch (Exception e) {
					Log.e("Error trying to tick item");
					Log.e(e);
					Log.e("Adding " + is.getItem() + " to blacklist");
					if (!UpdateHandler.outdated) Log.e("\n\n\nPlease report to https://github.com/zabi94/perkmastery/issues\n\n\n");
					updateBlackList.add(is.getItem());
				}
		}
	}

	private static DamageSource shadowForm = new DamageSource("shadowForm").setDamageBypassesArmor().setMagicDamage();

	private void handleShadowForm(EntityPlayer player) { // ARCHER
		if (ExtendedPlayer.isEnabled(player, PlayerClass.ARCHER, 6) && player.isSneaking()) {
			if (!player.isPotionActive(Potion.invisibility)) {
				PotionEffect pfx = new PotionEffect(Potion.invisibility.id, Config.experienceCostTicksInterval, 1, false);
				pfx.getCurativeItems().clear();
				player.addPotionEffect(pfx);
			}
			if (player.ticksExisted % Config.experienceCostTicksInterval == 0 && !requestExperience(player)) {
				player.attackEntityFrom(shadowForm, 2F);
			}
		}

	}

	private void handleFurnace(EntityPlayer player) {// MINER

		if (ExtendedPlayer.isEnabled(player, PlayerClass.MINER, 6) && !player.worldObj.isRemote) {

			PortableFurnaceData data = PortableFurnaceData.getDataFor(player);
			ItemStack[] inv = ExtendedPlayer.getExtraInventory(player, InventoryType.REAL);

			if (data.furnaceBurnTime > 0) {
				--data.furnaceBurnTime;
				data.write();
			}

			if (data.furnaceBurnTime != 0 || inv[23] != null && inv[25] != null) { // Se sta già cuocendo qualcosa || (c'è carbone + qualcosa da cucinare)
				if (data.furnaceBurnTime == 0 && data.canSmelt()) { // Se deve iniziare una nuova operazione di smelting
					data.currentItemBurnTime = data.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(inv[25]); // Leggi il tempo necessario
					if (data.furnaceBurnTime > 0) { // Se c'è bisogno di cuocere
						if (inv[25] != null) { // Se c'è carbone
							--inv[25].stackSize; // Consuma carbone
							ExtendedPlayer.setInventorySlot(player, 25, inv[25]);
							if (inv[25].stackSize == 0) { // Se il carbone finisce
								ExtendedPlayer.setInventorySlot(player, 25, inv[25].getItem().getContainerItem(inv[25])); // Sostituiscilo con il contenitore (Per secchi di lava)
							}
						}
					}
				}

				if (data.isBurning() && data.canSmelt()) { // Se è tutto in regola per lo smelting
					++data.furnaceCookTime; // Avanza con il contatore
					if (data.furnaceCookTime == 200) { // Se il contatore raggiunge il limite
						data.furnaceCookTime = 0; // Resettalo
						PortableFurnaceData.smeltItem(inv, data, player); // Genera il risultato
					}
				} else { // Invece, se qualcosa non è in regola
					data.furnaceCookTime = 0; // Resetta il tempo di cottura
				}
			}

			data.write();
		}
	}

	private void handleFastMiner(EntityPlayer player) {// MINER
		if (ExtendedPlayer.isEnabled(player, PlayerClass.MINER, 1)) {
			if (player.getHeldItem() != null && (/* validItems.contains(player.getHeldItem().getItem()) || */IntegrationHelper.isPickaxe(player.getHeldItem())) && !player.isPotionActive(Potion.digSpeed.id)) {
				PotionEffect pfx = new PotionEffect(Potion.digSpeed.id, 0, 0, false);// Haste
				pfx.getCurativeItems().clear();
				player.addPotionEffect(pfx);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void handleKnight(EntityPlayer player) { // WARRIOR
		if (ExtendedPlayer.isEnabled(player, PlayerClass.WARRIOR, 4)) {
			if (player.ticksExisted % 40 < 2 && player.isRiding()) {
				Iterator<EntityLivingBase> i = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.ridingEntity.boundingBox.expand(3, 3, 3)).iterator();
				while (i.hasNext()) {
					EntityLivingBase et = i.next();
					if (!et.equals(player) && !et.equals(player.ridingEntity)) et.attackEntityFrom(DamageSource.causePlayerDamage(player), 2F);
				}
			}
		}

	}

	private void handleFloorLayer(EntityPlayer player) { // BUILDER
		if (!player.worldObj.isRemote && ExtendedPlayer.isEnabled(player, PlayerClass.BUILDER, 5) && player.getHeldItem() != null && player.isSneaking() && Block.getBlockFromItem(player.getHeldItem().getItem()) != Blocks.air && player.onGround) {
			int y = (int) (player.posY - 0.5);

			ItemStack is = player.getHeldItem();
			int px = (int) Math.floor(player.posX);
			int pz = (int) Math.floor(player.posZ);

			testAndPlace(is, px, y, pz, player);
			testAndPlace(is, px + 1, y, pz, player);
			testAndPlace(is, px - 1, y, pz, player);
			testAndPlace(is, px, y, pz + 1, player);
			testAndPlace(is, px, y, pz - 1, player);
		}

	}

	private void handleParkour(EntityPlayer player) {// BUILDER
		if (ExtendedPlayer.isEnabled(player, PlayerClass.BUILDER, 4)) {
			if (player.isCollidedHorizontally) {
				player.fallDistance = 0.0F;
				if (player.isSneaking()) player.motionY = 0.0D;
				else
					player.motionY = 0.1176D;
			}
			if (!player.worldObj.isRemote) {
				double motionX = player.posX - player.lastTickPosX;
				double motionZ = player.posZ - player.lastTickPosZ;
				double motionY = player.posY - player.lastTickPosY - 0.765D;
				if (motionY > 0.0D && (motionX == 0D || motionZ == 0D)) player.fallDistance = 0.0F;
			} else if (player.getDisplayName().equals(PerkMastery.proxy.getSinglePlayer().getDisplayName())) {
				EntityPlayer p = PerkMastery.proxy.getSinglePlayer();
				if (p.isCollidedHorizontally) {
					p.fallDistance = 0.0F;
					if (p.isSneaking()) p.motionY = 0.0D;
					else
						p.motionY = 0.1176D;

				}
			}

		}

	}

	// Sezione metodi servizio

	private ItemStack getRandomLoot(Random rnd) {
		ItemStack res = null;
		while (res == null) {
			res = ChestGenHooks.getOneItem(ChestGenHooks.STRONGHOLD_CORRIDOR, rnd);
		}
		return res;
	}

	public void testAndPlace(ItemStack is, int px, int y, int pz, EntityPlayer player) {
		if (is.stackSize > 0 && (player.worldObj.isAirBlock(px, y, pz) || player.worldObj.getBlock(px, y, pz).isReplaceable(player.worldObj, px, y, pz)) && Block.getBlockFromItem(is.getItem()).renderAsNormalBlock()) {
			player.worldObj.setBlock(px, y, pz, Block.getBlockFromItem(player.getHeldItem().getItem()), player.getHeldItem().getItemDamage(), 3);
			if (!player.capabilities.isCreativeMode) {
				is.stackSize--;
				if (is.stackSize == 0) player.setCurrentItemOrArmor(0, null);
			}

		}
	}

}
