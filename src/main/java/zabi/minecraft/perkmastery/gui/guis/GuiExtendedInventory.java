package zabi.minecraft.perkmastery.gui.guis;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.gui.GuiBase;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class GuiExtendedInventory extends GuiBase {

	private static final ResourceLocation texture = new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/extendedInventory.png");

	public GuiExtendedInventory(Container container) {
		super(container);
	}

	@Override
	protected ResourceLocation getTexture() {
		return texture;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ptick, int x, int y) {
		mc.getTextureManager().bindTexture(getTexture());
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
