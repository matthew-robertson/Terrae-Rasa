package net.dimensia.src;

import java.util.Vector;

/**
 * <code>Spell extends ActionbarItem</code>
 * <br><br>
 * 
 * This class is a WIP -- proper doc comment not yet done. Will be the baseclass for all spells
 * 
 * 
 */
public class Spell extends ActionbarItem
{
	private static final long serialVersionUID = 1L;

	public Spell(Spell spell)
	{
		super(spell);
		throw new RuntimeException("CPY CSTR @ SPELL NYI");
	}

	protected int manaReq;

	protected Spell setManaReq(int i){
		manaReq = i;
		return this;
	}
	
	public int getManaReq(){
		return manaReq;
	}
	
	/** -- do these belong here? -- pulled from Item*/
	protected Vector<ItemStack> ammo;
	
	public ItemStack[] getAmmoAsArray(){
		ItemStack[] ammunition = new ItemStack[ammo.size()];
		ammo.copyInto(ammunition);
		return ammunition;
	}
}
