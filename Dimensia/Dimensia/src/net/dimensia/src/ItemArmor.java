package net.dimensia.src;

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
		if(this instanceof ItemArmorGreaves)
		{
			defense = armorType.getGreavesDefense();
		}
		if(this instanceof ItemArmorBody)
		{
			defense = armorType.getBodyDefense();
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
}