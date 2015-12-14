package zabi.minecraft.perkmastery.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.WorldSettings.GameType;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HackyPlayerControllerMP extends PlayerControllerMP {
	
	boolean isExtended=false;
	protected GameType gt;
	
	public HackyPlayerControllerMP(PlayerControllerMP PController) {
		super(Minecraft.getMinecraft(),(NetHandlerPlayClient) ReflectionHelper.getPrivateValue(PlayerControllerMP.class, PController, "field_78774_b", "netClientHandler", "b"));
		gt = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, PController, "field_78779_k", "currentGameType", "k");
		boolean i_f=Minecraft.getMinecraft().thePlayer.capabilities.isFlying;
		boolean a_f=Minecraft.getMinecraft().thePlayer.capabilities.allowFlying;
		this.setGameType(gt);
		Minecraft.getMinecraft().thePlayer.capabilities.isFlying=i_f;
		Minecraft.getMinecraft().thePlayer.capabilities.allowFlying=a_f;
		
	}
	
	public float getBlockReachDistance() {
		if (isExtended) return 9F;
        return gt.isCreative() ? 5.0F : 4.5F;
    }
	
	public void setExtended(boolean val) {
		isExtended=val;
	}
}

