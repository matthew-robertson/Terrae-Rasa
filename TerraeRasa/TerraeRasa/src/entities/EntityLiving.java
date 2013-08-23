package entities;

import java.util.ArrayList;
import java.util.List;

import statuseffects.DisplayableStatusEffect;
import world.World;
import audio.SoundEngine;
import blocks.Block;

/**
 * It provides most of the features required for an Entity
 * that is alive. The list of methods for EntityLiving is extensive. It includes methods to: 
 * <br><br>
 * <li>Apply gravity: {@link #applyGravity(World)}
 * <li>Check for nearby blocks: {@link #blockInBounds(World, int, int, int, int, Block)}
 * <li>Check for vertexes inside the Entity: {@link #inBounds(double, double)}, or {@link #inBounds(double, double, double, double)}
 * <li>Check if the entity has health left: {@link #isDead()}
 * <li>Damage the entity: {@link #damageEntity(World, int, boolean)} 
 * <li>Handle a jump: {@link #hasJumped()}
 * <li>Handle movement: {@link #moveEntityDown(World)}, {@link #moveEntityUp(World)}, {@link #moveEntityLeft(World)}, {@link #moveEntityRight(World)}
 * <li>Determine movement: {@link #isWalkingSafe(World, boolean)}, {@link #isJumpNeeded(World, boolean, boolean)}, {@link #isJumpPossibleAndNeeded(World, boolean, boolean)}
 * <li>Launch projectiles: {@link #launchProjectile(World, EntityProjectile)}
 * <li>Heal the entity: {@link #healEntity(World, int)}
 * <li>Implement custom death behaviour: {@link #onDeath()}
 * <br><br>
 * More advanced features can be added by further extending the class. All methods in EntityLiving
 * are designed to scale with the entity's size, health, etc.
 * <br>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class EntityLiving extends Entity
{
	private static final long serialVersionUID = 1L;
	public List<DisplayableStatusEffect> statusEffects;	
	public int invincibilityTicks;	
	public int maxHealth;
	public int maxMana;
	public double mana;
	public double defense;
	protected double health;
	public String jumpSound;
	public String deathSound;
	public String hitSound;
	
	/**
	 * Overrides Entity's constructor, and constructs a new EntityLiving. Initializes combat and life
	 * related variables in addition to the variables from Entity.
	 */
	public EntityLiving()
	{
		super();
		defense = 0;
		health = 100;
		maxHealth = 100;
		mana = 0;
		maxMana = 0;
		statusEffects = new ArrayList<DisplayableStatusEffect>();
		x = 0;
		y = 0;	
		jumpSound = "Player Jump";
		deathSound = "Player Death";		
		hitSound = "Generic Hit";
	}
		
	/**
	 * Handles special death events. Generally this should be done by extending EntityLiving and overriding this method
	 */
	public void onDeath(World world)
	{		
		SoundEngine.playSoundEffect(deathSound);
	}	
	
	public boolean registerStatusEffect(DisplayableStatusEffect effect)
	{
		statusEffects.add(effect);
		return true;
	}
	
	/**
	 * Gets whether or not the entity is immune to damage. An entity is immune to damage if invincibilityTicks > 0.
	 * @return true if the entity is immune to damage, otherwise false
	 */
	public boolean isImmuneToDamage()
	{
		return invincibilityTicks > 0;
	}	
	
	public double getHealth() 
	{
		return health;
	}
	
	public void setHealth(double health)
	{
		this.health = health;
	}
	
	public DisplayableStatusEffect getStatusEffectByID(long id)
	{
		for(int i = 0; i < statusEffects.size(); i++)
		{
			if(id == statusEffects.get(i).getID())
			{
				return statusEffects.get(i);
			}
		}
		return null;
	}
}
