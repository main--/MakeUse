/**
 * 
 */
package me.main__.MakeUse;

import java.util.HashMap;
import java.util.logging.Logger;

import me.main__.MakeUse.commands.BaseCommand;
import me.main__.MakeUse.commands.MakeUseCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author main()
 *
 */
public class MakeUse extends JavaPlugin {

	public static Plugin permissions;
	Logger log = Logger.getLogger("Minecraft");
	private HashMap<String, BaseCommand> commands = new HashMap<String, BaseCommand>();
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onEnable()
	 */
	@Override
	public void onEnable() {
		commands.put("makeuse", new MakeUseCommand(this));
		permissions = this.getServer().getPluginManager().getPlugin("Permissions");
		
		PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		BaseCommand cmd = commands.get(command.getName().toLowerCase());
		if (cmd != null) return cmd.execute(sender, args);
		return false;
	}

}
