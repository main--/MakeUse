package me.main__.MakeUse;

import org.bukkit.entity.Player;

import com.nijikokun.bukkit.Permissions.Permissions;

public class Util {
	public static boolean permission(Player player, String line, boolean def)
	{
		if (MakeUse.permissions != null) {
			return (((Permissions) MakeUse.permissions).getHandler()).has(player, line);
		}
		return def;
	}
}
