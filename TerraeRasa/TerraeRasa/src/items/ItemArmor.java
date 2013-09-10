package items;

import java.util.ArrayList;
import java.util.List;

import passivebonuses.DisplayablePassiveBonus;
import auras.DisplayableAura;
import enums.EnumArmor;

public class ItemArmor extends Item
{
//	protected boolean isSavingRelic;
	protected DisplayablePassiveBonus[] bonuses;
	protected DisplayableAura[] auras;
	protected EnumArmor armorType;
	protected int defense;
	protected int dexterity;
	protected int strength;
	protected int intellect;
	protected int stamina;
	
	protected ItemArmor(int i) 
	{
		super(i);
		maxStackSize = 1;
		intellect = 0;
		stamina = 0;
		dexterity = 0;
		strength = 0;
		armorType = EnumArmor.NOTHING;
		bonuses = new DisplayablePassiveBonus[0];
		auras = new DisplayableAura[0];
		totalSockets = 0;
	}
	
	protected ItemArmor setArmorType(EnumArmor type)
	{
		armorType = type;
		if(this instanceof ItemArmorHelmet)
		{
			defense = armorType.getHelmetDefense();
		}
		else if(this instanceof ItemArmorPants)
		{
			defense = armorType.getGreavesDefense();
		}
		else if(this instanceof ItemArmorBody)
		{
			defense = armorType.getBodyDefense();
		}
		else if(this instanceof ItemArmorBoots)
		{
			defense = armorType.getBootsDefense();
		}
		else if(this instanceof ItemArmorGloves)
		{
			defense = armorType.getGlovesDefense();
		}
		else if(this instanceof ItemArmorBelt)
		{
			defense = armorType.getBeltDefense();
		}
		return this;
	}
	
	protected ItemArmor setIntellect(int i)
	{
		intellect = i;
		return this;
	}
	
	protected ItemArmor setStrength(int i)
	{
		strength = i;
		return this;
	}
	
	protected ItemArmor setDexterity(int i)
	{
		dexterity = i;
		return this;
	}
	
	public int getIntellect()
	{
		return intellect;
	}
	
	public int getStrength()
	{
		return strength;
	}
	
	public int getDexterity()
	{
		return dexterity;
	}
	
	public EnumArmor getArmorType()
	{
		return armorType;
	}
	
	protected ItemArmor passiveBonuses(DisplayablePassiveBonus[] bonuses)
	{
		this.bonuses = bonuses;
		return this;
	}
	
	protected ItemArmor setAuras(DisplayableAura[] auras)
	{
		this.auras = auras;
		return this;
	}
	
	public DisplayableAura[] getAuras()
	{
		return auras;
	}
	
	public int getDefense()
	{
		return defense;
	}
	
	protected ItemArmor setDefense(int i)
	{
		defense = i;
		return this;
	}
	
	public DisplayablePassiveBonus[] getBonuses()
	{
		return bonuses;
	}

	public ItemArmor setStamina(int i)
	{
		this.stamina = i;
		return this;
	}
	
	public int getStamina()
	{
		return stamina;
	}
	
	/**
	 * Converts all the set bonuses for this piece of armour, then armour set bonuses, then auras into a string 
	 * array. Useful for tooltips and visualization.
	 * @return this armour piece's PassiveBonuses, tier bonuses, and auras as a String[]
	 */
	public String[] getStringBonuses()
	{
//		String[] PassiveBonuses = armorType.getPassiveBonusesAsStringArray();
		String[] allBonuses = new String[bonuses.length + 0 + auras.length];
		int i = 0;
//		for(i = 0; i < PassiveBonuses.length; i++)
//		{
//			allBonuses[i] = PassiveBonuses[i].toString();
//		}
		for(i = 0; i < bonuses.length; i++)
		{
			allBonuses[0 + i] = bonuses[i].toString();
		}
		for(i = 0; i < auras.length; i++)
		{
			allBonuses[bonuses.length + 0 + i] = auras[i].toString();
		}
		return allBonuses;
	}
	
	/**
	 * Gets the stats of the given item. Basic Items shouldn't have stats, but other Items such as extensions of 
	 * ItemTool or ItemArmor can override this method and return an actual array of values.
	 * @return an array of this Item's stats, this should be of size 0
	 */
	public String[] getStats()
	{
		List<String> stats = new ArrayList<String>();
		
//		if(defense > 0)
//		{
//			stats.add("" + defense + "  Armor");
//		}
		if(dexterity > 0)
		{
			stats.add("+" + dexterity + " Dexterity");
		}
		if(strength > 0)
		{
			stats.add("+" + strength + " Strength");	
		}
		if(intellect > 0)
		{
			stats.add("+" + intellect + " Intellect");			
		}
		if(stamina > 0)
		{
			stats.add("+" + stamina + " Stamina");
		}
		
		String[] strings = new String[stats.size()];
		for(int i = 0; i < strings.length; i++)
		{
			strings[i] = stats.get(i);
		}
		return strings;
	}
	
	public DisplayablePassiveBonus[] getTierBonuses()
	{
		return armorType.getBonuses();
	}
	
	public String getTierName()
	{
		return armorType.getSetName();
	}
	
	protected ItemArmor setTotalSockets(int total)
	{
		this.totalSockets = total;
		return this;
	}
}