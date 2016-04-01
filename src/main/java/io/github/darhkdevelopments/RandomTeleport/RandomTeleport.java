package io.github.darhkdevelopments.RandomTeleport;

import org.bukkit.plugin.java.JavaPlugin;

public class RandomTeleport extends JavaPlugin
{
	private static RandomTeleport instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		CommandHandler cm = new CommandHandler();
		cm.setup();
		getLogger().info("Random Teleport Has Been Enabled");
		getCommand("rtp").setExecutor(cm);
		this.getServer().getPluginManager().registerEvents(new GameListener(), this);
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("Random Teleport Has Been Disabled");
	}
	
	
	
	public static RandomTeleport getInstance()
	{
		return instance;
	}
}
