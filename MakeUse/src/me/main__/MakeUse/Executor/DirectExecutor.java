package me.main__.MakeUse.Executor;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class DirectExecutor extends Executor {

	public boolean doExecute(JavaPlugin plugin, CommandSender sender, Player player, String command, String[] args) throws Throwable {
		boolean retval = true;
		
		boolean cmdresult = false;
		PermissionAttachment attachment = null;
		PluginCommand cmd = null;
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
		
		if (cmd != null)
			try {
				cmdresult = cmd.execute(player, command, args);            // ... execute the command ...
			} catch (Throwable t) {
				t.printStackTrace();
				retval = false;
			}
		else {
			// Couldn't find the command
			retval = false;
		}
		
		if (attachment != null)
			player.removeAttachment(attachment);                   // ... and remove the permission.			
		
		if (retval) {
			if (!cmdresult)
				sender.sendMessage("It's not MakeUse's fault, but I think that the command has failed.");
			else
				sender.sendMessage("Success!"); // Write "Success!" here instead of the regular ...
												// ... "Done." because here it's sure that everything worked.
		}
		
		return retval;
	}

}
