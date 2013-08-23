package items;


public class ItemMagic extends ItemRanged 
{
//	EntityProjectile projectile;
	protected int manaReq;
	
	protected ItemMagic(int i, int d, int m) {
		super(i, d);
		manaReq = 1;
		maxStackSize = 1;
//		projectile = p;
	}	
	
	public ItemMagic setManaReq(int i){
		manaReq = i;
		return this;
	}
	
	public int getManaReq(){
		return manaReq;
	}
	
//	public EntityProjectile getProjectile(){
//		return projectile;
//	}
}
