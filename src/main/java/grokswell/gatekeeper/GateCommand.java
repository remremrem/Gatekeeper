package grokswell.gatekeeper;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class GateCommand {
	CommandSender sender;
	List<String> argslist;
	int width;
	int height;
	int depth;
	int idnum;
	int mdata;
	int open_mdata;
	Material material;
	Material open_material;
	String gate_name;
	
	GateCommand(CommandSender snder, String[] args, GatekeeperPlugin plugin) {
		Player player = null;
		this.sender = snder;
		
		if (args.length == 0) {
			sender.sendMessage("Command /gate requires additional arguments");
			return;
		}
		
		//gate commands that can be executed from the server console
		ArrayList<String> consoleCommands = new ArrayList<String>(); 
		consoleCommands.add("open");
		consoleCommands.add("close");
		consoleCommands.add("toggle");
		consoleCommands.add("remove");
		consoleCommands.add("animation");
		consoleCommands.add("list");
		consoleCommands.add("portal");
		

		
		List<String> argslist = Arrays.asList(args);
		width=3;
		height=3;
		depth=1;
		idnum=0;
		mdata=0;
		material=Material.IRON_FENCE;
		open_material=Material.AIR;
		open_mdata=0;
		gate_name="";
		
		if (!(sender instanceof Player)) {
			if ((!consoleCommands.contains(args[0]))) {
				sender.sendMessage(ChatColor.YELLOW+"You must be a player to execute commnad: "+args[0]);
				return;
			}
		} else {
			player = (Player) snder;
			if (plugin.gate_selections.containsKey(player.getName())) {
				idnum = plugin.gate_selections.get(player.getName()).idnum;
			}
		}
		
		//check for prefixed arguments like "-m" for material
		try {
			if (argslist.contains("-w")){
				width=Integer.parseInt(argslist.get(argslist.indexOf("-w")+1));
			}
			if (argslist.contains("-h")){
				height=Integer.parseInt(argslist.get(argslist.indexOf("-h")+1));
			}
			if (argslist.contains("-d")){
				depth=Integer.parseInt(argslist.get(argslist.indexOf("-d")+1));
			}
			if (argslist.contains("-i")){
				idnum=Integer.parseInt(argslist.get(argslist.indexOf("-i")+1));
			}
		} catch (Exception e) {
			player.sendMessage("-h, -w, ,-d and -i must be followed by a simple Integer value, such as \"11\"");
		}
		
		if (argslist.contains("-n")){
			gate_name=argslist.get(argslist.indexOf("-n")+1);
		}
		
		//parse material data from command
		if (argslist.contains("-m")){
			String b = argslist.get(argslist.indexOf("-m")+1);
			if (b.contains(":")){
				String[] m = b.split(":");
			    try {
			    	int id = Integer.parseInt(m[0]);
			    	material = Material.getMaterial(id);
			    } catch (Exception e) {
					try {
				    	material = Material.getMaterial(m[0]);
					} catch (Exception e2) {
						player.sendMessage("Invalid Material: " +m[0]);
					}
			    } finally {
			    	try {
			    		mdata = Integer.parseInt(m[1]);
			    	} catch (Exception e) {
						player.sendMessage("Invalid Data: "+m[1]);
			    	}
			    }
			} else {
				try {
			    	int id = Integer.parseInt(b);
			    	material = Material.getMaterial(id);
				} catch (Exception e) {
					try {
						material = Material.getMaterial(b);
					} catch (Exception e2) {
						player.sendMessage("Invalid Material: "+b);
					}
				}
			}
		}
		
		//parse open material data from command
		if (argslist.contains("-o")){
			String b = argslist.get(argslist.indexOf("-o")+1);
			if (b.contains(":")){
				String[] m = b.split(":");
			    try {
			    	int id = Integer.parseInt(m[0]);
			    	open_material = Material.getMaterial(id);
			    } catch (Exception e) {
					try {
						open_material = Material.getMaterial(m[0]);
					} catch (Exception e2) {
						player.sendMessage("Invalid Material: " +m[0]);
					}
			    } finally {
			    	try {
			    		open_mdata = Integer.parseInt(m[1]);
			    	} catch (Exception e) {
						player.sendMessage("Invalid Data: "+m[1]);
			    	}
			    }
			} else {
				try {
			    	int id = Integer.parseInt(b);
			    	open_material = Material.getMaterial(id);
				} catch (Exception e) {
					try {
						open_material = Material.getMaterial(b);
					} catch (Exception e2) {
						player.sendMessage("Invalid Material: "+b);
					}
				}
			}
		}
		
		
		//CREATE
		if (args[0].toLowerCase().equals("create")){
			if (player == null){
				sender.sendMessage("Only players can create new gates");
				return;
			}
			Location player_loc = player.getLocation();
			//out.println("player_loc: "+player_loc);
			Vector direction=plugin.utils.getCardinalDirection(player.getLocation());
			//out.println("direction: "+direction);
			
			GateObject newgate = GateObject.Create(plugin, player_loc, direction, player.getName());
			//new GateObject(player.getName(), plugin, GateObject.getNewGateID(plugin), player_loc.add(direction), direction);
			newgate.SetSize(width, height, depth);
			newgate.SetMaterial(material, mdata);
			newgate.SetOpenMaterial(open_material, open_mdata);
			newgate.SetName(gate_name);
			return;
		}
		//!CREATE
		
		//REMOVE
		if (args[0].toLowerCase().equals("remove")){
			if (plugin.gate_idnums_sorted.contains(idnum)){ 			
				GateObject.Remove(plugin, idnum);
				sender.sendMessage("Permanently removed gate "+idnum+".");
				return;
			} else {
				sender.sendMessage("Gate id '"+idnum+"' does not exist");
				return;
			}
		}
		//!REMOVE
		
		//GATE LIST
		if (args[0].toLowerCase().equals("list")) {
			for (Integer i : plugin.gate_idnums_sorted) {
				player.sendMessage(i+":");
				player.sendMessage(plugin.gates_by_idnum.get(i).gate_name+",");
			}
			return;
		}
		//!GATE LIST
		
		//OPEN
		if (args[0].toLowerCase().equals("open")) {
			if (plugin.gate_idnums_sorted.contains(idnum)){ 			
				plugin.gates_by_idnum.get(idnum).open();
				return;
			} else {
				sender.sendMessage("Gate id '"+idnum+"' does not exist");
				return;
			}
		}
		//!OPEN
		
		//CLOSE
		if (args[0].toLowerCase().equals("close")) {
			if (plugin.gate_idnums_sorted.contains(idnum)){ 			
				plugin.gates_by_idnum.get(idnum).close();
				return;
			} else {
				sender.sendMessage("Gate id '"+idnum+"' does not exist");
				return;
			}
		}
		//!CLOSE
		
		//TOGGLE
		if (args[0].toLowerCase().equals("toggle")) {
			if (plugin.gate_idnums_sorted.contains(idnum)){ 			
				plugin.gates_by_idnum.get(idnum).toggle();
				return;
			} else {
				sender.sendMessage("Gate id '"+idnum+"' does not exist");
				return;
			}
		}
		//!TOGGLE
		
		//SELECT
		if (args[0].toLowerCase().equals("select")) {
			if ((!argslist.contains("-i")) && argslist.size()>1){
				try {
					idnum=Integer.parseInt(args[1]);
				} catch(Exception e){
					sender.sendMessage(args[1]+" is not a valid gate id.");
					return;
				}
			}
			if (plugin.gate_idnums_sorted.contains(idnum)){ 			
				plugin.gate_selections.put(player.getName(), plugin.gates_by_idnum.get(idnum));
				return;
			} else {
				sender.sendMessage("Gate id '"+idnum+"' does not exist");
				return;
			}
		}
		//!SELECT
		
		//INFO
		if (args[0].toLowerCase().equals("info")) {
			if (plugin.gate_idnums_sorted.contains(idnum)){ 			
				GateObject gate = plugin.gates_by_idnum.get(idnum);
				sender.sendMessage("ID: "+gate.idnum+ " Name: "+gate.gate_name);
				sender.sendMessage("W: "+gate.width+ " H: "+gate.height+ " D: "+gate.depth);
				sender.sendMessage("Material: "+gate.material.name());
				sender.sendMessage("Owner: "+gate.owner);
				sender.sendMessage("Location: "+gate.location);
				return;
			} else {
				sender.sendMessage("Gate id '"+idnum+"' does not exist");
				return;
			}
		}
		//!INFO
		
		//EDIT
		if (args[0].toLowerCase().equals("edit")) {
			GateObject gate;
			if (plugin.gate_idnums_sorted.contains(idnum)){ 			
				gate = plugin.gates_by_idnum.get(idnum);
			} else {
				sender.sendMessage("Gate id '"+idnum+"' does not exist");
				return;
			}
			
			if (!this.gate_name.equals("")){
				gate.SetName(this.gate_name);
			}
			
			if (argslist.contains("-h")){
				gate.SetHeight(this.height);
			}
			
			if (argslist.contains("-w")){
				gate.SetWidth(this.width);
			}
			
			if (argslist.contains("-d")){
				gate.SetDepth(this.depth);
			}
			
			if (argslist.contains("-p")){
				gate.SetPosition(player);
			}
			
			if (argslist.contains("-m")){
				gate.SetMaterial(this.material, this.mdata);
			}
			
			if (argslist.contains("-o")){
				gate.SetOpenMaterial(this.open_material, this.open_mdata);
			}

			plugin.gateFile.saveGate(gate);
		}
		//!EDIT
	}

}
