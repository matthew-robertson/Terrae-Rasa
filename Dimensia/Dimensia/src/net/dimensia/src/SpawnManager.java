package net.dimensia.src;
import java.util.Vector;

public class SpawnManager{
	public SpawnManager(){
		if (forestNightEnemies == null){
			forestNightEnemies = new Vector<EntityEnemy>();
		}
		
		if (forestDayEnemies == null){
			forestDayEnemies = new Vector<EntityEnemy>();
		}
		
		if (desertNightEnemies == null){
			desertNightEnemies = new Vector<EntityEnemy>();
		}
		
		if (desertDayEnemies == null){
			desertDayEnemies = new Vector<EntityEnemy>();
		}
		
		if (arcticNightEnemies == null){
			arcticNightEnemies = new Vector<EntityEnemy>();
		}
		
		if (arcticDayEnemies == null){
			arcticDayEnemies = new Vector<EntityEnemy>();
		}
		
		
	}
	
	/**
	 * Get the forestNightEnemies Vector as an array
	 * @return forestNightEnemies Vector as an array
	 */
	public EntityEnemy[] getForestNightEnemiesAsArray()
	{
		EntityEnemy[] enemies = new EntityEnemy[forestNightEnemies.size()];
		forestNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the desertNightEnemies Vector as an array
	 * @return desertNightEnemies Vector as an array
	 */
	public EntityEnemy[] getDesertNightEnemiesAsArray()
	{
		EntityEnemy[] enemies = new EntityEnemy[desertNightEnemies.size()];
		desertNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the arcticNightEnemies Vector as an array
	 * @return arcticNightEnemies Vector as an array
	 */
	public EntityEnemy[] getArcticNightEnemiesAsArray()
	{
		EntityEnemy[] enemies = new EntityEnemy[arcticNightEnemies.size()];
		arcticNightEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the forestDayEnemies Vector as an array
	 * @return forestDayEnemies Vector as an array
	 */
	public EntityEnemy[] getForestDayEnemiesAsArray()
	{
		EntityEnemy[] enemies = new EntityEnemy[forestDayEnemies.size()];
		forestDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the desertDayEnemies Vector as an array
	 * @return desertDayEnemies Vector as an array
	 */
	public EntityEnemy[] getDesertDayEnemiesAsArray()
	{
		EntityEnemy[] enemies = new EntityEnemy[desertDayEnemies.size()];
		desertDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * Get the arcticDayEnemies Vector as an array
	 * @return arcticDayEnemies Vector as an array
	 */
	public EntityEnemy[] getArcticDayEnemiesAsArray()
	{
		EntityEnemy[] enemies = new EntityEnemy[arcticDayEnemies.size()];
		arcticDayEnemies.copyInto(enemies);
		return enemies;
	}
	
	/**
	 * add an enemy to the forestNightEnemies vector
	 * @param enemy to add.
	 */
	public void addForestNightEnemy(EntityEnemy enemy)
	{
		if(forestNightEnemies == null)
		{
			forestNightEnemies = new Vector<EntityEnemy>();
		}
		forestNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the desertNightEnemies vector
	 * @param enemy to add.
	 */
	public void addDesertNightEnemy(EntityEnemy enemy)
	{
		if(desertNightEnemies == null)
		{
			desertNightEnemies = new Vector<EntityEnemy>();
		}
		desertNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the arcticNightEnemies vector
	 * @param enemy to add.
	 */
	public void addArcticNightEnemy(EntityEnemy enemy)
	{
		if(arcticNightEnemies == null)
		{
			arcticNightEnemies = new Vector<EntityEnemy>();
		}
		arcticNightEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the forestDayEnemies vector
	 * @param enemy to add.
	 */
	public void addForestDayEnemy(EntityEnemy enemy)
	{
		if(forestDayEnemies == null)
		{
			forestDayEnemies = new Vector<EntityEnemy>();
		}
		forestDayEnemies.add(enemy);
	}

	/**
	 * add an enemy to the desertDayEnemies vector
	 * @param enemy to add.
	 */
	public void addDesertDayEnemy(EntityEnemy enemy)
	{
		if(desertDayEnemies == null)
		{
			desertDayEnemies = new Vector<EntityEnemy>();
		}
		desertDayEnemies.add(enemy);
	}
	
	/**
	 * add an enemy to the desertDayEnemies vector
	 * @param enemy to add.
	 */
	public void addArcticDayEnemy(EntityEnemy enemy)
	{
		if(arcticDayEnemies == null)
		{
			arcticDayEnemies = new Vector<EntityEnemy>();
		}
		arcticDayEnemies.add(enemy);
	}
	
	/** EntityEnemy Declarations **/
	public final EntityEnemy goblin = new EntityEnemy(this, 0, "Goblin").setSpawnVector("forest", " ").setSpawnVector("desert", "night").setTexture(Render.goblin).setDamageDone(7).setMaxHealth(50).setDrops(new MonsterDrop[] { 
			new MonsterDrop(new ItemStack(Item.coal), 1, 2, 5), new MonsterDrop(new ItemStack(Item.healthPotion1), 1, 2, 20)
	});
	public final  EntityEnemy zombie = new EntityEnemy(this, 1, "zombie").setSpawnVector(" ", "night").setDamageDone(16).setMaxHealth(60).setTexture(Render.zombie).setDrops(new MonsterDrop[]{
			new MonsterDrop(new ItemStack(Item.healthPotion2), 1, 1, 50), new MonsterDrop(new ItemStack(Item.ironSword), 1, 1, 100), new MonsterDrop(new ItemStack(Item.bronzeSword), 1, 1, 66)
	});
	public final EntityEnemy slime = new EntityEnemy(this, 2, "Slime").setSpawnVector(" ", " ").setBlockAndWorldDimensions(1,1).setTexture(Render.slime).setMaxHealth(30).setDamageDone(4).setBaseSpeed(1.0f).setDrops(new MonsterDrop[]{
			new MonsterDrop(new ItemStack(Item.silverIngot), 1, 1, 200), new MonsterDrop(new ItemStack(Item.healingHerb1), 1, 1, 15), new MonsterDrop(new ItemStack(Item.vialOfWater), 1, 1, 15)  
	});
	public final EntityEnemy dino = new EntityEnemy(this, 3, "Dinosaur").setSpawnVector("forest", "night").setBlockAndWorldDimensions(5,4).setTexture(Render.dino).setMaxHealth(150).setDamageDone(20).setBaseSpeed(3.4f).setDrops(new MonsterDrop[]{
			new MonsterDrop(new ItemStack(Item.ringOfVigor), 1, 1, 100), new MonsterDrop(new ItemStack(Item.coal), 1, 3, 4), new MonsterDrop(new ItemStack(Item.healingHerb2), 1, 1, 20) 
	});
	public final EntityEnemy floatingEye = new EntityEnemy(this, 4, "Eyeball").setDamageDone(12).setMaxHealth(60);
	
	public Vector<EntityEnemy> forestNightEnemies;
	public Vector<EntityEnemy> desertNightEnemies;
	public Vector<EntityEnemy> arcticNightEnemies;
	public Vector<EntityEnemy> forestDayEnemies;
	public Vector<EntityEnemy> desertDayEnemies;
	public Vector<EntityEnemy> arcticDayEnemies;
}