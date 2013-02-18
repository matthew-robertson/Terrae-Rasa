package net.dimensia.src;

/*
 * m - requires mp
 * b - requires bow
 */
/**
 * 
 * @author Matt
 * 
 */
public class EntityProjectile extends Entity 
{
	private static final long serialVersionUID = 1L;
	protected char type;
	protected int projectileId;
	protected double xVel;
	protected double yVel;
	protected int damage;
	float xDisplacement;
	float yDisplacement;
	protected boolean active;
	protected boolean isFriendly;
	protected boolean isHostile;
	protected float speed;
	protected String name;
	protected int direction;
	protected int blockWidth;
	protected int width;
	protected int blockHeight;
	protected int height;
	protected float criticalStrikeChance;
	protected int ticksActive;
	protected int iconIndex;
	protected ItemStack drop;
	protected int ticksNonActive;
	
	public EntityProjectile(int i, String name, int damage, char c, int w, int h, float s){
		projectileId = i;
		type = c;
		x = 0;
		xVel = 0;
		y = 0;
		ticksNonActive = 0;
		this.name = name;
		yVel = 0;
		xDisplacement = 0;
		yDisplacement = 0;
		speed = s;
		direction = 0;
		active = true;
		ticksActive = 1;
		isFriendly = false;
		isHostile = false;
		blockWidth = w;
		width = w * 6;
		blockHeight = h;
		height = h * 6;
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
		this.xVel = entity.xVel;
		this.yVel = entity.yVel;
		this.damage = entity.damage;
		this.xDisplacement = entity.xDisplacement;
		this.yDisplacement = entity.yDisplacement;
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
		this.ticksActive = entity.ticksActive;
		this.iconIndex = entity.iconIndex;
		this.drop = entity.drop;
		this.ticksNonActive = entity.ticksNonActive;
	}
	
	/**
	 * Moves the projectile based off of its x and y veloctiy as well as gravity.
	 * @param world = the current world
	 */
	public void moveProjectile(World world){
		xDisplacement = (float) xVel;
		
		if (isAffectedByGravity){
			yDisplacement = (float) (yVel + 0.5f * 0.01f * Math.pow(ticksActive, 2));
		}
		else{
			yDisplacement = (float) (yVel);
		}
		x += xDisplacement;	
		y += yDisplacement;
		
		if (isOnGround(world) || (canMoveRight(world) == 0 && xVel > 0) || (canMoveLeft(world) == 0 && xVel < 0) || canMoveUp(world) == 0){
			active = false;
			setIsFriendly(false);
			setIsHostile(false);
		}
		if (active){
			ticksActive++;
		}
	}
	/**
	 * Tries to move the entity right
	 */
	public void moveEntityRight(World world)
	{
		float movementValue = (float) (xVel);
		int loops = (int) movementValue + 1;
				
		for(int i = 0; i < loops; i++)
		{
			float f = canMoveRight(world);
			
			if(x + f < (world.getWidth() * 6) && f >= movementValue) //full movement
				x += f;
			else if(f > 0) //partial movement
				x += f;
			
			if(x > world.getWidth() * 6 - blockWidth * 6 - 6) //bounds check
				x = (world.getWidth() * 6) - blockWidth * 6 - 6;

			movementValue -= f;
		}
	}
	
	/**
	 * Applies a jump to the entity's Y Position. Disables jumping if (y < 0) or the upwardJumpCounter exceeds the upwardJumpHeight.
	 */
	public void moveEntityUp(World world, float movementValue)
	{		
		int loops = (int) (movementValue) + 1;	
		
		for(int i = 0; i < loops; i++)
		{
			float f = canMoveUp(world);			
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
	
	/**
	 * Tries to move the entity left
	 */
	public void moveEntityLeft(World world) 
	{
		float movementValue = (float) (Math.abs(xVel));
		int loops = (int) movementValue + 1;				
		
		for(int i = 0; i < loops; i++)
		{
			float f = canMoveLeft(world);
			
			if((x - f) >= 0 && f >= movementValue) //full movement
				x -= f;   
			else if(f > 0) //partial movement
				x -= f;
			
			if(x < 0)//out of bounds check
				x = 0;
			
			movementValue -= f;
		}
	}
	
	public void moveEntityDown(World world, float movementValue)  
	{		
		int loops = (int) (movementValue) + 1;
		
		for(int i = 0; i < loops; i++)
		{
			float f = canMoveDown(world);
			
			y += f;
			
			
			if(y > (world.getHeight() * 6) - blockHeight * 6 - 6)
			{
				y = (world.getHeight() * 6) - blockHeight * 6 - 6;
				break;
			}

			movementValue -= f;
		}
	}
	
	/**
	 * Gets how far the entity can fall, upto 1 block (use multiple times to go further)
	 * @return the distance it's possible to fall, upto 6 ortho units (1 block)
	 */
	private float canMoveDown(World world)
	{
		int offset = (int) ((y % 6 == 0) ? blockHeight : (blockHeight + 1)); 
		
		if((int)((y + fallSpeed) / 6) + offset + 1 > world.getHeight() - 1) //bounds check
		{
			return 6.0f;
		}
		
		Block[] blocks = new Block[(int) (blockWidth + ((x % 6 == 0) ? 0 : 1))]; //blocks to check
		boolean flag = true;
		
		for(int i = 0; i < blocks.length; i++) //get blocks to check
		{
			blocks[i] = world.getBlock((x / 6) + i, ((y + fallSpeed) / 6) + offset); 
		}		

		for(int i = 0; i < blocks.length; i++)
		{
			if(!blocks[i].isPassable()) //hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}
		
		if(flag && y + fallSpeed < world.getHeight() * 6) //Normal Gravity
		{
			return fallSpeed;
		}
		else if(y % 6 != 0) //Near ground partial gravity 
		{
			return (6 - (y % 6));
		}		
		return 0.0f;
	}
	
	/**
	 * Gets how far up the entity can move, upto 1 block (6 ortho units). Use multiple times to go further. 
	 * @return how far the entity can move, upto 6 ortho units (1 block)
	 */
	private float canMoveUp(World world)
	{
		if((int)(y / 6) <= 0) //bounds check
		{
			return 6;
		}
		
		Block[] blocks = new Block[(int) (blockWidth + ((x % 6 == 0) ? 0 : 1))]; //blocks to check	
		boolean flag = true;
		
		for(int i = 0; i < blocks.length; i++) //get blocks to check
		{
			blocks[i] = world.getBlock((x / 6) + i, ((y -(float) yVel) / 6));
		}
		
		for(int i = 0; i < blocks.length; i++)
		{
			if(!blocks[i].isPassable()) //hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}
		
		if(flag && y - yVel >= 0) //Normal upward movement
		{
			return (float) yVel;
		}
		else if(y % 6 != 0) //Partial jump
		{
			isJumping = false;
			return y % 6;
		}
		return 0.0f;
	}
	
	/**
	 * Determines whether the entity is on something solid?
	 */
	public boolean isOnGround(World world)		
	{
		int xOffset = (int) ((x % 6 == 0) ? blockWidth : (blockWidth + 1));
		try
		{
			for(int i = 0; i < xOffset; i++) //for each block below the entity, check if any are solid
			{
				if(!world.getBlock((int)(x / 6) + i, (int)((y / 6) + Math.ceil(blockHeight))).isPassable())
				{
					return true; //if one is solid, the entity is on the ground
				}
			}
			return false;
		}
		catch(Exception e) //if there's an out of bounds error, assume the entity is on something solid
		{
			return true;
		}
	}
	
	/**
	 * Gets how far right can the entity move (0-6 ortho)
	 * @return the distance that can be moved right (from 0-6 ortho)
	 */
	private float canMoveRight(World world)
	{
		if(!isAffectedByWalls) //no point in performing a hittest
			return 6;
		
		boolean flag = false;
		int offset = (int) ((x % 6 == 0) ? blockWidth : (blockWidth + 1)); //How far right are the blocks for the hit test, if the player is perfectly on a block 2, otherwise 3		
		Block[] blocks = new Block[(int) (blockHeight + ((y % 6 == 0) ? 0 : 1))]; //blocks to check
		
		for(int i = 0; i < blocks.length; i++) //get the blocks to check against
		{
			blocks[i] = world.getBlock(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)(x / 6) + offset), MathHelper.returnIntegerInWorldMapBounds_Y(world, (int)(y / 6) + i));
		}
		
		for(int i = 0; i < blocks.length; i++)
		{
			if(!blocks[i].isPassable()) //the hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}
		
		return (flag) ? 6 : (offset == blockWidth) ? (x % 6) : (6 - (x % 6));//6-complete success; offset==2-0; otherwise, remaining value to move over
	}
	
	/**
	 * Gets how far left can the entity move (0-6 ortho)
	 * @return the distance that can be moved left (from 0-6 ortho)
	 */
	private float canMoveLeft(World world)
	{
		if(!isAffectedByWalls) //no point in performing a hittest
			return 6;
		
		boolean flag = false;				
		Block[] blocks = new Block[(int) (blockHeight + ((y % 6 == 0) ? 0 : 1))]; //blocks to check

		for(int i = 0; i < blocks.length; i++) //get the blocks to check against
		{
			blocks[i] =  world.getBlock(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)(x / 6) - 1), MathHelper.returnIntegerInWorldMapBounds_Y(world, ((int)(y / 6) + i)));
		}
		
		for(int i = 0; i < blocks.length; i++)
		{
			if(!blocks[i].isPassable()) //is the block isnt passable, the hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}	

		return (flag) ? 6 : (x % 6); //6-> complete success, otherwise the remaining amount it's possible to move
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

	public EntityProjectile setXLocAndYLoc(float x, float y){
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
		xVel = (float) speed * (Math.cos(r));
		yVel = (float) speed * -(Math.sin(r));
		
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