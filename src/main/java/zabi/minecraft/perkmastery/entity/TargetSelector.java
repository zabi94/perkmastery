package zabi.minecraft.perkmastery.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

public class TargetSelector implements IEntitySelector {

	EntityPlayer player;
	EntityLiving target;
	
	public TargetSelector(EntityPlayer p, EntityLiving firstTarget) {
		player=p;
		target=firstTarget;
	}
	
	@Override
	public boolean isEntityApplicable(Entity entity) {
		
		if (entity.equals(target)) return true;
		
		if (entity instanceof EntityLiving) {
			EntityLiving e=(EntityLiving) entity;
			if (player.equals(e.getAttackTarget())) return true;
			if (e instanceof EntityMob && (e.getDataWatcher().getWatchableObjectString(10).indexOf(player.getDisplayName())<0)) return true;
			if (e instanceof EntityMob && player.equals( ((EntityMob)e).getAITarget() ) ) return true; 
		}
		
		return false;
	}

}
