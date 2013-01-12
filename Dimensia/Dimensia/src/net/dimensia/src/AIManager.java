package net.dimensia.src;

import java.util.Random;

public class AIManager {
	public static void AISpeech(String s){
		//Flesh this out once displaying text works properly
		System.out.println(s);
	}
	
	public static void AIChaseAndRetreat(World world, EntityLiving npc, boolean chase){
		
	}
	
	/**
	 * a Function to allow npcs and enemies not alerted to the player to wander about the map safely
	 * @param world the current world
	 * @param npc the actor to be moved
	 */
	public static void AIWander(World world, EntityLiving npc){
		//If there are no values set, pick a direction and distance
		Random gen = new Random();
		if (npc.wanderLeft <= 0 && npc.wanderRight <= 0){
			int r = gen.nextInt(10);
			if (r == 1){
				r = gen.nextInt(2);
				if (r == 0){
					npc.wanderLeft = gen.nextInt(10);				
				}
				else{
					npc.wanderRight = gen.nextInt(10);
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
			if ((npc.isJumpPossibleAndNeeded(world, true, up) && npc.wanderRight > 0) 
			 || (npc.isJumpPossibleAndNeeded(world, false, up) && npc.wanderLeft > 0 )){
				npc.hasJumped();
			}
			if (npc.wanderRight > 0){
				npc.moveEntityRight(world);
				npc.wanderRight -= npc.baseSpeed * npc.movementSpeedModifier;
			}
			else if (npc.wanderLeft > 0){			
				npc.moveEntityLeft(world);
				npc.wanderLeft -= npc.baseSpeed * npc.movementSpeedModifier;
			}
			npc.ticksSinceLastWander = 0;
		}
	}
	/**
	 * Launch a projectile at a provided angle
	 * @param world the current world
	 * @param npc the npc launching the projectile
	 * @param angle the angle to fire at
	 * @param projectile the projectile to launch
	 */
	public static void AIProjectile(World world, EntityLiving npc, int angle, EntityProjectile projectile){
		npc.launchProjectile(world, angle, projectile);
		npc.ticksSinceLastProjectile = 0;
	}
	
	/**
	 * launch a projectile to hit a specific target point. Improve to account for gravity later.
	 * @param npc to launch the projectile
	 * @param x location of the target
	 * @param y location of the target
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
