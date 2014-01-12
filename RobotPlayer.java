package potatos;

import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.*;
import java.util.*;

public class RobotPlayer{
	
	public static RobotController rc;
	static Random randomThing = new Random();
	
	public static void run(RobotController rcin){
		rc = rcin;
		randomThing.setSeed(rc.getRobot().getID());
		while(true){
			try{
				if(rc.getType()==RobotType.HQ){//if I'm a headquarters
					runHeadquarters();
				}else if(rc.getType()==RobotType.SOLDIER){
					runSoldier();
				}
				rc.yield();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private static void runSoldier() throws GameActionException {
		
	}
	
	private static void runHeadquarters() throws GameActionException {
		
	}
}