package net.dimensia.src;

public class ItemMagic extends ItemRanged {

	protected ItemMagic(int i, int d, int m, EntityProjectile p) {
		super(i, d);
		manaReq = 1;
		maxStackSize = 1;
		projectile = p;
	}
	
}
