package enums;

/**
 * Defines the different damage sources for damage done to entities. Version 1.0 defines the following different
 * sources of damage: 
 * <ul>
 * <li> MELEE </li>
 * <li> RANGE </li>
 * <li> MAGIC </li>
 * <li> STATUS_EFFECT </li>
 * <li> FALL </li>
 * <li> ENVIRONMENT </li>
 * <li> UNDEFINED </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumDamageSource
{
	MELEE(),
	RANGE(),
	MAGIC(),
	STATUS_EFFECT(),
	FALL(),
	ENVIRONMENT(), 
	UNDEFINED();
}
