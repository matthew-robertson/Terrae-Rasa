package render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import utils.FileManager;
import utils.MainMenuHelper;
import utils.MathHelper;
import utils.Particle;
import utils.Vector2F;
import client.Settings;
import client.TerraeRasa;
import enums.EnumColor;
import enums.EnumPlayerDifficulty;
import enums.EnumWorldDifficulty;
import enums.EnumWorldSize;

/**
 * MainMenu handles the main game menu functions and functionality. This includes mouse input, keyboard input, and rendering.
 * In general the menu is fairly self contained aside from the settings.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class MainMenu extends Render
{
	private final Random random = new Random();
	private String[] mainMenuFooter = { };
	private String[] mainMenuVarying = { "Single Player", "Multiplayer", "Settings", "Quit" };
	private String[] playerMenuFooter = { "New Player", "Delete Player", "Back" };
	private String[] worldMenuFooter = { "New World", "Delete World", "Back" };
	private String[] worldNames;
	private String[] playerNames;
	private String selectedPlayerName;
	private String selectedWorldName;
	private boolean isMainMenuOpen;
	private boolean isPlayerMenuOpen;
	private boolean isWorldMenuOpen;
	private boolean isDeleteWorldMenuOpen;
	private boolean isDeletePlayerMenuOpen;
	private boolean isNewPlayerMenuOpen;
	private boolean isNewWorldMenuOpen;
	private boolean isWaitingToDelete;
	private boolean flaggedForWorldGen;
	private boolean flaggedForGameStart;
	private List<Particle> stars;
	private List<Particle> starTrails;
	private List<Particle> staticStars;
	private EnumColor[] fieryColors = { EnumColor.FIERY1, EnumColor.FIERY2, EnumColor.FIERY3, EnumColor.FIERY4, EnumColor.FIERY5 };
	private MainMenuHelper menuHelper;
	private FileManager fileManager;
	private MainSettingsMenu settingsMenu;
	private GuiMenu menu;
	private GuiTextbox characterName;
	private GuiButton characterMode;
	private GuiTitle createNewCharacter;
	private GuiTitle stopCreatingCharacter;	
	private GuiTextbox worldName;
	private GuiButton worldMode;
	private GuiButton worldSize;
	private GuiTitle createNewWorld;
	private GuiTitle stopCreatingWorld;		
	private GuiTitle deleteWorldBack;
	private GuiTitle deleteWorldConfirm;
	private GuiTitle deleteWorldMessage;
	private GuiTitle deletePlayerBack;
	private GuiTitle deletePlayerConfirm;
	private GuiTitle deletePlayerMessage;	
	
	/**
	 * Creates a new MainMenu, initializing all the particles and components required.
	 */
	public MainMenu(Settings settings)
	{
		menuHelper = new MainMenuHelper();
		fileManager = new FileManager();
		settingsMenu = new MainSettingsMenu(settings);
		worldNames = menuHelper.getWorldFileNames();
		playerNames = menuHelper.getPlayerFileNames();
		isMainMenuOpen = true;
		selectedPlayerName = "";
		selectedWorldName = "";
		stars = new ArrayList<Particle>();
		starTrails = new ArrayList<Particle>();
		for(int i = 0; i < 5; i++)
		{
			Particle star = new Particle();
			star.active = true;
			star.life = 1.0;
			star.fade = 0.02;
			star.texture = Render.starParticleBackground;
		    star.r = 1;
		    star.g = 1;
		    star.b = 1;
		    star.a = 1;
		    star.x = (-1 * random.nextInt(200));
		    star.y = (-1 * random.nextInt(200));
		    star.zrot = random.nextInt(100);
		    star.velocity = new Vector2F(random.nextFloat() * 3, random.nextFloat() * 3);
		    star.acceleration = new Vector2F(random.nextFloat() / 20, random.nextFloat() / 20);
			stars.add(star);
		}
		staticStars = new ArrayList<Particle>();
		for(int i = 0; i < 7; i++)
		{
			Particle star = new Particle();
			star.active = true;
			star.life = 1 + random.nextDouble();
			star.fade = 0.01;
			star.texture = Render.starParticleBackground;
		    star.r = 1;
		    star.g = 1;
		    star.b = 1;
		    star.a = 1;
		    star.rotateRight = random.nextBoolean();
		    star.x = random.nextInt((int) (Display.getWidth() * 0.5));
		    star.y = random.nextInt((int) (Display.getHeight() * 0.5));
		    star.zrot = random.nextInt(100);
		    star.velocity = new Vector2F(0, 0);
		    star.acceleration = new Vector2F(0, 0);
			staticStars.add(star);
		}
		
		//Character Creation Menu:
		characterName = (GuiTextbox) new GuiTextbox(0.225, 70, 0.55, 0.1).setStopVerticalScaling(true);
		characterName.setRenderTexture(Render.menuButtonBackground);
		characterMode = (GuiButton) new GuiButton(EnumPlayerDifficulty.getAllEnumAsStringArray(), 0.25, 100, 0.50, 0.1).setStopVerticalScaling(true);
		characterMode.setRenderTexture(Render.menuButtonBackground);
		createNewCharacter = (GuiTitle) new GuiTitle("Create Character", .30, 130, .20, 0.1).setStopVerticalScaling(true);
		stopCreatingCharacter = (GuiTitle) new GuiTitle("Back", 0.60, 130, 0.2, 0.1).setStopVerticalScaling(true);
		//World Creation Menu:
		worldName = (GuiTextbox) new GuiTextbox(0.225, 70, 0.55, 0.1).setStopVerticalScaling(true);
		worldName.setRenderTexture(Render.menuButtonBackground);
		worldMode = (GuiButton) new GuiButton(EnumWorldDifficulty.getAllEnumAsStringArray(), 0.25, 100, 0.50, 0.1).setStopVerticalScaling(true);
		worldMode.setRenderTexture(Render.menuButtonBackground);
		worldSize = (GuiButton) new GuiButton(EnumWorldSize.getAllEnumAsStringArray(), 0.25, 130, 0.50, 0.1).setStopVerticalScaling(true);
		worldSize.setRenderTexture(Render.menuButtonBackground);
		createNewWorld = (GuiTitle) new GuiTitle("Create World", .30, 160, .20, 0.1).setStopVerticalScaling(true);
		stopCreatingWorld = (GuiTitle) new GuiTitle("Back", 0.60, 160, 0.2, 0.1).setStopVerticalScaling(true);
		//Delete Confirmation Menu(world):
		deleteWorldMessage = new GuiTitle("Are you sure?", 0.3, 0.25, 0.4, 0.1);
		deleteWorldBack = new GuiTitle("Back", .25, .4, .20, 0.1);
		deleteWorldConfirm = new GuiTitle("Confirm", 0.55, .4, 0.2, 0.1);
		//Delete Confirmation Menu(player):
		deletePlayerMessage = new GuiTitle("Are you sure?", 0.3, 0.25, 0.4, 0.1);
		deletePlayerBack = new GuiTitle("Back", .25, .4, .20, 0.1);
		deletePlayerConfirm = new GuiTitle("Confirm", 0.55, .4, 0.2, 0.1);
		menu = new GuiMenu(0.02, 0.05, 0.25, 0.90, "Main Menu", mainMenuVarying, mainMenuFooter);
	}
	
	/**
	 * Handles the entire menu - first taking hardware input (keyboard/mouse) then rendering
	 * the appropriate menu.
	 */
	public void render(Settings settings)
	{
		GL11.glLoadIdentity();
		GL11.glTranslatef(0, 0, -2000f);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST);	

		if(flaggedForWorldGen)
		{
			flaggedForWorldGen = false;
			fileManager.generateAndSaveWorld(worldName.getText(), EnumWorldSize.getSize(worldSize.getValue()), EnumWorldDifficulty.getDifficulty(worldMode.getValue()));
			updateMenus();
			isNewWorldMenuOpen = false;
			worldName.setText("");
			worldName.freeFocused();
		}
		if(flaggedForGameStart)
		{
			try {
				playGame();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if(settingsMenu.open)
		{
			settingsMenu.mouse(settings);
			settingsMenu.keyboard(settings);
		}
		else
		{
			keyboard();
			mouse();
		}
		renderBackground();
		renderShootingStarAnimation();
		renderStaticStarAnimation();
		renderComponents();
		renderVersion();
		
		if(flaggedForWorldGen)
		{
			overwriteWithLoadingScreen();
		}
		if(flaggedForGameStart)
		{
			overwriteWithGameLoadScreen();
		}		
		if(settingsMenu.open)
		{
			settingsMenu.render(settings);
		}
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
	
	/**
	 * Renders the stars that move around and twinkle in the background
	 */
	private void renderStaticStarAnimation()
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		
		//Render and update every star
		for(Particle star : staticStars)
		{
			if(!settingsMenu.open)
			{
				star.integrate();
				if(star.rotateRight) 
					star.zrot += random.nextDouble() * (random.nextInt(6));
				else
					star.zrot -= random.nextDouble() * random.nextInt(6);				
				star.index += 0.5;
				if(star.index >= 16)
					star.index = 0;
			}
				
			double width = 13;
			double height = 13;
			double xoff = star.x;
			double yoff = star.y;
		    
			Render.starAnimationSheet.bind();
		    t.startDrawingQuads();
		    double u = (double)((int)(star.index) * 32) / 512;
		    double v = 0;
		    t.setColorRGBA_F((float)star.r, (float)star.g, (float)star.b, (float)star.a * 0.8F);
		    t.addVertexWithUV(xoff, yoff + height, 0, u, v + 1);
		    t.addVertexWithUV(xoff + width, yoff + height, 0, u + (32.0 / 512), v + 1);
		    t.addVertexWithUV(xoff + width, yoff, 0, u + (32.0 / 512), v);
		    t.addVertexWithUV(xoff, yoff, 0, u, v);
			t.draw();
		    
			//Reset the background star if it's off the screen
			if(star.life <= 0)
			{
				star.active = true;
				star.life = 1;
				star.fade = (1.0 / 32.0);
				star.texture = Render.starParticleBackground;
			    star.r = 1;
			    star.g = 1;
			    star.b = 1;
			    star.a = 1;
			    star.index = 0;
			    star.rotateRight = random.nextBoolean();
			    star.x = random.nextInt((int) (Display.getWidth() * 0.5));
			    star.y = random.nextInt((int) (Display.getHeight() * 0.5));
			    star.zrot = random.nextInt(100);
			    star.velocity = new Vector2F(0, 0);
			    star.acceleration = new Vector2F(0, 0);
				
			}
		}	
		
		GL11.glLoadIdentity();
		GL11.glTranslated(0, 0, -2000);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4d(1, 1, 1, 1);
	}
	
	/**
	 * Renders anything that is animated, specifically particles such as the stars.
	 */
	private void renderShootingStarAnimation()
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		
		//Render and update the star trail particles, killing them if they've died
		double width = 2.6;
		double height = 2.6;
		Render.starParticleBackground.bind();
		t.startDrawingQuads();
		Iterator<Particle> iterator = starTrails.iterator();
		while(iterator.hasNext())
		{
			Particle starTrail = iterator.next();
			if(!settingsMenu.open)
			{
				starTrail.integrate();
			}
			t.setColorRGBA_F((float)starTrail.r, (float)starTrail.g, (float)starTrail.b, (float)starTrail.life);
			t.addVertexWithUV(starTrail.x, starTrail.y + height, 0, 0, 1);
		    t.addVertexWithUV(starTrail.x + width, starTrail.y + height, 0, 1, 1);
		    t.addVertexWithUV(starTrail.x + width, starTrail.y, 0, 1, 0);
		    t.addVertexWithUV(starTrail.x, starTrail.y, 0, 0, 0);
		    
		    if(starTrail.life <= 0)
		    {
		    	iterator.remove();
		    }
		    
		}
		t.draw();
		
		//The actual stars
		for(Particle star : stars)
		{
			if(!settingsMenu.open)
			{
				star.integrate();
			}
			width = 13;
			height = 13;
			double xoff = 0;
			double yoff = 0;
			
			//Back(1/4)
			Render.starParticleBackground.bind();
			t.startDrawingQuads();
			t.setColorRGBA_F((float)star.r, (float)star.g, (float)star.b, (float)star.a * 0.4F);
			t.addVertexWithUV(star.x + xoff, star.y + yoff + height, 0, 0, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff + height, 0, 1, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff, 0, 1, 0);
		    t.addVertexWithUV(star.x + xoff, star.y + yoff, 0, 0, 0);
			t.draw();

		    //Three Quarters (3/4)
			Render.starParticleForeground.bind();
		    t.startDrawingQuads();
		    t.setColorRGBA_F((float)star.r, (float)star.g, (float)star.b, (float)star.a * 0.8F);
		    t.addVertexWithUV(star.x + xoff, star.y + yoff + height, 0, 0, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff + height, 0, 1, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff, 0, 1, 0);
		    t.addVertexWithUV(star.x + xoff, star.y + yoff, 0, 0, 0);
			t.draw();
		    
			if(!settingsMenu.open)
			{
				//Star Trail particles
				for(int i = 0; i < 6; i++)
				{
					Particle starTrail = new Particle();
					starTrail.active = true;
					starTrail.life = 1.0;
					starTrail.fade = random.nextDouble() / 25 + 0.04;
					starTrail.texture = Render.starParticleBackground;
					EnumColor color = fieryColors[random.nextInt(fieryColors.length)];
					starTrail.r = color.COLOR[0];
					starTrail.g = color.COLOR[1];
				    starTrail.b = color.COLOR[2];
				    starTrail.a = 1;
				    starTrail.x = star.x - 2 + random.nextInt(4) + (width / 2);
				    starTrail.y = star.y - 2 + random.nextInt(4) + (height / 2);
				    starTrail.zrot = random.nextInt(100);
				    starTrail.velocity = new Vector2F(star.velocity.x * -0.35F, star.velocity.y * -0.35F);
				    starTrail.acceleration = new Vector2F(star.acceleration.x * -0.35F, star.acceleration.y * -0.35F);
				    starTrails.add(starTrail);
				}
			}
			
			//Reset the shooting star if it's off the screen
			if(star.x > Display.getWidth() * 0.6 || star.y > Display.getHeight() * 0.6)
			{
				star.active = true;
				star.life = 1.0;
				star.fade = 0.02;
				star.texture = Render.starParticleBackground;
			    star.r = 1;
			    star.g = 1;
			    star.b = 1;
			    star.a = 1;
			    star.x = (-1 * random.nextInt((int)(Display.getWidth() * 0.3)));
			    star.y = (-1 * random.nextInt((int)(Display.getHeight() * 0.3)));
			    star.zrot = random.nextInt(100);
			    float velocityModifier = 1;
			    if(random.nextInt(6) == 0)
			    {
			    	velocityModifier *= 2;
			    }
			    star.velocity = new Vector2F(velocityModifier * random.nextFloat() * 2 + 1, velocityModifier * random.nextFloat() * 3);
			    star.acceleration = new Vector2F(random.nextFloat() / 15, random.nextFloat() / 15);
			}
		}	
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4d(1, 1, 1, 1);
	}
	
	/**
	 * Renders the starry background and logo.
	 */
	private void renderBackground()
	{
		//Starry background
		int width = 450;
		int height = 250;
		background_menu.bind();
		t.startDrawingQuads();
		for(int x = 0; x < (Display.getWidth() / width) + 1; x++)
		{
			for(int y = 0; y < (Display.getHeight() / height) + 1; y++)
			{
		        t.setColorRGBA_F(1, 1, 1, 1);
		        t.addVertexWithUV(x * width, (y + 1) * height, 0, 0, 1);
		        t.addVertexWithUV((x + 1) * width, (y + 1) * height, 0, 1, 1);
		        t.addVertexWithUV((x + 1) * width, y * height, 0, 1, 0);
		        t.addVertexWithUV(x * width, y * height, 0, 0, 0);
			}
		}
        t.draw();
		
        //Logo
        width = 300;
        height = 63;
        int xoff = (int) (0.15 * Display.getWidth());
        int yoff = 0;
        logo.bind();
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(xoff, yoff + height, 0, 0, 1);
        t.addVertexWithUV(xoff + width, yoff + height, 0, 1, 1);
        t.addVertexWithUV(xoff + width, yoff, 0, 1, 0);
        t.addVertexWithUV(xoff, yoff, 0, 0, 0);
        t.draw();
	}

	/**
	 * Distributes mouse input as appropriate for the menu open
	 */
	private void mouse()
	{
		if(isMainMenuOpen || isPlayerMenuOpen || isWorldMenuOpen)
		{
			int index = menu.getCellWithoutScroll(MathHelper.getCorrectMouseXPosition(), MathHelper.getCorrectMouseYPosition());
			menu.setHighlighted(index);
		}
		if(!Mouse.isButtonDown(0))
		{
			return;
		}

		int x = MathHelper.getCorrectMouseXPosition();
		int y = MathHelper.getCorrectMouseYPosition();
		
		try {
			Mouse.destroy();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		
		if(isDeletePlayerMenuOpen)
		{
			mouseMenuDeleteCharacter(x, y);
		}
		else if(isDeleteWorldMenuOpen)
		{
			mouseMenuDeleteWorld(x, y);
		}
		else if(isNewPlayerMenuOpen)
		{
			mouseMenuCreateCharacter(x, y);
		}
		else if(isNewWorldMenuOpen)
		{
			mouseMenuCreateWorld(x, y);
		}
		
		else if(isMainMenuOpen)
		{
			mouseMainMenu(x, y);
		}
		else if(isPlayerMenuOpen)
		{
			mousePlayerMenu(x, y);
		}
		else if(isWorldMenuOpen)
		{
			mouseWorldMenu(x, y);
		}
	}

	/**
	 * Handles the input for the delete character menu, which contains a confirm and 
	 * delete button.
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 */
	private void mouseMenuDeleteCharacter(int mouseX, int mouseY)
	{		
		//Confirm Button
		if(deletePlayerConfirm.inBounds(mouseX, mouseY))
		{
			deleteCharacter(selectedPlayerName);
			updateMenus();
			isDeletePlayerMenuOpen = false;
			isWaitingToDelete = false;
		}
		//Back button
		if(deletePlayerBack.inBounds(mouseX, mouseY))
		{
			isDeletePlayerMenuOpen = false;
			isWaitingToDelete = false;
		}
	}

	/**
	 * Handles the mouse input for the player create menu. This involves a name textbox, 
	 * the mode select button, and confirm/back buttons.
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 */
	private void mouseMenuCreateCharacter(int mouseX, int mouseY)
	{
		//Player name text field
		if(characterName.inBounds(mouseX, mouseY))
		{
			characterName.setFocused();
		}			
		//Player Mode Button
		if(characterMode.inBounds(mouseX, mouseY))
		{
			characterMode.onClick(mouseX, mouseY);
		}		
		//Create new player button
		if(createNewCharacter.inBounds(mouseX, mouseY))
		{
			fileManager.generateAndSavePlayer(characterName.getText(), EnumPlayerDifficulty.getDifficulty(characterMode.getValue()));
			updateMenus();
			isNewPlayerMenuOpen = false;
			characterName.setText("");
			characterName.freeFocused();
		}
		//Back button
		if(stopCreatingCharacter.inBounds(mouseX, mouseY))
		{
			isNewPlayerMenuOpen = false;
			characterName.setText("");
			characterName.freeFocused();
		}
	}

	/**
	 * Handles mouse input for the delete world menu, which contains a confirm
	 * and back button.
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 */
	private void mouseMenuDeleteWorld(int mouseX, int mouseY)
	{
		//Confirm Button
		if(deleteWorldConfirm.inBounds(mouseX, mouseY))
		{
			deleteWorld(selectedWorldName);
			updateMenus();
			isDeleteWorldMenuOpen = false;
			isWaitingToDelete = false;
		}
		//Back button
		if(deleteWorldBack.inBounds(mouseX, mouseY))
		{
			isDeleteWorldMenuOpen = false;
			isWaitingToDelete = false;
		}
	}

	/**
	 * Handles all the mouse input for the create world menu. This holds fields for
	 * the world's name, the world mode, the world size (soon to be eliminated), 
	 * as well as 'create' and 'back' buttons.
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 */
	private void mouseMenuCreateWorld(int mouseX, int mouseY)
	{
		//World Name Text Box
		if(worldName.inBounds(mouseX, mouseY))
		{
			worldName.setFocused();
		}		
		//World Mode Button
		if(worldMode.inBounds(mouseX, mouseY))
		{
			worldMode.onClick(mouseX, mouseY);
		}
		//World Size Button
		if(worldSize.inBounds(mouseX, mouseY))
		{
			worldSize.onClick(mouseX, mouseY);
		}
		//Create World 'confirm' button
		if(createNewWorld.inBounds(mouseX, mouseY))
		{
			flaggedForWorldGen = true;
		}
		//Back button
		if(stopCreatingWorld.inBounds(mouseX, mouseY))
		{
			isNewWorldMenuOpen = false;
			worldName.setText("");
			worldName.freeFocused();
		}
	}
	
	/**
	 * Handles mouse input for the main menu - this gives options for single player,
	 * multiplayer, settings, and also to quit the game.
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 */
	private void mouseMainMenu(int mouseX, int mouseY)
	{
		menu.onClick(mouseX, mouseY);
		
		int selectedMenuIndex = menu.getCellWithoutScroll(mouseX, mouseY);
		if(selectedMenuIndex == 1) //SP
		{
			isPlayerMenuOpen = true;
			menu.updateLockedInComponents(playerMenuFooter);
			menu.updateTitle("Players");
			menu.updateVaryingItems(playerNames);
			isMainMenuOpen = false;
		}
		if(selectedMenuIndex == 2) //MP
		{
			Sys.alert("Feature Not Available", "This feature will be patched in soon. It is currently not available."); 
		}
		if(selectedMenuIndex == 3) //Settings
		{
			settingsMenu.open = true;
		}
		if(selectedMenuIndex == 4) //Quit
		{
			TerraeRasa.done = true;
		}
	}
	
	/**
	 * Handles mouse input for the player select menu - which lists all the different players
	 * as well as options to create/delete players and go back to the main menu
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 */
	private void mousePlayerMenu(int mouseX, int mouseY)
	{
		menu.onClick(mouseX, mouseY);
		int selectedIndex = menu.getSelectedCell(mouseX, mouseY);
		if(selectedIndex == menu.getNumberOfItems() - 3)
		{
			//New Player
			isNewPlayerMenuOpen = true;
		}
		else if(selectedIndex == menu.getNumberOfItems() - 2)
		{
			//Delete Player
			isWaitingToDelete = true;
		}
		else if(selectedIndex == menu.getNumberOfItems() - 1)
		{
			//Back
			isPlayerMenuOpen = false;
			isMainMenuOpen = true;
			isWaitingToDelete = false;
			menu.updateLockedInComponents(mainMenuFooter);
			menu.updateTitle("Main Menu");
			menu.updateVaryingItems(mainMenuVarying);
		}
		else if(selectedIndex > 0 && selectedIndex < menu.getVaryingItems().length + 1)
		{
			//If the user is waiting to delete something, then prompt them with a delete confirm thing, 
			//otherwise go to the world select part
			if(isWaitingToDelete)
			{
				isDeletePlayerMenuOpen = true;
				selectedPlayerName = menu.getVaryingItems()[selectedIndex - 1];
				deletePlayerMessage.setText("Delete " + selectedPlayerName + "?");
			}
			else
			{
				selectedPlayerName = menu.getVaryingItems()[selectedIndex - 1];
				isPlayerMenuOpen = false;
				isWorldMenuOpen = true;
				menu.updateLockedInComponents(worldMenuFooter);
				menu.updateTitle("Worlds");
				menu.updateVaryingItems(worldNames);
			}
		}
	}
	
	/**
	 * Handles mouse input for the world select menu - which lists all the different worlds, as well as
	 * the option to create/delete worlds and go back
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 */
	private void mouseWorldMenu(int mouseX, int mouseY)
	{
		menu.onClick(mouseX, mouseY);
		
		int selectedIndex = menu.getSelectedCell(mouseX, mouseY);
		if(selectedIndex == menu.getNumberOfItems() - 3)
		{
			//New World
			isNewWorldMenuOpen= true;
		}
		else if(selectedIndex == menu.getNumberOfItems() - 2)
		{
			//Delete World
			isWaitingToDelete = true;			
		}
		else if(selectedIndex == menu.getNumberOfItems() - 1)
		{
			//Back
			isPlayerMenuOpen = true;
			isWorldMenuOpen = false;
			isWaitingToDelete = false;
			selectedPlayerName = "";
			menu.updateLockedInComponents(playerMenuFooter);
			menu.updateTitle("Players");
			menu.updateVaryingItems(playerNames);
		}
		else if(selectedIndex > 0 && selectedIndex < menu.getVaryingItems().length + 1)
		{
			//If the user wants to delete a world, open that confirm part of the menu,
			//Otherwise, flag the menu to start playing the game. (allowing activation of a 
			//Impromptu loading screen)
			if(isWaitingToDelete)
			{
				selectedWorldName = menu.getVaryingItems()[selectedIndex - 1];
				isDeleteWorldMenuOpen = true;
				deleteWorldMessage.setText("Delete " + selectedWorldName + "?");
			}
			else
			{
				selectedWorldName = menu.getVaryingItems()[selectedIndex - 1];
				isWorldMenuOpen = false;
				flaggedForGameStart = true;
			}
		}
	}
	
	/**
	 * Starts the game and closes the main menu.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void playGame() 
			throws IOException, ClassNotFoundException
	{
		TerraeRasa.startGame(selectedWorldName, fileManager.loadWorld("Earth", selectedWorldName), fileManager.loadPlayer(selectedPlayerName));
	}
	
	/**
	 * Handles keyboard input. For the menu this means distributing typing to text boxes.
	 */
	private void keyboard()
	{	
		for( ; Keyboard.next(); handleKeyboardInput()) { } //Very hacky way of letting all keyboard input be recognized
	}
	
	/**
	 * Handles keyboard typing, such as that to text boxes
	 */
	private void handleKeyboardInput()
	{
		//If there're key events (Letters, numbers, etc typed to a textbox), update the textboxes appropriately
		if(Keyboard.getEventKeyState())
		{
			if(worldName.isFocused()) //World name text box
			{
				worldName.updateText(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
			else if(characterName.isFocused()) //character name textbox
			{
				characterName.updateText(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
			else //otherwise, throw away the input
			{
				Keyboard.getEventCharacter();
				Keyboard.getEventKey();
			}
		}
	}
	
	/**
	 * Renders the appropriate menu components for the menu that is open
	 */
	private void renderComponents()
	{		
		if(isDeletePlayerMenuOpen) //character deletion screen
		{
			deletePlayerBack.draw();
			deletePlayerConfirm.draw();
			deletePlayerMessage.draw(); 
		}
		else if(isDeleteWorldMenuOpen) //world deletion screen
		{
			deleteWorldBack.draw();
			deleteWorldConfirm.draw();
			deleteWorldMessage.draw();
		}
		else if(isNewPlayerMenuOpen) //Character creation
		{
			characterName.draw();
			characterMode.draw();		
			createNewCharacter.draw();
			stopCreatingCharacter.draw();	
		}
		else if(isNewWorldMenuOpen) //World creation
		{
			worldName.draw();
			worldMode.draw();
			worldSize.draw();
			createNewWorld.draw();
			stopCreatingWorld.draw();
		}
		else
		{
			menu.draw();
		}		
	}
	
	/**
	 * Renders text for the version of the game.
	 */
	private void renderVersion()
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0, 0, 0, 1);
		double width = trueTypeFont.getWidth(TerraeRasa.getVersion()) * 0.5;
		double height = 20;
		double x = Display.getWidth() / 2 - 10 - width;
		double y = Display.getHeight() / 2 - 40;
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 1, 1, 1);
		trueTypeFont.drawString(Display.getWidth() / 2 - 10, (Display.getHeight() / 2) - 20, TerraeRasa.getVersion(), 0.5f, -0.5f, TrueTypeFont.ALIGN_RIGHT);
	}
	
	/**
	 * Updates the contents of the playerNames, and worldNames arrays. Then, if either of those menus are open
	 * the left-side menu is refreshed.
	 */
	private void updateMenus()
	{
		worldNames = menuHelper.getWorldFileNames();
		playerNames = menuHelper.getPlayerFileNames();
		if(isPlayerMenuOpen)
		{
			menu.updateLockedInComponents(playerMenuFooter);
			menu.updateTitle("Players");
			menu.updateVaryingItems(playerNames);
		}
		if(isWorldMenuOpen)
		{
			menu.updateLockedInComponents(worldMenuFooter);
			menu.updateTitle("Worlds");
			menu.updateVaryingItems(worldNames);
		}
	}

	/**
	 * Deletes the specified character in the Player Saves directory
	 * @param name the name of the player to delete
	 * @return whether or not the operation succeeded
	 */
	private boolean deleteCharacter(String name)
	{
		boolean success = fileManager.deletefile("/Player Saves/" + name); //delete the file
		updateMenus();
		return success;
	}
	
	/**
	 * Deletes the specified world in the World Saves directory
	 * @param name the name of the world to delete
	 * @return whether the operation succeeded or not
	 */
	private boolean deleteWorld(String name)
	{
		boolean success = fileManager.deleteWorldSave("/World Saves/" + name); //try to delete the file
		updateMenus();
		return success;
	}
	
	/**
	 * A quick and dirty fix for a load screen when the player decides to generate a world. TODO: Make more accurate.
	 */
	private void overwriteWithLoadingScreen()
	{
		int width = 450;
		int height = 250;
		background_menu.bind();
		t.startDrawingQuads();
		for(int x = 0; x < (Display.getWidth() / width) + 1; x++)
		{
			for(int y = 0; y < (Display.getHeight() / height) + 1; y++)
			{
		        t.setColorRGBA_F(1, 1, 1, 1);
		        t.addVertexWithUV(x * width, (y + 1) * height, 0, 0, 1);
		        t.addVertexWithUV((x + 1) * width, (y + 1) * height, 0, 1, 1);
		        t.addVertexWithUV((x + 1) * width, y * height, 0, 1, 0);
		        t.addVertexWithUV(x * width, y * height, 0, 0, 0);
			}
		}
        t.draw();
		
        width = 300;
        height = 63;
        int xoff = (int) (0.15 * Display.getWidth());
        int yoff = 0;
        logo.bind();
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(xoff, yoff + height, 0, 0, 1);
        t.addVertexWithUV(xoff + width, yoff + height, 0, 1, 1);
        t.addVertexWithUV(xoff + width, yoff, 0, 1, 0);
        t.addVertexWithUV(xoff, yoff, 0, 0, 0);
        t.draw();

        trueTypeFont.drawString(Display.getWidth() * 0.25F, Display.getHeight() * 0.25F, "Generating the world...", 1F, -1F, TrueTypeFont.ALIGN_CENTER);
	
	}
	
	/**
	 * A quick and dirty fix for the load screen between the player starting to play and the game actually being loaded.
	 */
	private void overwriteWithGameLoadScreen()
	{
		int width = 450;
		int height = 250;
		background_menu.bind();
		t.startDrawingQuads();
		for(int x = 0; x < (Display.getWidth() / width) + 1; x++)
		{
			for(int y = 0; y < (Display.getHeight() / height) + 1; y++)
			{
		        t.setColorRGBA_F(1, 1, 1, 1);
		        t.addVertexWithUV(x * width, (y + 1) * height, 0, 0, 1);
		        t.addVertexWithUV((x + 1) * width, (y + 1) * height, 0, 1, 1);
		        t.addVertexWithUV((x + 1) * width, y * height, 0, 1, 0);
		        t.addVertexWithUV(x * width, y * height, 0, 0, 0);
			}
		}
        t.draw();
		
        width = 300;
        height = 63;
        int xoff = (int) (0.15 * Display.getWidth());
        int yoff = 0;
        logo.bind();
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(xoff, yoff + height, 0, 0, 1);
        t.addVertexWithUV(xoff + width, yoff + height, 0, 1, 1);
        t.addVertexWithUV(xoff + width, yoff, 0, 1, 0);
        t.addVertexWithUV(xoff, yoff, 0, 0, 0);
        t.draw();

        trueTypeFont.drawString(Display.getWidth() * 0.25F, Display.getHeight() * 0.25F, "Loading...", 1F, -1F, TrueTypeFont.ALIGN_CENTER);
		
	}
	
}
