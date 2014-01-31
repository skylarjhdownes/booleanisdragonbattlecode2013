package team097;

import java.util.Random;

import battlecode.common.*;

public class Movement {
	
	static Random randomThing = new Random();
	static Direction allDirections[] = Direction.values();
	enum movementState {
		noObstacle, foundObstacle, foundDeadEnd
	}
	static movementState dirFlag = movementState.noObstacle;
	static Direction currentDir;
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
			
			if (dirFlag == movementState.noObstacle)
			{
				if(rc.canMove(chosenDir)) 
				{
					rc.move(chosenDir);
					dirFlag = movementState.noObstacle;
				}
				if(rc.canMove(chosenDir.rotateLeft()))
				{
					rc.move(chosenDir.rotateLeft());
				}
				else if(rc.canMove(chosenDir.rotateLeft().rotateLeft()))
				{
					rc.move(chosenDir.rotateLeft().rotateLeft());
				}
				else
				{
					dirFlag = movementState.foundObstacle;
					currentDir = chosenDir;
				}
			}
			
			
			if (dirFlag == movementState.foundObstacle)
			{
				if(rc.canMove(currentDir)) 
				{
					rc.move(currentDir);
					dirFlag = movementState.noObstacle;
				}
				if(rc.canMove(currentDir.rotateRight()))
				{
					dirFlag = movementState.noObstacle;
					rc.move(currentDir.rotateRight());
				}
				else if(rc.canMove(currentDir.rotateRight().rotateRight()))
				{
					rc.move(currentDir.rotateRight().rotateRight());
				}
				else
				{
					dirFlag = movementState.foundDeadEnd;
				}
			}
			
			if (dirFlag == movementState.foundDeadEnd)
			{
				if(rc.canMove(currentDir.rotateLeft()))
				{
					rc.move(currentDir.rotateLeft());
				}
				else if(rc.canMove(currentDir.rotateLeft().rotateLeft()))
				{
					rc.move(currentDir.rotateLeft().rotateLeft());
				}
				else if(rc.canMove(currentDir.rotateLeft().rotateLeft().rotateLeft()))
				{
					rc.move(currentDir.rotateLeft().rotateLeft().rotateLeft());
				}
				else if(rc.canMove(currentDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft()))
				{
					rc.move(currentDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft());
				}
				else if(rc.canMove(currentDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft()))
				{
					rc.move(currentDir.rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft());
				}
			}
			
			
			//I bet there is actually a somewhat reasonable way to do this with an FSA.  This is not it though.
			//I'm applying the wrong tool here, but I could use some more practice building these anyway.
			//This gets confused easily.  I really want to make it work now though...
		} 
	}
	
	private static void moveTowardsLocationIntelligently(RobotController rc, MapLocation destination)
	{
		//TODO  Implement a pathing algorithm.  Heh.
	}
	
	
}
