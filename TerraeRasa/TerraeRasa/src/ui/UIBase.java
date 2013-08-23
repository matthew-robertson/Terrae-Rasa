package ui;

import java.awt.Font;

import render.Render;
import render.TrueTypeFont;
import utils.DisplayableItemStack;

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
	protected static DisplayableItemStack mouseItem; //What it is
	protected static int mouseXOffset; //How far it should be adjusted to avoid looking bad
	protected static int mouseYOffset;	
	//Socket Window Variables
	protected static boolean isSocketWindowOpen;
	protected static boolean socketItemEquipped;
	protected static int socketItemIndex;
	protected static DisplayableItemStack socketedItem;
	
	public static void init()
	{
		shouldDropItem = false;
		mouseItemSize = 0; 
		mouseItem = null; 
		mouseXOffset = 0; 
		mouseYOffset = 0;	
	}
}
