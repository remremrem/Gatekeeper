package grokswell.gatekeeper;

import static java.lang.System.out;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Animations {
	
	private void setMatArray(ArrayList<Location> locArray, Material mat) {
		out.println("setMatArray");
		out.println("material: "+mat.name());
		for (Location loc : locArray) {
			out.println(loc);
			loc.getBlock().setType(mat);
		}
	}

    //    -
	//x  - +
	//    +
	//
	//    z
	
	public Vector reorient(Vector facing, Vector hwd) {
		out.println("reorient");
		Vector new_vec;
		//facing E
		if (facing.getX() == 1){
			new_vec=new Vector(hwd.getZ(),hwd.getY(),hwd.getX());
			out.println("NEW_VEC E: "+new_vec);
			return new_vec;
		}
		//facing W
		if (facing.getX() == -1){
			new_vec=new Vector(hwd.getZ()*-1,hwd.getY(),hwd.getX()*-1);
			out.println("NEW_VEC W: "+new_vec);
			return new_vec;
			
		}

		//facing S
		if (facing.getZ() == 1){
			new_vec = new Vector(hwd.getX()*-1,hwd.getY(), hwd.getZ());
			out.println("NEW_VEC S: "+new_vec);
			return new_vec;
		}
		//facing N
		if (facing.getZ() == -1){
			new_vec = new Vector(hwd.getX(),hwd.getY(), hwd.getZ()*-1);
			out.println("NEW_VEC N: "+new_vec);
			return new_vec;
		} else {
			return null;
		}
	}
	//by default animation is (w,h,d) i.e. player_facing north, 
	//with positive depth extending behind player to south N=-d,S=d,E=w,W=-w
	
	
	class changeBlocks extends BukkitRunnable {
		ArrayList<Location> a;
    	Material m;
        public changeBlocks(ArrayList<Location> al, Material mat) {
    		out.println("changeBlocks");
        	a=al;
        	m=mat;
        }
        public void run() {
    		out.println("run changeblocks");
        	setMatArray(a,m);
        }
    }

	
	//top to bottom
	public void t2b(GateObject gate, Material mat, int mdata) {
		out.println("top 2 bottom");
		ArrayList<ArrayList<Location>> rows = new ArrayList<ArrayList<Location>>();
		Location origin = gate.origin.toLocation(Bukkit.getWorld(gate.world));
		
		int rowcount = gate.height;
		out.println("origin: "+origin);
		while (rowcount > 0) {
			rowcount = rowcount-1;
			ArrayList<Location> row = new ArrayList<Location>();
			int colcount = gate.width;
			while (colcount > 0) {
				colcount = colcount-1;
				int dcolcount = gate.depth;
				while (dcolcount > 0) {
					dcolcount = dcolcount-1;
					Vector vec = reorient(gate.player_facing, new Vector(colcount,rowcount,dcolcount));
					out.println("add vec: "+vec);
					row.add(origin.clone().add(vec));
					out.println("new row item: "+origin);
				}
			}
			rows.add(row);
		}
		int count = 1;
		for (ArrayList<Location> al : rows) {
			new changeBlocks(al,mat).runTaskLater(gate.plugin, gate.speed*count);
			count=count+1;
		}
	}
	
	//bottom to top
	public void b2t(GateObject gate, Material mat, int mdata) {
		out.println("bottom 2 top");
		ArrayList<ArrayList<Location>> rows = new ArrayList<ArrayList<Location>>();
		Location origin = gate.origin.toLocation(Bukkit.getWorld(gate.world));
		
		int rowcount = 0;
		while (rowcount < gate.height) {
			ArrayList<Location> row = new ArrayList<Location>();
			int colcount = gate.width;
			while (colcount > 0) {
				colcount = colcount-1;
				int dcolcount = gate.depth;
				while (dcolcount > 0) {
					dcolcount = dcolcount-1;
					Vector vec = reorient(gate.player_facing, new Vector(colcount,rowcount,dcolcount));
					row.add(origin.clone().add(vec));
				}
			}
			rows.add(row);
			rowcount = rowcount+1;
		}
		int count = 1;
		for (ArrayList<Location> al : rows) {
			new changeBlocks(al,mat).runTaskLater(gate.plugin, gate.speed*count);
			count=count+1;
		}
	}
	
	//right to left
	public void r2l(GateObject gate, Material mat, int mdata) {
		out.println("right 2 left");
		ArrayList<ArrayList<Location>> cols = new ArrayList<ArrayList<Location>>();
		Location origin = gate.origin.toLocation(Bukkit.getWorld(gate.world));
		
		int colcount = gate.width;
		while (colcount > 0) {
			colcount = colcount-1;
			ArrayList<Location> col = new ArrayList<Location>();
			int dcolcount = gate.depth;
			while (dcolcount > 0) {
				dcolcount = dcolcount-1;
				int rowcount = 0;
				while (rowcount > 0) {
					rowcount = rowcount-1;
					Vector vec = reorient(gate.player_facing, new Vector(colcount,rowcount,dcolcount));
					col.add(origin.clone().add(vec));
					
				}
			}
			cols.add(col);
		}
		int count = 1;
		for (ArrayList<Location> al : cols) {
			new changeBlocks(al,mat).runTaskLater(gate.plugin, gate.speed*count);
			count=count+1;
		}
	}
	
	//left to right
	public void l2r(GateObject gate, Material mat, int mdata) {
		out.println("left 2 right");
		ArrayList<ArrayList<Location>> cols = new ArrayList<ArrayList<Location>>();
		Location origin = gate.origin.toLocation(Bukkit.getWorld(gate.world));
		
		int colcount = 0;
		while (colcount < gate.width) {
			ArrayList<Location> col = new ArrayList<Location>();
			int dcolcount = gate.depth;
			while (dcolcount > 0) {
				dcolcount = dcolcount-1;
				int rowcount = 0;
				while (rowcount > 0) {
					rowcount = rowcount-1;
					Vector vec = reorient(gate.player_facing, new Vector(colcount,rowcount,dcolcount));
					col.add(origin.clone().add(vec));
					
				}
			}
			cols.add(col);
			colcount = colcount+1;
		}
		int count = 1;
		for (ArrayList<Location> al : cols) {
			new changeBlocks(al,mat).runTaskLater(gate.plugin, gate.speed*count);
			count=count+1;
		}
	}
}
