package me.main__.MakeUse.Executor;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class PerformExecutor extends Executor {
	
	protected boolean doExecute(JavaPlugin plugin, CommandSender sender, Player player, String command, String[] args) throws Throwable {
		StringBuilder commandLine = new StringBuilder(command);
        for (int i = 0; i < args.length; i++) {
            commandLine.append(" ");
            commandLine.append(args[i]);
        }
        
        PluginCommand cmd = null;
        PermissionAttachment attachment = null;
		try {
			cmd = plugin.getServer().getPluginCommand(command);
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
		
		player.performCommand(commandLine.toString());             // ... execute the command ...
		
		if (attachment != null)
			player.removeAttachment(attachment);                   // ... and remove the permission.
		
        sender.sendMessage("Done.");
        
	    return true;  //basically this just can't fail
	}
}
