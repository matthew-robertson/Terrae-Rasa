package net.dimensia.src;
import java.util.Random;
import java.util.Vector;

/**
 * <code>EntityEnemy extends EntityLiving</code>, 
 * and <code>implements Cloneable, Serializable</code>
 * (as a result of extending <code>EntityLiving</code>)
 * <br> 
 * <code>EntityEnemy</code> implements many of the features and fields needed for a monster.
 * It provides a public implementation of {@link #clone()}, so that monsters
 * can be added to world, from their public final static declarations here.
 * <br><br>
 * The other main method of interest here is {@link #applyAI(World)}. This method is the main method to 
 * make a monster do something, based on where/what the player is doing. It should be called every game tick
 * or at least several times per second, to avoid having laggy monsters.
 * <br><br>
 * Fields and getters/settings are included for storage of:
 * <li>Texture, and texture coordinates
 * <li>Unique id number (this is a non-changing value, therefore there is no setter)
 * <li>Monster type, and monster damage type (these are enums)
 * <li>Damage done
 * <li>Drops
 * <li> where/when the monster can spawn (biome/time)
 * <li>Whether the monster is a boss (this should involve an extension of EntityEnemy)
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class EntityEnemy extends EntityNPC implements Cloneable
{	
	private static final long serialVersionUID = 1L;	
	/**
	 * Constructs a new <code>EntityEnemy</code>. This constructor is protected
	 * because monsters are created in <code>EntityEnemy.java</code> and
	 * then cloned into <code>world.entityList</code>. This reduces the margin of error
	 * greatly, as instead of any number of possible failures, only a simple
	 * clone must be made. It also ensures consistency between monsters
	 * @param i the EntityEnemy's unique ID number, used for getting it from EntityList
	 */
	protected EntityEnemy(SpawnManager managers, int i, String s)
	{
		super(i, s);
		this.manager = managers;
		name = s;
		damageDone = 0;
		monsterId = i;
		damageType = EnumDamageType.MELEE;
		monsterType = EnumMonsterType.GROUNDED;
		blockWidth = 2;
		blockHeight = 3;
		textureWidth = 16;
		textureHeight = 16;
		maxHealth = 1;
		health = 1;
		damageDone = 1;
		width = 12;
		height = 18;
		baseSpeed = 2.5f;
		possibleDrops = new MonsterDrop[0];
		
		if (enemyList[i] != null){
			throw new RuntimeException("Entity already exists @" + i);
		}		
		enemyList[i] = this;
	}	

	/**
	 * Overrides Object.clone() to provide cloning functionality to EntityEnemy.
	 * Creates a deep copy of the EntityEnemy instance on which the method is 
	 * called.
	 * @return a deep copy of the EntityEnemy
	 */
	public EntityEnemy clone()
	{
		try
		{
			return (EntityEnemy) super.clone();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Sets possible spawn locations (biomes/times) for the EntityEnemy 
	 * instance the method is called on.
	 * @param manager
	 * @param t = chosen biome/time to allow spawning
	 * @return
	 */
	protected EntityEnemy setSpawnVector(String type, String time){
		//Arctic spawns
		if (type == "arctic" && time == "day"){
			manager.addArcticDayEnemy(this);
		}
		
		if (type == "arctic" && time == "night"){
			manager.addArcticNightEnemy(this);
		}
		
		if (type == "arctic" && time == " "){
			manager.addArcticDayEnemy(this);
			manager.addArcticNightEnemy(this);
		}
		
		//Desert spawns
		if (type == "desert" && time == "day"){
			manager.addDesertDayEnemy(this);
		}
		
		if (type == "desert" && time == "night"){
			manager.addDesertNightEnemy(this);
		}
		
		if (type == "desert" && time == " "){
			manager.addDesertDayEnemy(this);
			manager.addDesertNightEnemy(this);
		}
		
		//Forest spawns
		if (type == "forest" && time == "day"){
			manager.addForestDayEnemy(this);
		}	
		
		if (type == "forest" && time == "night"){
			manager.addForestNightEnemy(this);
		}		
		
		if (type == "forest" && time == " "){
			manager.addForestDayEnemy(this);
			manager.addForestNightEnemy(this);
		}
		
		//All spawns
		if (type == " " && time == "day"){
			manager.addArcticDayEnemy(this);
			manager.addDesertDayEnemy(this);
			manager.addForestDayEnemy(this);			
		}
		
		if (type == " " && time == "night"){
			manager.addArcticNightEnemy(this);
			manager.addDesertNightEnemy(this);
			manager.addForestNightEnemy(this);			
		}	
		
		if (type == " " && time == " "){
			manager.addArcticDayEnemy(this);
			manager.addDesertDayEnemy(this);
			manager.addForestDayEnemy(this);			
			manager.addArcticNightEnemy(this);
			manager.addDesertNightEnemy(this);
			manager.addForestNightEnemy(this);
		}
		
		
		
		return this;
	}
		
	protected EntityEnemy setDamageType(EnumDamageType type)
	{
		damageType = type;
		return this;
	}
	
	protected EntityEnemy setWidthandHeight(int x, int y)
	{
		width = x;
		height = y;
		return this;
	}
	
	protected EntityEnemy setTexture(Texture tex)
	{
		texture = tex;
		return this;
	}
	
	protected EntityEnemy setDamageDone(int i)
	{
		damageDone = i;
		return this;
	}
	
	protected EntityEnemy setMaxHealth(int i)
	{
		maxHealth = i;
		health = i;
		return this;
	}
		
	protected EntityEnemy setWorldDimensions(int i, int j)
	{
		width = i;
		height = j;
		return this;
	}
	
	protected EntityEnemy setBlockDimensions(int i, int j)
	{
		blockWidth = i;
		blockHeight = j;
		return this;
	}
	
	protected EntityEnemy setBlockAndWorldDimensions(int i, int j)
	{
		setWorldDimensions(i * 6, j * 6);
		setBlockDimensions(i, j);
		return this;
	}
	
	protected EntityEnemy setIconIndex(int i, int j)
	{
		iconIndex = i * 16 + j;
		return this;
	}
	
	protected EntityEnemy setTextureDimensions(int i, int j)
	{
		textureWidth = i;
		textureHeight = j;
		return this;
	}
	
	protected EntityEnemy setBaseSpeed(float f)
	{
		baseSpeed = f;
		return this;
	}
		
	public float getWidth()
	{
		return width;
	}
	
	public float getHeight()
	{
		return height; 
	}
	
	public Texture getTexture()
	{
		return texture;
	}

	/**
	 * Sets Monster drops using the following format (constructor) to declare the monster drops:
	 * public MonsterDrop(ItemStack stack, int min, int max, int rollMax)
	 * @param drops the array of possible drops
	 */
	public EntityEnemy setDrops(MonsterDrop[] drops)
	{
		this.possibleDrops = drops;
		return this;
	}	
	
	/**
	 * Gets the drops for the specific enemy kill
	 * @return all the drops for the kill, or null if none are dropped
	 */
	public ItemStack[] getDrops()
	{
		Vector<ItemStack> drops = new Vector<ItemStack>();
		for(MonsterDrop drop : possibleDrops) //for each possible drop
		{
			if(random.nextInt(drop.getRollMaximum()) == 0) //See if the drop will happen (and how much of it)
			{
				drops.add(new ItemStack(drop.getDrop().getItemID(), (drop.getMinimum() + (((drop.getMaximum() - drop.getMinimum()) > 0) ? random.nextInt(drop.getMaximum() - drop.getMinimum()) : 0))));
			}
		}
		ItemStack[] stacks = new ItemStack[drops.size()];
		drops.copyInto(stacks);
		return (drops.size() > 0) ? stacks : null; 
	}
	
	public float getBlockWidth()
	{
		return blockWidth;
	}
	
	public float getBlockHeight()
	{
		return blockHeight;
	}
	
	public String getEnemyName()
	{
		return name;
	}
	
	/**
	 * Makes the monster do something somewhat smart, based on its type. This includes a movement
	 * on the X axis, based on player direction, as well as an attempt to jump if applicable. Gravity 
	 * is also applied if the EntityEnemy is not currently jumping.
	 * @param world the main world Object for the current game
	 */
	public void applyAI(World world)
	{
		//if(!isStunned)
		//{
			//ground AI
			if(world.player.x + 3 < x) //if the player is to the left, move left
			{	        
				moveEntityLeft(world);
			}
			else if(world.player.x - 3 > x) //if the player is to the right, move right
			{
				moveEntityRight(world);
			}
			
			if(world.player.y < y) //if the player is above the entity, jump
			{
				hasJumped();
			}
		//}
		//else
		//{
		//	System.out.println("Skipped Entity Movement@>>" + this);
		//}
		
		
		applyGravity(world); //and attempt to apply gravity
	}
	
	public final static EntityEnemy[] enemyList = new EntityEnemy[10];		
	
	protected final Random random = new Random();
	protected MonsterDrop[] possibleDrops;
	protected int iconIndex;
	protected int monsterId;
	protected int damageDone;
	protected EnumDamageType damageType;
	protected EnumMonsterType monsterType;
	protected Texture texture;
	protected boolean isBoss;
	protected String name;
	protected SpawnManager manager;
}