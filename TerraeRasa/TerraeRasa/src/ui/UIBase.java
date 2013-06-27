package ui;

import java.awt.Font;

import render.Render;
import render.TrueTypeFont;
import utils.ItemStack;

public class UIBase extends Render
{
	protected final static TrueTypeFont boldTooltip = new TrueTypeFont(((new Font("times", Font.BOLD, 24)).deriveFont(16.0f)), true);
	protected final static TrueTypeFont plainTooltip = new TrueTypeFont(((new Font("times", Font.PLAIN, 24)).deriveFont(16.0f)), true);
	//Variables for the item picked up by the mouse:
	protected static boolean shouldDropItem;
	protected static int mouseItemSize; //How big it is
	protected static ItemStack mouseItem; //What it is
	protected static int mouseXOffset; //How far it should be adjusted to avoid looking bad
	protected static int mouseYOffset;	
	//Socket Window Variables
	protected static boolean isSocketWindowOpen;
	protected static boolean socketItemEquipped;
	protected static int socketItemIndex;
	protected static ItemStack socketedItem;
	
	public static void init()
	{
		shouldDropItem = false;
		mouseItemSize = 0; 
		mouseItem = null; 
		mouseXOffset = 0; 
		mouseYOffset = 0;	
	}
}
