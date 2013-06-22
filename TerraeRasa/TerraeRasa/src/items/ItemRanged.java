package items;


public class ItemRanged extends Item
{
	protected int cooldownTicks;
	
	protected ItemRanged(int i, int d) {
		super(i);
		maxStackSize = 1;
		damage = d;
		cooldownTicks = 5;
	}	
	
	public ItemRanged setCooldownTicks(int i){
		cooldownTicks = i;
		return this;
	}
		
	public ItemRanged setdamage(int d){
		damage = d;
		return this;
	}
	
	public int getCooldownTicks(){
		return cooldownTicks;
	}
}
