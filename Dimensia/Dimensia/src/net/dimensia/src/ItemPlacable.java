package net.dimensia.src;

public class ItemPlacable extends Item
{
	protected boolean direction;
	
	protected ItemPlacable(int i, boolean flag) {
		super(i);
		this.direction = flag;
	}
	
	protected Item setDirection(boolean flag){
		direction = flag;
		return this;
	}
	
	public boolean getDirection(){
		return this.direction;
	}
}