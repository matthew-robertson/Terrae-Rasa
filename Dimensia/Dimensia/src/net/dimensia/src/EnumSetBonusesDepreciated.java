package net.dimensia.src;


/**
 * EnumSetBonuses provides a list of possible set bonuses, and a name for each of them which can be retrieved by calling 
 * an EnumSetBonues' {@link #toString()}  method.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumSetBonusesDATBONUS 
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
	
	JUMP3("Jump 3 Blocks higher"), //Jumps 3 blocks higher (gives increased fall distance too)
	JUMP8("Jump 8 Blocks higher"), //Jumps 8 blocks higher (gives increased fall distance too)
	CRITICAL10("+10% Critical Chance"), //+10% chance to critically hit
	CRITICAL20("+20% Critical Chance"), //+20% chance to critically hit
	
	KNOCKBACK_20("+20% Knockback"), //Knockbacks 20% more powerful 
	KNOCKBACK_40("+40% Knockback"), //Knockbacks 40% more powerful
	KNOCKBACK_60("+60% Knockback"), //Knockbacks 60% more powerful
	
	
//	Convert to auras:
	
	HEAVENS_REPRIEVE("Heaven's Reprieve"), //A hit that would kill instead grants 15% maximum health and 6 seconds immunity (should destroy the accessory)
	CRITICAL_IMMUNE("Immune to Criticals"), //Immune to critical hits
	FALL_IMMUNE("Immune to Fall Damage"), //Immunity to fall damage
	FIRE_IMMUNE("Immune to Fire"), //Immunity to fire and lava damage
	
	;
	private String name;
	
	EnumSetBonusesDATBONUS(String name)
	{
		this.name = name;
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