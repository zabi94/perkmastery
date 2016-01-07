package zabi.minecraft.perkmastery.visual.renderer;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class RendererGrapplingHook extends RenderEntity {

	private static final ResourceLocation texture = new ResourceLocation(LibGeneral.MOD_ID, "textures/items/grapplingHook.png");

	public void doRender(Entity entity, double x, double y, double z, float rotYaw, float ptick) {

		EntityPlayer player = PerkMastery.proxy.getSinglePlayer();
		Tessellator tessellator = Tessellator.instance;

		float swingProgress = player.getSwingProgress(ptick);
		float handMotion = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
		Vec3 handOffset = Vec3.createVectorHelper(-0.7D, -0.7D, 0.8D);
		handOffset.rotateAroundX(-(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * ptick) * (float) Math.PI / 180.0F);
		handOffset.rotateAroundY(-(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * ptick) * (float) Math.PI / 180.0F);
		handOffset.rotateAroundY(handMotion * 0.5F);
		handOffset.rotateAroundX(-handMotion * 0.7F);
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * (double) ptick + handOffset.xCoord;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * (double) ptick + handOffset.yCoord;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) ptick + handOffset.zCoord;
		double yOffset = player == Minecraft.getMinecraft().thePlayer ? 0.0D : (double) player.getEyeHeight();

		if (this.renderManager.options.thirdPersonView > 0 || player != Minecraft.getMinecraft().thePlayer) {
			float rotationYaw = (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * ptick) * (float) Math.PI / 180.0F;
			double rotationProjectionAxis1 = (double) MathHelper.sin(rotationYaw);
			double rotationProjectionAxis2 = (double) MathHelper.cos(rotationYaw);

			double allingX = 0.40D;
			double allignY = 0.20D;

			playerX = player.prevPosX + (player.posX - player.prevPosX) * (double) ptick - rotationProjectionAxis2 * allingX - rotationProjectionAxis1 * allignY;
			playerY = player.prevPosY + yOffset + (player.posY - player.prevPosY) * (double) ptick - 0.70D;
			playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) ptick - rotationProjectionAxis1 * allingX + rotationProjectionAxis2 * allignY;
		}

		double entityX = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) ptick;
		double entityY = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) ptick + 0.25D;
		double entityZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) ptick;
		double directionX = (double) ((float) (playerX - entityX));
		double directionY = (double) ((float) (playerY - entityY));
		double directionZ = (double) ((float) (playerZ - entityZ));
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		tessellator.startDrawing(3);
		tessellator.setColorOpaque_I(0);
		byte stringQuality = 1;

		for (int i = 0; i <= stringQuality; ++i) {
			float segment = (float) i / (float) stringQuality;
			tessellator.addVertex(x + directionX * (double) segment, y + directionY * (double) (segment * segment + segment) * 0.5D + 0.25D, z + directionZ * (double) segment);
		}

		tessellator.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return texture;
	}
}
