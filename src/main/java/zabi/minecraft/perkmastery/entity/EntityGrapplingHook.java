package zabi.minecraft.perkmastery.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.items.ItemList;
import zabi.minecraft.perkmastery.misc.Log;


public class EntityGrapplingHook extends Entity {

	private static final float	speed				= 1F;
	private static final double	maxDist				= 20;

	private World				world;
	private EntityPlayer		player;
	private double[]			start, end, look;
	private boolean				hit;
	private double				distanceTravelled	= 0,
										minimumDistanceAfterHit = 1 + maxDist;

	public EntityGrapplingHook(World world) {
		super(world);
		// this.setDead();
		// Log.w("Called wrong constructor");
		if (world.isRemote) {
			this.ignoreFrustumCheck = true;
			this.world = world;
			this.player = PerkMastery.proxy.getSinglePlayer();
			start = new double[] { player.posX, player.posY + 1, player.posZ };
			end = new double[] { player.posX, player.posY, player.posZ };
			look = new double[] { player.getLookVec().xCoord * speed, player.getLookVec().yCoord * speed, player.getLookVec().zCoord * speed };
			this.setPosition(start[0], start[1], start[2]);
		}
	}

	public EntityGrapplingHook(World world, EntityPlayer player) {
		super(world);
		this.ignoreFrustumCheck = true;
		this.world = world;
		this.player = player;
		start = new double[] { player.posX, player.posY + 1, player.posZ };
		end = new double[] { player.posX, player.posY, player.posZ };
		look = new double[] { player.getLookVec().xCoord * speed, player.getLookVec().yCoord * speed, player.getLookVec().zCoord * speed };
		this.setPosition(start[0], start[1], start[2]);
	}

	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	public void writeEntityToNBT(NBTTagCompound tag) {

	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {

	}

	public void onUpdate() {
		super.onUpdate();
		if (player == null || world == null) {
			this.setDead();
			return;
		}

		Vec3 movement = Vec3.createVectorHelper(look[0], look[1], look[2]);
		Vec3 pStart = Vec3.createVectorHelper(start[0], start[1], start[2]);
		Vec3 pEnd = Vec3.createVectorHelper(end[0], end[1], end[2]);

		if (hit) {

			pStart = pStart.addVector(movement.xCoord, movement.yCoord, movement.zCoord);
			if (world.isRemote) player.setVelocity(movement.xCoord, movement.yCoord, movement.zCoord);
			player.moveEntity(movement.xCoord, movement.yCoord, movement.zCoord);
			double dist = player.getDistanceSq(pEnd.xCoord, pEnd.yCoord, pEnd.zCoord);
			if (dist < 1.5D) { // ARRIVO
				this.setDead();
				player.motionX = 0;
				player.motionY = 0;
				player.motionZ = 0;
			} else {
				if (dist <= minimumDistanceAfterHit) {
					minimumDistanceAfterHit = dist;
				} else if (dist < 5) {
					Log.i("Distance grew in proximity of target, player may be projected over, aborting");
					player.motionX = 0;
					player.motionY = 0;
					player.motionZ = 0;
					this.setDead();
				}
			}
			distanceTravelled += movement.lengthVector();
			if (distanceTravelled > maxDist) {
				this.setDead();
				player.motionX = 0;
				player.motionY = 0;
				player.motionZ = 0;
				player.fallDistance = -10;
				Log.i("Exceeded maximum distance");
			}
		} else {
			pEnd = pEnd.addVector(movement.xCoord, movement.yCoord, movement.zCoord);
			if (player.getDistanceSq(pEnd.xCoord, pEnd.yCoord, pEnd.zCoord) > (maxDist * maxDist)) {
				this.setDead();
				Log.i("Killing for length");
			}
			Block hitBlock = world.getBlock((int) Math.floor(pEnd.xCoord), (int) Math.floor(pEnd.yCoord), (int) Math.floor(pEnd.zCoord));
			if (hitBlock != null && !world.isAirBlock((int) Math.floor(pEnd.xCoord), (int) Math.floor(pEnd.yCoord), (int) Math.floor(pEnd.zCoord))) {
				hit = true;
			}
		}

		start = new double[] { pStart.xCoord, pStart.yCoord, pStart.zCoord };
		end = new double[] { pEnd.xCoord, pEnd.yCoord, pEnd.zCoord };
		this.setPosition(pStart.xCoord, pStart.yCoord, pStart.zCoord);
	}

	public void setDead() {
		if (!world.isRemote && !this.isDead) world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(ItemList.hook)));
		super.setDead();
	}

}
