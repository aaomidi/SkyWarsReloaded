package com.walrusone.skywars.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.walrusone.skywars.SkyWarsReloaded;

public class DeleteMapCommand implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean hasPerm = false;
		if (!(sender instanceof Player)) {
			hasPerm = true;
		} else if (sender instanceof Player) {
			Player player = (Player) sender;
			if (SkyWarsReloaded.perms.has(player, "swr.maps")) {
				hasPerm = true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
		}
		if (hasPerm) {
			if (args.length == 2) {
				String worldName = args[1].toLowerCase();
				for (World world: SkyWarsReloaded.get().getServer().getWorlds()) {
					if (world.getName().equalsIgnoreCase(worldName)) {
						World editWorld = SkyWarsReloaded.get().getServer().getWorld(worldName);
						for (Player player: editWorld.getPlayers()) {
							player.teleport(new Location(SkyWarsReloaded.get().getServer().getWorld("world"), -250, 64, -50));
						}
						editWorld.save();
						SkyWarsReloaded.getWC().unloadWorld(worldName);
						File dataDirectory = new File (SkyWarsReloaded.get().getDataFolder(), "maps");
						File target = new File (dataDirectory, worldName);
						File source = new File (SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath(), worldName);
						SkyWarsReloaded.getWC().deleteWorld(target);
						SkyWarsReloaded.getWC().deleteWorld(source);
						if (SkyWarsReloaded.getMC().mapRegistered(worldName)) {
							SkyWarsReloaded.getMC().removeMap(worldName);
						}
						if (SkyWarsReloaded.getMC().mapExists(worldName)) {
							SkyWarsReloaded.getMC().removeEditMap(worldName);
						}
						sender.sendMessage(ChatColor.RED + "Map " + worldName + " was deleted!");
						return true;
					}
				}
				File dataDirectory = new File (SkyWarsReloaded.get().getDataFolder(), "maps");
				File target = new File (dataDirectory, worldName);
				SkyWarsReloaded.getWC().deleteWorld(target);
				sender.sendMessage(ChatColor.RED + "Map " + worldName + " was deleted!");
				if (SkyWarsReloaded.getMC().mapRegistered(worldName)) {
					SkyWarsReloaded.getMC().removeMap(worldName);
				}
				if (SkyWarsReloaded.getMC().mapExists(worldName)) {
					SkyWarsReloaded.getMC().removeEditMap(worldName);
				}
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "USAGE: /swr delete <map name>");
			}
		} 
		return true;
	}
	
}
