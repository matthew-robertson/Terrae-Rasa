package items;

import java.util.ArrayList;
import java.util.List;

import passivebonuses.PassiveBonus;
import auras.Aura;
import auras.AuraHeavensReprieve;
import enums.EnumArmor;

public class ItemArmor extends Item
{
	protected boolean isSavingRelic;
	protected PassiveBonus[] bonuses;
	protected Aura[] auras;
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
		bonuses = new PassiveBonus[0];
		auras = new Aura[0];
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
	
	protected ItemArmor PassiveBonuses(PassiveBonus[] bonuses)
	{
		this.bonuses = bonuses;
		return this;
	}
	
	protected ItemArmor setAuras(Aura[] auras)
	{
		this.auras = auras;
		for(Aura aura : auras)
		{
			if(aura instanceof AuraHeavensReprieve)
			{
				isSavingRelic = true;
			}
		}
		return this;
	}
	
	public Aura[] getAuras()
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
	
	public PassiveBonus[] getBonuses()
	{
		return bonuses;
	}
	
	public boolean getIsSavingRelic()
	{
		return isSavingRelic;
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
		String[] PassiveBonuses = armorType.getPassiveBonusesAsStringArray();
		String[] allBonuses = new String[bonuses.length + PassiveBonuses.length + auras.length];
		int i = 0;
		for(i = 0; i < PassiveBonuses.length; i++)
		{
			allBonuses[i] = PassiveBonuses[i].toString();
		}
		for(i = 0; i < bonuses.length; i++)
		{
			allBonuses[PassiveBonuses.length + i] = bonuses[i].toString();
		}
		for(i = 0; i < auras.length; i++)
		{
			allBonuses[bonuses.length + PassiveBonuses.length + i] = auras[i].toString();
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
		
		if(defense > 0)
		{
			stats.add("Defense: " + defense);
		}
		if(dexterity > 0)
		{
			stats.add("Dexterity: " + dexterity);
		}
		if(strength > 0)
		{
			stats.add("Strength: " + strength);	
		}
		if(intellect > 0)
		{
			stats.add("Intellect: " + intellect);			
		}
		if(stamina > 0)
		{
			stats.add("Stamina: " + stamina);
		}
		
		String[] strings = new String[stats.size()];
		for(int i = 0; i < strings.length; i++)
		{
			strings[i] = stats.get(i);
		}
		return strings;
	}
	
	protected ItemArmor setTotalSockets(int total)
	{
		this.totalSockets = total;
		return this;
	}
}