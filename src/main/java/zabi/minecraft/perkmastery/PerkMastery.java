package zabi.minecraft.perkmastery;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import zabi.minecraft.perkmastery.blocks.BlockList;
import zabi.minecraft.perkmastery.crafting.Recipes;
import zabi.minecraft.perkmastery.gui.GuiHandler;
import zabi.minecraft.perkmastery.handlers.EventModHandler;
import zabi.minecraft.perkmastery.handlers.IntegrationHelper;
import zabi.minecraft.perkmastery.handlers.TickHandler;
import zabi.minecraft.perkmastery.items.ItemList;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.misc.Log;
import zabi.minecraft.perkmastery.misc.commands.CommandForceEnable;
import zabi.minecraft.perkmastery.misc.commands.CommandReset;
import zabi.minecraft.perkmastery.misc.commands.CommandSetAbilityLevel;
import zabi.minecraft.perkmastery.misc.commands.CommandUnlock;
import zabi.minecraft.perkmastery.network.NetworkModRegistry;
import zabi.minecraft.perkmastery.proxy.CommonProxy;
import zabi.minecraft.perkmastery.tileentity.TileList;


@Mod(modid = LibGeneral.MOD_ID, name = LibGeneral.MOD_NAME, version = LibGeneral.MOD_VERSION)
public class PerkMastery {

	public static SimpleNetworkWrapper	network	= NetworkRegistry.INSTANCE.newSimpleChannel(LibGeneral.MOD_ID);
	public static TickHandler			tickHandler;
	public static EventModHandler		eventi;

	@Instance
	public static PerkMastery			instance;

	@SidedProxy(clientSide = LibGeneral.PROXY_CLIENT, serverSide = LibGeneral.PROXY_SERVER)
	public static CommonProxy			proxy;

	@EventHandler
	public static void PreInit(FMLPreInitializationEvent evt) {
		Log.i("o/ Thanks for trying this mod! Loading!");

		Log.i("Loading Configs");
		Config.init(evt.getSuggestedConfigurationFile());

		Log.i("Registering classes on their bus");
		tickHandler = new TickHandler();
		FMLCommonHandler.instance().bus().register(tickHandler);
		eventi = new EventModHandler();
		MinecraftForge.EVENT_BUS.register(eventi);
		proxy.registerAnimationHelper();

		Log.i("Registering Blocks, Items and Tile Entities");
		BlockList.register();
		ItemList.register();
		TileList.register();

		Log.i("Registering Keybindings");
		proxy.registerKeyBindings();

		Log.i("Registering GUIs");
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		Log.i("Registering renderers");
		proxy.registerRenderers();

	}

	@EventHandler
	public static void Init(FMLInitializationEvent evt) {
		Log.i("Registering network messages");
		NetworkModRegistry.registerMessages(network);
	}

	@EventHandler
	public static void PostInit(FMLPostInitializationEvent evt) {
		Log.i("Registering recipes");
		Recipes.registerRecipes();
		Log.i("Checking for known mods");
		IntegrationHelper.checkLoadedMods();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		Log.i("Registering server commands");
		event.registerServerCommand(new CommandUnlock());
		event.registerServerCommand(new CommandReset());
		event.registerServerCommand(new CommandForceEnable());
		event.registerServerCommand(new CommandSetAbilityLevel());
	}

	@EventHandler
	public void serverLoaded(FMLServerStartedEvent event) {
		if (Config.disableTileEntities) Log.w("Tile Entities from this mod are disabled and not ticking. To enable them use the config file!");
		if (Config.maxIterations != 512) Log.w("\n\n\nRecursive functions limits are modified.\nAny bug report regarding java stack overflows will be immediately and promptly ignored.\nFurther attempts to contact dev(s) will result in blocking\n\n\n");
	}

	@EventHandler
	public void handleIMCMessages(FMLInterModComms.IMCEvent event) {
		for (final FMLInterModComms.IMCMessage msg : event.getMessages()) {
			if (msg.key.equalsIgnoreCase("excludeticking")) {
				if (msg.isItemStackMessage()) {
					Item it = msg.getItemStackValue().getItem();
					Log.i("Item added to backpack tick blacklist: " + it);
					tickHandler.updateBlackList.add(it);
				} else {
					Log.w(msg.getSender() + " tried to add an item to the blacklist, but did not use an ItemStack IMC. Ignoring");
				}
			} else if (msg.key.equalsIgnoreCase("addpickaxe")) {
				if (msg.isItemStackMessage()) {
					Log.i("Item added to pickaxes list: " + msg.getItemStackValue().getItem());
					IntegrationHelper.addPickaxe(msg.getItemStackValue());
				} else {
					Log.w(msg.getSender() + " tried to add a pickaxe, but did not use an ItemStack IMC. Ignoring");
				}
			} else {
				Log.w("Unsupported message from " + msg.getSender() + ": " + msg.key);
			}
		}
	}

}
