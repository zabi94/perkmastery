package zabi.minecraft.perkmastery.container;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerEnchanter extends ContainerBase {

	@SuppressWarnings("unused")
	private EntityPlayer player;
	
	public ContainerEnchanter(EntityPlayer player) {
		this.player=player;

	}

}
