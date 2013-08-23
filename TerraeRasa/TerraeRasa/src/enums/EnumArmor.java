package enums;

import items.Item;
import items.ItemArmor;

import java.util.EnumSet;
import java.util.Vector;

import passivebonuses.DisplayablePassiveBonus;
import utils.DisplayableItemStack;
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
	NOTHING("None", 0, 0, 0, 0, 0, 0, new DisplayablePassiveBonus[]{ }),
	COPPER("Copper", 1, 2, 1, 1, 1, 1, new DisplayablePassiveBonus[]{
			new DisplayablePassiveBonus("+1 Defense").setPiecesRequiredToActivate(3),
			new DisplayablePassiveBonus("+1 Defense").setPiecesRequiredToActivate(6)
		}),
	BRONZE("Bronze", 2, 3, 2, 1, 1, 1, new DisplayablePassiveBonus[]{ 
			new DisplayablePassiveBonus("+1 Defense").setPiecesRequiredToActivate(3), 
			new DisplayablePassiveBonus("+2 Defense").setPiecesRequiredToActivate(6)
		}),
	IRON("Iron", 2, 4, 3, 1, 2, 1, new DisplayablePassiveBonus[]{ 
			new DisplayablePassiveBonus("+2 Defense").setPiecesRequiredToActivate(3),
			new DisplayablePassiveBonus("+2 Defense").setPiecesRequiredToActivate(6)
		}),
	SILVER("Silver", 3, 4, 3, 2, 2, 2, new DisplayablePassiveBonus[]{ 
			new DisplayablePassiveBonus("+2 Defense").setPiecesRequiredToActivate(3),
			new DisplayablePassiveBonus("+3 Defense").setPiecesRequiredToActivate(6),
		}),
	GOLD("Gold", 4, 5, 4, 2, 3, 2, new DisplayablePassiveBonus[]{ 
			new DisplayablePassiveBonus("+2 Defense").setPiecesRequiredToActivate(2),
			new DisplayablePassiveBonus("+3 Defense").setPiecesRequiredToActivate(4),
			new DisplayablePassiveBonus("+10% Movement Speed").setPiecesRequiredToActivate(6)
		}),
	LUNARIUM("Mage's Grace", 4, 5, 4, 3, 3, 3, new DisplayablePassiveBonus[]{ //'Tier 6 - Mage'
			new DisplayablePassiveBonus("+10% Movement Speed").setPiecesRequiredToActivate(2),
			new DisplayablePassiveBonus("+200 Maximum Mana").setPiecesRequiredToActivate(4),
			new DisplayablePassiveBonus("+5 Intellect").setPiecesRequiredToActivate(6)
		}),
	RANGE_SET_1("Archer's Finesse", 4, 5, 4, 3, 3, 3, new DisplayablePassiveBonus[]{ //'Tier 6 - Archer'
			new DisplayablePassiveBonus("+10% Movement Speed").setPiecesRequiredToActivate(2),
			new DisplayablePassiveBonus("+5% Critical Strike").setPiecesRequiredToActivate(4),
			new DisplayablePassiveBonus("+5 Dexterity").setPiecesRequiredToActivate(6)
		}),
	STRENGTH_SET_1("Warriors's Tact", 4, 5, 4, 3, 3, 3, new DisplayablePassiveBonus[]{ //'Tier 6 - Melee'
			new DisplayablePassiveBonus("+10% Movement Speed").setPiecesRequiredToActivate(2),
			new DisplayablePassiveBonus("+5% Attack Speed").setPiecesRequiredToActivate(4),
			new DisplayablePassiveBonus("+5 Strength").setPiecesRequiredToActivate(6)
		}),		
	ARTEMIS_SET("Spirit of the Hunt", 5, 7, 5, 3, 4, 3, new DisplayablePassiveBonus[]{ //'Tier 7 - Archer'
			//(2): Arrows deal 3 extra fire damage
			//(4): 6% chance to nullify any hit of damage
			//(6): 10% chance for an arrow to deal double damage
		}),
	CELESTIAL_SET("Grace of the Celestials", 6, 8, 5, 4, 4, 3, new DisplayablePassiveBonus[]{ //'Tier 7 - Mage'
			//(2): +10% Elemental Damage
			//(4): +300 Mana. -7% Spell Cost
			//(6): 15% chance a spell deals 65% more damage
		}),		
	BERSERKER_SET("Berserk's Rage", 5, 7, 5, 3, 4, 3, new DisplayablePassiveBonus[]{ //'Tier 7 - Melee'
			//(2): +20% Movement Speed
			//(4): -8% Damage taken
			//(6): 40% crit for 5-7 seconds. 2 PPM
		}),
	GUARDIAN_SET("Guardian's Aegis", 7, 9, 7, 5, 6, 5, new DisplayablePassiveBonus[] { //'Tier 7 - Tank'
			//(2): +20 stamina
			//(4): -8% Damage taken
			//(6): 8% chance to restore damage taken over 6 seconds
	}),
	SPIRITUALIST_SET("Purity of the Spirit", 6, 8, 6, 0, 0, 0, new DisplayablePassiveBonus[] {
		//(3):+2% HP / sec
		//(3):+10% Special Energy / minute (40% more)
	}),	
	
	
	
	
	OP_TEST_SET("OP-Ness", 7, 9, 7, 5, 5, 5, new DisplayablePassiveBonus[]{ 
			new DisplayablePassiveBonus("+40% Movement Speed").setPiecesRequiredToActivate(2),
			new DisplayablePassiveBonus("+200 Defense").setPiecesRequiredToActivate(4),
			new DisplayablePassiveBonus("+100 Intellect").setPiecesRequiredToActivate(6)
		});

	private final int helmDefense;
	private final int bodyDefense;
	private final int pantsDefense;
	private final int bootsDefense;
	private final int glovesDefense;
	private final int beltDefense;
	private final String setname;
	private DisplayablePassiveBonus[] bonuses;
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
	
	EnumArmor(String setname, int helmDef, int bodyDef, int pantsDef, int bootsDefense, int glovesDefense, int beltDefense, DisplayablePassiveBonus[] bonuses)
	{
		this.setname = setname;
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
	
	public String getSetName()
	{
		return setname;
	}
	
	public DisplayablePassiveBonus[] getBonuses()
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
	public DisplayablePassiveBonus[] getBonuses(int piecesEquipped)
	{
		Vector<DisplayablePassiveBonus> bonusVect = new Vector<DisplayablePassiveBonus>();
		for(DisplayablePassiveBonus set : bonuses)
		{
			if(set.getPiecesRequiredToActivate() <= piecesEquipped)
			{
				bonusVect.add(set);
			}
		}
		DisplayablePassiveBonus[] bonus = new DisplayablePassiveBonus[bonusVect.size()];
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
		for(DisplayableItemStack stack: player.inventory.getArmorInventory())
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
