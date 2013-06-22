package entities;

import utils.Vector2F;
import world.World;

public class EntityParticle extends EntityLiving
{
	private static final long serialVersionUID = 1L;
	protected Vector2F velocity;
	protected Vector2F acceleration;
	protected int ticksActive;
	public boolean active;
	
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
}