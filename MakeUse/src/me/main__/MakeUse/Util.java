package me.main__.MakeUse;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.nijikokun.bukkit.Permissions.Permissions;

public class Util {
	public static final Logger logger = Logger.getLogger("Minecraft");
	public static boolean permission(Player player, String line, boolean def)
	{
		if (MakeUse.permissions != null) {
			return (((Permissions) MakeUse.permissions).getHandler()).has(player, line);
		}
		return def;
	}
	public static void log(String message)
	{
		log(message, Level.INFO);
	}
	public static void log(String message, Level loglevel)
	{
		logger.log(loglevel, "[MakeUse] message".replaceAll("message", message));
	}
}
