package client.hardware;

import java.util.Vector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import transmission.ClientUpdate;
import world.World;
import client.Keybinds;
import client.Settings;
import client.entities.EntityPlayer;
import entry.SPGameEngine;
import entry.SPGameLoop;
import enums.EnumHardwareInput;

/**
 * <code>Keys</code> is responsible for handling most keyboard input within the application. Keyboard input
 * for the main menu is self-contained in <code>GuiMainMenu</code>, but all other keyboard input
 * is handled here. Currently only implements one method {@link #keyboard(World, EntityPlayer, Settings)}
 * to handle ingame key binds. All methods in Keys are static, so it cannot be instantiated.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Keys
{	
	private static boolean actionKeys[] = new boolean[12];
	private static boolean ic;
	private static boolean enterDown;
	public static boolean ec;
	public static boolean lshiftDown;
	
	/**
	 * Everything in keys is static, so no instances may be created.
	 */
	private Keys()
	{
	}
	
	/**
	 * The following keyboard input will always be taken regardless of what part of the game engine is handling keyboard input.
	 * @param world
	 * @param player
	 * @param settings
	 * @param keybinds
	 */
	public static void universalKeyboard(ClientUpdate update, World world, EntityPlayer player, Settings settings, Keybinds keybinds, Vector<EnumHardwareInput> hardwareInput)
	{
		//Check for the (left) shift modifier (this may be useful in other parts of the program)
		lshiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);		
	}
	
	/**
	 * Handles all standard keyboard input in the game. This includes all key binds and movement, but excludes
	 * any typing in text boxes.
	 * @param world
	 * @param player
	 * @param settings
	 */
	public static void keyboard(SPGameLoop engine, ClientUpdate update, World world, EntityPlayer player, Settings settings, Keybinds keybinds, Vector<EnumHardwareInput> hardwareInput)
	{	
		if(SPGameEngine.initInDebugMode)
		{
			if((Keyboard.isKeyDown(Keyboard.KEY_Q) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) || Display.isCloseRequested()) //Exit if Escape is pressed or the Window is Closed
			{
				SPGameEngine.done = true;
			}
		}
				
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !ec && !SPGameEngine.isMainMenuOpen)
		{
			ec = true;
			settings.menuOpen = !settings.menuOpen;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			ec = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) && !enterDown)
		{
			enterDown = true;
			player.isInventoryOpen = false;
			player.clearViewedChest();
			engine.toggleChatOpen();
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_RETURN))
		{
			enterDown = false;
		}
		
		if(Keyboard.isKeyDown(keybinds.inventoryToggle) && !ic) //Toggle inventory
		{
			ic = true;
			player.isInventoryOpen = !player.isInventoryOpen;
        	player.clearSwing();
			if(!player.isInventoryOpen)
			{
				player.clearViewedChest();
			}
		}
		if(!Keyboard.isKeyDown(keybinds.inventoryToggle))
		{
			ic = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) //Move Left
        {			
			player.isFacingRight = false;
			hardwareInput.add(EnumHardwareInput.MOVE_LEFT);
		}
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) //Move Right
        {
			player.isFacingRight = true;
			hardwareInput.add(EnumHardwareInput.MOVE_RIGHT);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) //Jump
        {
			hardwareInput.add(EnumHardwareInput.JUMP);
        }
        
        final int[] actionKeyValues = 
        { 
        		Keyboard.KEY_1, 
        		Keyboard.KEY_2, 
        		Keyboard.KEY_3, 
        		Keyboard.KEY_4, 
        		Keyboard.KEY_5, 
        		Keyboard.KEY_6, 
        		Keyboard.KEY_7, 
        		Keyboard.KEY_8, 
        		Keyboard.KEY_9, 
        		Keyboard.KEY_0, 
        		Keyboard.KEY_MINUS, 
        		Keyboard.KEY_EQUALS 
        };
        
        for(int i = 0; i < actionKeys.length; i++)
        {
        	if(Keyboard.isKeyDown(actionKeyValues[i]) && !actionKeys[i])
        	{
        		actionKeys[i] = true;
        		int selectedSlot = player.selectedSlot;
        		player.selectedSlot = i;
        		if(selectedSlot != player.selectedSlot)
        		{
		        	update.addCommand("/player " + player.entityID + " setactionbarslot " + player.selectedSlot);
        		}
        		player.clearSwing();
	        	update.addCommand("/player " + player.entityID + " cancelswing");
        	}
        	if(!Keyboard.isKeyDown(Keyboard.KEY_EQUALS))
        	{
        		actionKeys[i] = false;
        	}
        }			
	}	
}