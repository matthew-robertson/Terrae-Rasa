package entities;

import utils.Vector2F;
import world.World;

public class EntityParticle extends EntityLiving
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