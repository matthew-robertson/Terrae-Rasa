package net.dimensia.src;

import net.dimensia.client.Dimensia;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderMenu extends Render
{
	private boolean isGraphicsMenuOpen;
	private boolean isControlsMenuOpen;
	private GuiButtonImproved saveAndQuitButton;
	
	public RenderMenu()
	{
		saveAndQuitButton = new GuiButtonImproved(new String[] { "Save And Quit" }, 0.3F, 0.2F, false);
	}
	
	public void render(World world, Settings settings) 
			throws LWJGLException
	{
		GL11.glLoadIdentity();
		GL11.glTranslatef(0, 0, -2000f);		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST);			

		drawComponents();
		
		keyboard(settings);
		mouseEvents();
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);		
	}
	
	private void drawComponents()
	{
		saveAndQuitButton.draw();
	}
	
	private void mouseEvents()
	{
		GL11.glColor4f(1, 1, 1, 1);
		
		int x = MathHelper.getCorrectMouseXPosition(); 
		int y = MathHelper.getCorrectMouseYPosition();	
	
		if(isGraphicsMenuOpen)
		{
			
		}
		else if(isControlsMenuOpen)
		{
			
		}
		else
		{
			
		}
		
	//.....
		/*
		if(isDeletingCharacter) //Character Deletion screen
		{
			if(deletePlayerConfirm.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(34);
			}
			if(deletePlayerBack.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(35);
			}
		}
		*/
		//etc,
	
	}
	
	/**
	 * Due to the menu structure, this selects one of 30+ different mouse events based on what was clicked
	 * @param buttonPushed which button event to activate
	 */
	private void mouseClicks(int buttonPushed)
	{
		if(!Mouse.isButtonDown(0)) //Stupid Check
		{
			throw new RuntimeException("Stop failing at mouse events");
		}
		
		try
		{
			Mouse.destroy();
			Mouse.create();
		} 
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	
		//System.out.println("Mouse Clicked on Button: " + buttonPushed);
		
		//Button Events By ID:
		//Main Menu:
		if(buttonPushed == 0) //Play
		{
			//isPlayMenuOpen = true;
			//onMenuOpen();
		}
	
	}
	/**
	 * Reads all keyboard input. (put key binds here), also passes input to text boxes 
	 */
	private void keyboard(Settings settings)
	{		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Keys.ec && !Dimensia.isMainMenuOpen)
		{
			Keys.ec = true;
			closeCurrentMenu(settings);
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			Keys.ec = false;
		}
	}	
	
	private void closeCurrentMenu(Settings settings)
	{
		if(isGraphicsMenuOpen)
		{
			isGraphicsMenuOpen = false;
		}
		else
		{
			settings.menuOpen = false;
		}
	}
}
