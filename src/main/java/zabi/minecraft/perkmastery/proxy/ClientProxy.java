package zabi.minecraft.perkmastery.proxy;

import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.blocks.BlockList;
import zabi.minecraft.perkmastery.entity.EntityGrapplingHook;
import zabi.minecraft.perkmastery.entity.HackyPlayerControllerMP;
import zabi.minecraft.perkmastery.handlers.IntegrationHelper;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.libs.LibModIDs;
import zabi.minecraft.perkmastery.misc.Log;
import zabi.minecraft.perkmastery.network.packets.SyncFilterToServer;
import zabi.minecraft.perkmastery.network.packets.SyncInventoryToServer;
import zabi.minecraft.perkmastery.tileentity.TileEntityDecanter;
import zabi.minecraft.perkmastery.visual.AnimationHelper;
import zabi.minecraft.perkmastery.visual.renderer.RendererDecanter;
import zabi.minecraft.perkmastery.visual.renderer.RendererGrapplingHook;


public class ClientProxy extends CommonProxy {

	public static KeyBinding guiKey;

	@Override
	public void setPlayerExtraInventory(EntityPlayer player, int slot, ItemStack is) {
		PerkMastery.network.sendToServer(new SyncInventoryToServer(slot, is));
	}

	@Override
	public void setPlayerFilter(EntityPlayer player, int slot, ItemStack is) {
		PerkMastery.network.sendToServer(new SyncFilterToServer(slot, is));
	}

	@Override
	public void registerKeyBindings() {
		guiKey = new KeyBinding("key.openGui", Keyboard.KEY_R, LibGeneral.MOD_NAME);
		ClientRegistry.registerKeyBinding(guiKey);
	}

	@Override
	public void registerAnimationHelper() {
		FMLCommonHandler.instance().bus().register(new AnimationHelper());
	}

	@Override
	public EntityPlayer getSinglePlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void setupHackyController(boolean enable) {
		PlayerControllerMP pc = Minecraft.getMinecraft().playerController;
		if (IntegrationHelper.isModLoaded(LibModIDs.Botania) && isBotaniaController(pc)) Log.w("Botania is loaded, and modified reach distance at least one time. This means that the Long Hands perk may conflict with the Ring of Far Reach");
		if (!(pc instanceof HackyPlayerControllerMP)) Minecraft.getMinecraft().playerController = new HackyPlayerControllerMP(Minecraft.getMinecraft().playerController);
		((HackyPlayerControllerMP) Minecraft.getMinecraft().playerController).setExtended(enable);
	}

	private boolean isBotaniaController(PlayerControllerMP pc) {
		Class<?>[] interfacce = pc.getClass().getInterfaces();
		for (Class<?> cl : interfacce) {
			if (cl.getName().contains("IExtendedPlayerController")) return true;
		}
		return false;
	}

	@Override
	public void registerRenderers() {
		RendererDecanter rendererDecanter = new RendererDecanter();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecanter.class, rendererDecanter);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockList.decanter), rendererDecanter);
		RenderingRegistry.registerEntityRenderingHandler(EntityGrapplingHook.class, new RendererGrapplingHook());
	}

}
