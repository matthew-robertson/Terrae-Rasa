package net.dimensia.src;

public class ItemPlacable extends Item{

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
	
	protected boolean direction;
}