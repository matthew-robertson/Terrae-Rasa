package hardware;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import client.Dimensia;
import client.Keybinds;
import client.Settings;

import entities.EntityPlayer;

import world.World;

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
	public static void universalKeyboard(World world, EntityPlayer player, Settings settings, Keybinds keybinds)
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
	public static void keyboard(World world, EntityPlayer player, Settings settings, Keybinds keybinds)
	{	
		if(Dimensia.initInDebugMode)
		{
			if((Keyboard.isKeyDown(Keyboard.KEY_Q) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) || Display.isCloseRequested()) //Exit if Escape is pressed or the Window is Closed
			{
				Dimensia.done = true;
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !ec && !Dimensia.isMainMenuOpen)
		{
			ec = true;
			settings.menuOpen = !settings.menuOpen;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			ec = false;
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
			//player.x -= 20;
			player.moveEntityLeft(world);
			player.isFacingRight = false;
		}
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) //Move Right
        {
			//player.x += 20;
        	player.moveEntityRight(world);
			player.isFacingRight = true;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) //Jump
        {
        	player.tryToJumpAgain();
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
        		player.selectedSlot = i;
        		player.clearSwing();
        	}
        	if(!Keyboard.isKeyDown(Keyboard.KEY_EQUALS))
        	{
        		actionKeys[i] = false;
        	}
        }
		
		if(Dimensia.initInDebugMode)
		{
	        //Debug inputs
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) 
			{
				player.y -= 7;
			}
	        if(Keyboard.isKeyDown(Keyboard.KEY_S)) //Broken
	        {
				player.y += 18;			
	        } 
	        if (Keyboard.isKeyDown(Keyboard.KEY_F))
			{
				//player.launchProjectile(world, player.x, player.y, Item.snowball);
				/*EntityProjectile.woodenArrow.clone().setXLocAndYLoc(world.player.x, world.player.y).setDirection(count));
				count++;
				if (count == 360) count = 0;*/
			}	
	        if (Keyboard.isKeyDown(Keyboard.KEY_G))
			{
	        	//player.launchProjectile( world, player.x, player.y, Item.magicMissileSpell);
	        	/*world.addEntityToProjectileList(EntityProjectile.magicMissile.clone().setXLocAndYLoc(world.player.x, world.player.y).setDirection(count));
				count++;
				if (count == 360) count = 0;*/
			}	 
	        if(Keyboard.isKeyDown(Keyboard.KEY_P)) //Broken
	        {
	        	//for(int i = 0; i < 3;i++){
	        	//player.registerStatusEffect(new StatusEffectStun(5, 1));
//	        	EntityNPCEnemy enemy = new EntityNPCEnemy(EntityNPCEnemy.slime);//EntityNPC.test.clone();
//				enemy.setPosition((int)player.x, (int)player.y);
//				world.addEntityToEnemyList(enemy);	
	        	//}
	        	
//	        	if(engine.renderMode != GameEngine.RENDER_MODE_WORLD_HELL)
//	        	{
//	        		try {
//						engine.changeWorld(GameEngine.RENDER_MODE_WORLD_HELL);
//					} catch (IOException e) {
//						e.printStackTrace();
//					} catch (ClassNotFoundException e) {
//						e.printStackTrace();
//					}
//	        	}
	        }   
	        if(!Keyboard.isKeyDown(Keyboard.KEY_L))
	        {
	        }
	        
			//End debug inputs\             
		}		
	}	
}