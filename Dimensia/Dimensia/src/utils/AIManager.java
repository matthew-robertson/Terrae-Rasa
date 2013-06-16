package utils;

import java.util.Random;

import entities.EntityLiving;
import entities.EntityNPC;
import entities.EntityProjectile;

import world.World;

public class AIManager {
	/**
	 * Has the AI speak the given string
	 * @param s - the string to display
	 */
	public static void AISpeech(String s){
		//Flesh this out once displaying text works properly
		//System.out.println(s);
	}
	
	/**
	 * Allows NPCs who are alerted (to a player, other npc, etc...) to either chase, or run away
	 * @param world - current world
	 * @param npc - the actor to be moved
	 * @param target - the actor to be chased or fled from
	 * @param chase - true if the actor should chase, false if the actor should run away
	 */
	public static void AIChaseAndRetreat(World world, EntityNPC npc, EntityLiving target, boolean chase){
		//If the target is to the right of the npc
		if (target.getX() < npc.getX()){
			if (chase){
				if (npc.isWalkingSafe(world, false)){
					AIMoveLeft(world, npc, true);
				}
			}
			else{
				if (npc.isWalkingSafe(world, true)){
					AIMoveRight(world, npc, true);
				}
			}
		}
		//else if the target is to the left
		else if (target.getX() > npc.getX()){
			if (chase){
				if (npc.isWalkingSafe(world, true)){
					AIMoveRight(world, npc, true);
				}
			}
			else{
				if (npc.isWalkingSafe(world, false)){
					AIMoveLeft(world, npc, true);
				}
			}
		}
	}
	
	/**
	 * Allows npcs and enemies not alerted to the player to wander about the map safely
	 * @param world - current world
	 * @param npc - the actor to be moved
	 */
	public static void AIWander(World world, EntityNPC npc){
		//If there are no values set, pick a direction and distance
		Random gen = new Random();
		if (npc.wanderLeft <= 0 && npc.wanderRight <= 0){
			int r = gen.nextInt(10);
			if (r == 1){
				r = gen.nextInt(2);
				if (r == 0){
					npc.wanderLeft = gen.nextInt(20);				
				}
				else{
					npc.wanderRight = gen.nextInt(20);
				}
			}
			
		}
		npc.ticksSinceLastWander++;
		//Broken. Fix this
		//If there is a drop of 2 or more blocks to the direction of movement, and the npc is not in the air
		if (((!npc.isWalkingSafe(world, true) && npc.wanderRight > 0)
			|| (!npc.isWalkingSafe(world, false) && npc.wanderLeft > 0)) 
			&& npc.isOnGround(world)){
			npc.wanderRight = 0;
			npc.wanderLeft = 0;
		}
		
		if (npc.ticksSinceLastWander >= 2){
			boolean up = gen.nextBoolean();
			if (npc.wanderRight > 0){
				if (npc.isWalkingSafe(world, false)){
					AIMoveRight(world, npc, up);
					npc.wanderRight -= npc.getBaseSpeed() * npc.getMovementSpeedModifier();
				}
			}
			else if (npc.wanderLeft > 0){
				if (npc.isWalkingSafe(world, false)){
					AIMoveLeft(world, npc, up);
					npc.wanderLeft -= npc.getBaseSpeed() * npc.getMovementSpeedModifier();
				}
			}
			npc.ticksSinceLastWander = 0;
		}
	}
	
	/**
	 * Basic AI movement to the left, checks if it should jump, then moves.
	 * @param world - current world
	 * @param npc - the actor to move
	 * @param up - whether or not they want to move up
	 */
	public static void AIMoveLeft(World world, EntityNPC npc, boolean up){
		if (npc.isJumpPossibleAndNeeded(world, false, up)){
			npc.tryToJumpAgain();
		}
		npc.moveEntityLeft(world);
	}
	
	/**
	 * Basic AI movement to the right, checks if it should jump, then moves.
	 * @param world - current world
	 * @param npc - the actor to move
	 * @param up - whether or not they want to move up
	 */
	public static void AIMoveRight(World world, EntityNPC npc, boolean up){
		if (npc.isJumpPossibleAndNeeded(world, true, up)){
			npc.tryToJumpAgain();
		}
		npc.moveEntityRight(world);
	}
	
	/**
	 * Launches a projectile at a provided angle
	 * @param world - current world
	 * @param npc - actor launching the projectile
	 * @param angle - angle to fire at
	 * @param projectile - projectile to launch
	 */
	public static void AIProjectile(World world, EntityNPC npc, int angle, EntityProjectile projectile){
		world.launchProjectile(world, angle, projectile, npc.x, npc.y);
		npc.ticksSinceLastProjectile = 0;
	}
	
	/**
	 * launches a projectile to hit a specific target point. Improve to account for gravity later.
	 * @param npc - actor to launch the projectile
	 * @param x - x location of the target
	 * @param y - y location of the target
	 * @param projectile - projectile to launch
	 */
	public static void AIProjectile(World world, EntityNPC npc, int x, int y, EntityProjectile projectile){
		int angle = MathHelper.angleMousePlayer(x, y, npc.x, npc.y) - 90;
		if (angle < 0){
			angle += 360;
		}
		world.launchProjectile(world, angle, projectile, npc.x, npc.y);
		npc.ticksSinceLastProjectile = 0;
	}		
}
