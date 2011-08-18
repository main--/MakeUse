package me.main__.MakeUse.commands;

import me.main__.MakeUse.MakeUse;
import me.main__.MakeUse.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MakeUseCommand extends BaseCommand {
	public MakeUseCommand(MakeUse instance) {
		super(instance);
		needPlayer = true;
		permission = "";
	}
	
	public boolean run(CommandSender sender, String[] args) {
		if (args.length < 2) return false;
		//check permissions without arguments
		if (!Util.permission((Player) sender, "makeuse." + args[1], sender.isOp())) return false;
		//append them after that
		if (args.length > 2)
		{
			for (int i = 2; i < args.length; i++)
			{
				args[1] += " " + args[i];
			}
		}
		Player targetPlayer = plugin.getServer().getPlayer(args[0]);
		if (targetPlayer == null)
		{
			sender.sendMessage("That player doesn't exist!");
		}
		else
		{
			targetPlayer.performCommand(args[1]);
			sender.sendMessage("Success!");
		}
		
		return true;
	}

}
