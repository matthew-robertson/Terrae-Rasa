package client.render;



import math.MathHelper;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import client.Settings;
import client.hardware.Keys;

//TODO: Prevent keybinds from overwriting each other

public class MainSettingsMenu extends Render
{
	public boolean open;
	private boolean isGraphicsMenuOpen;
	private boolean isOptionsMenuOpen;
	private boolean isControlsMenuOpen;
	private GuiTitle mainMenuTitle;
	private GuiButton saveAndQuitButton;
	private GuiButton optionsButton;

	private GuiTitle optionsTitle;
	private GuiButton graphicsButton;
	private GuiButton keybindsButton;
	private GuiSlideBar musicVolumeSlider;
	private GuiSlideBar soundVolumeSlider;
	private GuiButton autosaveButton;
	
	private GuiTitle keybindsTitle;
	private GuiButton inventoryToggleButton;
	private boolean settingInventoryKeybind;
	
	private GuiTitle graphicsTitle;
	
	public MainSettingsMenu(Settings settings)
	{
		mainMenuTitle = (GuiTitle) new GuiTitle("Settings", 0.2, 10, 0.6, 0.1).setStopVerticalScaling(true);
		optionsButton = (GuiButton) new GuiButton(new String[] { "Options" }, 0.3, 50, 0.4, 0.1).setStopVerticalScaling(true);
		saveAndQuitButton = (GuiButton) new GuiButton(new String[] { "Close" }, 0.3, 90, 0.4, 0.1).setStopVerticalScaling(true);
		
		//Options Part of the menu
		optionsTitle = (GuiTitle) new GuiTitle("Options", 0.2, 10, 0.6, 0.1).setStopVerticalScaling(true);
		musicVolumeSlider = (GuiSlideBar) new GuiSlideBar("Music Volume: ", settings.musicVolume, 0.075, 50, 0.4, 0.1).setStopVerticalScaling(true);		
		soundVolumeSlider = (GuiSlideBar) new GuiSlideBar("Sound Volume: ", settings.soundVolume, 0.525, 50, 0.4, 0.1).setStopVerticalScaling(true);		
		autosaveButton = (GuiButton) new GuiButton(new String[] { 
				"Autosave off", "Autosave 3 Minutes", "Autosave 5 Minutes", "Autosave 10 Minutes", "Autosave 30 Minutes"
			}, 0.325, 90, 0.35, 0.1 ).setStopVerticalScaling(true);
		graphicsButton = (GuiButton) new GuiButton(new String[] { "Graphics" }, 0.15, 130, 0.3, 0.1).setStopVerticalScaling(true);
		keybindsButton = (GuiButton) new GuiButton(new String[] { "Controls" }, 0.55, 130, 0.3, 0.1).setStopVerticalScaling(true);
		
	
		//Graphics
		graphicsTitle = new GuiTitle("Graphic Options", 0.2, 10, 0.6, 0.1);
		
		//Keybinds
		keybindsTitle = new GuiTitle("Keybinds", 0.2, 10, 0.6, 0.1);
		inventoryToggleButton = (GuiButton) new GuiButton(new String[] { "Inventory: " + Keyboard.getKeyName(settings.keybinds.inventoryToggle)},
				0.125, 60, 0.3, 0.1).setStopVerticalScaling(true);
	}
	
	public void render(Settings settings) 
	{
		GL11.glLoadIdentity();
		GL11.glTranslatef(0, 0, -2000f);		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST);		

		drawBackground();		
		drawComponents();	
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);		
	}
	
	/**
	 * Handles mouse events for the pause menu
	 * @param settings the client-wide settings object
	 */
	public void mouse(Settings settings)
	{
		GL11.glColor4f(1, 1, 1, 1);
		
		int x = MathHelper.getCorrectMouseXPosition(); 
		int y = MathHelper.getCorrectMouseYPosition();	
		
		//Return if it isn't a click
		if(!Mouse.isButtonDown(0)) 
		{
			return;
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
		
		if(isOptionsMenuOpen)
		{
			if(isGraphicsMenuOpen)
			{
				return;
			}
			else if(isControlsMenuOpen)
			{
				if(inventoryToggleButton.inBounds(x, y))
				{
					settingInventoryKeybind = true;
				}
				return;
			}
		
			if(autosaveButton.inBounds(x, y))
			{
				autosaveButton.onClick(x, y);
				settings.autosave = !settings.autosave;
			}
			if(graphicsButton.inBounds(x, y))
			{
				isGraphicsMenuOpen = true;
			}
			if(keybindsButton.inBounds(x, y))
			{
				isControlsMenuOpen = true;
			}
			if(musicVolumeSlider.inBounds(x, y))
			{
				musicVolumeSlider.onClick(x, y);
				settings.musicVolume = musicVolumeSlider.getValue();
			}
			if(soundVolumeSlider.inBounds(x, y))
			{
				soundVolumeSlider.onClick(x, y);
				settings.soundVolume = soundVolumeSlider.getValue();
			}
		}
		else
		{
			if(saveAndQuitButton.inBounds(x, y))
			{
				closeCurrentMenu(settings);
				this.open = false;
			}
			if(optionsButton.inBounds(x, y))
			{
				isOptionsMenuOpen = true;
			}
		}			
	}
	
	/**
	 * Draws a black partially transparent quad over the game when the menu is open, to give a more real
	 * impression that the game is paused.
	 */
	private void drawBackground()
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		t.startDrawingQuads();
		t.setColorRGBA_F(0,0, 0, 0.8F);
		t.addVertexWithUV(0, Display.getHeight() / 2, 0, 0, 1);
        t.addVertexWithUV(Display.getWidth() / 2, Display.getHeight() / 2, 0, 1, 1);
        t.addVertexWithUV(Display.getWidth() / 2, 0, 0, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	/**
	 * Draws all the components for a given part of the game menu
	 */
	private void drawComponents()
	{
		if(isOptionsMenuOpen)
		{	
			if(isGraphicsMenuOpen)
			{
				graphicsTitle.draw();	
				return;
			}
			else if(isControlsMenuOpen)
			{
				keybindsTitle.draw();
				inventoryToggleButton.draw();
				return;
			}
			optionsTitle.draw();
			graphicsButton.draw();
			keybindsButton.draw();
			autosaveButton.draw();
			musicVolumeSlider.draw();
			soundVolumeSlider.draw();
		}
		else
		{
			mainMenuTitle.draw();
			optionsButton.draw();
			saveAndQuitButton.draw();
		}
	}
	
	/**
	 * Reads all keyboard input. (put key binds here), also passes input to text boxes 
	 */
	public void keyboard(Settings settings)
	{		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Keys.ec)
		{
			Keys.ec = true;
			closeCurrentMenu(settings);
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			Keys.ec = false;
		}
		
		for( ; Keyboard.next(); handleKeyboardInput(settings)) { } //Very hacky way of letting all keyboard input be recognized
	}	
	
	/**
	 * Handles keyboard typing, such as that to text boxes
	 */
	private void handleKeyboardInput(Settings settings)
	{
		//If there're key events (Letters, numbers, etc typed to a textbox), update the textboxes appropriately
		if(Keyboard.getEventKeyState())
		{
			char character = Keyboard.getEventCharacter();
			int characterValue = Keyboard.getEventKey();
			
			if(settingInventoryKeybind)
			{
				settingInventoryKeybind = false;	
				settings.keybinds.inventoryToggle = characterValue;
				inventoryToggleButton.setValues(new String[] { "Inventory: " + character });
				System.out.println("Inventory_Toggle=" + character + " @int_value=" + characterValue);
			}			
		}
	}
	
	/**
	 * Closes the current menu. This basically means the last menu entered into. Ex. on the main pause menu this will resume the game;
	 * on the keybinds menu it will go back to the options menu.
	 * @param settings
	 */
	private void closeCurrentMenu(Settings settings)
	{
		if(isOptionsMenuOpen)
		{
			if(isGraphicsMenuOpen)
			{
				isGraphicsMenuOpen = false;
				return;
			}
			if(isControlsMenuOpen)
			{
				isControlsMenuOpen = false;
				settingInventoryKeybind = false;
				return;
			}
			isOptionsMenuOpen = false;
		}
		else
		{
			settings.menuOpen = false;
		}
	}
}
