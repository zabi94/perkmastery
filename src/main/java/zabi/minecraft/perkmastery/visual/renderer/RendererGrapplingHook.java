package zabi.minecraft.perkmastery.visual.renderer;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.entity.EntityGrapplingHook;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class RendererGrapplingHook extends RenderEntity {

	private static final ResourceLocation texture = new ResourceLocation(LibGeneral.MOD_ID, "textures/items/grapplingHook.png");

	public void doRender(Entity entity, double x, double y, double z, float rotYaw, float ptick) {
		GL11.glPushMatrix();
		renderOffsetAABB(entity.boundingBox, x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		// Minecraft.getMinecraft().renderEngine.bindTexture(this.getEntityTexture(null));
		EntityGrapplingHook hookEntity = (EntityGrapplingHook) entity;
		Tessellator t = Tessellator.instance;
		// t.startDrawingQuads();
		double xPosHook = (hookEntity.prevPosX + (hookEntity.posX - hookEntity.prevPosX) * (double) ptick);
		double yPosHook = (hookEntity.prevPosY + (hookEntity.posY - hookEntity.prevPosY) * (double) ptick);
		double zPosHook = (hookEntity.prevPosZ + (hookEntity.posZ - hookEntity.prevPosZ) * (double) ptick);
		EntityPlayer player = PerkMastery.proxy.getSinglePlayer();
		double xPosPlayer = (player.prevPosX + (player.posX - player.prevPosX) * (double) ptick);
		double yPosPlayer = (player.prevPosY + (player.posY - player.prevPosY) * (double) ptick);
		double zPosPlayer = (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) ptick);
		// double w = hookEntity.width;
		// double h = hookEntity.height;
		// double d = hookEntity.width;
		// t.addVertexWithUV(xPosHook - w, yPosHook - h, zPosHook - d, 1, 1);
		// t.addVertexWithUV(xPosHook - w, yPosHook + h, zPosHook + d, 1, 0);
		// t.addVertexWithUV(xPosHook + w, yPosHook + h, zPosHook + d, 0, 0);
		// t.addVertexWithUV(xPosHook + w, yPosHook - h, zPosHook - d, 0, 1);
		// t.draw();

		t.startDrawing(GL11.GL_LINES);// GL11.glBegin(GL11.GL_LINES);
		// GL11.glColor3d(0, 0, 0);
		GL11.glLineWidth(3f);
		t.addVertex(xPosHook, yPosHook, zPosHook);// GL11.glVertex3d(xPosHook, yPosHook, zPosHook);
		t.addVertex(xPosPlayer, yPosPlayer, zPosPlayer);// GL11.glVertex3d(xPosPlayer, yPosPlayer, zPosPlayer);
		t.draw();// GL11.glEnd();
		GL11.glPopMatrix();// GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return texture;
	}
}
