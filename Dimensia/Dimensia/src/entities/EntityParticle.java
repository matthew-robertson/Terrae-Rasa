package entities;

import utils.MathHelper;
import utils.Vector2F;
import world.World;
import blocks.Block;

public class EntityParticle extends Entity
{
	private static final long serialVersionUID = 1L;
	protected Vector2F velocity;
	protected Vector2F acceleration;
	protected int ticksActive;
	public int blockWidth;
	public int width;
	public int blockHeight;
	public int height;
	public boolean active;
	
	public EntityParticle(int w, int h){		
		blockWidth = w;
		width = w * 6;
		blockHeight = h;
		height = h * 6;
	}
	
	public EntityParticle(EntityProjectile entity){
		acceleration = new Vector2F(0, +0.3f);
	}
	
	/**
	 * Designed to move the particle throughout the world on each tick.
	 * Applies dampening, then adds the acceleration vector to the velocity, then updated position.
	 */
	public void integrate(World world){
		velocity = velocity.addF(acceleration);
		
		this.x += velocity.getX();
		this.y += velocity.getY();
				
		if (active){
			ticksActive++;
		}
	}
	
	/**
	 * Tries to move the entity right
	 */
	public void moveEntityRight(World world)
	{
		float movementValue = (float) (velocity.x);
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
	 * Tries to move the entity left
	 */
	public void moveEntityLeft(World world) 
	{
		float movementValue = (float) (Math.abs(velocity.x));
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
	
	/**
	 * Gets how far the entity can fall, upto 1 block (use multiple times to go further)
	 * @return the distance it's possible to fall, upto 6 ortho units (1 block)
	 */
	protected float canMoveDown(World world)
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
	protected float canMoveUp(World world)
	{
		if((int)(y / 6) <= 0) //bounds check
		{
			return 6;
		}
		
		Block[] blocks = new Block[(int) (blockWidth + ((x % 6 == 0) ? 0 : 1))]; //blocks to check	
		boolean flag = true;
		
		for(int i = 0; i < blocks.length; i++) //get blocks to check
		{
			blocks[i] = world.getBlock((x / 6) + i, ((y -(float) velocity.y) / 6));
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
		
		if(flag && y - velocity.y >= 0) //Normal upward movement
		{
			return (float) velocity.y;
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
	protected float canMoveRight(World world)
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
	protected float canMoveLeft(World world)
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
	
	public Vector2F getVelocity(){
		return velocity;
	}
	
	public Vector2F getAcceleration(){
		return acceleration;
	}
	
	public EntityParticle setVelocity(Vector2F vel){
		velocity = vel;
		return this;
	}
	
	public EntityParticle setAcceleration(Vector2F acc){
		acceleration = acc;
		return this;
	}
}