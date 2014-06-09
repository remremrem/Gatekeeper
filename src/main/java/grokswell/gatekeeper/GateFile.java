package grokswell.gatekeeper;

import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class GateFile {
	private static File dataFolder;
	private GatekeeperPlugin plugin;
	YamlConfiguration gateFD;

	public GateFile(GatekeeperPlugin plgn) {
	    plugin = plgn;
		dataFolder = plugin.getDataFolder();
		gateFD = new YamlConfiguration();
		if ( !dataFolder.isDirectory() )  dataFolder.mkdir();
		//loadGateFile(plugin);
	}
	
	
	public YamlConfiguration getGateData() {
		return this.gateFD;
	}
	
	
	public void saveGate(GateObject gate) {
		gateFD.set(String.valueOf(gate.idnum)+".name", gate.gate_name);
		gateFD.set(String.valueOf(gate.idnum)+".width", gate.width);
		gateFD.set(String.valueOf(gate.idnum)+".height", gate.height);
		gateFD.set(String.valueOf(gate.idnum)+".depth", gate.depth);
		gateFD.set(String.valueOf(gate.idnum)+".material", gate.material.name());
		gateFD.set(String.valueOf(gate.idnum)+".open_material", gate.open_material.name());
		gateFD.set(String.valueOf(gate.idnum)+".mdata", gate.mdata);
		gateFD.set(String.valueOf(gate.idnum)+".open_mdata", gate.open_mdata);
		gateFD.set(String.valueOf(gate.idnum)+".world", gate.world);
		gateFD.set(String.valueOf(gate.idnum)+".location.x", gate.location.getX());
		gateFD.set(String.valueOf(gate.idnum)+".location.y", gate.location.getY());
		gateFD.set(String.valueOf(gate.idnum)+".location.z", gate.location.getZ());
		gateFD.set(String.valueOf(gate.idnum)+".player_facing.x", gate.player_facing.getBlockX());
		gateFD.set(String.valueOf(gate.idnum)+".player_facing.y", gate.player_facing.getBlockY());
		gateFD.set(String.valueOf(gate.idnum)+".player_facing.z", gate.player_facing.getBlockZ());
		gateFD.set(String.valueOf(gate.idnum)+".animation", gate.animation);
		gateFD.set(String.valueOf(gate.idnum)+".animation_effect", gate.animation_effect);
		gateFD.set(String.valueOf(gate.idnum)+".sound_effect", gate.sound_effect);
		gateFD.set(String.valueOf(gate.idnum)+".speed", gate.speed);
		gateFD.set(String.valueOf(gate.idnum)+".portal", gate.portal);
		gateFD.set(String.valueOf(gate.idnum)+".owner", gate.owner);
		gateFD.set(String.valueOf(gate.idnum)+".state", gate.state);
		gateFD.set(String.valueOf(gate.idnum)+".disabled", gate.disabled);

		File gateFile = null;
		gateFile = new File(dataFolder, "gates.yml");
		try {
			
			if (!gateFile.exists()) {
				gateFile.setWritable(true);
				//InputStream defConfigStream = plugin.getResource("gates.yml");
				gateFD = YamlConfiguration.loadConfiguration(gateFile);
			}
			gateFD.save(gateFile);
		}
		catch (Exception e) {
			plugin.getLogger().severe("cant save gate file!");
			return;
		}
	
	}
	
	
	public void removeGate(GateObject gate) {
		gateFD.set(String.valueOf(gate.idnum), null); //remove gate entry
		
		File gateFile = null;
		gateFile = new File(dataFolder, "gates.yml");
		try {
			
			if (!gateFile.exists()) {
				gateFile.setWritable(true);
				//InputStream defConfigStream = plugin.getResource("gates.yml");
				gateFD = YamlConfiguration.loadConfiguration(gateFile);
			}
			gateFD.save(gateFile);
		}
		catch (Exception e) {
			plugin.getLogger().severe("cant save gate file!");
			return;
		}
	
	}
	
	
	public void loadGateFile(GatekeeperPlugin GKP) {
		File gateFile = null;
		gateFile = new File(dataFolder, "gates.yml");
	
		try {
			if (!gateFile.exists()) {
				gateFile.setWritable(true);
				//InputStream defConfigStream = plugin.getResource("gates.yml");
				gateFD = YamlConfiguration.loadConfiguration(gateFile);
			}
			gateFD.load(gateFile);
		}
		catch (InvalidConfigurationException e) {
			plugin.getLogger().severe("Invalid gates.yml file. An entry is missing or formatting is wrong.");
			return;
		} 
		catch(IOException ex) {
			plugin.getLogger().severe("Cannot load gates.yml");
			return;
		}
		
		
		for (String s: gateFD.getValues(false).keySet()) {
			out.println("S: "+s);
			String wrld = gateFD.getString(s+".world");
			Vector loc = new Vector(gateFD.getInt(s+".location.x"), gateFD.getInt(s+".location.y"), 
					gateFD.getInt(s+".location.z"));
			Vector player_facing = new Vector(gateFD.getInt(s+".player_facing.x"), gateFD.getInt(s+".player_facing.y"), 
					gateFD.getInt(s+".player_facing.z"));
			String owner = gateFD.getString(s+".owner");
			out.println("Location: "+loc);
			out.println("Direction: "+player_facing);
			out.println("owner: "+owner);
			GateObject newgate = GateObject.Load(GKP, wrld, loc.subtract(player_facing), player_facing, owner, Integer.parseInt(s));
			
			newgate.SetSize(gateFD.getInt(s+".width"), gateFD.getInt(s+".height"), gateFD.getInt(s+".depth"));
			newgate.SetMaterial(Material.getMaterial(gateFD.getString(s+".material")), gateFD.getInt(s+".mdata"));
			newgate.SetOpenMaterial(Material.getMaterial(gateFD.getString(s+".open_material")), gateFD.getInt(s+".open_mdata"));
			newgate.SetName(gateFD.getString(s+".name"));
			newgate.SetAnimation(gateFD.getString(s+".animation"), gateFD.getInt(s+".speed"));
			newgate.state = (gateFD.getInt(s+".state"));
			newgate.disabled = (gateFD.getBoolean(s+".disabled"));
		}
	}

}
