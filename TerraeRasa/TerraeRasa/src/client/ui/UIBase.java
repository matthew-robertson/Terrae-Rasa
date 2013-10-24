package client.ui;

import java.awt.Font;

import client.render.Render;
import client.render.TrueTypeFont;


/**
 * UIBase holds common variables that the entire UI shares.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class UIBase extends Render
{
	//protected final static TrueTypeFont plainTooltip = new TrueTypeFont(((new Font("times", Font.PLAIN, 48)).deriveFont(48.0f)), true);
	protected final static TrueTypeFont tooltipFont = new TrueTypeFont(((new Font("times", Font.PLAIN, 36)).deriveFont(36.0f)), true);
	//plainTooltip.drawString(getCameraX() + 50, getCameraY() + 50, "ABCDEFGHIJKLMNOPQRSTUVWXYZ!?.", 0.16F, -0.16F);
	//plainTooltip.drawString(getCameraX() + 50, getCameraY() + 50, "ABCDEFGHIJKLMNOPQRSTUVWXYZ!?.", 0.22F, -0.22F);
	protected final static TrueTypeFont boldTooltip = new TrueTypeFont(((new Font("times", Font.BOLD, 24)).deriveFont(16.0f)), true);
	protected final static TrueTypeFont plainTooltip = new TrueTypeFont(((new Font("times", Font.PLAIN, 24)).deriveFont(16.0f)), true);
	//Variables for the item picked up by the mouse:
	protected static boolean shouldDropItem;
	protected static int mouseItemSize; //How big it is
	protected static int mouseXOffset; //How far it should be adjusted to avoid looking bad
	protected static int mouseYOffset;	
	//Socket Window Variables
	protected static boolean isSocketWindowOpen;
	protected static boolean socketItemEquipped;
	protected static int inventoryID;
	protected static int inventoryIndex;
//	protected static int socketItemIndex;
//	protected static DisplayableItemStack socketedItem;
	
	public static void init()
	{
		shouldDropItem = false;
		mouseItemSize = 0; 
		mouseXOffset = 0; 
		mouseYOffset = 0;	
	}
	
	public static void clearSocketVariables()
	{
		isSocketWindowOpen = false;
		inventoryID = -1;
		inventoryIndex = -1;
	}
	
	public static boolean getIsSocketWindowOpen()
	{
		return isSocketWindowOpen;
	}
	
	public static int getInventoryID()
	{
		return inventoryID;
	}
	
	public static int getInventoryIndex()
	{
		return inventoryIndex;
	}
	
	public static void forceHeldItemSize(int size)
	{
		mouseItemSize = size;
	}
}
