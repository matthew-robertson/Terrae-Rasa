package world;

import blocks.Block;


/**
 * 
 * @author Matt
 *
 */
public class WorldGen{
	protected int count;
	
	/**
	 * Ensures that the world is not solid or null
	 * @param world - current world
	 */
	protected void verifyAirExists(World world)
	{
		for(int i = 0; i < world.getWidth(); i++)
		{
			for(int j = 0; j < world.getHeight(); j++)
			{
				if(world.getBlockGenerate(i, j) == null)
				{
					world.setBlockGenerate(Block.air, i, j);
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
	protected void caves(World world, int x, int w, int y, int h, int emptyChance){
		int empty = 0;
		for (int i = (y + h) - 8; i > y; i--){ //Go through the height
			for (int j = x; j < (x + w); j++){ //go through the width
				empty = (int)(Math.random()*emptyChance + 1); //Select if the cell should be emptied
				if (empty <= 50){ //If the number is beneath the cutoff
					world.setBlockGenerate(Block.air, j, i); //Empty the cell
					world.setBlockGenerate(Block.air, j+1, i);						
				}
			}
		}			
	}	
	
	/**
	 * Compares each cell to the cells around it, determining if it should be empty or solid 
	 * @param world - current world
	 */
	protected void cellauto(World world, int x, int w, int y, int h){
		int solid = 0, choice = 0;
		count ++;
		for (int i = (y + h) - 1; i > y + 1; i--){ //Go through the height
			for (int j = x + 1; j < w - 1; j++){ //go through the width
				solid = 0; //Reset the solid counter
				//Figure out how many solid blocks there are in a 3x3 area
				for(int k = i - 1; k <= i + 1; k++){ //Height
					for(int l = j - 1; l <= j + 1; l++){ //Width
						if (world.getBlockGenerate(l, k).getID() != Block.air.getID()) solid++; //If the block is solid, add to the count
					}
				}					
				if (solid >= 5 || (solid == 0 && count <= 2)){ //if there is 5 or more walls or if there are 0 walls and it is the first 1 iterations
					if (world.getBlockGenerate(j, i).getID() == Block.air.getID()){ //If the cell is currently empty
						choice = (int)(Math.random() * 100 + 1); //select which block
						if (i > (int)(world.getHeight() / 3 * 2)){	//If the current cell is in the bottom third of the map							
							if (choice < 75){ //75% chance of stone
								world.setBlockGenerate(Block.stone, j, i); //Fill cell with stone
							}
							else{ //25% chance of dirt
								world.setBlockGenerate(Block.dirt, j, i); //Fill cell with dirt
							}
						}
						else{ //If the current cell is in the top two thirds
							if (world.getBlockGenerate(j, i-1).getID() == Block.air.getID()){
								if (choice < 80){ //80% chance of dirt
									world.setBlockGenerate(Block.dirt, j, i); //Fill cell with dirt
								}
								else{ //20% Chance of stone
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
	protected void ores(World world, int x, int w, int y, int h, Block[] placeableOres){
		int minchance = 0;
		int ore = 0;
		Block orec = Block.air;
		
		for (int i = (y + h); i > y; i--){ //Go through the height
			for (int j = x; j < (x + w); j++){ //go through the width
				orec = Block.air;
				minchance = (int)(Math.random()*1000+1);	//Decide if an ore vein will be placed
				if (minchance >=988 && world.getBlockGenerate(j, i).getID() == Block.stone.getID()){ // if a vein is to be placed						
					if (i <= world.getHeight()/10*7){ //if it is placing in the top 7/10's of the map							
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
					else if (i >= world.getHeight()/10 * 7){ //If it's instead in the bottom 3/10's
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
	 * @param j - x location to start the vien
	 * @param i - y location to start the vein
	 * @param ore - which ore to use (copper, tin, iron, coal, silver, gold, diamond)
	 */
	protected void oreplace (World world, int am, int j, int i, Block ore){
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
					if (world.getBlockGenerate(l, k) != Block.air) solid++; //If the block is solid, add to the count
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