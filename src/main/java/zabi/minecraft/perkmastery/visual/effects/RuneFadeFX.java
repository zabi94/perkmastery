package zabi.minecraft.perkmastery.visual.effects;

import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.world.World;

public class RuneFadeFX extends EntityEnchantmentTableParticleFX {

	private static final double RADIUS=1.1;
	private double radDev;
	private double px,pz;
	private boolean reverted;
	private int startAngle;
	
	public RuneFadeFX(World world, double x, double y, double z) {
		super(world, x, y, z, 0, 0, 0);
		this.particleMaxAge=100;
		this.motionY=0.04;
		this.radDev=rand.nextGaussian()*0.1;
		posY+=rand.nextGaussian()*0.2;
		px=posX;
		pz=posZ;
		startAngle=rand.nextInt(360);
		prevPosX=posX=getPosX(0);
		prevPosZ=posZ=getPosZ(0);
		reverted=rand.nextBoolean();		
		this.particleMaxAge*=0.8;
	}

	public void onUpdate() {
		float f = (float)this.particleAge / (float)this.particleMaxAge;
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.posY+=(motionY*f);
        this.posX=getPosX(f);
        this.posZ=getPosZ(f);
        if (this.particleAge++ >= this.particleMaxAge) this.setDead();
    }
	
	private double getRadius() {
		return RADIUS+radDev;
	}
	
	private double getPosX(float f) {
		
		double conic=(1-f);
		if (reverted) f=-f;
		return px+Math.cos(f*(6+radDev)+startAngle)*getRadius()*conic;
	}
	
	private double getPosZ(float f) {
		double conic=(1-f);
		if (reverted) f=-f;
		return pz+Math.sin(f*(6+radDev)+startAngle)*getRadius()*conic;
	}
	
}
