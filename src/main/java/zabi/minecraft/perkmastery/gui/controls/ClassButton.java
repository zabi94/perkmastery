package zabi.minecraft.perkmastery.gui.controls;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.visual.RenderHelper;


public class ClassButton extends GuiButton {

	protected static final ResourceLocation	buttonTexture	= new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/gui_book_frame.png");
	protected static final int				lato			= 26;
	private static final ItemStack[]		stacks			= new ItemStack[] { new ItemStack(Blocks.enchanting_table),						// Mago
															new ItemStack(Items.bow),														// Arciere
															new ItemStack(Items.golden_pickaxe),											// Minatore
															new ItemStack(Blocks.brick_block),												// Costruttore
															new ItemStack(Items.golden_sword),												// Guerriero
															new ItemStack(Items.map, 1, 1)													// Esploratore
																};

	public ClassButton(int id, int x, int y) {
		super(id, x, y, lato, lato, "");
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
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 26 * k, 202, lato, lato);
			GL11.glDisable(GL11.GL_BLEND);
			this.mouseDragged(mc, mouseX, mouseY);

			// Log.i(item.getItem().getUnlocalizedName());
			RenderHelper.renderItemstackInGUI(stacks[id], this.xPosition + 5, this.yPosition + 5);

		}
	}

	public int getHoverState(boolean isHovered) {
		return enabled ? (isHovered ? 2 : isComplete() ? 1 : 3) : 0;
	}

	private boolean isComplete() {
		return ExtendedPlayer.hasPlayerAquired(Minecraft.getMinecraft().thePlayer, PlayerClass.values()[id], 6);
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height && this.visible;
	}

	public static String getTooltip(int id) {
		switch (id) {
		case 0:
			return StatCollector.translateToLocal("classes.mage");
		case 1:
			return StatCollector.translateToLocal("classes.archer");
		case 2:
			return StatCollector.translateToLocal("classes.miner");
		case 3:
			return StatCollector.translateToLocal("classes.builder");
		case 4:
			return StatCollector.translateToLocal("classes.warrior");
		case 5:
			return StatCollector.translateToLocal("classes.explorer");
		}
		return null;
	}

	public String getTooltip() {
		return getTooltip(id);
	}

	public EnumChatFormatting getFormatting() {
		switch (id) {
		case 0:
			return EnumChatFormatting.LIGHT_PURPLE;
		case 1:
			return EnumChatFormatting.YELLOW;
		case 2:
			return EnumChatFormatting.DARK_GREEN;
		case 3:
			return EnumChatFormatting.GOLD;
		case 4:
			return EnumChatFormatting.RED;
		case 5:
			return EnumChatFormatting.AQUA;
		}
		return EnumChatFormatting.WHITE;
	}

}
