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
	protected int textureWidth;
	protected int textureHeight;
	public double width;
	public double height;
	public double blockWidth;
	public double blockHeight;
		
	/**
	 * Constructs a new instance of Entity, with some basic and generic values.
	 */
	public Entity()
	{
		x = 0;
		y = 0;
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
		this.x = entity.x;
		this.y = entity.y;
		this.textureWidth = entity.textureWidth;
		this.textureHeight = entity.textureHeight;
		this.width = entity.width;
		this.height = entity.height;
		this.blockWidth = entity.blockWidth;
		this.blockHeight = entity.blockHeight;
		entityID = -1; //TODO: Client-side this is an issue ==> ENTITY_ID
	}
	
	public Entity setEntityID(int id)
	{
		this.entityID = id;
		return this;
	}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
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

}
