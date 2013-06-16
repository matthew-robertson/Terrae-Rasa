package utils;

import entities.EntityPlayer;
import render.Render;
import world.World;

/**
 * ActionbarItem is a base class for something that can go in the actionbar. It contains variables regarding the unique id of 
 * that ActionbarItem, as well as its name, any extra tooltip information, and the maximum stack size. Item, Block, Spell and
 * anything else that can be put in the inventory or on the actionbar should extend this for compatability reasons.
 */
public class ActionbarItem 
{
	public final static int blockIndex = 0;
	public final static int itemIndex = 2048;
	public final static int spellIndex = 4096 + 2048;
	public int iconX;
	public int iconY;
	public int id;
	protected String name;
	public String extraTooltipInformation;
	protected int maxStackSize;
	public int iconOverrideX;
	public int iconOverrideY;
	public boolean iconOverriden;
	/**Corresponds to values from Render like TEXTURE_SHEET_TERRAIN_EARTH, to determine the inventory icon for a specific block.*/
	public int associatedTextureSheet;
	//private Socket[] sockets = { new Socket() };
	
	protected ActionbarItem setSockets(/*Socket[] sockets*/)
	{
//		this.sockets = sockets;
		return this;
	}
	
//	public Socket[] getSockets()
//	{
//		Socket[] copy = new Socket[sockets.length];
//		for(int i = 0; i < copy.length; i++)
//		{
//			copy[i] = new Socket(sockets[i]);
//		}
//		return copy;
//	}
	

	/**
	 * This constructor should not be used. It assigns an ActionbarItem without a proper ID which is dangerous. It is implemented 
	 * for special cases such as air blocks.
	 */
	protected ActionbarItem()
	{
		iconX = 0;
		iconY = 0;
		id = 0;
		name = "unnamed";
		extraTooltipInformation = "";
		maxStackSize = 250;
		iconOverrideX = 0;
		iconOverrideY = 0;
		iconOverriden = false;
		associatedTextureSheet = Render.TEXTURE_SHEET_ITEMS;
	}
	
	/**
	 * Creates a new ActionbarItem and initializes it to the specified ID. ID's should be unique. 
	 * <br><br>
	 * Also assigns several default instance variable values which can later be set properly. 
	 * @param id the ActionbarItem's unique ID.
	 */
	public ActionbarItem(int id)
	{
		this.id = id;
		iconX = 0;
		iconY = 0;
		this.name = "unnamed";
		this.extraTooltipInformation = "";
		this.maxStackSize = 250;
		iconOverriden = false;
		iconOverrideX = 0;
		iconOverrideY = 0;
		associatedTextureSheet = Render.TEXTURE_SHEET_ITEMS;
	}
	
	/**
	 * Creates a deep copy of this ActionbarItem with all the variables implemented in the baseclass. Super classes of ActionbarItem
	 * should call this when performing a copy operation
	 * @param item the ActionbarItem or extension to copy
	 */
	public ActionbarItem(ActionbarItem item)
	{
		this.iconX = item.iconX;
		this.iconY = item.iconY;
		this.id = item.id;
		this.name = item.name;
		this.extraTooltipInformation = item.extraTooltipInformation;
		this.maxStackSize = item.maxStackSize;
		this.iconOverrideX = item.iconOverrideX;
		this.iconOverrideY = item.iconOverrideY;
		this.iconOverriden = item.iconOverriden;
		this.associatedTextureSheet = item.associatedTextureSheet;
	}	
	
	/**
	 * Gets the ID of this ActionbarItem. An ID is a unique int value an Item, Block, or Spell in a given range that will match
	 * any copies of that Object but no other extensions of ActionbarItem.
	 * @return the ID of this ActionbarItem
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Gets the name of this ActionbarItem, the value displayed in tooltips. This may not be unique.
	 * @return the ActionbarItem's name, used in tooltips
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets any extra information that might be useful to the user in a tooltip.
	 * @return any information of extra value about this item, to be used in a tooltip.
	 */
	public String getExtraTooltipInformation()
	{
		return extraTooltipInformation;
	}
	
	/**
	 * Gets the maximum of this ActionbarItem that can be fit in a single inventory slot or ItemStack.
	 * @return the maximum of an ActionbarItem that can be stored in one ItemStack
	 */
	public int getMaxStackSize()
	{
		return maxStackSize;
	}
	
	/**
	 * Gets the X position of the texture on the specific sprite sheet. This is not based on pixels but rather the column that
	 * the sprite is in. 
	 * @return the X position of the texture on a specific sprite sheet
	 */
	public int getIconX()
	{
		return iconX;
	}
	
	/**
	 * Gets the Y position of the texture on the specific sprite sheet. This is not based on pixels but rather the row that
	 * the sprite is in. 
	 * @return the Y position of the texture on a specific sprite sheet
	 */
	public int getIconY()
	{
		return iconY;
	}
	
	/**
	 * Sets the name of the ActionbarItem to the given value. Returns a reference to this object to aid in ActionbarItem construction.
	 * @param name the ActionbarItem's new name
	 * @return a reference to this Object
	 */
	protected ActionbarItem setName(String name)
	{
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the position of the icon to the specified column (X), and row(Y) on a spritesheet.
	 * @param x the new x position of the icon on the appropriate sprite sheet
	 * @param y the new y position of the icon on the appropriate sprite sheet
	 * @return a reference to this Object
	 */
	protected ActionbarItem setIconPosition(int x, int y)
	{
		iconX = x;
		iconY = y;
		return this;
	}
	
	/**
	 * Sets the maxstacksize of this ActionbarItem to a different value than the default (250)
	 * @param i the new maxstacksize
	 * @return a reference to this Object
	 */
	protected ActionbarItem setMaxStackSize(int i)
	{
		maxStackSize = i;
		return this;
	}
	
	/**
	 * Sets the extraTooltipInformation that will be rendered as part of the tooltip. This should be something useful in some way.
	 * @param info any extra tooltip information useful to the user
	 * @return a reference to this object
	 */
	public ActionbarItem setExtraTooltipInformation(String info)
	{
		this.extraTooltipInformation = info;
		return this;
	}
	/**
	 * Overrides the icon used when rendering this item in the inventory to one on any terrain or item.png sprite sheet.
	 * @param x the x position of the sprite on the item.png sheet to render
	 * @param y the y position of the sprite on the item.png sheet to render
	 * @param associatedSheet the texture sheet to pull the inventory icon from, based on the Render fields like TEXTURE_SHEET_TERRAIN_EARTH
	 * @return this block, but updated to include this new data
	 */
	public ActionbarItem overrideItemIcon(int x, int y, int associatedSheet)
	{
		iconOverrideX = x;
		iconOverrideY = y;
		this.associatedTextureSheet = associatedSheet;
		iconOverriden = true;
		return this;
	}
	
	/**
	 * onRightClick(...) can be overriden to do something unique when an ActionbarItem is right clicked on the actionbar.
	 * @param world the World in use
	 * @param entity the EntityPlayer in use
	 */
	public void onRightClick(World world, EntityPlayer entity)
	{
	}
}
