package team097;

import battlecode.common.*;

import java.util.*;

public class RobotPlayer
{
	public static RobotController rc;
	static Random randomThing = new Random();
	static Direction allDirections[] = Direction.values();
	static int robotsProduced;
	static double[][] cowsOnMap;
	static int noisetowerDirTracker = 0;
	static int noisetowerFireTracker = 4;
	static int pastrsProduced; //if we find this to be much higher than our number of current pastrs, we know our opponent is aggresively targeting our pastrs. Any surving pastrs should be defended, and we should probably consider not building more.
	
	
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
						runPastrBuilder(new MapLocation(rc.getMapWidth()-1, (rc.getMapHeight()/2)));
					}
					else if (rc.getRobot().getID() >= 120 && rc.getRobot().getID() < 210)
					{
						runTowerBuilder(new MapLocation(rc.getMapWidth()-1, (rc.getMapHeight()/2)));
					}
					else if (rc.getRobot().getID() >= 210 && rc.getRobot().getID() < 320)
					{
						runPastrBuilder(new MapLocation(0, (rc.getMapHeight()/2)));
					}
					else if (rc.getRobot().getID() >= 320 && rc.getRobot().getID() < 500)
					{
						runTowerBuilder(new MapLocation(0, (rc.getMapHeight()/2)));
					}
					else if (rc.getRobot().getID() >= 500 && rc.getRobot().getID() < 650)
					{
						runPastrBuilder(new MapLocation(rc.getMapWidth()/2, (rc.getMapHeight()-1)));
					}
					else if (rc.getRobot().getID() >= 650 && rc.getRobot().getID() < 850)
					{
						runTowerBuilder(new MapLocation(rc.getMapWidth()/2, (rc.getMapHeight()-1)));
					}
					else
					{
						if (rc.sensePastrLocations(rc.getTeam().opponent()).length > 0)
						{
							runSoldier(rc.sensePastrLocations(rc.getTeam().opponent())[0]);
						} 
						else 
						{
							runSoldier(rc.senseEnemyHQLocation());
						} //end else
					}
				}
				
				else if(rc.getType()==RobotType.NOISETOWER)
				{
					runNoisetower();
				}
				else if(rc.getType()==RobotType.HQ)
				{
					runHeadquarters();
				}
				
				
				rc.yield();
			}catch (Exception e){
				e.printStackTrace();
			} //end catch
		} //end infinite loop while
	} //end run()

	private static void runPastrBuilder(MapLocation destination) throws GameActionException 
	{
		//attacking
		attackEnemiesInRange();
		
		cowsOnMap = rc.senseCowGrowth();
		if (cowsOnMap[destination.x][destination.y] >= 1)
		{
			if (rc.getLocation().equals(destination) || rc.getLocation().isAdjacentTo(destination) )
			{
				rc.construct(RobotType.PASTR);
			}
			else
			{
				//movement
				Movement movementInstance = new Movement();
				movementInstance.moveTowardsLocationBuglike(rc, destination);
			}																  
		}
		else
		{
			Movement.moveTowardsLocationBuglike(rc, destination);
		}
	} //end runPastrBuilder()
	
	private static void runTowerBuilder(MapLocation destination) throws GameActionException 
	{
		//attacking
		attackEnemiesInRange();
		
//		
		cowsOnMap = rc.senseCowGrowth();
		if (cowsOnMap[destination.x][destination.y] >= 1)
		{
			if (rc.getLocation().isAdjacentTo(destination) )
			{
				rc.construct(RobotType.NOISETOWER);
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
			Movement.moveTowardsLocationBuglike(rc, new MapLocation(rc.getMapWidth()/2, (rc.getMapHeight()-1)));
//			if (rc.getLocation().isAdjacentTo(new MapLocation(rc.getMapWidth()/2, (rc.getMapHeight()))-1) )
//			{
//				rc.construct(RobotType.PASTR);
//			}
		}
		
		
	} //end runTowerBuilder()
	
	private static void runSoldier(MapLocation destination) throws GameActionException 
	{
		
		//attacking
		attackEnemiesInRange();
		
		//movement

		//Movement.moveRobotRandomly(rc);
		
		//generate a route to the enemy HQ
		Movement movementInstance = new Movement();
		
		movementInstance.moveRobotRandomlyTowardsEnemyPastr(rc);
		
	} //end runSoldier()
	
	private static void runNoisetower() throws GameActionException 
	{
		if (noisetowerDirTracker%4 == 0) //Farm West.
		{
			if (rc.canAttackSquare(rc.getLocation().add(Direction.WEST, noisetowerFireTracker*2)))
			{
				rc.attackSquareLight(rc.getLocation().add(Direction.WEST, noisetowerFireTracker*2));
			}
			noisetowerFireTracker--;
			if (noisetowerFireTracker < 1)
			{
				noisetowerFireTracker = 4;
				noisetowerDirTracker++;
			}
		}
		else if (noisetowerDirTracker%4 == 1) //Farm North.
		{
			if (rc.canAttackSquare(rc.getLocation().add(Direction.NORTH, noisetowerFireTracker*2)))
			{
				rc.attackSquare(rc.getLocation().add(Direction.NORTH, noisetowerFireTracker*2));
			}
			noisetowerFireTracker--;
			if (noisetowerFireTracker < 1)
			{
				noisetowerFireTracker = 4;
				noisetowerDirTracker++;
			}
		}
		else if (noisetowerDirTracker%4 == 2) //Farm East.
		{
			if (rc.canAttackSquare(rc.getLocation().add(Direction.EAST, noisetowerFireTracker*2)))
			{
				rc.attackSquare(rc.getLocation().add(Direction.EAST, noisetowerFireTracker*2));
			}
			noisetowerFireTracker--;
			if (noisetowerFireTracker < 1)
			{
				noisetowerFireTracker = 4;
				noisetowerDirTracker++;
			}
		}
		else //Farm South
		{
			if (rc.canAttackSquare(rc.getLocation().add(Direction.SOUTH, noisetowerFireTracker*2)))
			{
				rc.attackSquare(rc.getLocation().add(Direction.SOUTH, noisetowerFireTracker*2));
			}
			noisetowerFireTracker--;
			if (noisetowerFireTracker < 1)
			{
				noisetowerFireTracker = 4;
				noisetowerDirTracker++;
			}
		}
		
		
	} //end runNoisetower()
	
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
	} //end attackEnemiesInRange()
	
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
	} //end getFirstEmptySquareClockwiseFromTop()
} //end RobotPlayer class
