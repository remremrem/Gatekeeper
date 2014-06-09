package grokswell.gatekeeper;

//import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashMap;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCSelector;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class GatekeeperPlugin extends JavaPlugin implements Listener {
	ArrayList<String> gateCommands;
	ArrayList<Material> portalMaterials;
	ArrayList<Integer> gate_idnums_sorted = new ArrayList<Integer>();
	HashMap<Integer,GateObject> gates_by_idnum = new HashMap<Integer,GateObject>();
	HashMap<String,ArrayList<GateObject>> gates_by_name = new HashMap<String,ArrayList<GateObject>>();
	HashMap<String,GateObject> gate_selections = new HashMap<String,GateObject>();
	
	GateFile gateFile;
	Utils utils;
	
	public GatekeeperPlugin() {
		utils = new Utils();
		gateFile  = new GateFile(this);
		gateFile.loadGateFile(this);
		
		//default /gate subcommands
		gateCommands = new ArrayList<String>();
		gateCommands.add("open");
		gateCommands.add("close");
		gateCommands.add("toggle");
		gateCommands.add("remove");
		gateCommands.add("edit");
		gateCommands.add("list");
		gateCommands.add("select");
		
		//allowed materials for portals
		portalMaterials = new ArrayList<Material>();
		portalMaterials.add(Material.FIRE);
		portalMaterials.add(Material.WATER);
		portalMaterials.add(Material.LAVA);
		portalMaterials.add(Material.PORTAL);
		portalMaterials.add(Material.ENDER_PORTAL);
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {

		//REMOTEMENU 
		if (cmd.getName().equalsIgnoreCase("gatekeeper")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can use the command "+ChatColor.RED+"/gatekeeper");
				return true;
			}
			
			Player player = (Player) sender;
			NPCSelector sel = CitizensAPI.getDefaultNPCSelector();
			NPC npc = sel.getSelected(sender);
			return true;
		} 
		else if (cmd.getName().equalsIgnoreCase("gate")) {
			new GateCommand(sender, args, this);
		}
		return true;
	}
}
