package enums;

import items.Item;
import items.ItemArmor;

import java.util.EnumSet;
import java.util.Vector;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusCriticalStrike;
import passivebonuses.PassiveBonusDefense;
import passivebonuses.PassiveBonusIntellect;
import passivebonuses.PassiveBonusManaBoost;
import passivebonuses.PassiveBonusSpeed;
import utils.ItemStack;
import entities.EntityPlayer;

/**
 * EnumArmor defines the armour values given by a specific grade of equipment. These values are 
 * available for any piece of the Armour set (the helmet, chestpiece, and pants, boots, belts, and gloves). 
 * Defense values are generally: Chestpiece > helmet >= pants > boots >= gloves >= belt.
 * <br><br>
 * V1.1: EnumArmor now contains PassiveBonuses for a given grade of material.
 * Ex. Gold has (2): +1Defense; (3): +2Defense; (4): +2Defense. Total = +5Defense
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public enum EnumArmor 
{
	NOTHING(0, 0, 0, 0, 0, 0, new PassiveBonus[]{ }),
	COPPER(2, 3, 2, 1, 1, 1, new PassiveBonus[]{
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(3),
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(6)
		}),
	BRONZE(2, 4, 3, 1, 1, 1, new PassiveBonus[]{ 
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(2), 
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(4),
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(6)
		}),
	IRON(3, 5, 4, 2, 2, 2, new PassiveBonus[]{ 
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(2),
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(4),
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(6)
		}),
	SILVER(4, 6, 5, 3, 3, 3, new PassiveBonus[]{ 
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(2),
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(4),
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(6),
		}),
	GOLD(5, 7, 5, 4, 4, 4, new PassiveBonus[]{ 
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(2),
			new PassiveBonusDefense(3).setPiecesRequiredToActivate(4),
			new PassiveBonusSpeed(0.1).setPiecesRequiredToActivate(6)
		}),
	LUNARIUM(4, 6, 5, 3, 3, 3, new PassiveBonus[]{ 
			new PassiveBonusSpeed(.1).setPiecesRequiredToActivate(2),
			new PassiveBonusManaBoost(20).setPiecesRequiredToActivate(4),
			new PassiveBonusIntellect(10).setPiecesRequiredToActivate(6)
		}),
	TIER6(7, 9, 7, 5, 5, 5, new PassiveBonus[]{ 
			new PassiveBonusSpeed(.4).setPiecesRequiredToActivate(2),
			new PassiveBonusDefense(200).setPiecesRequiredToActivate(4),
			new PassiveBonusIntellect(100).setPiecesRequiredToActivate(6)
		});

	private final int helmDefense;
	private final int bodyDefense;
	private final int pantsDefense;
	private final int bootsDefense;
	private final int glovesDefense;
	private final int beltDefense;
	private PassiveBonus[] bonuses;
	private static Vector<EnumArmor> armorTiers;
	static
	{
		//Add all the EnumArmor to the armorTiers Vector to automatically update the PassiveBonusFactory and other classes
		//of a change to EnumArmor
		armorTiers = new Vector<EnumArmor>();
		for (EnumArmor tier: EnumSet.allOf(EnumArmor.class))
        {
			armorTiers.add(tier);
        }
	}
	
	EnumArmor(int helmDef, int bodyDef, int pantsDef, int bootsDefense, int glovesDefense, int beltDefense, PassiveBonus[] bonuses)
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
	
	public PassiveBonus[] getBonuses()
	{
		return bonuses;
	}
	
	/**
	 * Gets the active PassiveBonuses for a given tier of armour, based on the number of pieces equipped.
	 * All PassiveBonuses which require less than or equal to that number of armour pieces equipped will 
	 * be returned.
	 * @param piecesEquipped the number of pieces from this set the player has equipped
	 * @return the PassiveBonuses that are active, based on piecesEquipped
	 */
	public PassiveBonus[] getBonuses(int piecesEquipped)
	{
		Vector<PassiveBonus> bonusVect = new Vector<PassiveBonus>();
		for(PassiveBonus set : bonuses)
		{
			if(set.getPiecesRequiredToActivate() <= piecesEquipped)
			{
				bonusVect.add(set);
			}
		}
		PassiveBonus[] bonus = new PassiveBonus[bonusVect.size()];
		bonusVect.copyInto(bonus);
		return bonus;
	}
	
	/**
	 * Gets all the PassiveBonuses for this armour tier in a nicely formatted array of the following general
	 * form: <br>
	 * (2): BONUS_NAME
	 * @return the PassiveBonuses for this armour tier in a well formatted way
	 */
	public String[] getPassiveBonusesAsStringArray()
	{
		String[] values = new String[bonuses.length];
		for(int i = 0; i < values.length; i++)
		{
			values[i] = "(" + bonuses[i].getPiecesRequiredToActivate() + "): " + bonuses[i].toString();
		}		
		return values;
	}
	
	/**
	 * Gets a boolean[] representing what PassiveBonuses from this tier are active. If a bonus is active then
	 * the player has enough pieces equipped for this to be the case and that index of the array will be true.
	 * The boolean[] provided by this is in the same order as the {@link #getPassiveBonusesAsStringArray()} method
	 * provides.
	 * @return a boolean[] indicating which bonuses from this tier are active
	 */
	public boolean[] getBonusesActivated(EntityPlayer player)
	{
		boolean[] activeBonuses = new boolean[bonuses.length];
		
		//Count the pieces of that tier active
		int piecesEquipped = 0;
		for(ItemStack stack: player.inventory.getArmorInventory())
		{
			if(stack != null && ((ItemArmor)(Item.itemsList[stack.getItemID()])).getArmorType() == this)
			{
				piecesEquipped++;
			}
		}		
		for(int i = 0; i < bonuses.length; i++)
		{	
			if(bonuses[i].getPiecesRequiredToActivate() <= piecesEquipped)
			{
				activeBonuses[i] = true;
			}
			else
			{
				activeBonuses[i] = false;
			}
		}		
		return activeBonuses;
	}
}
