package entities;

import java.io.Serializable;


public class DisplayableEntity 
		implements Serializable, IEntityTransmitBase
{
	private static final long serialVersionUID = 1L;

	public static final int TYPE_ENEMY = 1, 
			TYPE_FRIENDLY = 2,
			TYPE_ITEMSTACK = 3,
			TYPE_PROJECTILE = 4,
			TYPE_PLAYER = 5;
	
	public short type;
	public int iconX;
	public int iconY;
	protected String name;//debug feature
	public String jumpSound;
	public String deathSound;
	public String hitSound;
	public int entityID;	
	/** The x position of the entity in pixels. */
	public double x;
	/** The y position of the entity in pixels. */
	public double y;
	public int textureWidth;
	public int textureHeight;
	public double width;
	public double height;	
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public DisplayableEntity setSpriteIndex(int x, int y) {
		iconX = x;
		iconY = y;
		return this;
	}
	
	public DisplayableEntity setXLoc(int x){
		this.x = x;
		return this;
	}
	
	public DisplayableEntity setYLoc(int y){
		this.y = y;
		return this;
	}

	public DisplayableEntity setXLocAndYLoc(double x, double y){
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public int getEntityID() {
		return entityID;
	}

	@Override
	public int getEntityType() {
		return type;
	}

	@Override
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		
	}
}
