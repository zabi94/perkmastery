package zabi.minecraft.perkmastery.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.items.ItemList;


public class EntityGrapplingHook extends Entity {

	private static final float	hookSpeed			= 1.1F;
	private static final float	retractSpeed		= 0.3F;
	private static final double	maxDist				= 40;

	private World				world;
	private EntityPlayer		player;
	private double[]			look;
	private boolean				hit;
	private double				distanceLaunched	= 0;
	// private double distanceTravelled = 0;

	public EntityGrapplingHook(World world) {
		this(world, PerkMastery.proxy.getSinglePlayer());
	}

	public EntityGrapplingHook(World world, EntityPlayer player) {
		super(world);
		hit = false;
		this.ignoreFrustumCheck = true;
		this.setSize(0.2F, 0.2F);
		this.noClip = true;
		this.world = world;
		if (player != null) {
			this.player = player;
			look = new double[] { player.getLookVec().xCoord * hookSpeed, player.getLookVec().yCoord * hookSpeed, player.getLookVec().zCoord * hookSpeed };
			this.setPosition(player.posX, player.posY + 1.5, player.posZ);
		}
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
		if (player.isSneaking()) {
			this.setDead();
			return;
		}
		if (!hit) {
			this.moveEntity(movement.xCoord, movement.yCoord, movement.zCoord);
			distanceLaunched += movement.lengthVector();
			if (distanceLaunched > maxDist) setDead();
			hit = isCollisionValid();
		} else {
			double dx = (this.posX - player.posX) * retractSpeed;
			double dy = (this.posY - player.posY) * retractSpeed * 2;
			double dz = (this.posZ - player.posZ) * retractSpeed;
			if (dy < 0 && player.onGround) dy = 0; // Don't drag the player into the floor
			if (dy > 0 && !player.onGround && player.isCollidedVertically) dy = 0; // Don't drag the player into the ceiling
			if (!player.onGround && player.isCollidedHorizontally) dx = dz = 0; // Don't drag the player into blocks
			if ((dx == 0 && dy == 0 && dz == 0) || player.isEntityInsideOpaqueBlock()) this.setDead(); // Player is stuck
			player.moveEntity(dx, dy, dz);
			double minDist = player.isCollided ? 1.5 : 1;
			if (this.getDistanceToEntity(player) < minDist) this.setDead(); // Arrived
		}

	}

	private boolean isCollisionValid() {
		return (this.isEntityInsideOpaqueBlock() && !this.isInsideOfMaterial(Material.water) && !this.isInsideOfMaterial(Material.lava)) || this.isInsideOfMaterial(Material.glass);
	}

	public void setDead() {
		if (!world.isRemote && !this.isDead) player.entityDropItem(new ItemStack(ItemList.hook), -1F);
		player.fallDistance = -10;
		super.setDead();
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return p_70112_1_ < d1 * d1;
	}

}
