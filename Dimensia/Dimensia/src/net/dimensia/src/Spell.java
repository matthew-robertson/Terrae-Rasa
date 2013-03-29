package net.dimensia.src;

/**
 * <code>Spell extends ActionbarItem</code>
 * <br><br>
 * 
 * Spell is the base class for all Spells. A new spell can be constructed using {@link #Spell(int)} and 
 * should be created in this class similarly to Items or Blocks. 
 * 
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public class Spell extends ActionbarItem
{
	private static final long serialVersionUID = 1L;
	protected int manaCost;
	
	public Spell(int i)
	{
		super(i);
		this.id = i + spellIndex;
		
		if(spellList[id] != null)
		{
			System.out.println(new StringBuilder().append("Conflict@ itemsList").append(id).toString());
			throw new RuntimeException(new StringBuilder().append("Conflict@ itemsList").append(id).toString());
		}
		spellList[id] = this;		
		
	}
	
	public Spell(Spell spell)
	{
		super(spell);
		this.manaCost = spell.manaCost;
	}

	protected Spell setManaCost(int cost)
	{
		manaCost = cost;
		return this;
	}
	
	public int getManaCost()
	{
		return manaCost;
	}
	
	public final static Spell[] spellList = new Spell[spellIndex + 2096];
	
}
