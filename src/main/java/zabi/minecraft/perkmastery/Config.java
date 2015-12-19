package zabi.minecraft.perkmastery;

import java.io.File;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.misc.Log;


public class Config {

	public static Configuration	configuration;
	public static boolean		disableTileEntities;
	public static int			experienceCost;
	public static int			experienceCostTicksInterval;
	public static int			maxBookshelvesScanned;
	public static int			teleportationCost;
	public static int			maxIterations;
	public static int			newPerkTreeCost;

	public static void init(File configFile) {
		Log.i("Loading configuration...");
		if (configuration == null) {
			configuration = new Configuration(configFile);
			loadConfiguration();
		}
	}

	private static void loadConfiguration() {
		reload();
		if (configuration.hasChanged()) {
			configuration.save();
		}
	}

	public static void reload() {
		disableTileEntities = configuration.getBoolean("disableTileEntities", Configuration.CATEGORY_GENERAL, false, "Enable this to stop mod tile entities from ticking in case of world crash");
		experienceCost = configuration.getInt("experienceCost", Configuration.CATEGORY_GENERAL, 1, 1, 50, "Experience cost per tick of some abilities");
		experienceCostTicksInterval = configuration.getInt("experienceTick", Configuration.CATEGORY_GENERAL, 100, 1, 500, "The interval in ticks between experience drain");
		maxBookshelvesScanned = configuration.getInt("maxBookshelves", Configuration.CATEGORY_GENERAL, 30, -1, 100, "The max number of bookshelves scanned by the tome of experience. Setting this to a negative value disables the limit completely");
		teleportationCost = configuration.getInt("teleportationCost", Configuration.CATEGORY_GENERAL, 150, 1, 9999, "The experience cost of a use of the return tome");
		maxIterations = configuration.getInt("maxIterations", Configuration.CATEGORY_GENERAL, 512, 512, 8192, "The maximum depth for recursive functions. Changing this to high values may crash your game. If you really need to use bigger functions, use in conjunction with the java -Xss JVM option. [DO NOT MODIFY UNLESS YOU KNOW WHAT YOU ARE DOING - DO NOT REPORT BUGS WHEN THIS IS ENABLED]");
		newPerkTreeCost = configuration.getInt("newPerkTreeCost", Configuration.CATEGORY_GENERAL, 100, -1, 9999, "The experience cost to unlock the first ability of a new tree. Setting this to a negative value disables the ability to unlock a second tree");

	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(LibGeneral.MOD_ID)) {
			loadConfiguration();
		}
	}
}
