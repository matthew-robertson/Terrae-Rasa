package entities;

import java.io.Serializable;

import utils.MathHelper;
import world.World;
import blocks.Block;

/**
 * This class is a base class for Entities, and contains some basic information and methods. Any Entity that exists
 * in the world should inherite from Entity. For a more complete entity, Entity should be subclasses, and given 
 * custom functionality. The Entity base class accounts for movement - something all entities will want to
 * do at some point.
 * <br><br>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Entity 
		implements Serializable	 
{
	private static final long serialVersionUID = 1L;

	public int entityID;
	
	/** The x position of the entity in pixels. */
	public double x;
	/** The y position of the entity in pixels. */
	public double y;
	protected boolean isAffectedByWalls;
	protected boolean isAffectedByGravity;
	/**Holds the value of the jump, reset when hitting the ground*/
	protected double upwardJumpCounter; 	
	/** True if the entity is allowed to jump, otherwise false. */
	protected boolean canJumpAgain;
	protected boolean isJumping;	
	/** Determines the upward jump distance in pixels*/
	protected double upwardJumpHeight; 
	protected double jumpSpeed;	
	protected boolean isStunned;
	protected int ticksFallen;
	protected int textureWidth;
	protected int textureHeight;
	public double width;
	public double height;
	public double blockWidth;
	public double blockHeight;
	protected double distanceFallen;
	/**The maximum safe fall height in pixels not blocks.*/
	protected double maxHeightFallenSafely;
	protected double baseSpeed;
	protected double movementSpeedModifier;
	
	/**
	 * Constructs a new instance of Entity, with some basic and generic values.
	 */
	public Entity()
	{
		isAffectedByWalls = true;
		isAffectedByGravity = true;		
		setMovementSpeedModifier(1.0f);
		setBaseSpeed(1.0f);
		distanceFallen = 0;
		maxHeightFallenSafely = 78;
		x = 0;
		y = 0;
		upwardJumpCounter = 0;
		canJumpAgain = true;
		isJumping = false;
		upwardJumpHeight = 42;
		jumpSpeed = 5;
		isStunned = false;
		ticksFallen = 0;
		textureWidth = 16;
		textureHeight = 16;
		blockWidth = 2;
		blockHeight = 3;		
		entityID = -1; //TODO: Client-side this is an issue ==> ENTITY_ID
	}	
		
	/**
	 * Creates a deep copy of this Entity
	 * @param entity the entity to copy
	 */
	public Entity(Entity entity)
	{
		this.isAffectedByWalls = entity.isAffectedByWalls;
		this.isAffectedByGravity = entity.isAffectedByGravity;
		this.canJumpAgain = entity.canJumpAgain; 
		this.isJumping = entity.isJumping;	
		this.setUpwardJumpHeight(entity.getUpwardJumpHeight()); 
		this.jumpSpeed = entity.jumpSpeed;
		this.x = entity.x;
		this.y = entity.y;
		this.setStunned(entity.isStunned());
		this.upwardJumpCounter = entity.upwardJumpCounter; 
		this.textureWidth = entity.textureWidth;
		this.textureHeight = entity.textureHeight;
		this.width = entity.width;
		this.height = entity.height;
		this.blockWidth = entity.blockWidth;
		this.blockHeight = entity.blockHeight;
		this.distanceFallen = entity.distanceFallen;
		this.setMaxHeightFallenSafely(entity.getMaxHeightFallenSafely());
		this.setBaseSpeed(entity.getBaseSpeed());
		this.setMovementSpeedModifier(entity.getMovementSpeedModifier());
		this.ticksFallen = entity.ticksFallen;
		entityID = -1; //TODO: Client-side this is an issue ==> ENTITY_ID
	}
	
	public Entity setEntityID(int id)
	{
		this.entityID = id;
		return this;
	}
	
	public Entity setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Entity setAffectedByGravity(boolean flag)
	{
		isAffectedByGravity = flag;
		return this;
	}
	
	public Entity setAffectedByWalls(boolean flag)
	{
		isAffectedByWalls = flag;
		return this;
	}
	
	public boolean getAffectedByGravity()
	{
		return isAffectedByGravity;
	}
	
	public boolean getAffectedByWalls()
	{
		return isAffectedByWalls;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}

	public double getUpwardJumpHeight() 
	{
		return upwardJumpHeight;
	}

	public boolean isStunned() 
	{
		return isStunned;
	}
	
	public void setStunned(boolean isStunned) 
	{
		this.isStunned = isStunned;
	}

	public double getMaxHeightFallenSafely() 
	{
		return maxHeightFallenSafely;
	}

	public void setMaxHeightFallenSafely(double maxHeightFallenSafely) 
	{
		this.maxHeightFallenSafely = maxHeightFallenSafely;
	}	
	
	public double getBaseSpeed() 
	{
		return baseSpeed;
	}
	
	public Entity setBaseSpeed(double baseSpeed) 
	{
		this.baseSpeed = baseSpeed;
		return this;
	}

	public double getMovementSpeedModifier() 
	{
		return movementSpeedModifier;
	}

	public void setMovementSpeedModifier(double movementSpeedModifier) 
	{
		this.movementSpeedModifier = movementSpeedModifier;
	}
	
	public Entity setUpwardJumpHeight(double upwardJumpHeight)
	{
		this.upwardJumpHeight = upwardJumpHeight;
		return this;
	}
	
	public Entity setJumpSpeed(double speed)
	{
		this.jumpSpeed = speed;
		return this;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getTextureWidth()
	{
		return textureWidth;
	}
	
	public double getTextureHeight()
	{
		return textureHeight;
	}
	
	public double getBlockWidth()
	{
		return blockWidth;
	}
	
	public double getBlockHeight()
	{
		return blockHeight;
	}
	
	/**
	 * Applies gravity or a jump upward, depending on if the entity is jumping
	 */
	public void applyGravity(World world) 
	{
		if(isOnGround(world)) //Is the entity on the ground? If so they can jump again
		{						
			ticksFallen = 0;
			canJumpAgain = true;
			distanceFallen = 0;
		}	
				
		if(isJumping) //If the entity is jumping upwards, move them up
		{
			moveEntityUp(world, jumpSpeed * getMovementSpeedModifier());			
		}
		else if(!isOnGround(world) && isAffectedByGravity) //otherwise, if the entity is in the air, make them fall
		{
			moveEntityDown(world, MathHelper.getFallSpeed(jumpSpeed, world.g, ticksFallen));
			ticksFallen++;
		}	
	}
	
	/**
	 * Sees if it's possible to jump. If so then the jump will be performed
	 */
	public void tryToJumpAgain()
	{
		if(canJumpAgain)// If the entity can jump, let them
		{
			isJumping = true;
			upwardJumpCounter = 0;
			canJumpAgain = false;
		}	
	}
	
	/**
	 * Returns a string - "left" or "right" - indicating the direction of this entity relative to the given quad.
	 * @param x the x position of the quad's left top corner
	 * @param y the y position of the quad's left top corner 
	 * @param width the width of the quad
	 * @param height the height of the quad
	 * @return the direction of the quad relative to this entity in string form ("left" or "right")
	 */
	public String getDirectionOfQuadRelativeToEntityPosition(double x, double y, double width, double height)
	{
		if((x + (width * .5)) < this.x)
			return "left";
		if((x + (width * .5)) >= this.x)
			return "right";
		return "left";
	}
	
	/**
	 * Determines whether the entity standing on something solid
	 * @return true if the entity is standing on something solid, otherwise false
	 */
	public boolean isOnGround(World world)		
	{
		int xOffset = (int) ((x % 6 == 0) ? blockWidth : (blockWidth + 1));
		try
		{
			for(int i = 0; i < xOffset; i++) //for each block below the entity, check if any are solid
			{
				if(world.getBlock((int)(x / 6) + i, (int)((y / 6) + Math.ceil(blockHeight))).isSolid)
				{
					return true; //if one is solid, they entity is on the ground
				}
			}
			return false;
		}
		catch(Exception e) //if there's an out of bounds error, assume the player is standing on something solid
		{
			e.printStackTrace();
			return true;
		}
	}
	
	/**
	 * Applies a jump to the entity's Y Position. Disables jumping if (y < 0) or the upwardJumpCounter 
	 * exceeds the upwardJumpHeight.
	 * @param movementValue the distance to move up, if possible
	 */
	public void moveEntityUp(World world, double movementValue)
	{		
		int loops = (int) (movementValue / 6) + 1;
		
		if(isStunned())
		{
			isJumping = false;
			return;
		}
		
		for(int i = 0; i < loops; i++)
		{
			double f = canMoveUp(world);
			
			if((y - f) >= 0 && f >= movementValue) //full movement
			{
				upwardJumpCounter += f;
				y -= f;
			}
			else if(f > 0) //partial movement
			{
				upwardJumpCounter += f;
				y -= f;
			}
			
			if(upwardJumpCounter >= getUpwardJumpHeight()) //If the player has exceeded the jump height, stop jumping
			{
				if(upwardJumpCounter > getUpwardJumpHeight())
				{
					y += upwardJumpCounter - getUpwardJumpHeight();
				}
				isJumping = false;
			}	
			
			if(y < 0) 
			{
				y = 0;
				isJumping = false;
				break;
			}
			if(y > (world.getHeight() * 6) - height - 6)
			{
				y = (world.getHeight() * 6) - height - 6;
				break;
			}
			
			movementValue -= f;
		}
	}
		
	/**
	 * Tries to move the entity down by the specified amount.
	 * @param movementValue the distance to move down, if possible
	 */
	public void moveEntityDown(World world, double movementValue)  
	{		
		int loops = (int) (movementValue / 6) + 1;
		
		for(int i = 0; i < loops; i++)
		{
			double f = canMoveDown(world);
			
			if(y + f < (world.getHeight() * 6) && f >= movementValue) //full movement
			{
				distanceFallen += f;
				y += f;
			}
			else if(f > 0) //partial movement
			{
				distanceFallen += f;
				y += f;
			}
			
			if(y > (world.getHeight() * 6) - height - 6)
			{
				y = (world.getHeight() * 6) - height - 6;
				break;
			}

			movementValue -= f;
		}
	}
	
	/**
	 * Tries to move the entity right. This version of the method uses the entity's base speed and 
	 * movement speed modifier to determine how far to move.
	 */
	public void moveEntityRight(World world)
	{
		double movementValue = MathHelper.roundDowndouble20th(getBaseSpeed() * getMovementSpeedModifier());
		int loops = (int) (movementValue / 6) + 1;
		
		if(isStunned())
		{
			return;
		}
		
		for(int i = 0; i < loops; i++)
		{
			double possibleMovement = canMoveRight(world);
			double actualMovement = (movementValue > 6) ? 6 : movementValue;
			
			if(actualMovement > possibleMovement)
			{
				actualMovement = possibleMovement;
			}
			
			if(x + possibleMovement < (world.getWidth() * 6) && possibleMovement >= movementValue) //full movement
				x += actualMovement;
			else if(possibleMovement > 0) //partial movement
				x += actualMovement;
			
			if(x > world.getWidth() * 6 - width - 6) //bounds check
				x = (world.getWidth() * 6) - width - 6;

			movementValue -= actualMovement;
		}
	}
	
	/**
	 * Tries to move the entity left. This version of the method uses the entity's base speed and 
	 * movement speed modifier to determine how far to move.
	 */
	public void moveEntityLeft(World world) 
	{
		double movementValue = (int)(getBaseSpeed() * getMovementSpeedModifier());
		int loops = (int) (movementValue / 6) + 1;
		
		if(isStunned())
		{
			return;
		}
		
		for(int i = 0; i < loops; i++)
		{
			double possibleMovement = canMoveLeft(world);
			double actualMovement = (movementValue > 6) ? 6 : movementValue;
						
			if(actualMovement > possibleMovement)
			{
				actualMovement = possibleMovement;
			}
			
			x -= actualMovement;
			
			
			if(x < 0)//out of bounds check
				x = 0;
			
			movementValue -= actualMovement;
		}
	}
	
	/**
	 * Tries to move the entity right. This version of the method dictates how far to move right.
 	 * Basespeed is NOT Applied!
	 * @param movementValue the distance to move right, if possible
	 */
	public void moveEntityRight(World world, double movementValue)
	{
		int loops = (int) (movementValue / 6) + 1;
		
		if(isStunned())
		{
			return;
		}
		
		for(int i = 0; i < loops; i++)
		{
			double possibleMovement = canMoveRight(world);
			double actualMovement = (movementValue > 6) ? 6 : movementValue;

			if(actualMovement > possibleMovement)
			{
				actualMovement = possibleMovement;
			}
			
			x += actualMovement;
			
			if(x > world.getWidth() * 6 - width - 6) //bounds check
				x = (world.getWidth() * 6) - width - 6;

			movementValue -= actualMovement;
		}
	}
	
	/**
	 * Tries to move the entity left. This version of the method dictates how far to move left. 
	 * Basespeed is NOT Applied!
	 * @param movementValue the distance to move left, it possible
	 */
	public void moveEntityLeft(World world, double movementValue) 
	{
		int loops = (int) (movementValue / 6) + 1;
		
		if(isStunned())
		{
			return;
		}
		
		for(int i = 0; i < loops; i++)
		{
			double possibleMovement = canMoveLeft(world);
			double actualMovement = (movementValue > 6) ? 6 : movementValue;

			if(actualMovement > possibleMovement)
			{
				actualMovement = possibleMovement;
			}
			
			x -= actualMovement;
			
			if(x < 0)//out of bounds check
				x = 0;
			
			movementValue -= actualMovement;
		}
	}
	
	/**
	 * Gets how far up the entity can move, upto 1 block (6 ortho units). Use multiple times to go further. 
	 * @return how far the entity can move, upto 6 ortho units (1 block)
	 */
	protected double canMoveUp(World world)
	{
		if((int)(y / 6) <= 0) //bounds check
		{
			isJumping = false;
			return 6;
		}
		
		Block[] blocks = new Block[(int) (blockWidth + ((x % 6 == 0) ? 0 : 1))]; //blocks to check	
		boolean flag = true;
		
		for(int i = 0; i < blocks.length; i++) //get blocks to check
		{
			blocks[i] = world.getBlockGenerate((x / 6) + i, ((y - jumpSpeed) / 6));
		}
		
		for(int i = 0; i < blocks.length; i++)
		{
			if(blocks[i].getIsSolid()) //hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}
		
		if(flag && y - jumpSpeed >= 0) //Normal upward movement
		{
			return jumpSpeed;
		}
		else if(y % 6 != 0) //Partial jump
		{
			isJumping = false;
			return y % 6;
		}
		else //Anything else means the player cant jump anymore
		{
			isJumping = false;
		}

		return 0.0f;
	}
	
	/**
	 * Gets how far right can the entity move (0-6 ortho)
	 * @return the distance that can be moved right (from 0-6 ortho)
	 */
	protected double canMoveRight(World world)
	{
		if(!isAffectedByWalls) //no point in performing a hittest
			return 6;
		
		boolean flag = false;
		int offset = (int) ((x % 6 == 0) ? blockWidth : (blockWidth + 1)); //How far right are the blocks for the hit test, if the player is perfectly on a block 2, otherwise 3		
		Block[] blocks = new Block[(int) (blockHeight + ((y % 6 == 0) ? 0 : 1))]; //blocks to check
		
		for(int i = 0; i < blocks.length; i++) //get the blocks to check against
		{
			blocks[i] = world.getBlockGenerate(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)(x / 6) + offset), MathHelper.returnIntegerInWorldMapBounds_Y(world, (int)(y / 6) + i));
		}
		
		for(int i = 0; i < blocks.length; i++)
		{
			if(blocks[i].getIsSolid()) //the hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}
		
		return (flag) ? 6 : (offset == blockWidth) ? (x % 6) : (6 - (x % 6));
		//6-complete success; offset==2-0; otherwise, remaining value to move over
	}
	
	/**
	 * Gets how far left can the entity move (0-6 ortho)
	 * @return the distance that can be moved left (from 0-6 ortho)
	 */
	protected double canMoveLeft(World world)
	{
		if(!isAffectedByWalls) //no point in performing a hittest
			return 6;
		
		boolean flag = false;				
		Block[] blocks = new Block[(int) (blockHeight + ((y % 6 == 0) ? 0 : 1))]; //blocks to check

		for(int i = 0; i < blocks.length; i++) //get the blocks to check against
		{
			blocks[i] =  world.getBlockGenerate(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)(x / 6) - 1), MathHelper.returnIntegerInWorldMapBounds_Y(world, ((int)(y / 6) + i)));
		}
		
		for(int i = 0; i < blocks.length; i++)
		{
			if(blocks[i].getIsSolid()) //is the block isnt passable, the hittest has failed
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

	/**
	 * Gets how far the entity can fall, upto 1 block (use multiple times to go further)
	 * @return the distance it's possible to fall, upto 6 ortho units (1 block)
	 */
	protected double canMoveDown(World world)
	{
		int offset = (int) ((y % 6 == 0) ? blockHeight : (blockHeight + 1)); 
		
		if((int)(y / 6) + offset > world.getHeight() - 2) //bounds check
		{
			return 0F;
		}
		
		Block[] blocks = new Block[(int) (blockWidth + ((x % 6 == 0) ? 0 : 1))]; //blocks to check
		boolean flag = true;
		
		for(int i = 0; i < blocks.length; i++) //get blocks to check
		{
			blocks[i] = world.getBlockGenerate((x / 6) + i, (int)(y / 6) + offset); 
		}		

		for(int i = 0; i < blocks.length; i++)
		{
			if(blocks[i].getIsSolid()) //hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}
		
		
		if(flag && y < world.getHeight() * 6) //Normal Gravity
		{
			return 6.0F;
		}
		else if(y % 6 != 0) //Near ground partial gravity 
		{
			return (6 - (y % 6));
		}		
		return 0.0f;
	}
	
	/**
	 * Determines if the point is inside the EntityLiving
	 * @param x the x point to test against this entity
	 * @param y the y point to test against this entity
	 * @return whether the point is in bounds or not
	 */
	public boolean inBounds(double x, double y)
	{
		return (x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height);
	}
	
	/**
	 * Determines if a block from the 'world map' is nearby
	 * @param leftOffset how far left the block can be detected
	 * @param rightOffset how far right the block can be detected
	 * @param upOffset how far up the block can be detected
	 * @param downOffset how far down the block can be detected
	 * @param block what block to search for
	 * @return whether the block is nearby or not
	 */	
	public boolean blockInBounds(World world, int leftOffset, int rightOffset, int upOffset, int downOffset, Block block)
	{
		int x = (int)(this.x / 6);
		int y = (int)(this.y / 6);
		try {
			for(int i = leftOffset; i < rightOffset + blockWidth; i++) //for each block horizontally
			{
				for(int j = upOffset; j < downOffset + blockHeight; j++) //and each block vertically
				{
					if(world.getBlock(MathHelper.returnIntegerInWorldMapBounds_X(world, x + i), MathHelper.returnIntegerInWorldMapBounds_Y(world, y + j)).id == block.id)
					{ //see if the block matches the specified block
						return true;				
					}
				}
			}
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Determines if any corner point of the compared quad falls inside this entity's hitbox
	 * @param x x position (in ortho) of the entity to compare against this entity
	 * @param y y position (in ortho) of the entity to compare against this entity
	 * @param w width of the entity to compare against this entity
	 * @param h height of the entity to compare against this entity
	 * @return Whether a point falls in bounds or not
	 */
	public boolean inBounds(double x, double y, double w, double h)
	{
		if ((x >= this.x && x <= this.x + 12 && y >= this.y && y <= this.y + 18) //top left
		|| (x + w >= this.x && x + w <= this.x + 12 && y >= this.y && y <= this.y + 18) //top right
		|| (x + w >= this.x && x + w <= this.x + 12 && y + h >= this.y && y + h <= this.y + 18) //bottom right
		|| (x >= this.x && x <= this.x + 12 && y + h >= this.y && y + h <= this.y + 18)){ //bottom left
			return true;
		}			
		return false;
	}

}
