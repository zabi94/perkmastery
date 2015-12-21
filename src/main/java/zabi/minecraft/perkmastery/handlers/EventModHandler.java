package zabi.minecraft.perkmastery.handlers;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.InventoryType;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.items.ItemList;
import zabi.minecraft.perkmastery.items.special.EvocationTome;
import zabi.minecraft.perkmastery.items.special.UndeadSoul;
import zabi.minecraft.perkmastery.items.special.UndeadSoul.UndeadType;
import zabi.minecraft.perkmastery.misc.Log;
import zabi.minecraft.perkmastery.network.packets.JumpBoost;
import zabi.minecraft.perkmastery.visual.effects.EffectRegistry;
import zabi.minecraft.perkmastery.visual.effects.IRenderGeneral;


public class EventModHandler {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {

			if (!event.entity.worldObj.isRemote) {
				EntityPlayer player = (EntityPlayer) event.entity;

				// Fa un toggle dell'attività per sincronizzare il
				// HackyPlayerControllerMP
				ToggleHandler.toggleReachDistance(player, ExtendedPlayer.isEnabled(player, PlayerClass.BUILDER, 1));
				ToggleHandler.toggleWellTrained(player, ExtendedPlayer.isEnabled(player, PlayerClass.EXPLORER, 4));
				ToggleHandler.toggleExpertEye(player, ExtendedPlayer.isEnabled(player, PlayerClass.MINER, 3));

				// Sincronizza al client connesso
				ExtendedPlayer.syncToClient(player);

			} else {
				Thread updateChecker = new Thread(new UpdateHandler());
				updateChecker.setDaemon(true);
				updateChecker.setName("PerkMasteryUpdates");
				updateChecker.setPriority(Thread.MIN_PRIORITY);
				updateChecker.start();
			}

		}

	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = ((EntityPlayer) event.entityLiving);
			if (ExtendedPlayer.isEnabled(player, PlayerClass.EXPLORER, 4)) {
				player.motionY += 0.15F;
				player.fallDistance -= 0.15F;
				PerkMastery.network.sendToServer(new JumpBoost());
			}

		}

	}

	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = ((EntityPlayer) event.entityLiving);
			if (ExtendedPlayer.isEnabled(player, PlayerClass.BUILDER, 4)) {
				player.fallDistance = player.fallDistance / 2F;
			}
		}
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = ((EntityPlayer) event.entityLiving);

			if (ExtendedPlayer.isEnabled(player, PlayerClass.EXPLORER, 5) && event.source.equals(DamageSource.fall)) {
				player.setHealth(1);
				event.setCanceled(true);
			}

			if (ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 6) && ExtendedPlayer.hasDeathAmulet(player) && !event.isCanceled()) {
				ExtendedPlayer.destroyAmulet(player);
				player.setHealth(10);
				event.setCanceled(true);
			}

			if (!player.worldObj.isRemote && !event.isCanceled()) {
				ExtendedPlayer.dropItemsOnDeath(player);
			}

		} else {
			if (!event.entity.worldObj.isRemote && !event.isCanceled() && Math.random() < 2) {
				if (event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer && ExtendedPlayer.isEnabled((EntityPlayer) event.source.getEntity(), PlayerClass.MAGE, 4)) {
					if ((event.entityLiving instanceof EntityZombie)) {
						event.entityLiving.worldObj.spawnEntityInWorld(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, UndeadSoul.getNewSoul(UndeadType.ZOMBIE)));
					} else if ((event.entityLiving instanceof EntitySkeleton)) {
						event.entityLiving.worldObj.spawnEntityInWorld(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, UndeadSoul.getNewSoul(UndeadType.SKELETON)));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {

		// PLAYER BERSAGLIO
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (ExtendedPlayer.isEnabled(player, PlayerClass.WARRIOR, 6)) {
				if (((event.source.getSourceOfDamage() instanceof EntityLiving)) || ((event.source.getSourceOfDamage() instanceof EntityPlayer))) {
					if (!player.isPotionActive(Potion.field_76444_x.id)) {
						PotionEffect pfx = new PotionEffect(Potion.field_76444_x.id, 400, 1, false);// Absorption
						pfx.getCurativeItems().clear();
						player.addPotionEffect(pfx);
					}
				}
			}

			if (ExtendedPlayer.isEnabled(player, PlayerClass.WARRIOR, 3)) {
				for (int i = 0; i < 4; i++)
					if (ExtendedPlayer.getExtraInventory(player, InventoryType.REAL)[19 + i] != null) {
						event.ammount = 0.9F * event.ammount;
						ExtendedPlayer.getExtraInventory(player, InventoryType.REAL)[19 + i].attemptDamageItem(2, player.getRNG());
					}
			}

			if (ExtendedPlayer.isEnabled(player, PlayerClass.ARCHER, 2) && event.source.isProjectile() && !player.worldObj.isRemote && Math.random() < 0.7) {
				event.setCanceled(true);
			}

		}

		// PLAYER CAUSA
		if (event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			if (!player.worldObj.isRemote) {
				if (ExtendedPlayer.isEnabled(player, PlayerClass.WARRIOR, 1) && !event.source.isProjectile()) event.entityLiving.attackEntityFrom(DamageSource.generic, 2F);
				if (ExtendedPlayer.isEnabled(player, PlayerClass.WARRIOR, 2)) {
					EntityLivingBase et = event.entityLiving;
					if (et.getHeldItem() != null && player.ticksExisted % 10 == 1) {
						et.entityDropItem(et.getHeldItem().copy(), 2F);
						et.setCurrentItemOrArmor(0, null);
					}
				}
				if (ExtendedPlayer.isEnabled(player, PlayerClass.WARRIOR, 5)) if (event.entityLiving instanceof IBossDisplayData) event.entityLiving.attackEntityFrom(DamageSource.generic, 6F);
				if (ExtendedPlayer.isEnabled(player, PlayerClass.ARCHER, 1) && event.source.isProjectile()) event.entityLiving.attackEntityFrom(DamageSource.generic, 2F);
				if (ExtendedPlayer.isEnabled(player, PlayerClass.ARCHER, 4) && !event.entityLiving.worldObj.isRemote && Math.random() < 0.1) {
					if (event.entityLiving instanceof EntityPlayer) {

						PotionEffect pfx = new PotionEffect(Potion.confusion.id, 60, 2, false);
						pfx.getCurativeItems().clear();
						PotionEffect pfx2 = new PotionEffect(Potion.moveSlowdown.id, 60, 1, false);
						pfx2.getCurativeItems().clear();
						event.entityLiving.addPotionEffect(pfx2);
						event.entityLiving.addPotionEffect(pfx);
					} else {
						PotionEffect pfx = new PotionEffect(Potion.moveSlowdown.id, 60, 1, false);
						event.entityLiving.addPotionEffect(pfx);
					}
				}

				if (ExtendedPlayer.isEnabled(player, PlayerClass.MAGE, 4) && !event.entityLiving.worldObj.isRemote) {
					ItemStack ci = player.inventory.mainInventory[(player.inventory.currentItem + 1) % 9];
					if (ci != null && ci.getItem().equals(ItemList.tomeEvocation)) {
						EvocationTome.summonAtPlayer(ci, player, (EntityLiving) event.entityLiving);
					}
				}

			}
		}
	}

	@SubscribeEvent
	public void onBlockBreakEvent(BreakEvent evt) {
		if (evt.getPlayer() == null) return;
		int fortuneLevel = EnchantmentHelper.getFortuneModifier(evt.getPlayer());
		boolean delicate = (DigHandler.isToolDelicate(evt) && ExtendedPlayer.isEnabled(evt.getPlayer(), PlayerClass.BUILDER, 2));
		boolean silk = EnchantmentHelper.getSilkTouchModifier(evt.getPlayer());
		if (ExtendedPlayer.isEnabled(evt.getPlayer(), PlayerClass.MINER, 4)) fortuneLevel++;
		if (ExtendedPlayer.isEnabled(evt.getPlayer(), PlayerClass.MINER, 5)) DigHandler.applyCrumbling(evt.x, evt.y + 1, evt.z, evt.world, 0);
		if (ExtendedPlayer.isEnabled(evt.getPlayer(), PlayerClass.MINER, 2)) DigHandler.applyVeinminer(evt, evt.x, evt.y, evt.z, new int[] { 0, 0 }, true, silk, fortuneLevel, 0);
		else if (!silk && ExtendedPlayer.isEnabled(evt.getPlayer(), PlayerClass.MINER, 4)) DigHandler.applyFortune(evt);
		if (delicate) {
			if (evt.block.canSilkHarvest(evt.world, evt.getPlayer(), evt.x, evt.y, evt.z, evt.blockMetadata) && DigHandler.containsGlass(evt.block.getUnlocalizedName().toLowerCase())) {
				evt.setCanceled(true);
				evt.world.setBlockToAir(evt.x, evt.y, evt.z);
				evt.world.spawnEntityInWorld(new EntityItem(evt.world, evt.x, evt.y, evt.z, new ItemStack(evt.block, 1, evt.blockMetadata)));
			}
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerTrySleep(PlayerSleepInBedEvent evt) {
		if (ExtendedPlayer.isEnabled(evt.entityPlayer, PlayerClass.EXPLORER, 1) && !evt.entityPlayer.worldObj.isDaytime()) {
			evt.result = EnumStatus.OK;
			if (evt.entityPlayer.isRiding()) evt.entityPlayer.mountEntity((Entity) null);
			evt.entityPlayer.yOffset = 0.2F;
			if (evt.entityPlayer.worldObj.blockExists(evt.x, evt.y, evt.z)) {
				int l = evt.entityPlayer.worldObj.getBlock(evt.x, evt.y, evt.z).getBedDirection(evt.entityPlayer.worldObj, evt.x, evt.y, evt.z);
				float f1 = 0.5F;
				float f = 0.5F;
				switch (l) {
				case 0:
					f = 0.9F;
					break;
				case 1:
					f1 = 0.1F;
					break;
				case 2:
					f = 0.1F;
					break;
				case 3:
					f1 = 0.9F;
				}

				evt.entityPlayer.setPosition((double) ((float) evt.x + f1), (double) ((float) evt.y + 0.9375F), (double) ((float) evt.z + f));
			} else
				evt.entityPlayer.setPosition((double) ((float) evt.x + 0.5F), (double) ((float) evt.y + 0.9375F), (double) ((float) evt.z + 0.5F));

			ReflectionHelper.setPrivateValue(EntityPlayer.class, evt.entityPlayer, true, "sleeping", "field_71083_bS");
			ReflectionHelper.setPrivateValue(EntityPlayer.class, evt.entityPlayer, 0, "sleepTimer", "field_71076_b");
			evt.entityPlayer.playerLocation = new ChunkCoordinates(evt.x, evt.y, evt.z);
			evt.entityPlayer.motionX = evt.entityPlayer.motionZ = evt.entityPlayer.motionY = 0.0D;

			if (!evt.entityPlayer.worldObj.isRemote) evt.entityPlayer.worldObj.updateAllPlayersSleepingFlag();

		}
	}

	@SubscribeEvent
	public void onPlayerUseBow(PlayerUseItemEvent.Tick evt) {
		if (ExtendedPlayer.isEnabled(evt.entityPlayer, PlayerClass.ARCHER, 5) && evt.item.getItem().getItemUseAction(evt.item).equals(EnumAction.bow)) {
			if (!evt.entityPlayer.isPotionActive(Potion.nightVision)) {
				PotionEffect pfx = new PotionEffect(Potion.nightVision.id, 10, 10, false);
				pfx.getCurativeItems().clear();
				evt.entityPlayer.addPotionEffect(pfx);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.Clone event) {
		ExtendedPlayer.restoreTag(event.entityPlayer, ExtendedPlayer.backupTag(event.original));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		EntityPlayer player = PerkMastery.proxy.getSinglePlayer();
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialTicks;
		GL11.glPushMatrix();
		GL11.glTranslated(-playerX, -playerY, -playerZ);
		Iterator<IRenderGeneral> it = EffectRegistry.getList();
		try {
			while (it.hasNext())
				it.next().render(event.partialTicks);
		} catch (Exception e) {
			Log.e("Error rendering mod effect");
			e.printStackTrace();
			EffectRegistry.purge();
		}
		GL11.glPopMatrix();

	}

	@SubscribeEvent
	public void onUnloadingWorld(WorldEvent.Unload event) {
		if (event.world.isRemote) {
			EffectRegistry.purge();
		}
	}

	@SubscribeEvent
	public void onArrowShot(ArrowLooseEvent evt) {
		if (evt.entityPlayer == null) return;
		if (ExtendedPlayer.isEnabled(evt.entityPlayer, PlayerClass.ARCHER, 4)) {
			evt.charge = (int) (2 * evt.charge);
		}
	}

	@SubscribeEvent
	public void onTargetChanged(LivingSetAttackTargetEvent event) {
		if (event.target instanceof EntityPlayer && event.entityLiving instanceof EntityMob) {
			EntityPlayer p = (EntityPlayer) event.target;
			if (event.entityLiving.getDataWatcher().getWatchableObjectString(10).indexOf(p.getDisplayName()) >= 0) {

				((EntityMob) event.entityLiving).setAttackTarget(p.getLastAttacker());
				((EntityMob) event.entityLiving).setTarget(p.getLastAttacker());
				((EntityMob) event.entityLiving).setRevengeTarget(p.getLastAttacker());
			}
		}
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event) {
		if (event.entityPlayer != null && ExtendedPlayer.isEnabled(event.entityPlayer, PlayerClass.MINER, 5)) {
			ItemStack is = event.item.getEntityItem();
			Item item = is.getItem();
			int meta = is.getItemDamage();
			for (ItemStack filter : ExtendedPlayer.getExtraInventory(event.entityPlayer, InventoryType.FILTER)) {
				if (filter != null && filter.getItem().equals(item) && filter.getItemDamage() == meta) {
					event.item.setDead();
					event.setCanceled(true);
					return;
				}
			}
		}
	}
}
