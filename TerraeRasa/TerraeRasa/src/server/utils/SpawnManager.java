package server.utils;

import java.util.Vector;

import server.entities.EntityNPCEnemy;


public class SpawnManager
{
	public Vector<EntityNPCEnemy> forestNightEnemies;
	public Vector<EntityNPCEnemy> desertNightEnemies;
	public Vector<EntityNPCEnemy> arcticNightEnemies;
	public Vector<EntityNPCEnemy> forestDayEnemies;
	public Vector<EntityNPCEnemy> desertDayEnemies;
	public Vector<EntityNPCEnemy> arcticDayEnemies;
	
	public SpawnManager()
	{
		if (forestNightEnemies == null){
			forestNightEnemies = new Vector<EntityNPCEnemy>();
		}
		
		if (forestDayEnemies == null){
			forestDayEnemies = new Vector<EntityNPCEnemy>();
		}
		
		if (desertNightEnemies == null){
			desertNightEnemies = new Vector<EntityNPCEnemy>();
		}
		
		if (desertDayEnemies == null){
			desertDayEnemies = new Vector<EntityNPCEnemy>();
		}
		
		if (arcticNightEnemies == null){
			arcticNightEnemies = new Vector<EntityNPCEnemy>();
		}
		
		if (arcticDayEnemies == null){
			arcticDayEnemies = new Vector<EntityNPCEnemy>();
		}
		
		setSpawnVector();		
	}
	
	/**
	 * Get the forestNightEnemies Vector as an array
	 * @return forestNightEnemies Vector as an array
	 */
	public EntityNPCEnemy[] getForestNightEnemiesAsArray()
	{
		EntityNPCEnemy[] enemies = new EntityNPCEnemy[forestNightEnemies.size()];
		forestNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the desertNightEnemies Vector as an array
	 * @return desertNightEnemies Vector as an array
	 */
	public EntityNPCEnemy[] getDesertNightEnemiesAsArray()
	{
		EntityNPCEnemy[] enemies = new EntityNPCEnemy[desertNightEnemies.size()];
		desertNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the arcticNightEnemies Vector as an array
	 * @return arcticNightEnemies Vector as an array
	 */
	public EntityNPCEnemy[] getArcticNightEnemiesAsArray()
	{
		EntityNPCEnemy[] enemies = new EntityNPCEnemy[arcticNightEnemies.size()];
		arcticNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the forestDayEnemies Vector as an array
	 * @return forestDayEnemies Vector as an array
	 */
	public EntityNPCEnemy[] getForestDayEnemiesAsArray()
	{
		EntityNPCEnemy[] enemies = new EntityNPCEnemy[forestDayEnemies.size()];
		forestDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the desertDayEnemies Vector as an array
	 * @return desertDayEnemies Vector as an array
	 */
	public EntityNPCEnemy[] getDesertDayEnemiesAsArray()
	{
		EntityNPCEnemy[] enemies = new EntityNPCEnemy[desertDayEnemies.size()];
		desertDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the arcticDayEnemies Vector as an array
	 * @return arcticDayEnemies Vector as an array
	 */
	public EntityNPCEnemy[] getArcticDayEnemiesAsArray()
	{
		EntityNPCEnemy[] enemies = new EntityNPCEnemy[arcticDayEnemies.size()];
		arcticDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * add an enemy to the forestNightEnemies vector
	 * @param enemy to add.
	 */
	public void addForestNightEnemy(EntityNPCEnemy enemy)
	{
		if(forestNightEnemies == null)
		{
			forestNightEnemies = new Vector<EntityNPCEnemy>();
		}
		forestNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the desertNightEnemies vector
	 * @param enemy to add.
	 */
	public void addDesertNightEnemy(EntityNPCEnemy enemy)
	{
		if(desertNightEnemies == null)
		{
			desertNightEnemies = new Vector<EntityNPCEnemy>();
		}
		desertNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the arcticNightEnemies vector
	 * @param enemy to add.
	 */
	public void addArcticNightEnemy(EntityNPCEnemy enemy)
	{
		if(arcticNightEnemies == null)
		{
			arcticNightEnemies = new Vector<EntityNPCEnemy>();
		}
		arcticNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the forestDayEnemies vector
	 * @param enemy to add.
	 */
	public void addForestDayEnemy(EntityNPCEnemy enemy)
	{
		if(forestDayEnemies == null)
		{
			forestDayEnemies = new Vector<EntityNPCEnemy>();
		}
		forestDayEnemies.add(enemy);
	}

	/**
	 * add an enemy to the desertDayEnemies vector
	 * @param enemy to add.
	 */
	public void addDesertDayEnemy(EntityNPCEnemy enemy)
	{
		if(desertDayEnemies == null)
		{
			desertDayEnemies = new Vector<EntityNPCEnemy>();
		}
		desertDayEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the desertDayEnemies vector
	 * @param enemy to add.
	 */
	public void addArcticDayEnemy(EntityNPCEnemy enemy)
	{
		if(arcticDayEnemies == null)
		{
			arcticDayEnemies = new Vector<EntityNPCEnemy>();
		}
		arcticDayEnemies.add(enemy);
	}
	
	/**
	 * @depreciated [Forced compiler warning]-> replace string == string2 with string.equals(string2)
	 * 
	 * Sets possible spawn locations (biomes/times) for the EntityNPCEnemy 
	 * instance the method is called on.
	 * @param manager
	 * @param t = chosen biome/time to allow spawning
	 * @return
	 */
	private void setSpawnVector()
	{
		for(int i = 0; i < EntityNPCEnemy.enemyList.length; i++)
		{
			if(EntityNPCEnemy.enemyList[i] != null)
			{
				EntityNPCEnemy enemy = new EntityNPCEnemy(EntityNPCEnemy.enemyList[i]);
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