package net.dimensia.src;

import net.dimensia.client.Dimensia;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Keys
{	
	private static int count = 0;
	private static boolean printer;
	private static boolean actionKeys[] = new boolean[12];
	private static boolean ic;
	
	public static void keyboard(World world, EntityLivingPlayer player)
	{	
		if(Dimensia.initInDebugMode)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()) //Exit if Escape is pressed or the Window is Closed
			{
				Dimensia.done = true;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_E) && !ic) //Tooggle inventory
			{
				ic = true;
				player.isInventoryOpen = !player.isInventoryOpen;
			}
			if(!Keyboard.isKeyDown(Keyboard.KEY_E))
			{
				ic = false;
			}
		}
		else
		{
			if(Display.isCloseRequested()) //Exit if Escape is pressed or the Window is Closed
			{
				Dimensia.done = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !ic) //Tooggle inventory
			{
				ic = true;
				player.isInventoryOpen = !player.isInventoryOpen;
			}
			if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				ic = false;
			}	
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
        	player.hasJumped();
        }
		if(Keyboard.isKeyDown(Keyboard.KEY_1) && !actionKeys[0]) //Select Slot 1 of actionbar
		{
			actionKeys[0] = true;
			player.selectedSlot = 0;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			actionKeys[0] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2) && !actionKeys[1]) //Select Slot 2 of actionbar
		{
			actionKeys[1] = true;
			player.selectedSlot = 1;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			actionKeys[1] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_3) && !actionKeys[2]) //Select Slot 3 of actionbar
		{
			actionKeys[2] = true;
			player.selectedSlot = 2;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_3))
		{
			actionKeys[2] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_4) && !actionKeys[3]) //Select Slot 4 of actionbar
		{
			actionKeys[3] = true;
			player.selectedSlot = 3;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_4))
		{
			actionKeys[3] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_5) && !actionKeys[4]) //Select Slot 5 of actionbar
		{
			actionKeys[4] = true;
			player.selectedSlot = 4;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_5))
		{
			actionKeys[4] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_6) && !actionKeys[5]) //Select Slot 6 of actionbar
		{
			actionKeys[5] = true;
			player.selectedSlot = 5;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_6))
		{
			actionKeys[5] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_7) && !actionKeys[6]) //Select Slot 7 of actionbar
		{
			actionKeys[6] = true;
			player.selectedSlot = 6;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_7))
		{
			actionKeys[6] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_8) && !actionKeys[7]) //Select Slot 8 of actionbar
		{
			actionKeys[7] = true;
			player.selectedSlot = 7;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_8))
		{
			actionKeys[7] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_9) && !actionKeys[8]) //Select Slot 9 of actionbar
		{
			actionKeys[8] = true;
			player.selectedSlot = 8;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_9))
		{
			actionKeys[8] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_0) && !actionKeys[9]) //Select Slot 10 of actionbar
		{
			actionKeys[9] = true;
			player.selectedSlot = 9;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_0))
		{
			actionKeys[9] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_MINUS) && !actionKeys[10]) //Select Slot 10 of actionbar
		{
			actionKeys[10] = true;
			player.selectedSlot = 10;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_MINUS))
		{
			actionKeys[10] = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_EQUALS) && !actionKeys[11]) //Select Slot 10 of actionbar
		{
			actionKeys[11] = true;
			player.selectedSlot = 11;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_EQUALS))
		{
			actionKeys[11] = false;
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
				player.launchProjectile( world, player.x, player.y, Item.snowball);
				/*EntityProjectile.woodenArrow.clone().setXLocAndYLoc(world.player.x, world.player.y).setDirection(count));
				count++;
				if (count == 360) count = 0;*/
			}	
	        if (Keyboard.isKeyDown(Keyboard.KEY_G))
			{
	        	player.launchProjectile( world, player.x, player.y, Item.magicMissileSpell);
	        	/*world.addEntityToProjectileList(EntityProjectile.magicMissile.clone().setXLocAndYLoc(world.player.x, world.player.y).setDirection(count));
				count++;
				if (count == 360) count = 0;*/
			}	 
	        if(Keyboard.isKeyDown(Keyboard.KEY_P)) //Broken
	        {
	        	//for(int i = 0; i < 3;i++){
	        	//player.registerStatusEffect(new StatusEffectStun(5, 1));
	        	EntityLivingNPC enemy = new EntityLivingNPCEnemy(EntityLivingNPCEnemy.slime);//EntityLivingNPC.test.clone();
				enemy.setPosition((int)player.x, (int)player.y);
				world.npcList.add(enemy);	
	        	//}
	        }   
	        if(Keyboard.isKeyDown(Keyboard.KEY_L) && !printer)
	        {
	        	printer = true;
	        	for(int i =0 ; i < 5; i++)
	        	{
	        		System.out.println();
	        	}
	        	player.inventory.printHashTable();
	        }
	        if(!Keyboard.isKeyDown(Keyboard.KEY_L))
	        {
	        	printer = false;
	        }
	        
			//End debug inputs\             
		}		
	}	
}