/**
 * 
 */
package me.main__.MakeUse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.main__.MakeUse.commands.BaseCommand;
import me.main__.MakeUse.commands.MakeUseCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 * @author main()
 *
 */
public class MakeUse extends JavaPlugin {

	public static Plugin permissions;
	private HashMap<String, BaseCommand> commands = new HashMap<String, BaseCommand>();
	
	public List<String> onjoincmds = new ArrayList<String>();
	public List<String> onnewusercmds = new ArrayList<String>();
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onEnable()
	 */
	@Override
	public void onEnable() {
		commands.put("makeuse", new MakeUseCommand(this));
		permissions = this.getServer().getPluginManager().getPlugin("Permissions");
		
		File dataDirectory = new File("plugins" + File.separator + "MakeUse");
		dataDirectory.mkdirs();
		File configFile = new File(dataDirectory, "config.yml");
		if (!configFile.exists())
		{
			try {
				configFile.createNewFile();
///////////////////////////////////////////////////////////////////////////////////////////////////
/// Load defaults. (I know, it's a bit heavy for just 2 lines, but I like to do it properly ^^) ///
//////////////////                    (Taken from ModTRS)                        //////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
				InputStream input = MakeUse.class.getResourceAsStream("/defaults/config.yml");
				if (input != null)
				{
					FileOutputStream output = null;
					try {
		                output = new FileOutputStream(configFile);
		                byte[] buf = new byte[8192];
		                int length = 0;
		                while ((length = input.read(buf)) > 0) {
		                    output.write(buf, 0, length);
		                }

		            } catch (IOException e) {
		                e.printStackTrace();
		            } finally {
		                try {
		                    if (input != null) {
		                        input.close();
		                    }
		                } catch (IOException e) {
		                }

		                try {
		                    if (output != null) {
		                        output.close();
		                    }
		                } catch (IOException e) {
		                }
		            }
				}
///////////////////////////////////////////////////////////////////////////////////////////////////
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Configuration config = new Configuration(configFile);
		config.load();
		if (config.getProperty("onjoin") instanceof List)
			onjoincmds = config.getStringList("onjoin", null);
		else
			onjoincmds.add(config.getString("onjoin"));

		if (config.getProperty("onnewuser") instanceof List)
			onnewusercmds = config.getStringList("onnewuser", null);
		else
			onnewusercmds.add(config.getString("onnewuser"));

		if (onjoincmds == null)
			onjoincmds = new ArrayList<String>();
		if (onnewusercmds == null)
			onnewusercmds = new ArrayList<String>();
		
//		//debug
//		String onjoins = "onjoin: ";
//		for (String s : onjoincmds) {
//			onjoins += s + ", ";
//		}
//		Util.log(onjoins);
//		String onnews = "onnewuser: ";
//		for (String s : onnewusercmds) {
//			onnews += s + ", ";
//		}
//		Util.log(onnews);
		
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
	@Override
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
