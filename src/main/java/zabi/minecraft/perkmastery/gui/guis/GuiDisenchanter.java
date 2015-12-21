package zabi.minecraft.perkmastery.gui.guis;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.gui.GuiBase;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.tileentity.TileEntityDisenchanter;


public class GuiDisenchanter extends GuiBase {

	private static final ResourceLocation	texture	= new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/disenchanter.png");
	private TileEntityDisenchanter			te;

	public GuiDisenchanter(Container cont, TileEntityDisenchanter disenchanter) {
		super(cont);
		te = disenchanter;
	}

	@Override
	protected ResourceLocation getTexture() {
		return texture;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ptick, int x, int y) {
		mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		double progress = te.getProgress();
		this.drawTexturedModalRect(guiLeft + 119, guiTop + 35, 0, 166, 22 * progress, 15);

	}

}
