package entities;
import java.io.Serializable;

/**
 * This class is a base class for Entities, and contains some basic information and methods. For a more complete entity, Entity
 * should be extended, and given custom functionality/methods/fields. This class contains fields for:
 * <br><br>
 * <li>Position (x, y)
 * <li>Jumping and Gravity
 * <br><br>
 * And Methods for:
 * <br><br>
 * <li>Setting the entity's position
 * <li>Whether the entity is affected by walls or gravity
 * <li>Getting the entity's x or y position
 * <li>Getting whether the entity is affected by walls or gravity
 * <br><br>
 * Implements: Serializable
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Entity implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected boolean isAffectedByWalls;
	protected boolean isAffectedByGravity;
	protected boolean canJumpAgain; //Can the entity jump?
	protected boolean isJumping;	
	private float upwardJumpHeight; //Determines the jump distance
	protected float jumpSpeed;
	public float x;
	public float y;
	protected float fallSpeed;
	protected float upwardJumpCounter; //Holds the value of the jump, reset when hitting the ground	
	
	public Entity()
	{
		isAffectedByWalls = true;
		isAffectedByGravity = true;		
	}	
	
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
		this.fallSpeed = entity.fallSpeed;
		this.upwardJumpCounter = entity.upwardJumpCounter; 
	}
	
	public Entity setPosition(int i, int j)
	{
		this.x = i;
		this.y = j;
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
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}

	public float getUpwardJumpHeight() 
	{
		return upwardJumpHeight;
	}

	public Entity setUpwardJumpHeight(float upwardJumpHeight)
	{
		this.upwardJumpHeight = upwardJumpHeight;
		return this;
	}
	
	public Entity setJumpSpeed(float speed)
	{
		this.jumpSpeed = speed;
		return this;
	}
}
