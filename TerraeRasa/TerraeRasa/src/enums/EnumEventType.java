package enums;

/**
 * EnumEventType defines some enums for different events that happen in the world. There are currently
 * only events for Block updates in the following cases: 
 * <ul>
 *  <li> Block Placement </li>
 *  <li> Block destruction/breaking </li>
 *  <li> Block Placement that is a light block</li>
 *  <li> Block destruction that is a light block</li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public enum EnumEventType 
{
	EVENT_BLOCK_BREAK(),
	EVENT_BLOCK_PLACE(),
	EVENT_BLOCK_BREAK_LIGHT(),
	EVENT_BLOCK_PLACE_LIGHT();
}
