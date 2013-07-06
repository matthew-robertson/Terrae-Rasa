package render;

import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * GuiMenu defines a menu which can accept any number of arguments and will use a scrollbar if there are more items in the list
 * than are visible. Every GuiMenu has a title, a number of varying items, and a limited number of locked in values which are 
 * always visible. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GuiMenu extends GuiComponent
{
	protected final static TrueTypeFont ttf = new TrueTypeFont(((new Font("times", Font.BOLD, 24)).deriveFont(16.0f)), true);
	private final static int CELL_SIZE = 30;
	private String[] varyingItems;
	private String[] lockedInComponents;
	private int selectedIndex;
	private String title;
	private int highlightedIndex;
	private boolean visible;
	
	/**
	 * Constructs a new GuiMenu with the given positions
	 * @param x the x position as a value from 0.0 to 1.0 indicating a percent
	 * @param y the y position as a value from 0.0 to 1.0 indicating a percent, or a fixed value if the component does not adjust vertically
	 * @param width the width as a value from 0.0 to 1.0 indicating a percent
	 * @param height the height, a value from 0.0 to 1.0 indicating a percent
	 * @param title the title of the menu
	 * @param varyingItems the initial varying items in the menu
	 * @param lockedInComponents the initial values which are always visible in the menu
	 */
	public GuiMenu(double x, double y, double width, double height, String title, String[] varyingItems, String[] lockedInComponents)
	{
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.varyingItems = varyingItems;
		this.lockedInComponents = lockedInComponents;
		this.selectedIndex = 0;
		this.highlightedIndex = -1;
		this.title = title;
		this.visible = true;
	}
	
	/**
	 * Checks if the scrollbar needs adjusted due to a mouse click
	 */
	public void onClick(int mouseX, int mouseY) 
	{
		if(!visible)
		{
			return;
		}
		//If there is the need for a scrollbar, activate mouse functionality.
		if(varyingItems.length > (int) ((Display.getHeight() * 0.45) / 30) - lockedInComponents.length - 1)
		{
			double x = (this.x * Display.getWidth() * 0.5);
			double y = (this.y * Display.getHeight() * 0.5);
			double width = this.width * 0.5 * Display.getWidth() * 0.9;
			double scrollWidth = this.width * 0.5 * Display.getWidth() * 0.1;
			double scrollHeight = ((int) ((Display.getHeight() * 0.45) / 30) - lockedInComponents.length) * CELL_SIZE - 1;
			double yoff = CELL_SIZE;
			int totalMenuItems = (int) ((Display.getHeight() * 0.50 - (2 * y)) / 30);
			
			int maxDisplayedValues = totalMenuItems - lockedInComponents.length;
			if(maxDisplayedValues > varyingItems.length)
			{
				maxDisplayedValues = varyingItems.length;
			}
			//Check if the mouse is in the scrollbar part, if so then adjust the scrollbar to the appropriate index
			//Based on where there was a click.
			if(mouseX > x + width && 
				mouseX < x + width + scrollWidth && 
				mouseY > y + yoff && 
				mouseY < y + yoff + scrollHeight)
			{
				selectedIndex = (int) (varyingItems.length * ((double)(mouseY - y - CELL_SIZE) / scrollHeight));
				if(selectedIndex > varyingItems.length - maxDisplayedValues + 1)
				{
					selectedIndex = varyingItems.length - maxDisplayedValues + 1;
				}
			}
		}
	}
	
	/**
	 * Gets the cell of the menu that is selected, factoring in for scrolling through the different varying items.
	 * @param mouseX the X position of the mouse in ortho units
	 * @param mouseY the Y position of the mouse in ortho units
	 * @return the square the mouse is currently clicking, or -1 if not inside any particular square
	 */
	public int getSelectedCell(int mouseX, int mouseY)
	{
		double x = (this.x * Display.getWidth() * 0.5);
		double y = (this.y * Display.getHeight() * 0.5);
		double width = this.width * 0.5 * Display.getWidth() * 0.9;
		int totalMenuItems = (int) ((Display.getHeight() * 0.50 - (2 * y)) / 30);
		int index = -1;
		for(int i = 0; i < totalMenuItems; i++)
		{
			if(mouseX > x &&
				mouseX < x + width &&
				mouseY > y + CELL_SIZE * i &&
				mouseY < y + CELL_SIZE * (i + 1)
			){
				index = i;
			}
		}		
		if(index != -1)
		{
			if(index >= totalMenuItems - lockedInComponents.length)
			{
				return index - (totalMenuItems - lockedInComponents.length) + varyingItems.length + 1;
			}
			index += selectedIndex;
		}
		return index;
	}
	
	/**
	 * Gets the cell of the menu that is selected without factoring in scrolling through the
	 * scrollbar. 
	 * @param mouseX the X position of the mouse in ortho units
	 * @param mouseY the Y position of the mouse in ortho units
	 * @return the selected cell without regard for scrolling varying_items
	 */
	public int getCellWithoutScroll(int mouseX, int mouseY)
	{
		double x = (this.x * Display.getWidth() * 0.5);
		double y = (this.y * Display.getHeight() * 0.5);
		double width = this.width * 0.5 * Display.getWidth() * 0.9;
		int totalMenuItems = (int) ((Display.getHeight() * 0.50 - (2 * y)) / 30);
		int index = -1;
		for(int i = 0; i < totalMenuItems; i++)
		{
			if(mouseX > x &&
				mouseX < x + width &&
				mouseY > y + CELL_SIZE * i &&
				mouseY < y + CELL_SIZE * (i + 1)
			){
				index = i;
			}
		}		
		return index;
	}
	
	/**
	 * Updates the varying items in the list to the new given values
	 * @param newVaryingItems the new varying item values for the menu
	 */
	public void updateVaryingItems(String[] newVaryingItems)
	{
		this.varyingItems = newVaryingItems;
		selectedIndex = 0;
		highlightedIndex = -1;
	}
	
	/**
	 * Updates the locked in values in the list to the new given values
	 * @param newLockedInComponents the new locked in values' names
	 */
	public void updateLockedInComponents(String[] newLockedInComponents)
	{
		this.lockedInComponents = newLockedInComponents;
		selectedIndex = 0;
		highlightedIndex = -1;
	}
	
	/**
	 * Updates the menu title to the new value given.
	 * @param newTitle the new menu title
	 */
	public void updateTitle(String newTitle)
	{
		this.title = newTitle;
	}
	
	/**
	 * Marks a certain cell in the menu as being highlighted, which will cause it to show a different
	 * background for emphasis
	 * @param index the new cell of the menu to highlight
	 */
	public void setHighlighted(int index)
	{
		this.highlightedIndex = index; 
	}
	
	/**
	 * Draws this GuiMenu if it's visible
	 */
	public void draw() 
	{
		if(!visible)
		{
			return;
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		double x = (this.x * Display.getWidth() * 0.5);
		double y = (this.y * Display.getHeight() * 0.5);
		double width = this.width * 0.5 * Display.getWidth() * 0.9;
		int totalMenuItems = (int) ((Display.getHeight() * 0.50 - (2 * y)) / 30);
		
		Tessellator t = Tessellator.instance;
		//Draws the background cells, highlighting the cell if it is selected
		for(int i = 0; i < totalMenuItems; i++)
		{
			//If it is the currently selected part of the menu, apply some highlighting
			//To visually indicate that it is selected
			if(highlightedIndex == i) 
			{
				//GL11.glColor4d(135.0/255, 206.0/255, 250.0/255, 0.6);
				GL11.glColor4d(15.0/255, 82.0/255, 186.0/255, 0.6);
				t.startDrawingQuads();
				t.addVertexWithUV(x, ((i + 1) * CELL_SIZE) + y, 0, 0, 1);
			    t.addVertexWithUV(x + width, ((i + 1) * CELL_SIZE) + y , 0, 1, 1);
			    t.addVertexWithUV(x + width, (i * CELL_SIZE) + y, 0, 1, 0);
			    t.addVertexWithUV(x, (i * CELL_SIZE) + y, 0, 0, 0);		
				t.draw();
			}
			else
			{
				GL11.glColor4d(192.0/255, 192.0/255, 192.0/255, 0.6);
				t.startDrawingQuads();
				t.addVertexWithUV(x, ((i + 1) * CELL_SIZE) + y, 0, 0, 1);
			    t.addVertexWithUV(x + width, ((i + 1) * CELL_SIZE) + y , 0, 1, 1);
			    t.addVertexWithUV(x + width, (i * CELL_SIZE) + y, 0, 1, 0);
			    t.addVertexWithUV(x, (i * CELL_SIZE) + y, 0, 0, 0);		
				t.draw();
			}
		}

		//Populate the displayedValues[] with the values which should be shown, defaulting to an empty slot should there be none.
		String[] displayedValues = new String[totalMenuItems];
		for(int i = 0; i < displayedValues.length; i++)
		{
			displayedValues[i] = "";
		}
		displayedValues[0] = title;
		int invertedIndex = lockedInComponents.length - 1;
		for(int i = displayedValues.length - 1; i > displayedValues.length - 1 - lockedInComponents.length; i--)
		{
			displayedValues[i] = lockedInComponents[invertedIndex--];
		}
		int maxDisplayedValues = displayedValues.length - 1 - lockedInComponents.length;
		if(maxDisplayedValues > varyingItems.length)
			maxDisplayedValues = varyingItems.length;
		//Get the values that should be shown, based on the selectedIndex of the menu
		invertedIndex = (selectedIndex + maxDisplayedValues > varyingItems.length) ? varyingItems.length - maxDisplayedValues : selectedIndex;
		for(int i = 1; i < maxDisplayedValues + 1; i++)
		{
			displayedValues[i] = varyingItems[invertedIndex++]; 
		}
		
		//For a scrollbar, there has to actually be extra elements in the menu that arent always 
		//Visible. (as a part of the varyingItems[])
		if(varyingItems.length > displayedValues.length - 1 - lockedInComponents.length)
		{
			//Scrollbar on the side
			double scrollWidth = this.width * 0.5 * Display.getWidth() * 0.1;
			double scrollHeight = (displayedValues.length - lockedInComponents.length - 1) * CELL_SIZE - 1;
			double yoff = CELL_SIZE;
			GL11.glColor4d(2.0/3, 2.0/3, 3.0/3, 0.6); 
			t.startDrawingQuads();
			t.addVertexWithUV(x + width, y + yoff + scrollHeight, 0, 0, 1);
		    t.addVertexWithUV(x + width + scrollWidth, y + yoff + scrollHeight, 0, 1, 1);
		    t.addVertexWithUV(x + width + scrollWidth, y + yoff, 0, 1, 0);
		    t.addVertexWithUV(x + width, y + yoff, 0, 0, 0);		
		    t.draw();

			//The little move-able part
		    int partialHeight = (int) (((double)(maxDisplayedValues) / varyingItems.length) * scrollHeight);
		    yoff = CELL_SIZE + ((double)(scrollHeight - partialHeight) * ((double)(selectedIndex) / (varyingItems.length - maxDisplayedValues)));
			GL11.glColor4d(2.0/3, 2.0/3, 2.0/3, 0.6); 
			t.startDrawingQuads();
			t.addVertexWithUV(x + width, y + yoff + (partialHeight), 0, 0, 1);
		    t.addVertexWithUV(x + width + scrollWidth, y + yoff + (partialHeight), 0, 1, 1);
		    t.addVertexWithUV(x + width + scrollWidth, y + yoff, 0, 1, 0);
		    t.addVertexWithUV(x + width, y + yoff, 0, 0, 0);		
			t.draw();
		}
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4d(1, 1, 1, 1);
		
		//Render the title
		trueTypeFont.drawString((float)(x + (width / 2)), 
				(float)(((.85) * CELL_SIZE) + y), 
				displayedValues[0], 
				0.70F, 
				-0.70F, 
				TrueTypeFont.ALIGN_CENTER);
		
		//Render the components in the middle's text, then the values at the bottom
		for(int i = 1; i < displayedValues.length; i++)
		{
			float xScale = 0.5F;
			float yScale = 0.5F;
			if(i > varyingItems.length)
			{
				xScale += 0.1F;
				yScale += 0.1F;
			}
			trueTypeFont.drawString((float)(x + (width / 2)), 
					(float)(((i + 1 - (1 - yScale) / 2) * CELL_SIZE) + y), 
					displayedValues[i], 
					xScale, 
					-yScale, 
					TrueTypeFont.ALIGN_CENTER);
		}
	}
	
	public String[] getVaryingItems()
	{
		return varyingItems;
	}
	
	public int getNumberOfItems()
	{
		return varyingItems.length + lockedInComponents.length + 1; 
	}
	
	public int getTotalMenuLength()
	{
		return (int) ((Display.getHeight() * 0.50 - (2 * ((this.y * Display.getHeight() * 0.5)))) / 30);
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
}
