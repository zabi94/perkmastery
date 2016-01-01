package zabi.minecraft.perkmastery.gui.controls;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.gui.guis.GuiPerks;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class PerkButton extends GuiButton {

	private static final ResourceLocation	textureMap		= new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/perkMap.png");
	private static final ResourceLocation	buttonTexture	= new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/gui_book_frame.png");
	public int								dx, dy, lvl, cls;

	public PerkButton(int id, int x, int y) {
		super(id, x, y, 26, 26, "");
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
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 26 * k, 228, 26, 26);
			GL11.glDisable(GL11.GL_BLEND);
			this.mouseDragged(mc, mouseX, mouseY);
			mc.renderEngine.bindTexture(textureMap);
			this.drawTexturedModalRect(this.xPosition + 4, this.yPosition + 4, dx, dy, 18, 18);
		}
	}

	public int getHoverState(boolean isHovered) {
		if (isAbilityEnabled()) return 4;
		if (!enabled) return 0;
		if (isHovered) return 2;
		if (!isAbilityPurchased()) return 1;
		return 3;

	}

	public void setup(PlayerClass clazz, int level) {
		dx = (level - 1) * 18;
		dy = (clazz.ordinal()) * 18;
		lvl = level;
		cls = clazz.ordinal();
	}

	public void setdown() {
		dx = -1;
		dy = -1;
		lvl = -1;
		cls = -1;
	}

	public void clicked() {
		EntityPlayer p = GuiPerks.player;
		if (ExtendedPlayer.hasPlayerAquired(p, PlayerClass.values()[cls], lvl)) {
			ExtendedPlayer.toggle(p, !ExtendedPlayer.isEnabled(p, PlayerClass.values()[cls], lvl), PlayerClass.values()[cls], lvl);
		} else {
			ExtendedPlayer.requestUnlockLevel(p, PlayerClass.values()[cls], lvl);
		}

	}

	public boolean isAbilityPurchased() {
		return ExtendedPlayer.getAbilityLevelFor(GuiPerks.player, PlayerClass.values()[cls]) >= lvl || ExtendedPlayer.getAbilityLevelFor(Minecraft.getMinecraft().thePlayer, PlayerClass.values()[cls]) >= lvl;
	}

	public boolean isAbilityEnabled() {

		return ExtendedPlayer.isEnabled(GuiPerks.player, PlayerClass.values()[cls], lvl) || ExtendedPlayer.isEnabled(Minecraft.getMinecraft().thePlayer, PlayerClass.values()[cls], lvl);
	}

	public int getTree() {
		return cls;
	}

	public int getLevel() {
		return lvl;
	}

	public String getTooltip() {
		return getTooltip(cls, lvl);
	}

	public static String getTooltip(int cls, int lvl) {
		switch (cls) {
		case 0:
			return StatCollector.translateToLocal("perk.mage." + lvl);
		case 1:
			return StatCollector.translateToLocal("perk.archer." + lvl);
		case 2:
			return StatCollector.translateToLocal("perk.miner." + lvl);
		case 3:
			return StatCollector.translateToLocal("perk.builder." + lvl);
		case 4:
			return StatCollector.translateToLocal("perk.warrior." + lvl);
		case 5:
			return StatCollector.translateToLocal("perk.explorer." + lvl);
		}
		return null;
	}

	public static String getTooltipDescription(int cls, int lvl) {
		switch (cls) {
		case 0:
			return StatCollector.translateToLocal("perk.desc.mage." + lvl);
		case 1:
			return StatCollector.translateToLocal("perk.desc.archer." + lvl);
		case 2:
			return StatCollector.translateToLocal("perk.desc.miner." + lvl);
		case 3:
			return StatCollector.translateToLocal("perk.desc.builder." + lvl);
		case 4:
			return StatCollector.translateToLocal("perk.desc.warrior." + lvl);
		case 5:
			return StatCollector.translateToLocal("perk.desc.explorer." + lvl);
		}
		return null;
	}

	public String getTooltipDescription() {
		return getTooltipDescription(cls, lvl);
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height && this.visible;
	}
}
