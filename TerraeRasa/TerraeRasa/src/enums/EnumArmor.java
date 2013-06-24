package enums;

import java.util.EnumSet;
import java.util.Vector;

import setbonus.SetBonus;
import setbonus.SetBonusCriticalStrike;
import setbonus.SetBonusDefense;
import setbonus.SetBonusSpeed;

/**
 * EnumArmor defines the armour values given by a specific grade of equipment. These values are 
 * available for any piece of the Armour set (the helmet, chestpiece, and pants, boots, belts, and gloves). 
 * Defense values are generally: Chestpiece > helmet >= pants > boots >= gloves >= belt.
 * <br><br>
 * V1.1: EnumArmor now contains SetBonuses for a given grade of material.
 * Ex. Gold has (2): +1Defense; (3): +2Defense; (4): +2Defense. Total = +5Defense
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public enum EnumArmor 
{
	NOTHING(0, 0, 0, 0, 0, 0, new SetBonus[]{ }),
	COPPER(2, 3, 2, 1, 1, 1, new SetBonus[]{
			new SetBonusDefense(1).setPiecesRequiredToActivate(3),
			new SetBonusDefense(1).setPiecesRequiredToActivate(6)
		}),
	BRONZE(2, 4, 3, 1, 1, 1, new SetBonus[]{ 
			new SetBonusDefense(1).setPiecesRequiredToActivate(2), 
			new SetBonusDefense(1).setPiecesRequiredToActivate(4),
			new SetBonusDefense(1).setPiecesRequiredToActivate(6)
		}),
	IRON(3, 5, 4, 2, 2, 2, new SetBonus[]{ 
			new SetBonusDefense(1).setPiecesRequiredToActivate(2),
			new SetBonusDefense(1).setPiecesRequiredToActivate(4),
			new SetBonusDefense(2).setPiecesRequiredToActivate(6)
		}),
	SILVER(4, 6, 5, 3, 3, 3, new SetBonus[]{ 
			new SetBonusDefense(1).setPiecesRequiredToActivate(2),
			new SetBonusDefense(2).setPiecesRequiredToActivate(4),
			new SetBonusDefense(2).setPiecesRequiredToActivate(6),
		}),
	GOLD(5, 7, 5, 4, 4, 4, new SetBonus[]{ 
			new SetBonusDefense(2).setPiecesRequiredToActivate(2),
			new SetBonusDefense(3).setPiecesRequiredToActivate(4),
			new SetBonusSpeed(0.1).setPiecesRequiredToActivate(6)
		}),
	TIER6(7, 9, 7, 5, 5, 5, new SetBonus[]{ 
			new SetBonusDefense(4).setPiecesRequiredToActivate(2),
			new SetBonusCriticalStrike(0.1).setPiecesRequiredToActivate(4),
			new SetBonusSpeed(0.4).setPiecesRequiredToActivate(6)
		});

	private final int helmDefense;
	private final int bodyDefense;
	private final int pantsDefense;
	private final int bootsDefense;
	private final int glovesDefense;
	private final int beltDefense;
	private SetBonus[] bonuses;
	private static Vector<EnumArmor> armorTiers;
	static
	{
		//Add all the EnumArmor to the armorTiers Vector to automatically update the SetBonusFactory and other classes
		//of a change to EnumArmor
		armorTiers = new Vector<EnumArmor>();
		for (EnumArmor tier: EnumSet.allOf(EnumArmor.class))
        {
			armorTiers.add(tier);
        }
	}
	
	EnumArmor(int helmDef, int bodyDef, int pantsDef, int bootsDefense, int glovesDefense, int beltDefense, SetBonus[] bonuses)
	{
		this.helmDefense = helmDef;
		this.bodyDefense = bodyDef;
		this.pantsDefense = pantsDef;
		this.bootsDefense = bootsDefense;
		this.glovesDefense = glovesDefense;
		this.beltDefense = beltDefense;
		this.bonuses = bonuses;
	}
	
	/**
	 * Gets all the different tiers of armor from EnumArmor
	 * @return all the tiers of EnumArmor
	 */
	public static EnumArmor[] getTiers()
	{
		EnumArmor[] tiers = new EnumArmor[armorTiers.size()];
		armorTiers.copyInto(tiers);
		return tiers;
	}
	
	public int getHelmetDefense()
	{
		return helmDefense;
	}
	
	public int getBodyDefense()
	{
		return bodyDefense;
	}
	
	public int getGreavesDefense()
	{
		return pantsDefense;
	}
	
	public int getBootsDefense()
	{
		return bootsDefense;
	}
	
	public int getGlovesDefense()
	{
		return glovesDefense;
	}
	
	public int getBeltDefense()
	{
		return beltDefense;
	}
	
	public SetBonus[] getBonuses()
	{
		return bonuses;
	}
	
	/**
	 * Gets the active SetBonuses for a given tier of armour, based on the number of pieces equipped.
	 * All SetBonuses which require less than or equal to that number of armour pieces equipped will 
	 * be returned.
	 * @param piecesEquipped the number of pieces from this set the player has equipped
	 * @return the SetBonuses that are active, based on piecesEquipped
	 */
	public SetBonus[] getBonuses(int piecesEquipped)
	{
		Vector<SetBonus> bonusVect = new Vector<SetBonus>();
		for(SetBonus set : bonuses)
		{
			if(set.getPiecesRequiredToActivate() <= piecesEquipped)
			{
				bonusVect.add(set);
			}
		}
		SetBonus[] bonus = new SetBonus[bonusVect.size()];
		bonusVect.copyInto(bonus);
		return bonus;
	}
	
	public String[] getSetBonusesAsStringArray()
	{
		String[] values = new String[bonuses.length];
		for(int i = 0; i < values.length; i++)
		{
			values[i] = "(" + bonuses[i].getPiecesRequiredToActivate() + "): " + bonuses[i].toString();
		}		
		return values;
	}
}
