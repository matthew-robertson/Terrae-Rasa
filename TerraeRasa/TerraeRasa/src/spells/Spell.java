package spells;

import utils.ActionbarItem;

/**
 * Spell is the base class for all Spells. A Spell is something that can be put in the inventory and does
 * something when interacted with. A new spell can be constructed using {@link #Spell(int)}.
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public class Spell extends ActionbarItem
{
	/** A resource type constant for a Spell cost - mana or special energy. */
	protected static final int RESOURCE_MANA = 1, 
							   RESOURCE_SPECIAL = 2;
	/** The cost of the spell, in mana or special energy points. */
	protected int cost;
	/** The resource type of this spell, either mana or special. */
	protected int resourceType;
	
	/**
	 * Constructs a new Spell with the given ID. The true ID will be this given ID plus the spellIndex. By default
	 * all new spells cost 1 resource, and use mana as their resource. maxstacksize is also initialized to 1.
	 * @param i the ID of the new spell, a unique value
	 */
	protected Spell(int i)
	{
		super(i);
		this.id = i + spellIndex;
		this.cost = 1;
		this.resourceType = RESOURCE_MANA;
		this.maxStackSize = 1;
		
		if(spellList[id] != null)
		{
			System.out.println(new StringBuilder().append("Conflict@ spellList").append(id).toString());
			throw new RuntimeException(new StringBuilder().append("Conflict@ spellList").append(id).toString());
		}
		spellList[id] = this;				
	}
	
	/**
	 * Overrides ActionbarItem.setMaxStackSize(int) to prevent spells from having a stack size greater than 1.
	 */
	protected Spell setMaxStackSize(int size)
	{
		this.maxStackSize = 1;
		return this;		
	}
	
	/**
	 * Sets the resource type required to cast this spell.
	 * @param type the new resource type for this Spell
	 * @return a reference to this Spell
	 */
	protected Spell setResourceType(int type)
	{
		this.resourceType = type;
		return this;
	}

	/**
	 * Sets the cost of this Spell.
	 * @param cost the new cost of this Spell
	 * @return a reference to this Spell
	 */
	protected Spell setCost(int cost)
	{
		this.cost = cost;
		return this;
	}
	
	/**
	 * Gets the resource type required to cast this spell.
	 * @return the resource type required to cast this spell
	 */
	public int getResourceType()
	{
		return resourceType;
	}
	
	/**
	 * Gets the cost of this spell.
	 * @return the cost of this spell
	 */
	public int getCost()
	{
		return cost;
	}
	
	/**
	 * Overrides ActionbarItem.setExtraTooltipInformation(String) to make tooltip information easier to set.
	 */
	protected Spell setExtraTooltipInformation(String info) 
	{
		this.extraTooltipInformation = info;
		return this;
	}
	
	/**
	 * Overrides ActionbarItem.setName(String) to make setting the name easier.
	 */
	protected Spell setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public final static Spell[] spellList = new Spell[spellIndex + 2096];
	
	public final static Spell rejuvenate = (Spell) new SpellRejuvenate(1);
	public final static Spell bulwark = (Spell) new SpellBulwark(2);
	
}
