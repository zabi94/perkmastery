package zabi.minecraft.perkmastery.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;


public abstract class GuiBase extends GuiContainer {

	public GuiBase(Container p_i1072_1_) {
		super(p_i1072_1_);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		mc.getTextureManager().bindTexture(getTexture());
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	protected abstract ResourceLocation getTexture();

	public void drawTexturedModalRect(double x, double y, double u, double v, double w, double h) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double) (x + 0), (double) (y + h), (double) this.zLevel, (double) ((float) (u + 0) * f), (double) ((float) (v + h) * f1));
		tessellator.addVertexWithUV((double) (x + w), (double) (y + h), (double) this.zLevel, (double) ((float) (u + w) * f), (double) ((float) (v + h) * f1));
		tessellator.addVertexWithUV((double) (x + w), (double) (y + 0), (double) this.zLevel, (double) ((float) (u + w) * f), (double) ((float) (v + 0) * f1));
		tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) this.zLevel, (double) ((float) (u + 0) * f), (double) ((float) (v + 0) * f1));
		tessellator.draw();
	}

}
