package zabi.minecraft.perkmastery.visual.renderer;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.tileentity.TileEntityDecanter;
import zabi.minecraft.perkmastery.visual.model.ModelDecanter;


public class RendererDecanter extends RendererBase implements IItemRenderer {

	private static final ModelDecanter		model				= new ModelDecanter();
	private static final ResourceLocation	texture_full		= new ResourceLocation(LibGeneral.MOD_ID, "textures/texturemaps/decanter_full.png");
	private static final ResourceLocation	texture_empty		= new ResourceLocation(LibGeneral.MOD_ID, "textures/texturemaps/decanter_empty.png");
	private static final TileEntityDecanter	decanter_instance	= new TileEntityDecanter();

	@Override
	public ModelBase getModel(TileEntity te, double x, double y, double z) {
		return model;
	}

	@Override
	public ResourceLocation getTexture(TileEntity te, double x, double y, double z) {
		if (((TileEntityDecanter) te).isFull()) return texture_full;
		return texture_empty;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON)) {
			GL11.glTranslated(0.5, 0.5, 0);
			GL11.glScaled(1.1, 1.1, 1.1);
		}
		if (type.equals(ItemRenderType.EQUIPPED)) {
			GL11.glTranslated(0.6, 0.6, 0.6);
			GL11.glScaled(0.9, 0.9, 0.9);
		}
		TileEntityRendererDispatcher.instance.renderTileEntityAt(decanter_instance, 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();

	}

}
