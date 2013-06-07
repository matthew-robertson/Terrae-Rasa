package net.dimensia.src;

import setbonus.SetBonusContainer;
import utils.InventoryPlayer;
import items.Item;

/**
 * EnumSetBonuses provides a list of possible set bonuses, and a name for each of them which can be retrieved by calling 
 * an EnumSetBonues' {@link #toString()}  method.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumSetBonuses 
{
	DEFENSE1("+1 Defense"),
	DEFENSE2("+2 Defense"),
	DEFENSE3("+3 Defense"),
	DEFENSE4("+4 Defense"),
	DEFENSE5("+5 Defense"),
	DAMAGE_MAGIC_DONE_10("+10% Magic Damage"), //+10% magic damage done
	DAMAGE_MAGIC_DONE_20("+20% Magic Damage"), //+20% magic damage done
	DAMAGE_RANGE_DONE_10("+10% Ranged Damage"), //+10% range damage done
	DAMAGE_RANGE_DONE_20("+20% Ranged Damage"), //+20% range damage done
	DAMAGE_MELEE_DONE_10("+10% Melee Damage"), //+10% melee damage done
	DAMAGE_MELEE_DONE_20("+20% Melee Damage"), //+20% melee damage done
	DAMAGE_DONE_10("+10% Damage"), //+10% all damage done
	DAMAGE_DONE_20("+20% Damage"), //+20% all damage done
	MOVEMENT_SPEED_10("+10% Speed"), //+10% movement speed
	MOVEMENT_SPEED_20("+20% Speed"), //+20% movement speed
	MOVEMENT_SPEED_30("+30% Speed"), //+30% movement speed
	FALL_IMMUNE("Immune to Fall Damage"), //Immunity to fall damage
	FIRE_IMMUNE("Immune to Fire"), //Immunity to fire and lava damage
	JUMP3("Jump 3 Blocks higher"), //Jumps 3 blocks higher (gives increased fall distance too)
	JUMP8("Jump 8 Blocks higher"), //Jumps 8 blocks higher (gives increased fall distance too)
	CRITICAL10("+10% Critical Chance"), //+10% chance to critically hit
	CRITICAL20("+20% Critical Chance"), //+20% chance to critically hit
	CRITICAL_IMMUNE("Immune to Criticals"), //Immune to critical hits
	KNOCKBACK_20("+20% Knockback"), //Knockbacks 20% more powerful 
	KNOCKBACK_40("+40% Knockback"), //Knockbacks 40% more powerful
	KNOCKBACK_60("+60% Knockback"), //Knockbacks 60% more powerful
	HEAVENS_REPRIEVE("Heaven's Reprieve"); //A hit that would kill instead grants 15% maximum health and 6 seconds immunity (should destroy the accessory)
	
	private String name;
	
	EnumSetBonuses(String name)
	{
		this.name = name;
	}
	
	/**
	 * Gives the list of set bonuses for the armour the player currently has equiped
	 * @return list of set bonuses for the given equipment set, or nothing if the player has no valid combination equipped
	 */
	public static SetBonusContainer getSetBonus(InventoryPlayer inventory)
	{
		return get3SetBonuses(inventory);
	}
	
	private static SetBonusContainer get3SetBonuses(InventoryPlayer inventory)
	{
		if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX) == null ||
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX) == null || 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX) == null) //No set bonus possible
			
			{ 
				return new SetBonusContainer(new EnumSetBonuses[] { });
			}
			
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.copperHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.copperBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.copperPants.getID())
			{ //Copper Set Bonus
				//defense + 1... 
				return new SetBonusContainer(new EnumSetBonuses[] { EnumSetBonuses.DEFENSE1 });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.bronzeHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.bronzeBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.bronzePants.getID())
			{ //Bronze Set Bonus
				//defense + 2... 
				return new SetBonusContainer(new EnumSetBonuses[] { EnumSetBonuses.DEFENSE2 });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.ironHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.ironBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.ironPants.getID())
			{ //Iron Set Bonus
				//defense + 3... 
				return new SetBonusContainer(new EnumSetBonuses[] { EnumSetBonuses.DEFENSE3 });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.silverHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.silverBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.silverPants.getID())
			{ //Silver Set Bonus - Defense +4
				return new SetBonusContainer(new EnumSetBonuses[] { EnumSetBonuses.DEFENSE4 });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.goldHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.goldBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.goldPants.getID())
			{ //Gold Set Bonus - Defense +5
				return new SetBonusContainer(new EnumSetBonuses[] { EnumSetBonuses.DEFENSE5 });
			}
			
			return new SetBonusContainer(new EnumSetBonuses[] { }); //Nothing matched 
			
	}
	
	/**
	 * Overrides Enum.toString() to provide the name of the given EnumSetBonuses
	 * @return the EnumSetBonuses' name
	 */
	public String toString()
	{
		return name;
	}
}