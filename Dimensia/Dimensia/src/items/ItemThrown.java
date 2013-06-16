package items;

public class ItemThrown extends Item
{
	protected int cooldownTicks;
	
	protected ItemThrown(int i, int d) 
	{
		super(i);
		damage = d;
		cooldownTicks = 5;
	}

	public ItemThrown setCooldownTicks(int i){
		cooldownTicks = i;
		return this;
	}
		
	public ItemThrown setdamage(int d){
		damage = d;
		return this;
	}
	
	public int getCooldownTicks(){
		return cooldownTicks;
	}
}
