package zabi.minecraft.perkmastery.handlers;

import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.visual.AnimationHelper;
import zabi.minecraft.perkmastery.visual.effects.IRenderGeneral;


@SideOnly(Side.CLIENT)
public class OreDetectionHandler implements IRenderGeneral {

	EntityPlayer				player;
	World						worldObj;
	private static final int	RADIUS	= 6;
	private static IIcon		icon;

	public OreDetectionHandler(EntityPlayer p, World world) {
		player = p;
		worldObj = world;
		icon = Items.emerald.getIconFromDamage(0);
	}

	@Override
	public void render(float ptick) {
		if (player.isSneaking() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemPickaxe) {
			for (int i = -RADIUS; i <= RADIUS; i++)
				for (int j = -RADIUS; j <= RADIUS; j++)
					for (int k = -RADIUS; k <= RADIUS; k++) {
						Block b = worldObj.getBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ + k);
						if (b.getUnlocalizedName().toLowerCase().contains("ore")) {
							renderHintAt((int) player.posX + i, (int) player.posY + j, (int) player.posZ + k, b, worldObj.getBlockMetadata((int) player.posX + i, (int) player.posY + j, (int) player.posZ + k), ptick);
						}
					}
		}
	}

	private void renderHintAt(int x, int y, int z, Block b, int meta, float ptick) {

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		int disc = x * y * z;
		double angolo = AnimationHelper.rotation(0.3, ptick, disc);
		GL11.glTranslated(x, y, z);
		GL11.glScaled(0.5, 0.5, 0.5);
		GL11.glTranslated(0.5, 0.5, 0.5);
		GL11.glTranslated(0.5, 0, 0);
		GL11.glRotated(angolo, 0, 1, 0);
		GL11.glTranslated(-0.5, 0, 0);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0F);
		GL11.glRotated(-angolo, 0, 1, 0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();

	}

}
