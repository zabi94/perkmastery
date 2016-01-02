package zabi.minecraft.perkmastery.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;


public class TargetSelector implements IEntitySelector {

	public static final String	TAG_OWNER	= "evocationOwnerPlayer";

	EntityPlayer				player;
	EntityLiving				target;
	EntityLiving				self;

	public TargetSelector(EntityPlayer p, EntityLiving firstTarget, EntityLiving creature) {
		player = p;
		target = firstTarget;
		self = creature;
	}

	@Override
	public boolean isEntityApplicable(Entity entity) {

		if (entity.equals(target)) return true;
		if (entity.equals(player)) return false;
		if (entity instanceof EntityLiving) {
			EntityLiving e = (EntityLiving) entity;
			if (self.getEntityData() != null && self.getEntityData().hasKey(TAG_OWNER) && self.getEntityData().getString(TAG_OWNER).equals(player.getDisplayName())) return false;
			if (player.equals(e.getAttackTarget())) return true;
			if (e instanceof EntityMob && (e.getDataWatcher().getWatchableObjectString(10).indexOf(player.getDisplayName()) < 0)) return true;
			if (e instanceof EntityMob && player.equals(((EntityMob) e).getAITarget())) return true;
		}

		return false;
	}

}
