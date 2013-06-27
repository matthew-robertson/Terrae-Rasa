package entities;

import utils.ItemStack;
import utils.MathHelper;
import world.World;

public class EntityProjectile extends EntityParticle 
{
	private static final long serialVersionUID = 1L;
	public int damage;
	public boolean isFriendly;
	public boolean isHostile;
	protected double speed;
	protected int direction;	
	public double criticalStrikeChance;
	public int iconX;
	public int iconY;
	protected ItemStack drop;
	public int ticksNonActive;
	
	public EntityProjectile(int damage, int width, int height, double speed){
		super (width, height);
		x = 0;
		y = 0;
		ticksNonActive = 0;
		this.speed = speed;
		direction = 0;
		active = true;
		ticksActive = 1;
		isFriendly = false;
		isHostile = false;		
		this.damage = damage;
		criticalStrikeChance = 0.05f;
	}
	
	/**
	 * Creates a deep copy of the given EntityProjectile, calling all super class copy constructors.
	 * @param entity the EntityProjectile to make a deep copy of
	 */
	public EntityProjectile(EntityProjectile entity)
	{
		super(entity);
		this.damage = entity.damage;
		this.isFriendly = entity.isFriendly;
		this.isHostile = entity.isHostile;
		this.speed = entity.speed;
		this.direction = entity.direction;
		this.criticalStrikeChance = entity.criticalStrikeChance;
		this.iconX = entity.iconX;
		this.iconY = entity.iconY;
		this.drop = entity.drop;
		this.ticksNonActive = entity.ticksNonActive;
	}
	
	/**
	 * Moves the projectile based off of its x and y veloctiy as well as gravity.
	 * @param world the current world
	 */
	public void moveProjectile(World world){
		this.integrate(world);		
		
		if (isOnGround(world) || (canMoveRight(world) == 0 && velocity.x > 0) || (canMoveLeft(world) == 0 && velocity.x < 0) || canMoveUp(world) == 0){
			active = false;
			setIsFriendly(false);
			setIsHostile(false);
		}		
	}	
			
	public EntityProjectile setXLoc(int x){
		this.x = x;
		return this;
	}
	
	public EntityProjectile setYLoc(int y){
		this.y = y;
		return this;
	}

	public EntityProjectile setXLocAndYLoc(double x, double y){
		this.x = x;
		this.y = y;
		return this;
	}
		
	public EntityProjectile setDamage(int d){
		damage = d;
		return this;
	}
	
	/**
	 * This version of setDamage accepts a double and casts it to an integer
	 * @param d the damage done as a double
	 * @return a reference to this object
	 */
	public EntityProjectile setDamage(double d){
		damage = (int)d;
		return this;
	}

	public EntityProjectile setDirection(int d){
		direction = d;
		velocity = MathHelper.toRectangular(speed, Math.toRadians(d));
		velocity.y = -velocity.y;
		return this;
	}
	
	public EntityProjectile setSpriteIndex(int x, int y) {
		iconX = x;
		iconY = y;
		return this;
	}
	
	public EntityProjectile setIsFriendly(boolean friend){
		isFriendly = friend;
		return this;
	}
	
	public EntityProjectile setIsHostile(boolean hostile){
		isHostile = hostile;
		return this;
	}
	
	public EntityProjectile setAffectedByGravity(boolean flag){
		isAffectedByGravity = flag;
		return this;
	}
	
	public EntityProjectile setDrop(ItemStack stack){
		drop = new ItemStack(stack);
		return this;
	}
			
	public int getDamage(){
		return damage;
	}
	
	public boolean getActive(){
		return active;
	}
	
	public boolean getIsHostile(){
		return isHostile;
	}
	
	public boolean getIsFriendly(){
		return isFriendly;
	}
	
	public ItemStack getDrop(){
		return drop;
	}
	
	
//	public EntityProjectile[] projectileList = new EntityProjectile[2];
//	
//	public final static EntityProjectile magicMissile = new EntityProjectile(5, 1, 1, 7f).setAffectedByGravity(false).setIconIndex(0,0).setIsFriendly(true);
//	public final static EntityProjectile woodenArrow = new EntityProjectile(7, 1, 1, 8f).setIconIndex(0,1).setIsFriendly(true);
	
}