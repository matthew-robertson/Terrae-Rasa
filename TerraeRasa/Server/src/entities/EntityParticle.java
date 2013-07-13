package entities;

import utils.MathHelper;
import utils.Vector2F;
import world.World;

public class EntityParticle extends EntityLiving
{
	private static final long serialVersionUID = 1L;

	protected Vector2F velocity;
	protected Vector2F acceleration;
	protected int ticksActive;
	public boolean active;
	
	protected EntityParticle()
	{
		super();
		velocity = new Vector2F(0, 0);
		acceleration = new Vector2F(0, 0);
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
		velocity = new Vector2F(0, 0);
		acceleration = new Vector2F(0, 0);
	}
	
	public EntityParticle(EntityParticle entity)
	{
		super(entity);
		this.velocity = new Vector2F(entity.velocity);
		this.acceleration = new Vector2F(entity.acceleration);
		this.ticksActive = entity.ticksActive;
		this.active = entity.active;
	}
	
	/**
	 * Applies dampening, then adds the acceleration vector to the velocity, then updated position.
	 * This is designed to move the particle throughout the world on each tick.
	 */
	public void integrate(World world)
	{
		velocity = velocity.addF(acceleration);
		
		this.x += velocity.getX();
		this.y += velocity.getY();
				
		if (active){
			ticksActive++;
		}
	}

	public Vector2F getVelocity()
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
	
	public Vector2F getAcceleration()
	{
		return acceleration;
	}
	
	public EntityParticle setVelocity(Vector2F vel)
	{
		velocity = vel;
		return this;
	}
	
	public EntityParticle setAcceleration(Vector2F acc)
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
			double verticalVelocity = MathHelper.getVf(velocity.getY(), acceleration.getY() - world.g, ticksActive);
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