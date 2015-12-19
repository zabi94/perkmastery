package zabi.minecraft.perkmastery.visual.effects;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class EffectRegistry {

	private static ConcurrentLinkedQueue<IRenderGeneral> renderList = new ConcurrentLinkedQueue<IRenderGeneral>();

	public static void addEffect(IRenderGeneral effect) {
		if (FMLCommonHandler.instance().getEffectiveSide().equals(Side.SERVER)) return;
		renderList.offer(effect);
	}

	public static void removeEffect(IRenderGeneral effect) {
		if (FMLCommonHandler.instance().getEffectiveSide().equals(Side.SERVER)) return;
		renderList.remove(effect);
	}

	public static Iterator<IRenderGeneral> getList() {
		if (FMLCommonHandler.instance().getEffectiveSide().equals(Side.SERVER)) return null;
		return renderList.iterator();
	}

	public static void purge() {
		if (FMLCommonHandler.instance().getEffectiveSide().equals(Side.SERVER)) return;
		renderList.clear();
	}

	public static boolean isListed(IRenderGeneral effect) {
		return renderList.contains(effect);
	}
}
