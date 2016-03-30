package minezero.randomteleport;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor
{
	Teleporter warper = new Teleporter();
	
	Map<String, Biome> biomes = new HashMap<String, Biome>();
	 
	public void setup()
	{
		biomes.put("swamp", Biome.SWAMPLAND);
		biomes.put("forest", Biome.FOREST);
		biomes.put("taiga", Biome.TAIGA);
		biomes.put("desert", Biome.DESERT);
		biomes.put("plains", Biome.PLAINS);
		biomes.put("ice plains", Biome.ICE_PLAINS);
		biomes.put("beach", Biome.BEACH);
		biomes.put("desert hills", Biome.DESERT_HILLS);
		biomes.put("taiga hills", Biome.TAIGA_HILLS);
		biomes.put("extreme hills", Biome.EXTREME_HILLS);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		
		if(cmd.getName().equalsIgnoreCase("rtp"))
		{
			
			if(!(sender instanceof Player))
			{
				sender.sendMessage("RTP: This command can only be executed from in game");
				return true;
			}
			
			if(args.length == 0)
			{
				for(String s : biomes.keySet())
				{
					sender.sendMessage("/rtp " + s);
				}
				return true;
			}
			
			if(args.length > 2)
			{
				sender.sendMessage(ChatColor.RED + "RTP: Invalid format. Please type /rtp for a valid list of commands");
				return true;
			}
			
			String tb = args[0];
			
			if(args.length == 2)
			{
				tb += " " + args[1];
			}
			
			Biome targetBiome = biomes.get(tb);
			
			if(targetBiome == null)
			{
				sender.sendMessage(ChatColor.RED + "RTP: Invalid biome. Type /rtp for a list of valid biomes");
				return true;
			}
			
			sender.sendMessage(ChatColor.GOLD + "RTP: Attempting to teleport you to a " + tb + " biome...");
			warper.teleport((Player)sender, targetBiome);
			return true;
		}
		
		
		return true;
	}
}
