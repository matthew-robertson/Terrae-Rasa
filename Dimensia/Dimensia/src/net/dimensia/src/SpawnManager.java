package net.dimensia.src;

import java.util.Vector;

public class SpawnManager
{
	public Vector<EntityLivingNPCEnemy> forestNightEnemies;
	public Vector<EntityLivingNPCEnemy> desertNightEnemies;
	public Vector<EntityLivingNPCEnemy> arcticNightEnemies;
	public Vector<EntityLivingNPCEnemy> forestDayEnemies;
	public Vector<EntityLivingNPCEnemy> desertDayEnemies;
	public Vector<EntityLivingNPCEnemy> arcticDayEnemies;
	
	public SpawnManager()
	{
		if (forestNightEnemies == null){
			forestNightEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		
		if (forestDayEnemies == null){
			forestDayEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		
		if (desertNightEnemies == null){
			desertNightEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		
		if (desertDayEnemies == null){
			desertDayEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		
		if (arcticNightEnemies == null){
			arcticNightEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		
		if (arcticDayEnemies == null){
			arcticDayEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		
		setSpawnVector();		
	}
	
	/**
	 * Get the forestNightEnemies Vector as an array
	 * @return forestNightEnemies Vector as an array
	 */
	public EntityLivingNPCEnemy[] getForestNightEnemiesAsArray()
	{
		EntityLivingNPCEnemy[] enemies = new EntityLivingNPCEnemy[forestNightEnemies.size()];
		forestNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the desertNightEnemies Vector as an array
	 * @return desertNightEnemies Vector as an array
	 */
	public EntityLivingNPCEnemy[] getDesertNightEnemiesAsArray()
	{
		EntityLivingNPCEnemy[] enemies = new EntityLivingNPCEnemy[desertNightEnemies.size()];
		desertNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the arcticNightEnemies Vector as an array
	 * @return arcticNightEnemies Vector as an array
	 */
	public EntityLivingNPCEnemy[] getArcticNightEnemiesAsArray()
	{
		EntityLivingNPCEnemy[] enemies = new EntityLivingNPCEnemy[arcticNightEnemies.size()];
		arcticNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the forestDayEnemies Vector as an array
	 * @return forestDayEnemies Vector as an array
	 */
	public EntityLivingNPCEnemy[] getForestDayEnemiesAsArray()
	{
		EntityLivingNPCEnemy[] enemies = new EntityLivingNPCEnemy[forestDayEnemies.size()];
		forestDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the desertDayEnemies Vector as an array
	 * @return desertDayEnemies Vector as an array
	 */
	public EntityLivingNPCEnemy[] getDesertDayEnemiesAsArray()
	{
		EntityLivingNPCEnemy[] enemies = new EntityLivingNPCEnemy[desertDayEnemies.size()];
		desertDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the arcticDayEnemies Vector as an array
	 * @return arcticDayEnemies Vector as an array
	 */
	public EntityLivingNPCEnemy[] getArcticDayEnemiesAsArray()
	{
		EntityLivingNPCEnemy[] enemies = new EntityLivingNPCEnemy[arcticDayEnemies.size()];
		arcticDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * add an enemy to the forestNightEnemies vector
	 * @param enemy to add.
	 */
	public void addForestNightEnemy(EntityLivingNPCEnemy enemy)
	{
		if(forestNightEnemies == null)
		{
			forestNightEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		forestNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the desertNightEnemies vector
	 * @param enemy to add.
	 */
	public void addDesertNightEnemy(EntityLivingNPCEnemy enemy)
	{
		if(desertNightEnemies == null)
		{
			desertNightEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		desertNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the arcticNightEnemies vector
	 * @param enemy to add.
	 */
	public void addArcticNightEnemy(EntityLivingNPCEnemy enemy)
	{
		if(arcticNightEnemies == null)
		{
			arcticNightEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		arcticNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the forestDayEnemies vector
	 * @param enemy to add.
	 */
	public void addForestDayEnemy(EntityLivingNPCEnemy enemy)
	{
		if(forestDayEnemies == null)
		{
			forestDayEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		forestDayEnemies.add(enemy);
	}

	/**
	 * add an enemy to the desertDayEnemies vector
	 * @param enemy to add.
	 */
	public void addDesertDayEnemy(EntityLivingNPCEnemy enemy)
	{
		if(desertDayEnemies == null)
		{
			desertDayEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		desertDayEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the desertDayEnemies vector
	 * @param enemy to add.
	 */
	public void addArcticDayEnemy(EntityLivingNPCEnemy enemy)
	{
		if(arcticDayEnemies == null)
		{
			arcticDayEnemies = new Vector<EntityLivingNPCEnemy>();
		}
		arcticDayEnemies.add(enemy);
	}
	
	/**
	 * @depreciated [Forced compiler warning]-> replace string == string2 with string.equals(string2)
	 * 
	 * Sets possible spawn locations (biomes/times) for the EntityLivingNPCEnemy 
	 * instance the method is called on.
	 * @param manager
	 * @param t = chosen biome/time to allow spawning
	 * @return
	 */
	private void setSpawnVector()
	{
		for(int i = 0; i < EntityLivingNPCEnemy.enemyList.length; i++)
		{
			if(EntityLivingNPCEnemy.enemyList[i] != null)
			{
				EntityLivingNPCEnemy enemy = new EntityLivingNPCEnemy(EntityLivingNPCEnemy.enemyList[i]);
				String type = enemy.type;
				String time = enemy.time;
				
				//Arctic spawns
				if (type.equals("arctic") && time.equals("day")){
					addArcticDayEnemy(enemy);
				}
				
				if (type.equals("arctic") && time.equals("night")){
					addArcticNightEnemy(enemy);
				}
				
				if (type.equals("arctic") && time.equals(" ")){
					addArcticDayEnemy(enemy);
					addArcticNightEnemy(enemy);
				}
				
				//Desert spawns
				if (type.equals("desert") && time.equals("day")){
					addDesertDayEnemy(enemy);
				}
				
				if (type.equals("desert") && time.equals("night")){
					addDesertNightEnemy(enemy);
				}
				
				if (type.equals("desert") && time.equals(" ")){
					addDesertDayEnemy(enemy);
					addDesertNightEnemy(enemy);
				}
				
				//Forest spawns
				if (type.equals("forest") && time.equals("day")){
					addForestDayEnemy(enemy);
				}	
				
				if (type.equals("forest") && time.equals("night")){
					addForestNightEnemy(enemy);
				}		
				
				if (type.equals("forest") && time.equals(" ")){
					addForestDayEnemy(enemy);
					addForestNightEnemy(enemy);
				}
				
				//All spawns
				if (type.equals(" ") && time.equals("day")){
					addArcticDayEnemy(enemy);
					addDesertDayEnemy(enemy);
					addForestDayEnemy(enemy);			
				}
				
				if (type.equals(" ") && time.equals("night")){
					addArcticNightEnemy(enemy);
					addDesertNightEnemy(enemy);
					addForestNightEnemy(enemy);			
				}	
				
				if (type.equals(" ") && time.equals(" ")){
					addArcticDayEnemy(enemy);
					addDesertDayEnemy(enemy);
					addForestDayEnemy(enemy);			
					addArcticNightEnemy(enemy);
					addDesertNightEnemy(enemy);
					addForestNightEnemy(enemy);
				}	
			}
		}
	}
}