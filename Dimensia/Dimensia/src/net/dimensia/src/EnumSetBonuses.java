package net.dimensia.src;

public enum EnumSetBonuses 
{
	DEFENSE1,
	DEFENSE2,
	DEFENSE3,
	DEFENSE4,
	DEFENSE_P_10, //+10% defense
	DEFENSE_P_20, //+20% defense
	DAMAGE_MAGIC_DONE_10, //+10% magic damage done
	DAMAGE_MAGIC_DONE_20, //+20% magic damage done
	DAMAGE_RANGE_DONE_10, //+10% range damage done
	DAMAGE_RANGE_DONE_20, //+20% range damage done
	DAMAGE_MELEE_DONE_10, //+10% melee damage done
	DAMAGE_MELEE_DONE_20, //+20% melee damage done
	DAMAGE_DONE_10, //+10% all damage done
	DAMAGE_DONE_20, //+20% all damage done
	DAMAGE_SOAK_5, //-5% damage taken
	DAMAGE_SOAK_10, //-10% damage taken
	DAMAGE_SOAK_15, //-15% damage taken
	DAMAGE_SOAK_20, //-20% damage taken
	MOVEMENT_SPEED_10, //+10% movement speed
	MOVEMENT_SPEED_20, //+20% movement speed
	MOVEMENT_SPEED_30, //+30% movement speed
	FALL_IMMUNE, //Immunity to fall damage
	FIRE_IMMUNE, //Immunity to fire and lava damage
	JUMP3, //Jumps 3 blocks higher (gives increased fall distance too)
	JUMP8, //Jumps 8 blocks higher (gives increased fall distance too)
	CRITICAL10, //+10% chance to critically hit
	CRITICAL20, //+20% chance to critically hit
	CRITICAL_IMMUNE, //Immune to critical hits
	KNOCKBACK_20, //Knockbacks 20% more powerful 
	KNOCKBACK_40, //Knockbacks 40% more powerful
	KNOCKBACK_60, //Knockbacks 60% more powerful
	HEAVENS_REPRIEVE; //A hit that would kill instead grants 15% maximum health and 6 seconds immunity (should destroy the accessory)
	
	/**
	 * Gives the list of set bonuses for the armour the player currently has equiped
	 * @return list of set bonuses for the given equipment set, or nothing if the player has no valid combination equipped
	 */
	public static EnumSetBonuses[] getSetBonus(InventoryPlayer inventory)
	{
		if (inventory.getArmorInventoryStack(0) == null || inventory.getArmorInventoryStack(1) == null || inventory.getArmorInventoryStack(2) == null) //No set bonus
		{ 
			return new EnumSetBonuses[] { };
		}
		
		if (inventory.getArmorInventoryStack(0).getItemID() == Item.copperHelmet.getItemID() && 
			inventory.getArmorInventoryStack(1).getItemID() == Item.copperBody.getItemID() && 
			inventory.getArmorInventoryStack(2).getItemID() == Item.copperGreaves.getItemID())
		{ //Copper Set Bonus
			//defense + 1... 
			return new EnumSetBonuses[] { EnumSetBonuses.DEFENSE1 };
		}
		if (inventory.getArmorInventoryStack(0).getItemID() == Item.bronzeHelmet.getItemID() && 
			inventory.getArmorInventoryStack(1).getItemID() == Item.bronzeBody.getItemID() && 
			inventory.getArmorInventoryStack(2).getItemID() == Item.bronzeGreaves.getItemID())
		{ //Bronze Set Bonus
			//defense + 2... 
			return new EnumSetBonuses[] { EnumSetBonuses.DEFENSE2 };
		}
		if (inventory.getArmorInventoryStack(0).getItemID() == Item.ironHelmet.getItemID() && 
			inventory.getArmorInventoryStack(1).getItemID() == Item.ironBody.getItemID() && 
			inventory.getArmorInventoryStack(2).getItemID() == Item.ironGreaves.getItemID())
		{ //Iron Set Bonus
			//defense + 3... 
			return new EnumSetBonuses[] { EnumSetBonuses.DEFENSE3 };
		}
		if (inventory.getArmorInventoryStack(0).getItemID() == Item.silverHelmet.getItemID() && 
			inventory.getArmorInventoryStack(1).getItemID() == Item.silverBody.getItemID() && 
			inventory.getArmorInventoryStack(2).getItemID() == Item.silverGreaves.getItemID())
		{ //Silver Set Bonus
			//defense + 4... 
			return new EnumSetBonuses[] { EnumSetBonuses.DEFENSE4 };
		}
		if (inventory.getArmorInventoryStack(0).getItemID() == Item.goldHelmet.getItemID() && 
			inventory.getArmorInventoryStack(1).getItemID() == Item.goldBody.getItemID() && 
			inventory.getArmorInventoryStack(2).getItemID() == Item.goldGreaves.getItemID())
		{ //Gold Set Bonus
			//defense + 4... 
			return new EnumSetBonuses[] { EnumSetBonuses.DEFENSE4 };
		}
		
		return new EnumSetBonuses[] { }; //Nothing matched 
	}
}