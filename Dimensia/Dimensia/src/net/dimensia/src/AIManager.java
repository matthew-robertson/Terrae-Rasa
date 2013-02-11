package net.dimensia.src;

import java.util.Random;

/**
 * AIManager serves to provide all EntityLivingNPC's with the methods needed to traverse the game world.
 * As such, it interfaces with EntityLivingNPC to control movement.
 * <br><br>
 * 
 * The general methods {@link #AIMoveLeft(world, EntityLicingNPC, boolean)} and {@link #AIMoveRight(world, EntityLicingNPC, boolean)}
 * serve as the basis for this movement, ensuring the actor moves safely.
 * <br><br>
 * 
 * Advanced AI movement functions are performed by {@link #AIChaseAndRetreat(World, EntityLiving, EntityLiving, boolean)} and {@link #AIWander(World, EntityLiving)},
 *  which handle the seeking and wandering of actors, respectively.
 * <br><br>
 * 
 * Assorted advanced AI functions are also handled here, namely speech, and the launching of projectiles.
 * {@link #AISpeech(String)}, {@link #AIProjectile(World, EntityLiving, int, int, EntityProjectile)}, and {@link #AIProjectile(World, EntityLiving, int, EntityProjectile)}
 * perform these tasks.
 * 
 * @author Alec Sobeck
 * @author Matt Robertson
 * @version 1.0
 * @since 1.0
 */
public class AIManager {
	/**
	 * Has the AI speak the given string
	 * @param s - the string to display
	 */
	public static void AISpeech(String s){
		//Flesh this out once displaying text works properly
		System.out.println(s);
	}
	
	/**
	 * Allows NPCs who are alerted (to a player, other npc, etc...) to either chase, or run away
	 * @param world - current world
	 * @param npc - the actor to be moved
	 * @param target - the actor to be chased or fled from
	 * @param chase - true if the actor should chase, false if the actor should run away
	 */
	public static void AIChaseAndRetreat(World world, EntityLiving npc, EntityLiving target, boolean chase){
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
	public static void AIWander(World world, EntityLiving npc){
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
					npc.wanderRight -= npc.baseSpeed * npc.movementSpeedModifier;
				}
			}
			else if (npc.wanderLeft > 0){
				if (npc.isWalkingSafe(world, false)){
					AIMoveLeft(world, npc, up);
					npc.wanderLeft -= npc.baseSpeed * npc.movementSpeedModifier;
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
	public static void AIMoveLeft(World world, EntityLiving npc, boolean up){
		if (npc.isJumpPossibleAndNeeded(world, false, up)){
			npc.hasJumped();
		}
		npc.moveEntityLeft(world);
	}
	
	/**
	 * Basic AI movement to the right, checks if it should jump, then moves.
	 * @param world - current world
	 * @param npc - the actor to move
	 * @param up - whether or not they want to move up
	 */
	public static void AIMoveRight(World world, EntityLiving npc, boolean up){
		if (npc.isJumpPossibleAndNeeded(world, true, up)){
			npc.hasJumped();
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
	public static void AIProjectile(World world, EntityLiving npc, int angle, EntityProjectile projectile){
		npc.launchProjectile(world, angle, projectile);
		npc.ticksSinceLastProjectile = 0;
	}
	
	/**
	 * launches a projectile to hit a specific target point. Improve to account for gravity later.
	 * @param npc - actor to launch the projectile
	 * @param x - x location of the target
	 * @param y - y location of the target
	 * @param projectile - projectile to launch
	 */
	public static void AIProjectile(World world, EntityLiving npc, int x, int y, EntityProjectile projectile){
		int angle = MathHelper.angleMousePlayer(x, y, npc.x, npc.y) - 90;
		if (angle < 0){
			angle += 360;
		}
		npc.launchProjectile(world, angle, projectile);
		npc.ticksSinceLastProjectile = 0;
	}		
}
