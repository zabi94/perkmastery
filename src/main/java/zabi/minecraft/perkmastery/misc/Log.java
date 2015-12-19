package zabi.minecraft.perkmastery.misc;

import org.apache.logging.log4j.Level;
import cpw.mods.fml.common.FMLLog;
import zabi.minecraft.perkmastery.libs.LibGeneral;


public class Log {

	public static void log(Level logLevel, Object object) {
		FMLLog.log(LibGeneral.MOD_ID, logLevel, String.valueOf(object));
	}

	public static void all(Object object) {
		log(Level.ALL, object);
	}

	public static void d(Object object) {
		log(Level.DEBUG, object);
	}

	public static void e(Object object) {
		log(Level.ERROR, object);
	}

	public static void i(Object object) {
		log(Level.INFO, object);
	}

	public static void w(Object object) {
		log(Level.WARN, object);
	}

}
