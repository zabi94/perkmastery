package zabi.minecraft.perkmastery.gui.controls;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.visual.RenderHelper;


public class InventoryButton extends GuiButton {

	protected static final ResourceLocation	buttonTexture	= new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/gui_book_frame.png");
	protected static final int				lato			= 26;
	protected ItemStack						representative;

	public InventoryButton(int id, int x, int y, ItemStack icon) {
		super(id, x, y, lato, lato, "");
		this.representative = icon;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			mc.getTextureManager().bindTexture(buttonTexture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int k = this.getHoverState(this.field_146123_n);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 26 * k, 228, lato, lato);
			GL11.glDisable(GL11.GL_BLEND);
			this.mouseDragged(mc, mouseX, mouseY);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderHelper.renderItemstackInGUI(representative, this.xPosition + 5, this.yPosition + 5);
		}
	}

	public int getHoverState(boolean isHovered) {
		return enabled ? (isHovered ? 2 : 1) : 0;
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height && this.visible;
	}

}
