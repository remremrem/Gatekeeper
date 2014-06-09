package grokswell.gatekeeper;


import java.util.Arrays;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCSelector;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GatekeeperCommand {
	
	CommandSender sender;
	int IDarg;
	
	GatekeeperCommand(CommandSender snder, String[] args, GatekeeperPlugin GKP) {
		IDarg=-1;
		this.sender = snder;
		Player player = null;
		NPCSelector sel = CitizensAPI.getDefaultNPCSelector(); 
		List<String> argslist = Arrays.asList(args);
		
		if (!(sender instanceof Player)) {
			if ((!argslist.contains("--id")) && (!args[0].equals("list"))) {
				sender.sendMessage(ChatColor.YELLOW+"You must have an NPC selected or use the --id flag to execute this commnad");
				return;
			}
		}
	}
}
