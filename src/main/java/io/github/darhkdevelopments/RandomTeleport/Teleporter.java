package io.github.darhkdevelopments.RandomTeleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Teleporter 
{
	public static ArrayList<String> teleporters;
	public static Map<String, Integer> users;
	
	public Teleporter () {
		teleporters = new ArrayList<String> ();
		users = new HashMap<String, Integer> ();
	};
	
	public void teleport(Player p, Biome biome)
	{
		boolean success = false;
		int attempts = 0;
		
		Random rand = new Random();
		
		while(!success && attempts <= 1000)
		{
			boolean chunkLoadStatus = true;
			
			int x = rand.nextInt(6000) - 3000;
			int z = rand.nextInt(6000) - 3000;
			int newY = 150;
			Block b = null;
			Material blockType = null;
			Location teleLocation = null;
			
			Chunk ch = p.getWorld().getChunkAt(x, z);
			
			if(!(ch.isLoaded()))
			{
				if(!(ch.load()))
				{
					chunkLoadStatus = false;
				}
			}
			
			if(chunkLoadStatus)
			{
				double newX = (double)x + 0.5;
				double newZ = (double)z + 0.5;
				
				newY = p.getWorld().getHighestBlockYAt(Location.locToBlock(newX), Location.locToBlock(newZ));
				
				b = p.getWorld().getBlockAt(Location.locToBlock(newX), Location.locToBlock(newY), Location.locToBlock(newZ));
				
				if((b != null) && (b.getBiome() == biome))
				{
					blockType = b.getType();
					
					Location loc = new Location(p.getWorld(), newX, newY, newZ);
				
					Block head = p.getWorld().getBlockAt(loc).getRelative(BlockFace.UP);
					
					if(blockType != Material.LAVA && blockType != Material.STATIONARY_LAVA && blockType != Material.WATER && blockType != Material.STATIONARY_WATER && !(head.getType().isSolid()))
					{
						
						p.getWorld().refreshChunk(x, z);
						p.sendMessage(ChatColor.GREEN + "RTP: Success! Teleporting you in 5 seconds. Do not move.");
						newY = 150;
						teleLocation = new Location(p.getWorld(), x, newY, z);
						teleporters.add(p.getName());
						users.put(p.getName(), 600);
						this.countDown(p.getName(), teleLocation, p.getLocation());
						success = true;
					}
				}
				
				else
				{
					if(!(p.getWorld().isChunkInUse(Location.locToBlock(newX), Location.locToBlock(newZ))))
					{
						ch.unload();
					}
				}
				
			}
		
			attempts++;
		}
		
		
		if(!success)
		{
			p.sendMessage(ChatColor.RED + "Attempted 1000 random locations and failed... Please try another biome or try again later.");
			return;
		}
		
	}
	
	
	public void countDown(final String name, final Location teleportLocation, final Location previousLocation)
	{
		new BukkitRunnable()
		{
			int countVal = 5;
			Player p = Bukkit.getPlayer(name);
			@Override
			public void run()
			{
				if(previousLocation.getX() != p.getLocation().getX() || previousLocation.getY() != p.getLocation().getY() || previousLocation.getZ() != p.getLocation().getZ())
				{
					p.sendMessage(ChatColor.RED + "RTP: You moved. Teleportation Canceled.");
					if(!(p.getWorld().isChunkInUse(teleportLocation.getBlockX(), teleportLocation.getBlockZ())))
					{
						p.getWorld().unloadChunk(teleportLocation.getBlockX(), teleportLocation.getBlockZ());
					}
					
					
					this.cancel();
					return;
					
				}
				
				
				if(countVal <=0)
				{
					p.sendMessage(ChatColor.GREEN + "RTP: Teleporting now. May take a few seconds for the map to load. (Don't worry! The fall wont hurt!)");
					p.teleport(teleportLocation);
					this.cancel();
					return;
				}
				
				p.sendMessage(ChatColor.GREEN + "RTP: Teleporting in " + countVal);
				countVal--;
			}
		}.runTaskTimer(RandomTeleport.getInstance(), 0, 20);
	}
}
