package grokswell.gatekeeper;

import static java.lang.System.out;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Utils {
	
	//THANKS TO sk89q FOR THIS METHOD!
	public Vector getCardinalDirection(Location loc) {
		double rotation = (loc.getYaw() - 90) % 360;
		out.println("Rotation1: "+ rotation);
		if (rotation < 0) {
			rotation += 360.0;
		}
		
		out.println("Rotation: "+ rotation);

	    //    -
		//x  - +
		//    +
		//
		//    z
		
		if (0 <= rotation && rotation <= 45) {
			//return "W";
			out.println("W");
			return new Vector(-1,0,0);
			
		} else if (45 < rotation && rotation < 135) {
			//return "N";
			out.println("N");
			return new Vector(0,0,-1);
			
		} else if (135 <= rotation && rotation <= 225) {
			//return "E";
			out.println("E");
			return new Vector(1,0,0);
			
		} else if (225 < rotation && rotation < 315) {
			//return "S";
			out.println("S");
			return new Vector(0,0,1);
			
		} else if (315 <= rotation && rotation <= 360.0) {
			//return "W";
			out.println("W");
			return new Vector(-1,0,0);
			
		} else {
			out.println("None direction");
			return null;
		}
	}
	
	
	public Vector getPlayerRight(Vector facing) {
		//facing E
		if (facing.getX() == 1){
			return new Vector(0,0,1);
		}
		//facing W
		if (facing.getX() == -1){
			return new Vector(0,0,-1);
		}
		//facing S
		if (facing.getZ() == 1){
			return new Vector(-1,0,0);
		}
		//facing N
		if (facing.getZ() == -1){
			return new Vector(1,0,0);
		}
		else {
			return null;
		}
	}
    //    -
	//x  - +
	//    +
	//
	//    z
	
	public Vector reorient(Vector facing, Vector loc) {
		Vector new_vec;
		new_vec=null;
		//facing E
		if (facing.getX() == 1){
			new_vec=new Vector(loc.getZ(),loc.getY(),loc.getX());
		}
		//facing W
		if (facing.getX() == -1){
			new_vec=new Vector(loc.getZ()*-1,loc.getY(),loc.getX()*-1);
		}
		//facing S
		if (facing.getZ() == 1){
			new_vec=new Vector(loc.getX()*-1, loc.getY(), loc.getZ());
		}
		//facing N
		if (facing.getZ() == -1){
			new_vec=new Vector(loc.getX(), loc.getY(), loc.getZ()*-1);
		}
		out.println("NEW_VEC: "+new_vec);
		return new_vec;
	}
}