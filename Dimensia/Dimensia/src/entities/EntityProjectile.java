package entities;

import utils.ItemStack;
import utils.MathHelper;
import world.World;
import items.Item;

/*
 * m - requires mp
 * b - requires bow
 */
/**
 * 
 * @author Matt
 * 
 */
public class EntityProjectile extends EntityParticle 
{
	private static final long serialVersionUID = 1L;
	protected char type;
	protected int projectileId;
	public int damage;
	public boolean isFriendly;
	public boolean isHostile;
	protected double speed;
	protected String name;
	protected int direction;	
	public double criticalStrikeChance;
	public int iconIndex;
	protected ItemStack drop;
	public int ticksNonActive;
	
	public EntityProjectile(int i, String name, int damage, char c, int w, int h, double s){
		super (w, h);
		projectileId = i;
		type = c;
		x = 0;
		y = 0;
		ticksNonActive = 0;
		this.name = name;
		speed = s;
		direction = 0;
		active = true;
		ticksActive = 1;
		isFriendly = false;
		isHostile = false;		
		this.damage = damage;
		criticalStrikeChance = 0.05f;
		
		if (projectileList[i] != null){
			throw new RuntimeException("Entity already exists @" + i);
		}		
		projectileList[i] = this;
	}
	
	public EntityProjectile(EntityProjectile entity)
	{
		super(entity);

		this.type = entity.type;
		this.projectileId = entity.projectileId;
		this.damage = entity.damage;
		this.active = entity.active; 
		this.isFriendly = entity.isFriendly;
		this.isHostile = entity.isHostile;
		this.speed = entity.speed;
		this.name = entity.name;
		this.direction = entity.direction;
		this.blockWidth = entity.blockWidth;
		this.width = entity.width;
		this.blockHeight = entity.blockHeight;
		this.height = entity.height;
		this.criticalStrikeChance = entity.criticalStrikeChance;
		this.iconIndex = entity.iconIndex;
		this.drop = entity.drop;
		this.ticksNonActive = entity.ticksNonActive;
	}
	
	/**
	 * Moves the projectile based off of its x and y veloctiy as well as gravity.
	 * @param world = the current world
	 */
	public void moveProjectile(World world){
		this.integrate(world);		
		
		if (isOnGround(world) || (canMoveRight(world) == 0 && velocity.x > 0) || (canMoveLeft(world) == 0 && velocity.x < 0) || canMoveUp(world) == 0){
			active = false;
			setIsFriendly(false);
			setIsHostile(false);
		}		
	}	
	
	/**
	 * Applies a jump to the entity's Y Position. Disables jumping if (y < 0) or the upwardJumpCounter exceeds the upwardJumpHeight.
	 */
	public void moveEntityUp(World world, double movementValue)
	{		
		int loops = (int) (movementValue) + 1;	
		
		for(int i = 0; i < loops; i++)
		{
			double f = canMoveUp(world);			
			y += f;							
			if(y < 0) 
			{
				y = 0;
				break;
			}
			if(y > (world.getHeight() * 6) - blockHeight * 6 - 6)
			{
				y = (world.getHeight() * 6) - blockHeight * 6 - 6;
				break;
			}
			
			movementValue -= f;
		}
	}	
	
	
	public void moveEntityDown(World world, double movementValue)  
	{		
		int loops = (int) (movementValue) + 1;
		
		for(int i = 0; i < loops; i++)
		{
			double f = canMoveDown(world);
			
			y += f;
			
			
			if(y > (world.getHeight() * 6) - blockHeight * 6 - 6)
			{
				y = (world.getHeight() * 6) - blockHeight * 6 - 6;
				break;
			}

			movementValue -= f;
		}
	}	
	
	public EntityProjectile setType(char c){
		type = c;
		return this;
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


	public EntityProjectile setDirection(int d){
		direction = d;
		double r = Math.toRadians(d);
		velocity = MathHelper.toRectangular(speed, (double) r);
		velocity.y = -velocity.y;
		
		return this;
	}
	
	public EntityProjectile setIconIndex(int i, int j){
		iconIndex = i * 16 + j;
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
	
	public EntityProjectile setDrop(Item d){
		drop = new ItemStack(d);
		return this;
	}
	
	
	public int getBlockWidth(){
		return blockWidth;
	}
	
	public int getBlockHeight(){
		return blockHeight;
	}
	
	public char getType(){
		return type;
	}
	
	public int getProjectileId(){
		return projectileId;
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
	
	public String getName(){
		return name;
	}
	
	public EntityProjectile[] projectileList = new EntityProjectile[2];
	
	public final static EntityProjectile magicMissile = new EntityProjectile(0, "Magic Missile", 5, 'm', 1, 1, 7f).setAffectedByGravity(false).setIconIndex(0,0).setIsFriendly(true);
	public final static EntityProjectile woodenArrow = new EntityProjectile(1, "wooden Arrow", 7, 'b', 1, 1, 8f).setIconIndex(0,1).setIsFriendly(true);
	
}