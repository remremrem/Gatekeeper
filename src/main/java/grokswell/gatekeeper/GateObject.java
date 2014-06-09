package grokswell.gatekeeper;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GateObject {
	public int idnum;
	public String gate_name;
	public int width;
	public int height;
	public int depth;
	public Material material;
	public Material open_material;
	public int mdata;
	public int open_mdata;
	public String world;
	public Vector location;
	public Vector player_facing;
	public Vector player_right;
	public String animation;
	public String animation_effect;
	public String sound_effect;
	public int speed;
	public String portal;
	public String owner;
	public ArrayList<String> portal_effects;
	public Vector origin;
	public Vector limit;
	private Utils util;
	public GatekeeperPlugin plugin;
	public int state; //0 = open, 1 = closed, 2 = busy
	public boolean disabled;

	GateObject(String ownr, GatekeeperPlugin plgn, int idn, String wrld, Vector loc, Vector dir) {
		idnum = idn;
		gate_name = "";
		world = wrld;
		location = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		state = 0;
		disabled = false;
		plugin = plgn;
		util = new Utils();
		player_facing = dir;
		player_right = util.getPlayerRight(player_facing);
		width = 3;
		height = 3;
		depth = 1;
		mdata = 0;
		open_mdata = 0;
		material = Material.IRON_FENCE;
		open_material = Material.AIR;
		speed = 5;
		animation = "bottom2top";
		portal = null;
		owner = ownr;
		CalcLimits();
//		animation_values: right2left,left2right,back2front,front2back,
//		center2width,center2height,center2depth,center2corners,bottom2top,top2bottom,random
	}
	
	private void CalcLimits(){
		out.println("CalcLimits");
		out.println("origin a: "+origin);
		origin = location.clone();
		limit=origin.clone();
		limit.add(player_right.clone().multiply(width));
		limit.add(new Vector(0,height,0));
		limit.add(player_facing.clone().multiply(depth));
		//limit= new Vector(limit.getBlockX(), limit.getBlockY(), limit.getBlockZ());
		out.println("limit: "+limit);
		out.println("origin b: "+origin);
	}

	public void SetSize(int w, int h, int d){
		if (!(w == 0)){
			width = w;
		}
		if (!(h == 0)){
			height = h;
		}
		if (!(d == 0)){
			depth = d;
		}
		CalcLimits();
	}
	
	public void SetWidth(int w){
		if (!(w == 0)){
			width = w;
		}
		CalcLimits();
	}
	
	public void SetHeight(int h){
		if (!(h == 0)){
			height = h;
		}
		CalcLimits();
	}
	
	public void SetDepth(int d){
		if (!(d == 0)){
			depth = d;
		}
		CalcLimits();
	}

	public void SetPosition(Player player){
		player_facing = util.getCardinalDirection(player.getLocation());
		player_right = util.getPlayerRight(player_facing);
		location = player.getLocation().getBlock().getLocation().toVector().add(player_facing);
		CalcLimits();
	}

	public void SetMaterial(Material m,int d){
		material = m;
		mdata = d;
	}

	public void SetOpenMaterial(Material m,int d){
		open_material = m;
		open_mdata = d;
	}

	public void SetName(String gatename){
		gate_name = gatename;
	}

	public void SetAnimation(String a, int s){
		animation = a;
		if (!(s == 11)){
			speed=s;
		}
	}

	public void SetPortal(String p){
		if (!(p.equals(""))){
			portal=p;
		}
	}

	public void ToggleDisabled(){
		disabled = !disabled;
		
	}

	public void close(){
		out.println("close gate");
		if (this.state == 1) {
			//do nothing if gate is already closed.
			return;
		}
		this.state = 1;
		Animations a = new Animations();
		if (this.animation.equals("top2bottom")) {
			a.b2t(this, this.material, this.mdata);
			return;
		}
		if (this.animation.equals("bottom2top")) {
			a.t2b(this, this.material, this.mdata);
			return;
		}
		if (this.animation.equals("left2right")) {
			a.r2l(this, this.material, this.mdata);
			return;
		}
		if (this.animation.equals("right2left")) {
			a.l2r(this, this.material, this.mdata);
			return;
		}
	}

	public void open(){
		out.println("open gate");
		if (this.state == 0) {
			//do nothing if gate is already open.
			return;
		}
		this.state = 0;
		Animations a = new Animations();
		if (this.animation.equals("top2bottom")) {
			a.t2b(this, this.open_material, this.open_mdata);
			return;
		}
		if (this.animation.equals("bottom2top")) {
			a.b2t(this, this.open_material, this.open_mdata);
			return;
		}
		if (this.animation.equals("left2right")) {
			a.l2r(this, this.open_material, this.open_mdata);
			return;
		}
		if (this.animation.equals("right2left")) {
			a.r2l(this, this.open_material, this.open_mdata);
			return;
		}
	}
	
	public void toggle() {
		if (this.state == 0) {
			this.close();
		} else {
			this.open();
		}
	}

	
	static void Remove(GatekeeperPlugin GKP, int idnum) {
		if (GKP.gate_idnums_sorted.contains(idnum)){ 		
			GKP.gateFile.removeGate(GKP.gates_by_idnum.get(idnum));	
			removeGateFromLists(GKP, idnum);
			//sender.sendMessage("Permanently removed gate "+idnum+".");
			return;
		} else {
			//sender.sendMessage("Gate id '"+idnum+"' does not exist");
			return;
		}
	}

	
	static GateObject Load(GatekeeperPlugin GKP, String wrld, Vector loc, Vector direction, String owner, int gateid) {
		out.println("direction: "+direction);
		GateObject newgate = new GateObject(owner, GKP, gateid, wrld, loc.add(direction), direction);
		out.println("GKP: "+GKP);
		out.println("NEWGATE: "+newgate);
		addGateToLists(GKP, newgate);
		return newgate;
		
	}

	
	static GateObject Create(GatekeeperPlugin GKP, Location loc, Vector direction, String owner) {
		out.println("direction: "+direction);
		GateObject newgate = new GateObject(owner, GKP, getNewGateID(GKP), loc.getWorld().getName(), loc.add(direction).toVector(), direction);
		out.println("GKP: "+GKP);
		out.println("NEWGATE: "+newgate);
		addGateToLists(GKP, newgate);
		GKP.gateFile.saveGate(newgate);
		return newgate;
		
	}
	
	
	static Integer getNewGateID(GatekeeperPlugin GKP){
		//Check if there are any unused idnums for gates
		
		int size = GKP.gate_idnums_sorted.size();
		
		//if the largest gate idnum is greater than the length of the idnum list, 
		//then there is an unused idnum somewhere
		if (GKP.gate_idnums_sorted.size() == 0) {
			return 1;
		}
		if ( size < GKP.gate_idnums_sorted.get(size-1)) {
			int index = 1;
			for (Integer i : GKP.gate_idnums_sorted) {
				if (i > index) {			
					return i-1; //return the first missing idnum that is found
				}
				index++;
			}
		}
		
		return size+1;
	}
	
	
	static void addGateToLists(GatekeeperPlugin GKP, GateObject newgate){
		//Check if there are any unused idnums for gates
		
		GKP.gate_idnums_sorted.add(newgate.idnum);
		Collections.sort(GKP.gate_idnums_sorted);
		GKP.gates_by_idnum.put(newgate.idnum, newgate);
		
		if (GKP.gates_by_name.containsKey(newgate.gate_name)) {
			GKP.gates_by_name.get(newgate.gate_name).add(newgate);
		} else {
			ArrayList<GateObject> a = new ArrayList<GateObject>();
			a.add(newgate);
			GKP.gates_by_name.put(newgate.gate_name, a);
		}
	}
	
	
	static void removeGateFromLists(GatekeeperPlugin GKP, Integer oldgateid){
		GateObject oldgate = GKP.gates_by_idnum.get(oldgateid);
		GKP.gate_idnums_sorted.remove(oldgate.idnum-1);
		GKP.gates_by_idnum.remove(oldgate.idnum);
		
		if (GKP.gates_by_name.get(oldgate.gate_name).size() == 1) {
			GKP.gates_by_name.remove(oldgate.gate_name);
		} else {
			GKP.gates_by_name.get(oldgate.gate_name).remove(oldgate);
		}
	}
}


