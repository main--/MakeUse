package me.main__.MakeUse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class MakeUsePlayerListener extends PlayerListener {
	List<String> login = new ArrayList<String>();
	MakeUse plugin;
	
	public MakeUsePlayerListener(MakeUse instance)
	{
		plugin = instance;
	}
	
	@Override
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		Player p = e.getPlayer();
		String name = p.getName();
		File file = new File(p.getWorld().getName() + File.separator + "players" + File.separator, name + ".dat");
		boolean exists = file.exists();
		if (!exists)
		{
			login.add(name);
			Util.log("player logged in for the first time. The specified commands will be executed.".replaceAll("player", name));
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		String name = p.getName();
		if (login.contains(name))
		{
			//execute onnewusercmds
			for (String cmd : plugin.onnewusercmds)
				p.performCommand(cmd);
			
			login.remove(name);
		}
		else
		{
			//execute onjoincmds
			for (String cmd : plugin.onjoincmds)
				p.performCommand(cmd);
		}
	}
}
