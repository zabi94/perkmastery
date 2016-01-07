package zabi.minecraft.perkmastery.handlers;

import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import zabi.minecraft.perkmastery.Config;
import zabi.minecraft.perkmastery.PerkMastery;
import zabi.minecraft.perkmastery.libs.LibGeneral;
import zabi.minecraft.perkmastery.misc.Log;


public class UpdateHandler implements Runnable {

	public static final String	urlbase		= "http://zabi.altervista.org/perkmastery_updates/";
	public static boolean		outdated	= false;

	private void checkForUpdates() {
		String remote = "";
		String s = Config.updateBranch.toLowerCase();
		if (!s.equals("stable") && !s.equals("alpha") && !s.equals("beta")) return;
		try {
			remote = urlbase + Config.updateBranch + "/" + LibGeneral.MOD_BUILD;
		} catch (Throwable e) {
			Log.i("This may be a dev environment, no update checking");
			return;
		}
		Log.i("Checking for updates: " + remote);
		if (!exists(remote)) {
			Log.i("Updates were found!");
			PerkMastery.proxy.getSinglePlayer().addChatMessage(new ChatComponentText(StatCollector.translateToLocal("mod.updates").replace("%url%", LibGeneral.UPDATE_MIRROR.replace("%channel%", Config.updateBranch))));
			outdated = true;
		}
	}

	private boolean exists(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void run() {

		try {
			Thread.sleep(10 * 1000);
			checkForUpdates();
		} catch (InterruptedException e) {

		}
	}

}
