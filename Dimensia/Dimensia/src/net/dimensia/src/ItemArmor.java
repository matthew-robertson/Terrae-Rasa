package net.dimensia.src;

import java.util.ArrayList;
import java.util.List;

public class ItemArmor extends Item
{
	protected boolean isSavingRelic;
	protected EnumSetBonuses[] bonuses;
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
		bonuses = new EnumSetBonuses[0];
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
	
	protected ItemArmor setBonuses(EnumSetBonuses[] bonuses)
	{
		this.bonuses = bonuses;
		
		for(int i = 0; i < bonuses.length; i++)
		{
			if(bonuses[i] == EnumSetBonuses.HEAVENS_REPRIEVE)
			{
				isSavingRelic = true;
				break;
			}
		}
		
		return this;
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
	
	public EnumSetBonuses[] getBonuses()
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
	
	public String[] getStringBonuses()
	{
		String[] strBonuses = new String[bonuses.length];
		for(int i = 0; i < strBonuses.length; i++)
		{
			strBonuses[i] = bonuses[i].toString();
		}
		return strBonuses;
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
}