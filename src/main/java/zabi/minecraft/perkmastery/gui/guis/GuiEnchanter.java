package zabi.minecraft.perkmastery.gui.guis;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.gui.GuiBase;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.tileentity.TileEntityEnchanter;

public class GuiEnchanter extends GuiBase {

	private static final ResourceLocation texture=new ResourceLocation(LibGeneral.MOD_ID,"textures/gui/enchanter.png");//TODO
	private TileEntityEnchanter te;
	
	
	public GuiEnchanter(Container cont, TileEntityEnchanter enchanter) {
		super(cont);
		te=enchanter;
	}

	@Override
	protected ResourceLocation getTexture() {
		return texture;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float ptick, int x, int y) {
		mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		int progress= (int) (73*te.getProgress());
//		int progress=(int) (73*AnimationHelper.rotation(0.2, ptick)/360);
		int sep=8;
		this.drawTexturedModalRect(guiLeft, guiTop+sep, 0, 166+sep, xSize, progress);
		
	}

}
