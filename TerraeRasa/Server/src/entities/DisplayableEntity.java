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
	public double x;
	public double y;
	public int textureWidth;
	public int textureHeight;
	public double width;
	public double height;	
	
	private DisplayableEntity()
	{
	}
	
	public DisplayableEntity(EntityProjectile entity)
	{
		type = TYPE_PROJECTILE;
		iconX = entity.iconX;
		iconY = entity.iconY;
		name = entity.toString();
		jumpSound = "";
		deathSound = "";
		hitSound = "";
		entityID = entity.entityID;
		x = entity.x;
		y = entity.y;
		textureWidth = entity.textureWidth;
		textureHeight = entity.textureHeight;
		width = entity.width;
		height = entity.height;
	}
	
	public DisplayableEntity(EntityNPCEnemy entity)
	{
		type = TYPE_ENEMY;
		
		iconX = entity.iconX;
		iconY = entity.iconY;
		name = entity.getName();
		jumpSound = entity.jumpSound;
		deathSound = entity.deathSound;
		hitSound = entity.hitSound;
		entityID = entity.entityID;
		x = entity.x;
		y = entity.y;
		textureWidth = entity.textureWidth;
		textureHeight = entity.textureHeight;
		width = entity.width;
		height = entity.height;
	}
	
	public DisplayableEntity(EntityNPC entity)
	{
		type = TYPE_FRIENDLY;
		
		iconX = entity.iconX;
		iconY = entity.iconY;
		name = entity.getName();
		jumpSound = entity.jumpSound;
		deathSound = entity.deathSound;
		hitSound = entity.hitSound;
		entityID = entity.entityID;
		x = entity.x;
		y = entity.y;
		textureWidth = entity.textureWidth;
		textureHeight = entity.textureHeight;
		width = entity.width;
		height = entity.height;
	}
	
	public DisplayableEntity(EntityItemStack entity)
	{
		type = TYPE_ITEMSTACK;
		
		iconX = entity.getStack().getItemID();
		iconY = 0;
		name = entity.getStack().getItemName();
		jumpSound = entity.jumpSound;
		deathSound = entity.deathSound;
		hitSound = entity.hitSound;
		entityID = entity.entityID;
		x = entity.x;
		y = entity.y;
		textureWidth = entity.textureWidth;
		textureHeight = entity.textureHeight;
		width = entity.width;
		height = entity.height;
	}
	
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
