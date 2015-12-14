package zabi.minecraft.perkmastery.gui.guis;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.gui.GuiBase;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.tileentity.TileEntityDecanter;
import zabi.minecraft.perkmastery.visual.AnimationHelper;

public class GuiDecanter extends GuiBase {
	
	private static final ResourceLocation texture=new ResourceLocation(LibGeneral.MOD_ID,"textures/gui/decanter.png");

	TileEntityDecanter te;
	
	public GuiDecanter(Container container, TileEntityDecanter te) {
		super(container);
		this.te=te;
		ySize+=55;
		guiTop-=30;
	}

	@Override
	protected ResourceLocation getTexture() {
		return texture;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float ptick, int x, int y) {
		mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.drawTexturedModalRect(guiLeft, guiTop+9, 0, 222, xSize*te.getProgress(), 31);
		double dh=29-(AnimationHelper.rotation(0.15, ptick)*29/360);
		if (te.isRunning()) this.drawTexturedModalRect(guiLeft+10, guiTop+33+dh, 176, dh, 12, 29);
	}

}
