package io.github.darhkdevelopments.RandomTeleport;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerFall(EntityDamageEvent ev)
	{
		if(ev.getEntity() instanceof Player)
		{
			String playerName = ((Player)ev.getEntity()).getName();
			if(Teleporter.teleporters.contains(playerName))
			{
				ev.setCancelled(true);
				Teleporter.teleporters.remove(playerName);
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent ev)
	{
		String playerName = ev.getPlayer().getName();
		
		if(Teleporter.teleporters.contains(playerName))
		{
			Teleporter.teleporters.remove(playerName);
		}
	}
}
