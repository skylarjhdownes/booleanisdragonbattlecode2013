package team097;

import battlecode.common.*;
import java.util.*;

public class RobotPlayer
{
	public static RobotController rc;
	static Random randomThing = new Random();
	static Direction allDirections[] = Direction.values();
	static int robotsProduced;
	static MapLocation place;
	static double[][] cowsOnMap;
	static int noisetowerTracker = 0;
	public static void run(RobotController rcin)
	{
		rc = rcin;
		randomThing.setSeed(rc.getRobot().getID());
		while(true){
			try{
				if(rc.getType()==RobotType.SOLDIER)
				{
					if (rc.getRobot().getID() < 120)
					{
						runBuilder(place = new MapLocation(rc.getMapWidth(), (rc.getMapHeight()/2)));
						System.out.println(rc.getMapWidth() + "///" + (rc.getMapHeight()/2));
					}
					else
					{
						runSoldier();
					}
				}
				
//				else if(rc.getType()==RobotType.NOISETOWER)
//				{
//					runNoisetower();
//				}
				else if(rc.getType()==RobotType.HQ)
				{
					runHeadquarters();
				}
				
				
				rc.yield();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private static void runBuilder(MapLocation destination) throws GameActionException 
	{
		//attacking
		attackEnemiesInRange();
		
//		
		cowsOnMap = rc.senseCowGrowth();
		if (cowsOnMap[rc.getMapWidth()-1][(rc.getMapHeight()/2)] >= 1)
		{
			if (rc.getLocation().equals(destination) || rc.getLocation().isAdjacentTo(destination) )
			{
				rc.construct(RobotType.PASTR);
			}
			else
			{
				//movement
				Movement movementInstance = new Movement();
				movementInstance.moveTowardsLocationBuglike(rc, destination); //Should probably set this destination up to be whichever 
			}																  //is farther from enemy HQ, or some other similar thing.
		}
		else
		{
			runSoldier();
		}
		
		
	} //end runSoldier()
	
	private static void runSoldier() throws GameActionException 
	{
		//attacking
		attackEnemiesInRange();
		
		//movement
		Movement.moveRobotRandomlyTowardsEnemyPastr(rc);
		
	} //end runSoldier()
	
//	private static void runNoisetower() throws GameActionException 
//	{
//		if 
//		if (canAttackSquare();)
//		{
//			attackSquare();
//		}
//	} //end runNoisetower()
	
	private static void runHeadquarters() throws GameActionException 
	{
		Direction spawnDir = getFirstEmptySquareClockwiseFromTop();
		
		attackEnemiesInRange();
		
		if(rc.isActive()&&rc.senseRobotCount()<GameConstants.MAX_ROBOTS&&spawnDir!=Direction.NONE){
			rc.spawn(spawnDir);
			robotsProduced++;
		}
	} //end runHeadquarters()
	
	private static void attackEnemiesInRange() throws GameActionException 
	{
		Robot[] enemyRobotsInRange = rc.senseNearbyGameObjects(Robot.class, 10, rc.getTeam().opponent());
		if(enemyRobotsInRange.length > 0) {
			Robot target = enemyRobotsInRange[0];
			RobotInfo targetInfo;
			targetInfo = rc.senseRobotInfo(target);
			if(rc.isActive() && targetInfo.type != RobotType.HQ) {
				rc.attackSquare(targetInfo.location);
			} //end inner if
		} //end outer if
//		else { //no enemies in range, so build a tower
//			Robot[] pastrsWithinRange = rc.senseNearbyGameObjects(Robot.class, 9, null);
//			if(randomThing.nextDouble() < 0.03 && rc.sensePastrLocations(rc.getTeam()).length < 6 && pastrsWithinRange.length < 1) {
//				rc.construct(RobotType.PASTR);
//			}//end if
//		}//end else
	}
	
	
	
	private static void moveTowardsLocationDirectly(MapLocation destination) throws GameActionException 
	{
		Direction chosenDir = rc.getLocation().directionTo(destination);
		if(rc.isActive() && rc.canMove(chosenDir)) {
			rc.move(chosenDir);
		} 
	}
	
	
	// Keep this method checking canMove and not if the square is empty (Void squares)
	private static Direction getFirstEmptySquareClockwiseFromTop() throws GameActionException 
	{
		Direction returnDir = Direction.NORTH_WEST; //Start arbitrarily with NorthWest
		int dirChangeCount = 0;
		while (!rc.canMove(returnDir) && dirChangeCount < 8) //If the square is full, 
		{					     //and we haven't checked all the squares yet,
			returnDir = returnDir.rotateRight(); //check the next square, clockwise,
			dirChangeCount++;                    //and increment the counter
		}
		if(dirChangeCount>=8)
		{
			return Direction.NONE; //If all the squares are full
		}
		else 
		{
			return returnDir; //Returns the first empty square detected
		}
	}
}
