package team097;

import battlecode.common.*;
import java.util.*;

public class RobotPlayer
{
	public static RobotController rc;
	static Random randomThing = new Random();
	static Direction allDirections[] = Direction.values();
	static int robotsProduced;
	static ArrayList soldierList = new ArrayList();
	static MapLocation place;
	public static void run(RobotController rcin)
	{
		rc = rcin;
		randomThing.setSeed(rc.getRobot().getID());
		while(true){
			try{
				if(!soldierList.contains(rc.getRobot().getID()))
				{
					soldierList.add(rc.getRobot().getID());
				}
				if(rc.getType()==RobotType.HQ)
				{
					runHeadquarters();
				}
				else if(rc.getType()==RobotType.SOLDIER){
					if (rc.getRobot().getID() == soldierList.get(0))//This is bad until we make our arraylist handle dead bots
					{
						runBuilder(place = new MapLocation(2,2));
					}
					else
					{
						runSoldier();
					}
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
		
		if (rc.getLocation().equals(destination))
		{
			rc.construct(RobotType.PASTR);
		}
		else
		{
			//movement
			moveTowardsLocationBuglike(destination);
		}
		
	} //end runSoldier()
	
	private static void runSoldier() throws GameActionException 
	{
		//attacking
		attackEnemiesInRange();
		
		//movement
		moveRobotRandomly();
		
	} //end runSoldier()
	
	
	
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
	
	private static void moveRobotRandomly() throws GameActionException 
	{
		int randomMovement = (int)(randomThing.nextDouble()*12);
		if(randomMovement < 9) {
			Direction chosenDir = allDirections[randomMovement];
			if(rc.isActive() && rc.canMove(chosenDir)) {
				rc.move(chosenDir);
			} //end inner if
		} //end outer if
		else {
			Direction towardsEnemyHQ = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
			if(rc.isActive() && rc.canMove(towardsEnemyHQ)) {
				rc.move(towardsEnemyHQ);
			}//end inner if
		} //end else
	}
	
	private static void moveTowardsLocationDirectly(MapLocation destination) throws GameActionException 
	{
		Direction chosenDir = rc.getLocation().directionTo(destination);
		if(rc.isActive() && rc.canMove(chosenDir)) {
			rc.move(chosenDir);
		} 
	}
	
	//Currently craptastic
	private static void moveTowardsLocationBuglike(MapLocation destination) throws GameActionException 
	{
		Direction chosenDir = rc.getLocation().directionTo(destination);
		if(rc.isActive())
		{
			if(rc.canMove(chosenDir)) 
			{
				rc.move(chosenDir);
			}
			else if(rc.canMove(chosenDir.rotateLeft().rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft().rotateLeft());
			}
			else if(rc.canMove(chosenDir.rotateRight().rotateRight()))
			{
				rc.move(chosenDir.rotateRight().rotateRight());
			}
			
		} 
	}
	
	private static void moveTowardsLocationIntelligently(MapLocation destination)
	{
		//TODO  Implement a pathing algorithm
	}
	
	private static void runHeadquarters() throws GameActionException 
	{
		Direction spawnDir = getFirstEmptySquareClockwiseFromTop();
		
		attackEnemiesInRange();
		
		if(rc.isActive()&&rc.senseRobotCount()<GameConstants.MAX_ROBOTS&&spawnDir!=Direction.NONE){
			rc.spawn(spawnDir);
			robotsProduced++;
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
