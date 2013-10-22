package enums;

import items.Item;
import items.ItemArmor;

import java.util.EnumSet;
import java.util.Vector;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusAttackSpeed;
import passivebonuses.PassiveBonusCriticalStrike;
import passivebonuses.PassiveBonusDefense;
import passivebonuses.PassiveBonusDexterity;
import passivebonuses.PassiveBonusIntellect;
import passivebonuses.PassiveBonusManaBoost;
import passivebonuses.PassiveBonusSpeed;
import passivebonuses.PassiveBonusStrength;
import server.entities.EntityPlayer;
import utils.ItemStack;

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
	NOTHING("None", 0, 0, 0, 0, 0, 0, new PassiveBonus[]{ }),
	COPPER("Copper Fortification", 1, 2, 1, 1, 1, 1, new PassiveBonus[]{
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(3),
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(6)
		}),
	BRONZE("Bronze Fortification", 2, 3, 2, 1, 1, 1, new PassiveBonus[]{ 
			new PassiveBonusDefense(1).setPiecesRequiredToActivate(3), 
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(6)
		}),
	IRON("Iron Fortification", 2, 4, 3, 1, 2, 1, new PassiveBonus[]{ 
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(3),
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(6)
		}),
	SILVER("Silver Fortification", 3, 4, 3, 2, 2, 2, new PassiveBonus[]{ 
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(3),
			new PassiveBonusDefense(3).setPiecesRequiredToActivate(6),
		}),
	GOLD("Gold Fortification", 4, 5, 4, 2, 3, 2, new PassiveBonus[]{ 
			new PassiveBonusDefense(2).setPiecesRequiredToActivate(2),
			new PassiveBonusDefense(3).setPiecesRequiredToActivate(4),
			new PassiveBonusSpeed(0.1).setPiecesRequiredToActivate(6)
		}),
	LUNARIUM("Mage's Grace", 4, 5, 4, 3, 3, 3, new PassiveBonus[]{ //'Tier 6 - Mage'
			new PassiveBonusSpeed(.1).setPiecesRequiredToActivate(2),
			new PassiveBonusManaBoost(200).setPiecesRequiredToActivate(4),
			new PassiveBonusIntellect(5).setPiecesRequiredToActivate(6)
		}),
	RANGE_SET_1("Archer's Finesse", 4, 5, 4, 3, 3, 3, new PassiveBonus[]{ //'Tier 6 - Archer'
			new PassiveBonusSpeed(.1).setPiecesRequiredToActivate(2),
			new PassiveBonusCriticalStrike(0.05).setPiecesRequiredToActivate(4),
			new PassiveBonusDexterity(5).setPiecesRequiredToActivate(6)
		}),
	STRENGTH_SET_1("Warriors's Tact", 4, 5, 4, 3, 3, 3, new PassiveBonus[]{ //'Tier 6 - Melee'
			new PassiveBonusSpeed(.1).setPiecesRequiredToActivate(2),
			new PassiveBonusAttackSpeed(0.05).setPiecesRequiredToActivate(4),
			new PassiveBonusStrength(5).setPiecesRequiredToActivate(6)
		}),		
	ARTEMIS_SET("Spirit of the Hunt", 5, 7, 5, 3, 4, 3, new PassiveBonus[]{ //'Tier 7 - Archer'
			//(2): Arrows deal 3 extra fire damage
			//(4): 6% chance to nullify any hit of damage
			//(6): 10% chance for an arrow to deal double damage
		}),
	CELESTIAL_SET("Grace of the Celestials", 6, 8, 5, 4, 4, 3, new PassiveBonus[]{ //'Tier 7 - Mage'
			//(2): +10% Elemental Damage
			//(4): +300 Mana. -7% Spell Cost
			//(6): 15% chance a spell deals 65% more damage
		}),		
	BERSERKER_SET("Berserk's Rage", 5, 7, 5, 3, 4, 3, new PassiveBonus[]{ //'Tier 7 - Melee'
			new PassiveBonusSpeed(.2).setPiecesRequiredToActivate(2),
			//(4): -8% Damage taken
			//(6): 40% crit for 5-7 seconds. 2 PPM
		}),
	GUARDIAN_SET("Guardian's Aegis", 7, 9, 7, 5, 6, 5, new PassiveBonus[] { //'Tier 7 - Tank'
			//(2): +20 stamina
			//(4): -8% Damage taken
			//(6): 8% chance to restore damage taken over 6 seconds
	}),
	SPIRITUALIST_SET("Purity of the Spirit", 6, 8, 6, 0, 0, 0, new PassiveBonus[] {
		//(3):+2% HP / sec
		//(3):+10% Special Energy / minute (40% more)
	}),	
	OP_TEST_SET("OP-Ness", 7, 9, 7, 5, 5, 5, new PassiveBonus[]{ 
			new PassiveBonusSpeed(.4).setPiecesRequiredToActivate(2),
			new PassiveBonusDefense(200).setPiecesRequiredToActivate(4),
			new PassiveBonusIntellect(100).setPiecesRequiredToActivate(6)
		});

	/** The defense provided by equipping the helm of this set. */
	private final int helmDefense;
	/** The defense provided by equipping the body of this set. */
	private final int bodyDefense;
	/** The defense provided by equipping the pants of this set. */
	private final int pantsDefense;
	/** The defense provided by equipping the boots of this set. */
	private final int bootsDefense;
	/** The defense provided by equipping the gloves of this set. */
	private final int glovesDefense;
	/** The defense provided by equipping the belt of this set. */
	private final int beltDefense;
	/** The set's name in plain text. */
	private final String setname;
	/** Any PassiveBonuses assigned to this EnumArmor. */
	private PassiveBonus[] bonuses;
	/** A Vector which contains all the different tiers of armour. */
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
	
	/**
	 * Constructs a new EnumArmour with the provided parameters.
	 * @param setname a displayable name for the overall set bonus
	 * @param helmDef the defense provided by the helm of the set
	 * @param bodyDef the defense provided by the body of the set
	 * @param pantsDef the defense provided by the pants of the set
	 * @param bootsDefense the defense provided by the boots of the set
	 * @param glovesDefense the defense provided by the gloves of the set
	 * @param beltDefense the defense provided by the belt of the set 
	 * @param bonuses the PassiveBonus[] assigned to this set 
	 */
	EnumArmor(String setname, int helmDef, int bodyDef, int pantsDef, int bootsDefense, int glovesDefense, int beltDefense, PassiveBonus[] bonuses)
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
	 * Gets all the different tiers of armor from EnumArmor as an array.
	 * @return all the tiers of EnumArmor
	 */
	public static EnumArmor[] getTiers()
	{
		EnumArmor[] tiers = new EnumArmor[armorTiers.size()];
		armorTiers.copyInto(tiers);
		return tiers;
	}
	
	/**
	 * Gets the defense value of the helmet for this tier of armour.
	 * @return the defense of the helm for this armour tier
	 */
	public int getHelmetDefense()
	{
		return helmDefense;
	}
	
	/**
	 * Gets the defense value of the body for this tier of armour.
	 * @return the defense of the body for this armour tier
	 */
	public int getBodyDefense()
	{
		return bodyDefense;
	}
	
	/**
	 * Gets the defense value of the legs for this tier of armour.
	 * @return the defense of the legs for this armour tier
	 */ 
	public int getGreavesDefense()
	{
		return pantsDefense;
	}

	/**
	 * Gets the defense value of the boots for this tier of armour.
	 * @return the defense of the boots for this armour tier
	 */
	public int getBootsDefense()
	{
		return bootsDefense;
	}

	/**
	 * Gets the defense value of the gloves for this tier of armour.
	 * @return the defense of the gloves for this armour tier
	 */
	public int getGlovesDefense()
	{
		return glovesDefense;
	}
	
	/**
	 * Gets the defense value of the belt for this tier of armour.
	 * @return the defense of the belt for this armour tier
	 */
	public int getBeltDefense()
	{
		return beltDefense;
	}
	
	/**
	 * Gets the name of this set as a plain text string. 
	 * @return the name of this set in plaintext
	 */
	public String getSetName()
	{
		return setname;
	}
	
	/**
	 * Gets any relevant PassiveBonuses provided by this armour set (does not consider pieces equipped)
	 * @return any relevant PassiveBonuses for this armour set
	 */
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
	 * { (2): BONUS_NAME, .... , (6): BONUS_NAME }
	 * @return the PassiveBonuses for this armour tier in a well formatted array
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
