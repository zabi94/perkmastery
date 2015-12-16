package zabi.minecraft.perkmastery.gui.guis;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.gui.GuiBase;
import zabi.minecraft.perkmastery.libs.LibGeneral;

public class GuiEnchanter extends GuiBase {

	private static final ResourceLocation texture=new ResourceLocation(LibGeneral.MOD_ID,"pathToTexture");//TODO
	
	public GuiEnchanter(Container p_i1072_1_) {
		super(p_i1072_1_);
	}

	@Override
	protected ResourceLocation getTexture() {
		return texture;
	}

}
