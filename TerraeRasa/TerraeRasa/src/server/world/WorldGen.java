package server.world;

import blocks.Block;
import blocks.BlockChest;
import blocks.MinimalBlock;


/**
 * 
 * @author Matt
 *
 */
public class WorldGen{
	protected int count;
	
	protected void generateChests(WorldServerEarth world, int x, int w, int y, int h){
		int chance = 0;
		int xskip = 1;
		int yskip = 1;
		for (int i = x; i < x + w; i += xskip){ //Go through the height
			//xskip = (int)(Math.random() * 15 + 1);
			for (int j = y; j < (y + h); j += yskip){ //go through the width
				//yskip = (int)(Math.random() * 15 + 1);
				chance = (int) (Math.random() * 1000 + 1);
				if (chance >= 999){
					placeChest(world, Block.chest, i,j);
				}
			}
		}
	}
	
	protected void placeChest(WorldServerEarth world, Block chest, int x, int y){
		world.generateLargeBlock(x, y, Block.chest);
		MinimalBlock active = world.getBlock(x, y);
		if (Block.blocksList[active.id] instanceof BlockChest){
			active.setMainInventory(world.lootGenerator.getLowLevelChestCommon(), true);
		}
	}
	
	/**
	 * Ensures that there are not null blocks in the world
	 * @param world - the world to check
	 * @param x - x-value to begin checking at
	 * @param w - width of the area
	 * @param y - y-value to begin checking at
	 * @param h - depth of the are
	 */
	protected void verifyAirExists(WorldServerEarth world, int x, int w, int y, int h)
	{
		for(int i = x; i < (x + w); i++)
		{
			for(int j = y; j < ( y + h); j++)
			{
				if(world.getAssociatedBlock(i, j) == null)
				{
					world.setBlockGenerate(Block.air, i, j);
				}
				if(world.getBackWallGenerate(i, j) == null)
				{
					world.setBackWallGenerate(Block.backAir, i, j);
				}
			}
		}
	}
	
	/**
	 * Adds caves to the world by creating random holes and performing cellular automata to hollow/smooth them
	 * @param world - current world
	 * @param x - x-value to start the caves at
	 * @param w - width of the cave area
	 * @param y - y-value to start caves at
	 * @param h - how deep the cave area goes
	 * @param emptyChance - the number to use for determining whether to place a hole or not (random number between 1 and emptyChance, empties cell on 50 or less)
	 */
	protected void caves(WorldServerEarth world, int x, int w, int y, int h, int emptyChance){
		int emptyB = 0, emptyW = 0;
		for (int i = (y + h) - 8; i > y; i--){ //Go through the height
			for (int j = x; j < (x + w); j++){ //go through the width
				emptyB = (int)(Math.random()*emptyChance + 1); //Select if the cell should be emptied
				emptyW = (int)(Math.random()*emptyChance + 1); //Select if the cell should be emptied
				if (emptyB <= 50){ //If the number is beneath the cutoff
					world.setBlockGenerate(Block.air, j, i); //Empty the cell
					world.setBlockGenerate(Block.air, j+1, i);						
				}
				if (emptyW <= 40){
					world.setBackWallGenerate(Block.backAir, j, i);
					world.setBackWallGenerate(Block.backAir, j + 1, i);
				}
			}
		}			
	}	
	
	/**
	 * Compares each cell to the cells around it, determining if it should be empty or solid
	 * @param world - the World to generate within
	 * @param x - inital x-Value of the area
	 * @param w - how wide the area is
	 * @param y - how deep the area begins
	 * @param h - how tall the area is (y + h yields final height)
	 */
	protected void cellauto(WorldServerEarth world, int x, int w, int y, int h){
		int solid = 0, solidW = 0, choice = 0;
		count ++;
		for (int i = (y + h) - 1; i > y + 1; i--){ //Go through the height
			for (int j = x + 1; j < w - 1; j++){ //go through the width
				solid = 0; //Reset the solid counter
				solidW = 0;
				//Figure out how many solid blocks there are in a 3x3 area
				for(int k = i - 1; k <= i + 1; k++){ //Height
					for(int l = j - 1; l <= j + 1; l++){ //Width
						if (world.getAssociatedBlock(l, k).getID() != Block.air.getID()) solid++; //If the block is solid, add to the count
						if (world.getBackWallGenerate(l, k).getID() != Block.backAir.getID()) solidW++;
					}
				}					
				if (solid >= 5 || (solid == 0 && count <= 2)){ //if there is 5 or more walls or if there are 0 walls and it is the first 1 iterations
					if (world.getAssociatedBlock(j, i).getID() == Block.air.getID()){ //If the cell is currently empty
						choice = (int)(Math.random() * 100 + 1); //select which block
						if (i > (int)(world.getHeight() / 3 * 2)){	//If the current cell is in the bottom third of the map							
							if (choice < 95){ //95% chance of stone
								world.setBlockGenerate(Block.stone, j, i); //Fill cell with stone
							}
							else{ //25% chance of dirt
								world.setBlockGenerate(Block.dirt, j, i); //Fill cell with dirt
							}
						}
						else{ //If the current cell is in the top two thirds
							if (world.getAssociatedBlock(j, i-1).getID() == Block.air.getID()){
								if (choice < 25){ //25% chance of dirt
									world.setBlockGenerate(Block.dirt, j, i); //Fill cell with dirt
								}
								else{ //75% Chance of stone
									world.setBlockGenerate(Block.stone, j, i); //Fill cell with stone
								}
							}
							else{
								if (choice < 30){ //30% chance of dirt
									world.setBlockGenerate(Block.dirt, j, i); //Fill cell with dirt
								}
								else{ //70% Chance of stone
									world.setBlockGenerate(Block.stone, j, i); //Fill cell with stone
								}
							}
						}						
					}
				}
				else{ //If there are less than 5 walls
					world.setBlockGenerate(Block.air, j, i); //Empty the cell
				}
				
				if (solidW >= 5 || (solidW == 0 && count <= 2)){ //if there is 5 or more walls or if there are 0 walls and it is the first 1 iterations
					if (world.getBackWallGenerate(j, i).getID() == Block.backAir.getID()){ //If the cell is currently empty
						choice = (int)(Math.random() * 100 + 1); //select which block
						if (i > (int)(world.getHeight() / 3 * 2)){	//If the current cell is in the bottom third of the map							
							if (choice < 75){ //75% chance of stone
								world.setBackWallGenerate(Block.backStone, j, i); //Fill cell with stone
							}
							else{ //25% chance of dirt
								world.setBackWallGenerate(Block.backDirt, j, i); //Fill cell with dirt
							}
						}
						else{ //If the current cell is in the top two thirds
							if (world.getBackWallGenerate(j, i-1).getID() == Block.backAir.getID()){
								if (choice < 80){ //80% chance of dirt
									world.setBackWallGenerate(Block.backDirt, j, i); //Fill cell with dirt
								}
								else{ //20% Chance of stone
									world.setBackWallGenerate(Block.backStone, j, i); //Fill cell with stone
								}
							}
							else{
								if (choice < 30){ //30% chance of dirt
									world.setBackWallGenerate(Block.backDirt, j, i); //Fill cell with dirt
								}
								else{ //70% Chance of stone
									world.setBackWallGenerate(Block.backStone, j, i); //Fill cell with stone
								}
							}
						}						
					}
				}
				else{ //If there are less than 5 walls
					world.setBackWallGenerate(Block.backAir, j, i); //Empty the cell
				}
			}
		}
	}	
	
	protected void gems(WorldServerEarth world, int x, int w, int y, int h, Block[] placeableGems){
		int minchance = 0;
		int ore = 0;
		Block gemc = Block.air;
		for (int i = (y + h); i > y; i--){ //Go through the height
			for (int j = x; j < (x + w); j++){ //go through the width
				gemc = Block.air;
				minchance = (int)(Math.random()*1000+1);	//Decide if an ore vein will be placed
				if (minchance >=990 && world.getAssociatedBlock(j, i).getID() == Block.stone.getID()){ // if a vein is to be placed						
					if (i >= world.getHeight()/10 * 7){ //If it's instead in the bottom 3/10's
						for (int k = 0; k < placeableGems.length; k++){
							ore = (int)(Math.random()*100+1); //Determine which ore will be placed
							if (ore <= placeableGems[k].getLRange()){
								gemc = placeableGems[k];
								break;
							}
							if (ore >= 80){
								break;
							}
						}						
					}
					if (gemc.getID() != Block.air.getID()){ //If an ore is actually being placed				
						oreplace(world, 3, j, i, gemc); //place the vein					
					}
				}
			}
		}
		
	}
	
	/**
	 * Selects places to create veins of various ores
	 * @param world - the current world.
	 * @param x - x-value to start the area
	 * @param w - width of the area
	 * @param y - y-value of the area
	 * @param h - depth of the area
	 * @param placeableOres - array of the blocks which may be placed (ores)
	 */
	protected void ores(WorldServerEarth world, int x, int w, int y, int h, Block[] placeableOres){
		int minchance = 0;
		int ore = 0;
		Block orec = Block.air;
		
		for (int i = (y + h); i > y; i--){ //Go through the height
			for (int j = x; j < (x + w); j++){ //go through the width
				orec = Block.air;
				minchance = (int)(Math.random()*1000+1);	//Decide if an ore vein will be placed
				if (minchance >=988 && world.getAssociatedBlock(j, i).getID() == Block.stone.getID()){ // if a vein is to be placed						
					if (i <= world.getHeight()/4*3){ //if it is placing in the top 3/4's of the map							
						for (int k = 0; k < placeableOres.length; k++){
							ore = (int)(Math.random()*100+1); //Determine which ore will be placed
							if (ore <= placeableOres[k].getHRange()){								
								orec = placeableOres[k];
								break;
							}
							if (ore >= 90){
								break;
							}
						}							
					}
					else if (i >= world.getHeight()/4 * 3){ //If it's instead in the bottom 1/4's
						for (int k = 0; k < placeableOres.length; k++){
							ore = (int)(Math.random()*100+1); //Determine which ore will be placed
							if (ore <= placeableOres[k].getLRange()){
								orec = placeableOres[k];
								break;
							}
							if (ore >= 90){
								break;
							}
						}						
					}
					if (orec.getID() != Block.air.getID()){ //If an ore is actually being placed				
						oreplace(world, 15, j, i, orec); //place the vein					
					}
				}
			}
		}
	}
	
	/**
	 * Designed to add individual veins of ores
	 * @param world - current world
	 * @param am - addition length of the vein (number from 0 to am -1, + 5 gives total length)
	 * @param j - x location to start the vein
	 * @param i - y location to start the vein
	 * @param ore - which ore to use
	 */
	protected void oreplace (WorldServerEarth world, int am, int j, int i, Block ore){
		int solid;
		int num = (int)(Math.random()*am+5); //Choose the number of ores attached
		world.setBlockGenerate(ore, j, i); //Set the starting cell to the chosen ore
		while (num > 0){ //while there is still ore to be placed
			i += (int)(Math.random()*3-1); //Adjust the height one up or down
			if (i <= 0){ //if the height goes off the map, set it to the edge
				i = 1;
			}
			else if (i >= world.getHeight() - 1){ //Same as above, for the other end of the map
				i = world.getHeight() - 3; //set it to the edge
			}			
			j += (int)(Math.random()*3-1); //adjust the width by one left or right
			if (j <= 0){
				j = 1; //if it goes off the map, set it to the edge
			}
			else if (j >= world.getWidth() - 1){ //Same as above case, for opposite side of map
				j = world.getWidth() - 3; //set it to the edge
			}
			num--; //decrease the counter
			solid = 0;
			for(int k = i - 1; k <= i + 1; k++){ //Height
				for(int l = j - 1; l <= j + 1; l++){ //Width
					if (world.getAssociatedBlock(l, k) != Block.air) solid++; //If the block is solid, add to the count
				}
			}
			if (solid >= 3){
			world.setBlockGenerate(ore, j, i); //set the current cell to the chosen ore
			}
			else{
				break;
			}
		}
	}	
}