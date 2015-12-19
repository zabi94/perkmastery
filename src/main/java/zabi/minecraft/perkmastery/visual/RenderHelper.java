package zabi.minecraft.perkmastery.visual;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;


@SideOnly(Side.CLIENT)
public class RenderHelper {

	private static final RenderItem ri = new RenderItem();

	public static void enableBlend(boolean sa_omsa) {
		GL11.glEnable(GL11.GL_BLEND);
		if (sa_omsa) GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void disableBlend() {
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void renderItemstackInGUI(ItemStack is, int x, int y) {
		Minecraft mc = Minecraft.getMinecraft();
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		ri.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), is, x, y);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
