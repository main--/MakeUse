package me.main__.MakeUse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import me.main__.MakeUse.commands.BaseCommand;
import me.main__.MakeUse.commands.MakeUseCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author main()
 */
public class MakeUse extends JavaPlugin {

	public static Plugin permissions;
	private HashMap<String, BaseCommand> commands = new HashMap<String, BaseCommand>();
	
	public List<String> onjoincmds = new ArrayList<String>();
	public List<String> onnewusercmds = new ArrayList<String>();
	
	public ExecutionMode execMode = ExecutionMode.DIRECT;
	
	public <T> List<T> test(List<T> def) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onEnable()
	 */
	@SuppressWarnings("unchecked")
	public void onEnable() {
		commands.put("makeuse", new MakeUseCommand(this));
		permissions = this.getServer().getPluginManager().getPlugin("Permissions");
		
		FileConfiguration config = this.getConfig();

		if (config.isString("onjoin"))
			onjoincmds.add(config.getString("onjoin"));
		else
			try {
				onjoincmds = config.getList("onjoin", new ArrayList<String>());
			} catch (Exception e) {
				e.printStackTrace();
				Util.log("Invalid config!", Level.SEVERE);
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			}

		if (config.isString("onnewuser"))
			onnewusercmds.add(config.getString("onnewuser"));
		else
			try {
				onnewusercmds = config.getList("onnewuser", new ArrayList<String>());
			} catch (Exception e) {
				e.printStackTrace();
				Util.log("Invalid config!", Level.SEVERE);
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			}
		
		execMode = ExecutionMode.parse(config.getString("execMode", execMode.getConfigString()));
		
		if (execMode == null)
			execMode = ExecutionMode.DIRECT;
		
		config.set("onjoin", onjoincmds);
		config.set("onnewuser", onnewusercmds);
		config.set("execMode", execMode.getConfigString());
		
		try {
			config.save(new File(this.getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			Util.log("Couldn't save the config! Disabling...", Level.SEVERE);
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		//now register the events
		if (!(onjoincmds.isEmpty() && onnewusercmds.isEmpty()))
		{
			PluginManager pm = getServer().getPluginManager();
			MakeUsePlayerListener playerListener = new MakeUsePlayerListener(this);
			pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
			if (!onnewusercmds.isEmpty())
				pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Event.Priority.Normal, this);
		}
		
		PluginDescriptionFile pdfFile = this.getDescription();
        Util.log(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
        Util.log(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		BaseCommand cmd = commands.get(command.getName().toLowerCase());
		if (cmd != null) return cmd.execute(sender, args);
		return false;
	}

}
