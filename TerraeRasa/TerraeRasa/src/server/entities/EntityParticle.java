package server.entities;

import math.MathHelper;
import math.Vector2;
import world.World;

public class EntityParticle extends EntityLiving
{
	private static final long serialVersionUID = 1L;

	protected Vector2 velocity;
	protected Vector2 acceleration;
	protected int ticksActive;
	public boolean active;
	
	protected EntityParticle()
	{
		super();
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 0);
	}
	
	/**
	 * Constructs a new EntityParticle of given height and width.
	 * @param w the width of the EntityParticle in blocks
	 * @param h the height of the EntityParticle in blocks
	 */
	public EntityParticle(int w, int h)
	{		
		super();
		blockWidth = w;
		width = w * 6;
		blockHeight = h;
		height = h * 6;
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 0);
	}
	
	public EntityParticle(EntityParticle entity)
	{
		super(entity);
		this.velocity = new Vector2(entity.velocity);
		this.acceleration = new Vector2(entity.acceleration);
		this.ticksActive = entity.ticksActive;
		this.active = entity.active;
	}
	
	/**
	 * Applies dampening, then adds the acceleration vector to the velocity, then updated position.
	 * This is designed to move the particle throughout the world on each tick.
	 */
	public void integrate(World world)
	{
		velocity.addVector(acceleration);
		
		this.x += velocity.getX();
		this.y += velocity.getY();
				
		if (active){
			ticksActive++;
		}
	}

	public Vector2 getVelocity()
	{
		return velocity;
	}
	
	public EntityParticle setWidthAndHeight(int w, int h)
	{
		blockWidth = w;
		width = w * 6;
		blockHeight = h;
		height = h * 6;
		return this;
	}
	
	public Vector2 getAcceleration()
	{
		return acceleration;
	}
	
	public EntityParticle setVelocity(Vector2 vel)
	{
		velocity = vel;
		return this;
	}
	
	public EntityParticle setAcceleration(Vector2 acc)
	{
		acceleration = acc;
		return this;
	}
	
	/**
	 * Moves the particle based on g from the world, and its own acceleration/velocity. Accounts for
	 * blocks in the world.
	 * @param world the world the EntityParticle is a part of
	 */
	public void move(World world)
	{
		if(!isOnGround(world))
		{
			double verticalVelocity = MathHelper.getVf(velocity.getY(), acceleration.getY() - world.getG(), ticksActive);
			double horizontalVelocity = MathHelper.getVf(velocity.getX(), acceleration.getX(), ticksActive); 
			
			if(verticalVelocity > 0)
			{
				moveEntityUp(world, verticalVelocity);			
			}
			else
			{
				moveEntityDown(world, Math.abs(verticalVelocity));
			}
			if(horizontalVelocity > 0)
			{
				moveEntityRight(world, horizontalVelocity);			
			}
			else
			{
				moveEntityLeft(world, Math.abs(horizontalVelocity));
			}
			
			ticksActive++;			
		}
	}



}