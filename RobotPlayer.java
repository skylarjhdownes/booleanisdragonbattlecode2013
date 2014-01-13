package potatos;

import battlecode.common.*;
import java.util.*;

public class RobotPlayer
{
	public static RobotController rc;
	static Random randomThing = new Random();
	
	public static void run(RobotController rcin)
	{
		rc = rcin;
		randomThing.setSeed(rc.getRobot().getID());
		while(true){
			try{
				if(rc.getType()==RobotType.HQ){
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

	private static void runSoldier() throws GameActionException 
	{
		
	}
	
	private static void runHeadquarters() throws GameActionException 
	{
		Direction spawnDir = getFirstEmptySquareClockwiseFromTop();
		System.out.println(spawnDir);
		if(rc.isActive()&&rc.senseRobotCount()<GameConstants.MAX_ROBOTS&&spawnDir!=Direction.NONE){
			rc.spawn(spawnDir);
		}
		
	}
	
	private static Direction getFirstEmptySquareClockwiseFromTop() throws GameActionException 
	{
		Direction returnDir = Direction.NORTH_WEST;
		int dirChangeCount = 0;
		while (!rc.canMove(returnDir) && dirChangeCount < 8)
		{
			System.out.println(dirChangeCount);
			returnDir = returnDir.rotateRight();
			dirChangeCount++;
		}
		if(dirChangeCount>=8)
		{
			System.out.println("Potato");
			return Direction.NONE;
		}
		else
		{
			return returnDir;
		}
	}
}