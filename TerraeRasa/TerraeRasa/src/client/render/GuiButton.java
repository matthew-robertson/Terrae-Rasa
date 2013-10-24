package client.render;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


/**
 * <code>GuiButton extends GuiComponent</code> <br>
 * GuiButton implements a simple button, with a single background texture. Contains
 * methods to draw and check for mouse clicks as well as
 * a method to get the current String value of the button (for external use)
 *
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GuiButton extends GuiComponent
{	
	/** The possible values for the button. An array of size 1 keeps a constant value. */
	private String[] values;
	/** Which of the values in values[] is being displayed*/
	private int buttonIndex;
	
	/**
	 * Constructs a new instance of GuiButton and initializes the button with the specified
	 * values.
	 * @param values the possible text strings the button can display (and return)
	 * @param x the x position of the button, as a percent of the screen, from 0.0F to 1.0F 
	 * @param y the y position of the button, as a percent of the screen, from 0.0F to 1.0F
	 */
	public GuiButton(String[] values, double x, double y)
	{	
		super();
		this.x = x;
		this.y = y;
		buttonIndex = 0;
		this.values = values;
	}
	
	/**
	 * Constructs a new instance of GuiButton and initializes the button with the specified
	 * values.
	 * @param values the possible text strings the button can display (and return)
	 * @param x the x position of the button, as a percent of the screen, from 0.0F to 1.0F 
	 * @param y the y position of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param width the width of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param height the height of the button, as a percent of the screen, from 0.0F to 1.0F
	 */
	public GuiButton(String[] values, double x, double y, double width, double height)
	{
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		buttonIndex = 0;
		this.values = values;
	}
	
	/**
	 * Returns the String value currently selected in values[]
	 * @return the String value of the currently selected button value
	 */
	public String getValue()
	{
		return values[buttonIndex];
	}
	
	/**
	 * Sets the values[] to the new String[] provided
	 * @param newvals the new String values for the button, this must be an array of at least of size 1
	 */
	public void setValues(String[] newvals)
	{
		values = newvals;
	}
			
	/**
	 * Draws the button, and fixes the position if the screen has been resized
	 */
	public void draw()
	{			
		//Texture:
		GL11.glColor4f(1, 1, 1, 1);
		Tessellator t = Tessellator.instance;
		renderTexture.bind();
		t.startDrawingQuads();
		
		double width = Display.getWidth() * this.width * 0.5F;
		double height = 530 * this.height * 0.5F;
		double x = Display.getWidth() * this.x * 0.5F;
		double y = Display.getHeight() * this.y * 0.5F;
		if(stopVerticalScaling)
		{	
			y = this.y;
		}		
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);		
		t.draw();		
		
		//Text:
		float xOffset = (float) (x + width/2 * 0.95f);//trueTypeFont.getWidth(values[buttonIndex]) / 2;
		float yOffset = (float) (y + height - (height - trueTypeFont.getHeight(values[buttonIndex])) / 2);			
		trueTypeFont.drawString(xOffset, yOffset, values[buttonIndex], 0.7f, -1, TrueTypeFont.ALIGN_CENTER); //Render the Text	
	}
	
	public boolean inBounds(int mouseX, int mouseY) 
	{
		double yoff = (stopVerticalScaling) ? y : this.y * Display.getHeight() * 0.5F;
		return (mouseX > (this.x * Display.getWidth() * 0.5F) && 
				mouseX < (this.x * Display.getWidth() * 0.5F) + (Display.getWidth() * width * 0.5F) && 
				mouseY > (yoff) && 
				mouseY < (yoff) + (530 * height * 0.5F));
	}
	
	/**
	 * Sets the button's value to the given value
	 * @param i the number to set buttonIndex to
	 */
	public void setButtonIndex(int i)
	{
		buttonIndex = i;
	}
	
	/**
	 * Overrides GuiComponent.onClick() to increment the index of the button by 1, and reset the index back to 0 when it 
	 * exceeds the length of the values[]
	 */
	public void onClick(int x, int y)
	{
		buttonIndex++;
		if(buttonIndex >= values.length)
		{
			buttonIndex = 0;
		}
	}	
}

