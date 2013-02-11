package net.dimensia.src;

public enum EnumStatusEffects 
{
	BUFF_REGEN_HEALTH_3,
	BUFF_REGEN_MANA_2,
	BUFF_DAMAGE_ALL,
	BUFF_DAMAGE_MELEE,
	BUFF_DAMAGE_RANGED,
	BUFF_DAMAGE_MAGIC,
	BUFF_ALL_STATS,
	BUFF_MOVEMENT_SPEED,
	BUFF_ATTACK_SPEED,
	BUFF_CRITICAL_STRIKE,
	BUFF_DODGE,
	DEBUFF_STUN_1, 
	DEBUFF_POISON, 
	DEBUFF_BLEEDING,
	DEBUFF_MOVEMENT_SPEED, //A dazed effect
	/**
	 * Fatal Woulds leech your strength and vitality. -5% max health and damage per second until dead. HEAVENS_REPRIEVE still works 
	 * and prevents dead (gives % actual max health, not lower amount)
	 */
	DEBUFF_FATAL_WOUNDS;
}
