package render;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


/**
 * <code> GuiSlideBar extends GuiComponent </code> <br>
 * GuiSlideBar implements a simple slider to measure a % value for a setting, etc. It contains methods to 
 * draw and check for mouse clicks as well as a method to get the current value of the slider(a value
 * from 0.0F to 1.0F, indicating the %). Instances of GuiSlideBar scale based on screensize and 
 * the: x, y, width, and heights are measured in respect to the Display size using a double value
 * from 0.0F to 1.0F (where 1.0F is 100%).
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GuiSlideBar extends GuiComponent
{	
	/** The % value of the slide bar, 0.0F is fully left (0%) and 1.0F is fully right (1.0F)*/
	private double value;	
	/** The final % value indicating how large the slide bar's "inner bar" is, from 0.0F to 1.0F */
	private final double BAR_WIDTH = 0.05F;
	/** */
	private String extraInformation;
	
	/**
	 * Constructs a new instance of GuiSlideBar. Initializes the slidebar at the given position and 
	 * and sets the width to 40%, height to 10%, and default gui component texture.
	 * @param x the x position of the button, as a percent of the screen, from 0.0F to 1.0F 
	 * @param y the y position of the button, as a percent of the screen, from 0.0F to 1.0F
	 */
	public GuiSlideBar(double x, double y)
	{
		super();
		this.x = x;
		this.y = y;
		value = 0.5F;
		extraInformation = "";
	}
	
	/**
	 * Constructs a new instance of GuiSlideBar. Initializes the button with the specified
	 * values, and the default GuiComponent texture
	 * @param x the x position of the button, as a percent of the screen, from 0.0F to 1.0F 
	 * @param y the y position of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param width the width of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param height the height of the button, as a percent of the screen, from 0.0F to 1.0F
	 */
	public GuiSlideBar(double x, double y, double width, double height)
	{
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = 0.5F;
		extraInformation = "";
	}

	/**
	 * Constructs a new instance of GuiSlideBar. Initializes the button with the specified
	 * values, and the default GuiComponent texture
	 * @param extra a String of extra information to render over the background texture
	 * @param value the value of the slide bar in %, from 0.0F to 1.0F
	 * @param x the x position of the button, as a percent of the screen, from 0.0F to 1.0F 
	 * @param y the y position of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param width the width of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param height the height of the button, as a percent of the screen, from 0.0F to 1.0F
	 */
	public GuiSlideBar(String extra, double value, double x, double y, double width, double height)
	{
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = value;
		extraInformation = extra;
	}
	
	/**
	 * Returns a double value representing how far over to the right the slide bar is. 0.0 represents fully left (0%)
	 * while 1.0 represents fully right (100%)
	 * @return a double value from 0.0 to 1.0, indicating the % value of the slide bar
	 */
	public double getValue()
	{
		return value;
	}
		
	/**
	 * Draws the button, and fixes the position if the screen has been resized
	 */
	public void draw()
	{			
		//Background
		GL11.glColor4f(1, 1, 1, 1);
		Tessellator t = Tessellator.instance;
		renderTexture.bind();
		
		double width = Display.getWidth() * this.width * 0.5F;
		double height = 530 * this.height * 0.5F;
		double x = Display.getWidth() * this.x * 0.5F;
		double y = Display.getHeight() * this.y * 0.5F;
		if(stopVerticalScaling)
		{
			y = this.y;
		}
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);		
		t.draw();
		
		//The "slide bar"
		double barPosition = ((this.value - (BAR_WIDTH * 0.5F)) < 0) ? 0 : ((this.value - (BAR_WIDTH * 0.5F)) > 0.95F) ? 0.95F : 
			(this.value - (BAR_WIDTH * 0.5F));
		double width1 = Display.getWidth() * this.width * 0.5F * BAR_WIDTH;
		double x1 = (Display.getWidth() * this.x * 0.5F) + (barPosition * this.width * Display.getWidth() * 0.5F);
		
		t.startDrawingQuads();
		t.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);		
	    t.addVertexWithUV(x1, y + height, 0, 0, 1);
	    t.addVertexWithUV(x1 + width1, y + height, 0, 1, 1);
	    t.addVertexWithUV(x1 + width1, y, 0, 1, 0);
	    t.addVertexWithUV(x1, y, 0, 0, 0);		
		t.draw();
		
		//Text indicating %
		float xOffset = (float) (x + width/2 * 0.95f);
		float yOffset = (float) (y + height - (height - trueTypeFont.getHeight("" + value)) / 2);
			
		trueTypeFont.drawString(xOffset, yOffset, extraInformation + String.format("%2d", (int)(value*100)) + "%", 0.7F, -1, TrueTypeFont.ALIGN_CENTER);
	}
	
	/**
	 * Overrides GuiComponent.onClick() to move the slider based on where the mouse is in the slider
	 */
	public void onClick(int x, int y)
	{
		double newValue = (x - (Display.getWidth() * 0.5F * this.x)) / (Display.getWidth() * 0.5F * (this.width));
		this.value = newValue;
	}
}

