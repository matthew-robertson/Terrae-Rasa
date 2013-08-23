package entities;
import items.Item;

import java.util.Random;
import java.util.Vector;

import utils.ItemStack;
import utils.MonsterDrop;
import world.World;
import enums.EnumDamageSource;
import enums.EnumMonsterType;

/**
 * <code>EntityNPCEnemy</code> implements many of the features and fields needed for a monster.
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
 * <li>Whether the monster is a boss (this should involve an extension of EntityNPCEnemy)
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class EntityNPCEnemy extends EntityNPC 
{	
	private static final long serialVersionUID = 1L;

	public String type;
	public String time;
	protected final Random random = new Random();
	protected MonsterDrop[] possibleDrops;
	protected int monsterId;
	public int damageDone;
	protected EnumDamageSource damageType;
	protected EnumMonsterType monsterType;
	public boolean isBoss;
	
	/**
	 * Constructs a new <code>EntityNPCEnemy</code>. This constructor is protected
	 * because monsters are created in <code>EntityNPCEnemy.java</code> and
	 * then cloned into <code>world.entityList</code>. This reduces the margin of error
	 * greatly, as instead of any number of possible failures, only a simple
	 * clone must be made. It also ensures consistency between monsters
	 * @param i the EntityNPCEnemy's unique ID number, used for getting it from EntityList
	 */
	protected EntityNPCEnemy(int i, String s)
	{
		super(i, s);
		name = s;
		damageDone = 0;
		monsterId = i;
		damageType = EnumDamageSource.MELEE;
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
		setBaseSpeed(2.5f);
		possibleDrops = new MonsterDrop[0];
		time = " ";
		type = " ";
		if (enemyList[i] != null){
			throw new RuntimeException("Entity already exists @" + i);
		}		
		enemyList[i] = this;
	}	

	/**
	 * Creates a deep copy of the given EntityNPCEnemy, calling all super class constructors
	 * @param entity the EntityNPCEnemy to create a deep copy of 
	 */
	public EntityNPCEnemy(EntityNPCEnemy entity)
	{
		super(entity);
		this.type = entity.type;
		this.time = entity.time;
		this.possibleDrops = entity.possibleDrops;
		this.monsterId = entity.monsterId;
		this.damageDone = entity.damageDone;
		this.damageType = entity.damageType;
		this.monsterType = entity.monsterType;
		this.isBoss = entity.isBoss;
		
	}

	protected EntityNPCEnemy setDamageType(EnumDamageSource type)
	{
		damageType = type;
		return this;
	}
	
	protected EntityNPCEnemy setWidthandHeight(int x, int y)
	{
		width = x;
		height = y;
		return this;
	}
		
	protected EntityNPCEnemy setDamageDone(int i)
	{
		damageDone = i;
		return this;
	}
	
	protected EntityNPCEnemy setMaxHealth(int i)
	{
		maxHealth = i;
		health = i;
		return this;
	}
		
	protected EntityNPCEnemy setWorldDimensions(int i, int j)
	{
		width = i;
		height = j;
		return this;
	}
	
	protected EntityNPCEnemy setBlockDimensions(int i, int j)
	{
		blockWidth = i;
		blockHeight = j;
		return this;
	}
	
	protected EntityNPCEnemy setBlockAndWorldDimensions(int i, int j)
	{
		setWorldDimensions(i * 6, j * 6);
		setBlockDimensions(i, j);
		return this;
	}
	
	protected EntityNPCEnemy setTextureDimensions(int i, int j)
	{
		textureWidth = i;
		textureHeight = j;
		return this;
	}
	
	public EntityNPCEnemy setBaseSpeed(double f)
	{
		baseSpeed = f;
		return this;
	}
	/**
	 * Sets Monster drops using the following format (constructor) to declare the monster drops:
	 * public MonsterDrop(ItemStack stack, int min, int max, int rollMax)
	 * @param drops the array of possible drops
	 */
	public EntityNPCEnemy setDrops(MonsterDrop[] drops)
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
		
	public EntityNPCEnemy setIsAlert(boolean alert){
		this.alert = alert;
		return this;
	}
	
	public EntityNPCEnemy setSpawnVector(String type, String time)
	{
		this.type = type;
		this.time = time;
		return this;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getTime()
	{
		return time;
	}
	
	protected EntityNPCEnemy setIconIndex(int x, int y)
	{
		iconX = x;
		iconY = y;
		return this;
	}
	
	protected EntityNPCEnemy setIconIndex(int x, int y, int texwidth, int texheight)
	{
		this.iconX = x;
		this.iconY = y;
		this.textureWidth = texwidth;
		this.textureHeight = texheight;
		return this;
	}
	
	public final static EntityNPCEnemy[] enemyList = new EntityNPCEnemy[10];		
	/** EntityNPCEnemy Declarations **/
	public final static EntityNPCEnemy goblin = new EntityNPCEnemy(0, "Goblin").setIconIndex(2, 0, 32, 64).setIsAlert(true).setSpawnVector("forest", " ").setSpawnVector("desert", "night").setDamageDone(7).setMaxHealth(50).setDrops(new MonsterDrop[] { 
			new MonsterDrop(new ItemStack(Item.coal), 1, 2, 5), new MonsterDrop(new ItemStack(Item.healthPotion1), 1, 2, 20)
	});
	public final static EntityNPCEnemy zombie = new EntityNPCEnemy(1, "zombie").setIconIndex(0, 0, 32, 64).setIsAlert(true).setSpawnVector(" ", "night").setDamageDone(16).setMaxHealth(60).setDrops(new MonsterDrop[]{
			new MonsterDrop(new ItemStack(Item.healthPotion2), 1, 1, 50), new MonsterDrop(new ItemStack(Item.ironSword), 1, 1, 100), new MonsterDrop(new ItemStack(Item.bronzeSword), 1, 1, 66)
	});
	public final static EntityNPCEnemy slime = new EntityNPCEnemy(2, "Slime").setIconIndex(1, 0, 32, 32).setIsAlert(true).setSpawnVector(" ", " ").setBlockAndWorldDimensions(1,1).setMaxHealth(30).setDamageDone(4).setBaseSpeed(1.4f).setDrops(new MonsterDrop[]{
			new MonsterDrop(new ItemStack(Item.silverIngot), 1, 1, 200), new MonsterDrop(new ItemStack(Item.healingHerb1), 1, 1, 15), new MonsterDrop(new ItemStack(Item.vialOfWater), 1, 1, 15)  
	});
	public final static EntityNPCEnemy dino = new EntityNPCEnemy(3, "Dinosaur").setIconIndex(3, 0, 96, 64).setIsAlert(true).setSpawnVector("forest", "night").setBlockAndWorldDimensions(5,4).setMaxHealth(150).setDamageDone(20).setBaseSpeed(3.4f).setDrops(new MonsterDrop[]{
			new MonsterDrop(new ItemStack(Item.ringOfVigor), 1, 1, 100), new MonsterDrop(new ItemStack(Item.coal), 1, 3, 4), new MonsterDrop(new ItemStack(Item.healingHerb2), 1, 1, 20) 
	});
	//public final static EntityNPCEnemy doubleingEye = new EntityNPCEnemy(4, "Eyeball").setDamageDone(12).setMaxHealth(60);
	
	
}