package team097;

import java.util.Random;

import battlecode.common.*;

public class Movement {
	
	static Random randomThing = new Random();
	static Direction allDirections[] = Direction.values();

	public static void moveRobotRandomly(RobotController rc) throws GameActionException 
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
	
	//Currently craptastic
	public static void moveTowardsLocationBuglike(RobotController rc, MapLocation destination) throws GameActionException 
	{
		Direction chosenDir = rc.getLocation().directionTo(destination);
		if(rc.isActive())
		{
			if(rc.canMove(chosenDir)) 
			{
				rc.move(chosenDir);
			}
			else if(rc.canMove(chosenDir.rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft());
			}
			else if(rc.canMove(chosenDir.rotateLeft().rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft().rotateLeft());
			}
			else if(rc.canMove(chosenDir.rotateLeft().rotateLeft().rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft().rotateLeft().rotateLeft());
			}
			else if(rc.canMove(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft());
			}
			else if(rc.canMove(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft());
			}
			else if(rc.canMove(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft());
			}
			else if(rc.canMove(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft()))
			{
				rc.move(chosenDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft());
			}
			//Fear my ridiculous triangle of DOOOOOOOOOM!!!
			//Hehehe...  So terrible.
		} 
	}
	
	private static void moveTowardsLocationIntelligently(RobotController rc, MapLocation destination)
	{
		//TODO  Implement a pathing algorithm.  Heh.
	}
	
	
}
