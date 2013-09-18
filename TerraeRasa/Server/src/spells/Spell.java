package spells;

import utils.ActionbarItem;

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
	public static final int RESOURCE_MANA = 1, 
							   RESOURCE_SPECIAL = 2;
	protected int cost;
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
	
	protected Spell setResourceType(int type)
	{
		this.resourceType = type;
		return this;
	}

	protected Spell setCost(int cost)
	{
		this.cost = cost;
		return this;
	}
	
	public int getResourceType()
	{
		return resourceType;
	}
	
	public int getManaCost()
	{
		return cost;
	}
	
	protected Spell setExtraTooltipInformation(String info) 
	{
		this.extraTooltipInformation = info;
		return this;
	}
	
	protected Spell setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public final static Spell[] spellList = new Spell[spellIndex + 2096];
	
	public final static Spell rejuvenate = (Spell) new SpellRejuvenate(1).setCost(80).setResourceType(RESOURCE_SPECIAL).setName("Rejuvenate").
			setExtraTooltipInformation("Restores 80% of the player's max health immediately. Costs 80% Special Energy"); 
	public final static Spell bulwark = (Spell) new SpellBulwark(2).setCost(55).setResourceType(RESOURCE_SPECIAL).setName("Bulwark").
			setExtraTooltipInformation("Shields the player for 100% of max health, lasting 8 seconds. Costs 55% Special Energy");

}
