package zabi.minecraft.perkmastery.gui.guis;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import zabi.minecraft.perkmastery.Config;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;
import zabi.minecraft.perkmastery.gui.GuiBase;
import zabi.minecraft.perkmastery.gui.controls.ClassButton;
import zabi.minecraft.perkmastery.gui.controls.InventoryButton;
import zabi.minecraft.perkmastery.gui.controls.PerkButton;
import zabi.minecraft.perkmastery.items.ItemList;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.network.packets.OpenGuiMessage;
import zabi.minecraft.perkmastery.visual.RenderHelper;


public class GuiPerks extends GuiBase {

	private static final ResourceLocation	texture			= new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/gui_book_frame.png");
	private static final ResourceLocation[]	parallax		= new ResourceLocation[] { new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/gui_book_bg0.png"), new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/gui_book_bg1.png"), new ResourceLocation(LibGeneral.MOD_ID, "textures/gui/gui_book_bg2.png") };

	private static final char				ENDLINE_CHAR	= '_';

	private static ClassButton[]			classes			= new ClassButton[6];
	private static PerkButton[]				abilities		= new PerkButton[6];
	private static InventoryButton[]		inventories		= new InventoryButton[6];

	public static EntityPlayer				player;

	public GuiPerks(Container container) {
		super(container);
		GuiPerks.player = PerkMastery.proxy.getSinglePlayer();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ptick, int mouseX, int mouseY) {
		if (!Config.fpsSavingMode) {
			RenderHelper.enableBlend(true);
			mc.getTextureManager().bindTexture(parallax[0]);
			this.drawTexturedModalRect(guiLeft - 30, guiTop - 3, mouseX * 0.05, mouseY * 0.05, 236, 165);
		}
		mc.getTextureManager().bindTexture(parallax[1]);
		this.drawTexturedModalRect(guiLeft - 30, guiTop - 3, 12 + (mouseX * 0.01), 45 + (mouseY * 0.01), 236, 165);
		if (!Config.fpsSavingMode) {
			mc.getTextureManager().bindTexture(parallax[2]);
			this.drawTexturedModalRect(guiLeft - 30, guiTop - 3, -mouseX * 0.07, -mouseY * 0.07, 236, 165);
			this.drawTexturedModalRect(guiLeft - 30, guiTop - 3, -mouseX * 0.03 + 20, -mouseY * 0.03 + 20, 236, 165);
		}

		GL11.glColor4d(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft - 40, guiTop - 18, 0, 0, xSize + 80, ySize + 36);
		GL11.glDisable(GL11.GL_BLEND);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		ArrayList<String> tooltip = new ArrayList<String>();
		for (Object b : buttonList) {
			if (b instanceof ClassButton) {
				ClassButton cb = (ClassButton) b;
				if (cb.isHovered(mouseX, mouseY)) tooltip.add(cb.getFormatting() + cb.getTooltip());
				if (cb.isHovered(mouseX, mouseY) && !cb.enabled) tooltip.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("general.mustUnlockClass"));
			} else if (b instanceof PerkButton) {
				PerkButton pb = (PerkButton) b;
				if (pb.isHovered(mouseX, mouseY)) {
					tooltip.add(EnumChatFormatting.UNDERLINE.toString() + EnumChatFormatting.BOLD + pb.getTooltip());
					if (!pb.isAbilityPurchased()) tooltip.add(getColorUnlocked(player, pb) + StatCollector.translateToLocal("generic.requiredExp") + ": " + ExtendedPlayer.getRequiredLevelsForAbility(player, pb.lvl));
					addMultipleTooltip(tooltip, pb.getTooltipDescription());
				}
			} else if (b instanceof InventoryButton) {
				InventoryButton ib = (InventoryButton) b;
				if (ib.isHovered(mouseX, mouseY)) {
					tooltip.add(InventoryTooltips[ib.id - 12]);
				}
			}

		}

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		if (!tooltip.isEmpty()) {
			drawHoveringText(tooltip, mouseX - k, mouseY - l, fontRendererObj);
		}
	}

	private EnumChatFormatting getColorUnlocked(EntityPlayer player, PerkButton pb) {
		return ExtendedPlayer.canAffordAbility(player, pb.lvl) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
	}

	@Override
	protected ResourceLocation getTexture() {
		return texture;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

		for (int i = 0; i < 6; i++) {
			classes[i] = new ClassButton(i, getDX(i), getDY(i));
			abilities[i] = new PerkButton(6 + i, getDX(i + 0.5), getDY(i + 0.5));
			abilities[i].visible = false;
			buttonList.add(classes[i]);
			buttonList.add(abilities[i]);
			if (ExtendedPlayer.hasUnfinishedTrees(player) && !ExtendedPlayer.isPlayer(player, PlayerClass.values()[i])) {
				classes[i].enabled = ExtendedPlayer.getEnabledAbilities(player)[i] > 0;

			}

		}

		for (int i = 0; i < inventories.length; i++) {
			inventories[i] = new InventoryButton(12 + i, 5, (this.height / 2) + (40 * i) - (40 * inventories.length / 2) + 10, icons[i]);
			buttonList.add(inventories[i]);
		}
		setActivationInventories();
	}

	private void setActivationInventories() {
		for (int i = 0; i < inventories.length; i++) {
			inventories[i].visible = isEnabledInventory(i);
		}
	}

	private boolean isEnabledInventory(int i) {

		switch (i) {
		case 2:
		case 1:
			return ExtendedPlayer.hasPlayerAquired(player, PlayerClass.MINER, 6);
		case 0:
			return ExtendedPlayer.hasPlayerAquired(player, PlayerClass.EXPLORER, 2);
		case 3:
			return ExtendedPlayer.hasPlayerAquired(player, PlayerClass.MAGE, 6);
		case 4:
			return ExtendedPlayer.hasPlayerAquired(player, PlayerClass.MINER, 5);
		case 5:
			return ExtendedPlayer.hasPlayerAquired(player, PlayerClass.WARRIOR, 3);
		}

		return false;
	}

	private int getDX(double i) {
		return (int) (guiLeft + 75 + (Math.cos((i * Math.PI / 3)) * 50));
	}

	private int getDY(double i) {
		return (int) (guiTop + 65 + (Math.sin((i * Math.PI / 3)) * 50));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id < 6) showSubMenu(guibutton.id);
		else if (guibutton.id < 12) {
			PerkButton btn = ((PerkButton) guibutton);
			btn.clicked();
			if (ExtendedPlayer.hasPlayerAquired(player, PlayerClass.values()[btn.getTree()], btn.getLevel()) && btn.getLevel() < 6) abilities[btn.getLevel()].enabled = true;
			this.setActivationInventories();
			this.updateScreen();
		} else {
			showExtraInv(guibutton.id % 6);
		}
	}

	private void showExtraInv(int i) {

		PerkMastery.network.sendToServer(new OpenGuiMessage(i));

		if (i == 1) {
			this.mc.displayGuiScreen(new GuiCrafting(player.inventory, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ));
		}
	}

	private void showSubMenu(int id) {

		for (int i = 0; i < 6; i++) {
			classes[i].visible = false;
			abilities[i].visible = true;
			abilities[i].setup(PlayerClass.values()[id], i + 1);
			abilities[i].enabled = true;
			if (ExtendedPlayer.getAbilityLevelFor(player, PlayerClass.values()[id]) < i) abilities[i].enabled = false;
		}

		this.updateScreen();

	}

	private void returnToMenu() {
		for (int i = 0; i < 6; i++) {
			classes[i].enabled = true;
			classes[i].visible = true;
			abilities[i].visible = false;
			abilities[i].setdown();
			if (ExtendedPlayer.hasUnfinishedTrees(player) && !ExtendedPlayer.isPlayer(player, PlayerClass.values()[i])) classes[i].enabled = ExtendedPlayer.getEnabledAbilities(player)[i] > 0;
		}

	}

	protected void mouseClicked(int mx, int my, int mb) {
		super.mouseClicked(mx, my, mb);
		if (mb == 1) returnToMenu();
	}

	private void addMultipleTooltip(ArrayList<String> tooltip, String rawString) {

		if (GuiPerks.isShiftKeyDown()) {
			while (rawString.contains("" + ENDLINE_CHAR)) {
				tooltip.add(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + rawString.substring(0, rawString.indexOf(ENDLINE_CHAR)));
				rawString = rawString.substring(rawString.indexOf(ENDLINE_CHAR) + 1);
			}
			tooltip.add(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + rawString);
		} else {
			tooltip.add((EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("general.gui.holdShift")).trim());
		}
	}

	private static final ItemStack[]	icons				= new ItemStack[] {
																	// Extra inventory
																	new ItemStack(Blocks.chest),
																	// Crafting Table
																	new ItemStack(Blocks.crafting_table),
																	// Furnace
																	new ItemStack(Blocks.furnace),
																	// Bone Amulet
																	new ItemStack(ItemList.boneAmulet),
																	// Filter
																	new ItemStack(Blocks.hopper),
																	// Chainmail
																	new ItemStack(Items.chainmail_helmet) };

	private static final String[]		InventoryTooltips	= new String[] {
																	// Extra inventory
																	getFormatted(PlayerClass.EXPLORER, 2),
																	// Crafting Table
																	getFormatted(PlayerClass.MINER, 6),
																	// Furnace
																	getFormatted(PlayerClass.MINER, 6),
																	// Bone Amulet
																	getFormatted(PlayerClass.MAGE, 6),
																	// Filter
																	getFormatted(PlayerClass.MINER, 5),
																	// Chainmail
																	getFormatted(PlayerClass.WARRIOR, 3), };

	private static String getFormatted(PlayerClass pc, int level) {
		return PerkButton.getTooltip(pc.ordinal(), level) + " (" + ClassButton.getTooltip(pc.ordinal()) + ")";
	}
}
