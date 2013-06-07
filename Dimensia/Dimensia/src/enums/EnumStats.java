package enums;


public enum EnumStats 
{
	DEFENSE(1),
	STAMINA(1),
	DEXTERITY(1),
	INTELLECT(1),
	ATTACK_SPEED(1),
	KNOCKBACK_POWER(2),
	MELEE_DAMAGE_MOD(2),
	RANGE_DAMAGE_MOD(2),
	MAGIC_DAMAGE_MOD(2),
	ALL_DAMAGE_MOD(2),
	CRITICAL_STRIKE(1), //From 0-1F
	DODGE(1), //From 0-1F
	MOVEMENT(2);
	
	public final static int ADDITIVE = 1,
							   MULTIPLICATIVE = 2;
	private final int stackVersion;
	
	EnumStats(int stackVersion)
	{
		this.stackVersion = stackVersion;
	}
	
	public final int getStackVersion()
	{
		return stackVersion;
	}	
}
