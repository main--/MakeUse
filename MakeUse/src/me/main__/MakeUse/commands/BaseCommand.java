package me.main__.MakeUse.commands;

import me.main__.MakeUse.MakeUse;
import me.main__.MakeUse.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand {
	public boolean needPlayer;
	public String permission;
	protected MakeUse plugin;
	
	public BaseCommand(MakeUse instance)
	{
		plugin = instance;
	}
	
	public abstract boolean run(CommandSender sender, String[] args);
	
	public boolean execute(CommandSender sender, String[] args)
	{
		if ((!(sender instanceof Player)) && needPlayer) return false;
		if (sender instanceof Player && !Util.permission((Player) sender, permission, ((Player) sender).isOp())) return false;
		
		return run(sender, args);
	}
}
