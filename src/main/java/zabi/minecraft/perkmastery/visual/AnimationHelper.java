package zabi.minecraft.perkmastery.visual;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimationHelper {
	
	public static int ticks=0;
	
	@SubscribeEvent
	public void clientTickEnd(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null || !gui.doesGuiPauseGame()) ticks++;
		}
	}
	
	public static double getNewAnimationTime(float ptick) {
//		Log.i("Generating new AT: "+(ticks+ptick));
		return ticks+ptick;
	}

	
	

	
	public static double oscillation(double slowness, float ptick) {
		return (Math.sin(((ticks+ptick)/slowness)+(0.75*Math.PI))+1)/2;
	}
	
	public static double oscillation(double slowness, float ptick, double animationTime) {
		return (Math.sin(((ticks+ptick-animationTime)/slowness)+(0.75*Math.PI))+1)/2;
	}
	
	public static double rotation(double slowness, float ptick) {
		return (((ticks+ptick)/slowness))%360;
	}
	
	public static double rotation(double slowness, float ptick, double animationTime) {
		return (((ticks+ptick-animationTime)/slowness))%360;
	}
	
		
	
	
	
	
	 
	
	
	
	
}
