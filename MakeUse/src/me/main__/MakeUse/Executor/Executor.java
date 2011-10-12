package me.main__.MakeUse.Executor;

import me.main__.MakeUse.ExecutionMode;
import me.main__.MakeUse.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Executor {
	private boolean used = false;
	private ExecutionMode mymode = null;
	
	/**
	 * @return the ExecutionMode
	 */
	public final ExecutionMode getMode() {
		if (mymode == null)
			throw new IllegalStateException("This hasn't been set!");
		
		return mymode;
	}

	/**
	 * @param the ExecutionMode to set
	 */
	public final void setMode(ExecutionMode mode) {
		if (mymode != null)
			throw new IllegalStateException("This Executor's mode has already been set!");
		
		this.mymode = mode;
	}

	/**
	 * Uses this executor.
	 * @param plugin
	 * The plugin that's executing.
	 * @param sender
	 * The CommandSender that has sent the command.
	 * @param player
	 * The player that's should use it.
	 * @param command
	 * The command.
	 * @param args
	 * The arguments of the command. (Including the command itself!)
	 * @return
	 * If executing worked fine. Note: 
	 */
 	public final boolean execute(JavaPlugin plugin, CommandSender sender, Player player, String command, String[] args) {
		if ((plugin == null) || (sender == null) || (player == null) || (command == null) || (args == null))
			return false;
		
		try {
			getMode();
		} catch (IllegalStateException e) {
			return false;
		}
		
		if (this.getUsed()) //can only use once
			return false;
		
		if (command.isEmpty())
			return false;
		
		try {
			boolean ret = this.doExecute(plugin, sender, player, command, args);
			used = true;
			if (!ret) {
				if (isFallbackPossible()) {
					ret = fallback(plugin, sender, player, command, args);
				}
			}
			return ret;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return
	 * If this Executor has already been used.
	 */
	public final boolean getUsed() {
		return used;
	}
	
	protected final boolean isFallbackPossible() {
		Util.log("isFallbackPossible was called and will return '" + String.valueOf(mymode.getFallback() != null) + "'!");
		return mymode.getFallback() != null;
	}
	
	protected final boolean fallback(JavaPlugin plugin, CommandSender sender, Player player, String command, String[] args) {
		return mymode.getFallback().getExecutor().execute(plugin, sender, player, command, args);
	}
	
	protected abstract boolean doExecute(JavaPlugin plugin, CommandSender sender, Player player, String command, String[] args) throws Throwable;
}
