package enums;

/**
 * EnumDamageType defines the different damage types. These can be used for individual resistances 
 * among other things. Version 1.0 includes the following damage types:
 * <ul>
 *  <li> FIRE </li>
 *  <li> DROWNING </li>
 *  <li> NONE </li>
 *  <li> POISON </li>
 *  <li> FALL </li>
 *  <li> BLEED </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumDamageType 
{
	FIRE(),
	DROWNING(),
	/**This would be non-elemental damage or something else which has no specific type. */
	NONE(),
	POISON(),
	FALL(),
	BLEED();	
}
