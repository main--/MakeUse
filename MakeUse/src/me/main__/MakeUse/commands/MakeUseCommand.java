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
		
		String command = args[1];
		String[] cmdargs = new String[args.length - 2];

		//check permissions
		if (!Util.permission((Player) sender, "makeuse." + command, sender.isOp())) return false;

		for (int i = 2; i < args.length; i++)
		{
			cmdargs[i - 2] = args[i];
		}
		
		Player targetPlayer = plugin.getServer().getPlayer(args[0]);
		
		if (targetPlayer == null)
		{
			sender.sendMessage("That player doesn't exist!");
			return true;
		}
/*		if (plugin.nodirectexec) {
			//the user doesn't want our new feature for whatever reasons
			targetPlayer.performCommand(commandLine);
			sender.sendMessage("Done.");
		} else {
			PluginCommand cmd = plugin.getServer().getPluginCommand(command);
			PermissionAttachment attachment = targetPlayer
					.addAttachment(plugin, cmd.getPermission(), true); // we add the permission to the player...
			boolean cmdresult = cmd.execute(sender, command, commandLine.split(" "));  // ... execute the command ...
			if (!cmdresult)
				sender.sendMessage("It's not MakeUse's fault, but I think that the command has failed.");
			else
				sender.sendMessage("Success!");
			
			targetPlayer.removeAttachment(attachment); // ... and remove the permission.
		} */
		try {
			if (plugin.execMode.getExecutor().execute(plugin, sender, targetPlayer, command, cmdargs))
				return true;
			else {
				sender.sendMessage("Failed...? You shouldn't see this, this is definitely a bug!");
				return true;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		sender.sendMessage("Something went seriously wrong.");
		return true;
	}

}
