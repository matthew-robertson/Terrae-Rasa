package render;

import java.awt.Font;


import org.lwjgl.opengl.Display;


import utils.Texture;

/**
 * GuiComponent defines an abstract base class for all GuiComponents, therefore it may not be instantiated. In order to 
 * create a custom GuiComponent, GuiComponent must be extended and the {@link #draw()} method must be implemented, in
 * addition to {@link #onClick(int, int)}.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public abstract class GuiComponent 
{
	/** A shared font rendering class for all gui components (due to there only being 1 render thread) */
	protected static TrueTypeFont trueTypeFont;
	/** The background texture of the gui component being rendered*/
	protected Texture renderTexture;
	/** The width of the button as a % of the screen, between 0.0F and 1.0F*/
	protected double width;
	/** The height of the button as a % of the screen, between 0.0F and 1.0F*/
	protected double height;
	/** The x position as a % of the screen, between 0.0F and 1.0F */
	protected double x;
	/** The y position as a % of the screen, between 0.0F and 1.0F */
	protected double y;
	
	/**
	 * Initializes all the variables to their default values and creates the font renderer if it's null.
	 * Default values include: a width of 40%, a height of 10%, the default GuiComponent texture, an 
	 * x position of 0%, and a y position of 0%.
	 */
	public GuiComponent()
	{	
		if(trueTypeFont == null)
		{
			trueTypeFont = new TrueTypeFont(new Font("Agent Orange", Font.BOLD, 20), false/*DO NOT use antialiasing*/);
		}
		renderTexture = Render.buttonTexture;
		width = 0.4F;
		height = 0.1F;
		x = 0.0F;
		y = 0.0F;
	}
	
	/**
	 * Sets the width of the button as a percent of the screen width
	 * @param width the new width of the width, in percent, between 0.0F and 1.0F
	 */
	public void setWidth(double width)
	{
		this.width = width;
	}
	
	/**
	 * Sets the height of the button as a percent of the screen height
	 * @param height the new height of the button, in percent, between 0.0F and 1.0F
	 */
	public void setHeight(double height)
	{
		this.height = height;
	}
	
	/**
	 * Assigns a new texture to the button
	 * @param texture the texture to assign to the button
	 */
	public void setRenderTexture(Texture texture)
	{
		this.renderTexture = texture;
	}
		
	/**
	 * Gets the texture the button is currently rendering
	 * @return the texture currently used by this button
	 */
	public Texture getRenderTexture()
	{
		return renderTexture;
	}
	
	/**
	 * Gets whether or not a point is inside the gui component
	 * @param x the x value of the point to compare
	 * @param y the y value of the point to compare
	 * @return whether the point is in bounds or not
	 */
	public boolean inBounds(int x, int y) 
	{
		return (x > (this.x * Display.getWidth() * 0.5F) && 
				x < (this.x * Display.getWidth() * 0.5F) + (Display.getWidth() * width * 0.5F) && 
				y > (this.y * Display.getHeight() * 0.5F) && 
				y < (Display.getHeight() * this.y * 0.5F) + (Display.getHeight() * height * 0.5F));
	}	
	
	/**
	 * An overridable method to perform an action when the GuiComponent is clicked. All GuiComponents must implement
	 * this.
	 * @param x the x position of the mouse, from 0 to Display.getWidth()
	 * @param y the y position of the mouse, from 0 to Display.getHeight()
	 */
	public abstract void onClick(int x, int y);
	
	/**
	 * An overridable method to draw the gui component. All GuiComponents must implement this.
	 */
	public abstract void draw();
}
