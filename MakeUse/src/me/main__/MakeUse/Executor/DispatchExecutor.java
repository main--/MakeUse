package me.main__.MakeUse.Executor;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class DispatchExecutor extends Executor {

	public boolean doExecute(JavaPlugin plugin, CommandSender sender, Player player, String command, String[] args) throws Throwable {
		boolean retval = true;
		
		StringBuilder commandLine = new StringBuilder(command);
        for (int i = 0; i < args.length; i++) {
            commandLine.append(" ");
            commandLine.append(args[i]);
        }
		
		boolean cmdret = false;
        PermissionAttachment attachment = null;
		try {
			PluginCommand cmd = plugin.getServer().getPluginCommand(command);
			attachment = player
					.addAttachment(plugin, cmd.getPermission(), true, 20); // we add the permission to the player...
			//we let this expire after 20 ticks (one second) to make sure that he doesn't get it forever by accident
		} catch (Throwable t) {
			//t.printStackTrace(); //No stacktrace since this can happen
			
			//apparently this failed. last chance: give him all permissions
			attachment = player.addAttachment(plugin, 20);
			for (Permission p : plugin.getServer().getPluginManager().getPermissions()) {
				attachment.setPermission(p, true);
			}
		}
		
		try {
			cmdret = plugin.getServer().dispatchCommand(player, commandLine.toString());       // ... execute the command ...
		} catch (Exception e) {
			e.printStackTrace();
			retval = false;
		}
		
		if (attachment != null)
			player.removeAttachment(attachment);                   // ... and remove the permission.
		
		if (retval) {
			if (cmdret)
				sender.sendMessage("Done.");
			else
				sender.sendMessage("Couldn't find the specified command.");
		}
		
		return retval;
	}

}
