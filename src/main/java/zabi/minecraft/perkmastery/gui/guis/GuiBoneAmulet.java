package zabi.minecraft.perkmastery.gui.guis;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.gui.GuiBase;
import zabi.minecraft.perkmastery.libs.LibGeneral;

public class GuiBoneAmulet extends GuiBase {

	private static final ResourceLocation texture=new ResourceLocation(LibGeneral.MOD_ID,"textures/gui/talisman.png");
	
	public GuiBoneAmulet(Container p_i1072_1_) {
		super(p_i1072_1_);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ptick, int x, int y) {
		mc.getTextureManager().bindTexture(getTexture());
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	
	@Override
	protected ResourceLocation getTexture() {
		return texture;
	}

}
