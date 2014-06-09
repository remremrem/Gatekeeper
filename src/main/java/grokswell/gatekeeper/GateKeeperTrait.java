package grokswell.gatekeeper;

//import static java.lang.System.out;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;


public class GateKeeperTrait extends Trait {
	static ArrayList<String> shoplist;
	ArrayList<String> click_cooldowns;
	final GatekeeperPlugin plugin;
	
	public DataKey trait_key;
	
	//MESSAGES
	String greet_neutral;
	String greet_friend;
	String greet_enemy;
	String deny_neutral;
	String deny_friend;
	String deny_enemy;
	String demand_payment;
	String recieve_payment;
	
	int gate_id;
	double cost;


	public GateKeeperTrait() {
		super("hypermerchant");
		plugin = (GatekeeperPlugin) Bukkit.getServer().getPluginManager().getPlugin("Gatekeeper");
		
		click_cooldowns = new ArrayList<String>();

	}

	@Override
	public void load(DataKey key) {
		this.trait_key = key;

		// Override defaults if they exist

		if (key.keyExists("greet_neutral.default"))
			this.greet_neutral = key.getString("greet_neutral.default");
		if (key.keyExists("greet_friend.default"))
			this.greet_friend = key.getString("greet_friend.default");
		if (key.keyExists("greet_enemy.default"))
			this.greet_enemy = key.getString("greet_enemy.default");
		if (key.keyExists("deny_neutral.default"))
			this.deny_neutral = key.getString("deny_neutral.default");
		if (key.keyExists("deny_friend.default"))
			this.deny_friend = key.getString("deny_friend.default");
		if (key.keyExists("deny_enemy.default"))
			this.deny_enemy = key.getString("deny_enemy.default");
		if (key.keyExists("demand_payment.default"))
			this.demand_payment = key.getString("demand_payment.default");
		if (key.keyExists("recieve_payment.default"))
			this.recieve_payment = key.getString("recieve_payment.default");
		if (key.keyExists("gate_id.default"))
			this.gate_id = key.getInt("gate_id.default");
		if (key.keyExists("cost.default"))
			this.cost = key.getDouble("cost.default");
	}
	
  class RemoveClickCooldown extends BukkitRunnable {
  	String playername;
      public RemoveClickCooldown(String plynam) {
      	playername = plynam;
      }
      public void run() {
          // What you want to schedule goes here
          click_cooldowns.remove(playername);
      }
  }
	
  
  
	@EventHandler
	public void onRightClick(net.citizensnpcs.api.event.NPCRightClickEvent event) {
		if(this.npc!=event.getNPC()) return;
		
		Player player = event.getClicker();
		
		//return if player has clicked on a gatekeeper npc in the last second.
		if (click_cooldowns.contains(player.getName())){
			event.setCancelled(true);
			return;
		}
		//add player to 3 second cooldown list to prevent spam clicking gatekeeper.

		click_cooldowns.add(player.getName());
		new RemoveClickCooldown(player.getName()).runTaskLater(this.plugin, 20);

		
	}
	

	@Override
	public void save(DataKey key) {
		key.setString("greet_neutral", this.greet_neutral);
		key.setString("greet_friend.default", this.greet_friend);
		key.setString("greet_enemy.default", this.greet_enemy);
		key.setString("deny_neutral.default", this.deny_neutral);
		key.setString("deny_friend.default", this.deny_friend);
		key.setString("deny_enemy.default", this.deny_enemy);
		key.setString("demand_payment.default", this.demand_payment);
		key.setString("recieve_payment.default", this.recieve_payment);
		key.setInt("gate_id.default", this.gate_id);
		key.setDouble("cost.default", this.cost);
	}
	
	@Override
	public void onAttach() {
	}

}