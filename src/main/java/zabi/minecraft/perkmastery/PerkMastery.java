package zabi.minecraft.perkmastery;

import net.minecraftforge.common.MinecraftForge;
import zabi.minecraft.perkmastery.blocks.BlockList;
import zabi.minecraft.perkmastery.crafting.Recipes;
import zabi.minecraft.perkmastery.gui.GuiHandler;
import zabi.minecraft.perkmastery.handlers.TickHandler;
import zabi.minecraft.perkmastery.items.ItemList;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.misc.CommandControl;
import zabi.minecraft.perkmastery.misc.Log;
import zabi.minecraft.perkmastery.network.NetworkModRegistry;
import zabi.minecraft.perkmastery.proxy.CommonProxy;
import zabi.minecraft.perkmastery.tileentity.TileList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;


@Mod(modid=LibGeneral.MOD_ID, name=LibGeneral.MOD_NAME, version=LibGeneral.MOD_VERSION)
public class PerkMastery {
	
	public static SimpleNetworkWrapper network=NetworkRegistry.INSTANCE.newSimpleChannel(LibGeneral.MOD_ID);
	public static TickHandler tickHandler;
	public static zabi.minecraft.perkmastery.handlers.EventHandler eventi;
	
	
	@Instance
	public static PerkMastery instance;
	
	@SidedProxy(clientSide=LibGeneral.PROXY_CLIENT, serverSide=LibGeneral.PROXY_SERVER)
	public static CommonProxy proxy;
	
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent evt) {
		Log.i("o/ Thanks for trying this mod! Loading!");
		
		Log.i("Loading Configs");
		Config.init(evt.getSuggestedConfigurationFile());
		
		Log.i("Registering classes on their bus");
		tickHandler=new TickHandler();
		FMLCommonHandler.instance().bus().register(tickHandler);
		eventi=new zabi.minecraft.perkmastery.handlers.EventHandler();
		MinecraftForge.EVENT_BUS.register(eventi);
		proxy.registerAnimationHelper();
		
		Log.i("Registering Blocks, Items and Tile Entities");
		BlockList.register();
		ItemList.register();
		TileList.register();
		
		Log.i("Registering Keybindings");
		proxy.registerKeyBindings();
		
		Log.i("Registering recipes");
		Recipes.registerRecipes();
		
		Log.i("Registering GUIs");
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		
		Log.i("Registering renderers");
		proxy.registerTESR();
		
		
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent evt) {
		Log.i("Registering network messages");
		NetworkModRegistry.registerMessages(network);
	}
	
	@EventHandler
	  public void serverLoad(FMLServerStartingEvent event) {
		Log.i("Registering server commands: use /perkmastery or /pema");
	    event.registerServerCommand(new CommandControl());
	  }
	
}
