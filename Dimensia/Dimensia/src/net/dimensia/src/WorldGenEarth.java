package net.dimensia.src;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

/**
 * WorldGenEarth implements all the features and methods needed to generate a complete 'world map'. 
 * This is stored in chunks, but WorldGenEarth is not directly required to interact with these chunks,
 * that is instead handled by {@link World#getBlockGenerate(int, int)} and {@link World#setBlockGenerate(Block, int, int)}. 
 * This costs some time, but due to the rarity of actually generating a new world, this time should be insignificant overall. 
 * <br><br>
 * 
 * WorldGenEarth exposes only one method {@link #generate(World)} which is used to completely 
 * generate an entire world. This generation should include:
 * <br><br>
 * <li>Adminium
 * <li>Stone
 * <li>Dirt
 * <li>Caves
 * <li>Ores	 
 * <li>Biome Specific Generation 
 * 
 * <br><br>
 * A world generation error is considered fatal, and as a result there is no exception handling. Any
 * failure in generation may or may not cause a complete failure of Dimensia, and corrupt whatever part of
 * the world is generated until that point. Exception Handling must be done at the external point of 
 * generation, if a fatal error must(or should) be recovered from.
 *
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */

public class WorldGenEarth
{	
	/**
	 * Generates a new world, including biomes. This applies:
	 * <br><br>
	 * <li>Adminium
	 * <li>Stone
	 * <li>Dirt
	 * <li>Caves
	 * <li>Ores	 
	 * <li>Biome Specific Generation 
	 */
	public World generate(World world) 
	{
		int m;
		System.gc();
		generateBiomes(world);
		count = 0;
		generateBase(world); //Create the stone base for the world
		generateStone(world); //Create a basic shape for stone
		generateDirt(world);
		System.gc();
		caves(world);
		stoneinter(world);
		cellauto(world);
		ores(world);
		System.gc();
		/**
		 * NOTE TO MATT (FROM ALEC):
		 * 
		 * Use 
		 * 		World.getBlockGenerate() not World.getBlockGenerate()
		 * 		World.setBlockGenerate() not World.setBlockGenerate()
		 * 
		 * This are specially designed to be worldgen safe. They take longer to execute, but are completely bounds safe (read 
		 * method descriptions, that might help understand what they do). Also a chunk will be generated freshly and placed if needed.
		 * 
		 * 
		 * 
		 * 
		 */
		
		Map<String, Biome> biomes = world.biomes;
		Biome biome;		
		//Place grass
		for(int j = world.getHeight() - 150; j > 0; j--){ //go through the the y-axis of the world
			for(int k = 1; k < world.getWidth()-1; k++){ //x-axis		
				if (world.getBlockGenerate(k, j).getBlockID() == Block.dirt.getBlockID() && (world.getBlockGenerate(k, j-1).getBlockID() == Block.air.getBlockID() || world.getBlockGenerate(k - 1, j).getBlockID() == Block.air.getBlockID() || world.getBlockGenerate(k + 1, j).getBlockID() == Block.air.getBlockID()) && j <= 460){ //If there if a block of dirt with air above, left or right
					world.setBlockGenerate(Block.grass, k, j); //make the block grass
				}
			}
		}
		
		//world = generateArctic(world, 25, 1000);
		//world = generateDesert(world, 25, 150);		
		for(int i = 0; i < world.getTotalBiomes(); i++){
			biome = biomes.get(new StringBuilder().append(i).toString());			
			int x = (int)(biome.getX());
			int w = (int)(biome.getWidth());
			if(biome.biomeID == 0){
				generateForest(world, x, w);
			}
			else if(biome.biomeID == 1){				
				generateDesert(world, x, w);
			}
			else if (biome.biomeID == 2){
				generateArctic(world, x, w);
			}
		}
		
		for(int j = world.getHeight() - 1; j > 0; j--){ //go through the the y-axis of the world
			for(int k = 1; k < world.getWidth() - 1; k++){ //x-axis
				if (world.getBlockGenerate(k,j).isSolid){
					m = getBitMap(world, k, j);
					world.getBlockGenerate(k, j).setBitMap(m); //set the appropriate texture
				}
				
			}
		}
		
		System.gc();
		verifyAirExists(world);
		world.assessForAverageSky();
		System.gc();
		return world;
	}
	
	private int getBitMap(World world, int x, int y){
		int bit = 0;
		if (world.getBlockGenerate(x, y - 1).isSolid){
			bit += 1;
		}
		if (world.getBlockGenerate(x, y + 1).isSolid){
			bit += 4;
		}
		if (world.getBlockGenerate(x - 1, y).isSolid){
			bit += 8;
		}
		if (world.getBlockGenerate(x + 1, y).isSolid){
			bit += 2;
		}
		return bit;
	}
	
	private void verifyAirExists(World world)
	{
		for(int i = 0; i < world.getWidth(); i++)
		{
			for(int j = 0; j < world.getHeight(); j++)
			{
				if(world.getBlock(i, j) == null)
				{
					world.setBlock(Block.air, i, j);
				}
			}
		}
	}
	
	private void generateArctic(World world, int x, int w){
		generateForest(world,x,w);
		for(int j = world.getHeight() - 200; j > 0; j--){ //go through the the y-axis of the world
			for(int k =  x + 1; k < x + w - 5; k++){ //x-axis	
				if (world.getBlockGenerate(k, j+1) == Block.branchleft) world.setBlockGenerate(Block.sbranchleft, k, j+1); //Start replacing trees with snow-covered varient
				else if (world.getBlockGenerate(k, j+1) == Block.branchright) world.setBlockGenerate(Block.sbranchright, k, j+1);
				else if (world.getBlockGenerate(k, j+1) == Block.treetopc2) world.setBlockGenerate(Block.streetopc2, k, j+1);
				else if (world.getBlockGenerate(k, j+1) == Block.treetopc1) world.setBlockGenerate(Block.streetopc1, k, j+1);
				else if (world.getBlockGenerate(k, j+1) == Block.treetopl2) world.setBlockGenerate(Block.streetopl2, k, j+1);
				else if (world.getBlockGenerate(k, j+1) == Block.treetopl1) world.setBlockGenerate(Block.streetopl1, k, j+1);
				else if (world.getBlockGenerate(k, j+1) == Block.treetopr2) world.setBlockGenerate(Block.streetopr2, k, j+1);
				else if (world.getBlockGenerate(k, j+1) == Block.treetopr1) world.setBlockGenerate(Block.streetopr1, k, j+1);
				else if (world.getBlockGenerate(k, j+1) == Block.treebl || world.getBlockGenerate(k, j+1) == Block.treebr || world.getBlockGenerate(k, j+1) == Block.branchrightdb || world.getBlockGenerate(k, j+1) == Block.branchleftdb
						|| world.getBlockGenerate(k, j+1) == Block.branchleftb || world.getBlockGenerate(k, j+1) == Block.branchrightb || world.getBlockGenerate(k, j+1) == Block.branchleftd || world.getBlockGenerate(k, j+1) == Block.branchrightd);// end replacement of trees
				else if (world.getBlockGenerate(k, j+1).isSolid){ //If there is a solid block with air above
					if (world.getBlockGenerate(k, j).isOveridable){
						world.setBlockGenerate(Block.snowCover, k, j); // If the current block is air or a unneeded plant, replace the block with snow
					}
				}
				if (world.getBlockGenerate(k, j).getBlockID() == Block.grass.getBlockID()){
					world.setBlockGenerate(Block.snowyGrass, k, j);
				}
			}
		}
	}
	
	private void generateForest(World world, int x, int w){
		int plant = 0, height = 10, count = 0, branchl = 0, branchr = 0;		
		for(int i = world.getHeight() - 20; i > 0 ; i--){ //for the depth
			for(int j = x + 2; j < x + w - 5; j++){ //Throughout the width of the forest
				if (world.getBlockGenerate(j, i+1).getBlockID() == Block.grass.getBlockID() && world.getBlockGenerate(j, i).getBlockID() == Block.air.getBlockID()){ //If the block beneath the current cell is grass
					plant = (int)(Math.random() * 100 + 1); //Decide what plant will be placed									
					int space = 4;
					if (plant <= 40 && i <= 450){ //If a tree is to be placed
						height = (int)(Math.random() * 5 + 4); //Determine the height of the tree
						if (i-height-4 <= 0){ //If the tree would go off the map
							height = 0; //don't place a tree
							space = 0; //Don't place a tree
						}
						 //If there is room for the tree up and to the left/right	
						if (world.getBlockGenerate(j, i-height-space).getBlockID() == Block.air.getBlockID() && world.getBlockGenerate(j-1, i).getBlockID() == Block.air.getBlockID() && world.getBlockGenerate(j+1, i).getBlockID() == Block.air.getBlockID() && world.getBlockGenerate(j-2, i).getBlockID() == Block.air.getBlockID() && world.getBlockGenerate(j+2, i).getBlockID() == Block.air.getBlockID()){
							world.setBlockGenerate(Block.dirt, j, i + 1);
							count = 1;
							int r = (int)Math.random() * 5;
							
							if (r <= 1 && (world.getBlockGenerate(j-1, i+1).getBlockID() == Block.grass.getBlockID()|| world.getBlockGenerate(j-1, i+1).getBlockID() == Block.dirt.getBlockID())){
								world.setBlockGenerate(Block.treebl, j-1, i);
								world.setBlockGenerate(Block.dirt, j-1, i+1);
							}
							else if (r == 5 && (world.getBlockGenerate(j-1, i+1).getBlockID() == Block.grass.getBlockID()|| world.getBlockGenerate(j-1, i+1).getBlockID() == Block.dirt.getBlockID()) && (world.getBlockGenerate(j+1, i+1).getBlockID() == Block.grass.getBlockID() || world.getBlockGenerate(j+1, i+1).getBlockID() == Block.dirt.getBlockID())){
								world.setBlockGenerate(Block.treebl, j-1, i);
								world.setBlockGenerate(Block.treebr, j+1, i);
								world.setBlockGenerate(Block.dirt, j+1, i+1);
								world.setBlockGenerate(Block.dirt, j-1, i+1);
							}
							else if (r <= 4 && (world.getBlockGenerate(j+1, i+1).getBlockID() == Block.grass.getBlockID()|| world.getBlockGenerate(j+1, i+1).getBlockID() == Block.dirt.getBlockID())){
								world.setBlockGenerate(Block.treebr, j+1, i);
								world.setBlockGenerate(Block.dirt, j+1, i+1);
							}
							
							for (int k = i; k >= i - height; k--){ //Place the tree
								if (world.getBlockGenerate(j, k).getBlockID() == Block.air.getBlockID()){ //If the cell is empty
									if (k == i-height){ //If at the top of the tree
										world.setBlockGenerate(Block.treetopr2, j+1, k); //Place the tree top
										world.setBlockGenerate(Block.treetopr1, j+1, k-1);
										world.setBlockGenerate(Block.treetopc2, j, k);
										world.setBlockGenerate(Block.treetopc1, j, k-1);
										world.setBlockGenerate(Block.treetopl2, j-1, k);
										world.setBlockGenerate(Block.treetopl1, j-1, k-1);
									}
									else{
										world.setBlockGenerate(Block.tree, j, k); //Otherwise, place a tree trunk
										System.out.println("Derp!");
									}
									if (count > 2 && k > i - height + 1){ //For each slice of tree, if it is more than the third log, determine if there should be a branch
										branchl = (int)(Math.random()*24); //Decide if a block should be placed left
										branchr = (int)(Math.random()*24); //Decide if a branch should be placed right
										
										if (branchl == 1){ //If 1, place a branch
											world.setBlockGenerate(Block.branchleft, j-1, k);
										}
										else if (branchl == 0){ //If 0, place a down branch
											world.setBlockGenerate(Block.branchleftd, j-1, k);
										}
										else if (branchl == 2){ //If 2, place a broken branch
											world.setBlockGenerate(Block.branchleftb, j-1, k);
										}
										else if (branchl == 3){ //If 3, place a down broken branch
											world.setBlockGenerate(Block.branchleftdb, j-1, k);
										}
										
										if (branchr == 1){ //If 1, place a branch
											world.setBlockGenerate(Block.branchright, j+1, k);
										}
										else if (branchr == 0){ //If 0, place a down branch
											world.setBlockGenerate(Block.branchrightd, j+1, k);
									}
										else if (branchr == 2){ //If 2, place a broken branch
											world.setBlockGenerate(Block.branchrightb, j+1, k);
										}
										else if (branchr == 3){ //If 3, place a down broken branch
											world.setBlockGenerate(Block.branchrightdb, j+1, k);
										}										
									}
									count++; //increment the counter 
								}
								else{
									break;
								}
							}
						}	
					}
					else if (plant <= 45 && i <=450){ //If a red flower is to be placed
						world.setBlockGenerate(Block.redflower, j, i); //Fill the cell with red flower						
					}
					else if (plant <= 50 && i <= 450){ //If a yellow flower is to be placed
						world.setBlockGenerate(Block.yellowflower, j, i); //Fill the cell with yellow flower
					}
					else if (i <= 450){ //If tall grass is to be placed
						world.setBlockGenerate(Block.tallgrass, j, i); //Fill the cell with tall grass
					}
				}
			}
		}
	}
	
	/**
	 * Generates a desert biome (surface only), at the specified location with the specified width
	 * @param x the x position of the desert in the 'world map' (block units, not ortho)
	 * @param w the width of the desert in blocks
	 */
	private void generateDesert(World world, int x, int w)
	{
		//Add deserts to the world
		int height = 0, space = 0, cacti = 0, sand = 0;
		for(int i = world.getHeight() -200; i > 1 ; i--){ //for the depth
			for(int j = x + 1; j < x + w - 1; j++){ //Throughout the width of the desert
				if (world.getBlockGenerate(j, i).getBlockID() == Block.dirt.getBlockID()|| world.getBlockGenerate(j, i).getBlockID() == Block.grass.getBlockID()){ //If the current block is dirt
					if (i > world.getHeight() - 390){
						if (j < x + 6 || j > x + w - 6 || i > world.getHeight() - 210){
							sand = (int)(Math.random()*100+1);
						}
						else{
							sand = (int)(Math.random()*50+1);
						}
						if (sand <= 50){
							if (world.getBlockGenerate(j, i+1).getBlockID() != Block.sandstone.getBlockID() && world.getBlockGenerate(j, i+1).getBlockID() != Block.sand.getBlockID()){
								world.setBlockGenerate(Block.sandstone, j, i);
							}
							else{
								world.setBlockGenerate(Block.sand, j, i);	
							}
						}
					}
					else{						
						if (world.getBlockGenerate(j, i+1).getBlockID() != Block.sandstone.getBlockID() && world.getBlockGenerate(j, i+1).getBlockID() != Block.sand.getBlockID()){
							world.setBlockGenerate(Block.sandstone, j, i);							
						}
						else{
							world.setBlockGenerate(Block.sand, j, i);	
						}
					}
				}
				if (i <= 460 && (world.getBlockGenerate(j, i+1).getBlockID() == Block.sand.getBlockID() || world.getBlockGenerate(j, i+1).getBlockID() == Block.sandstone.getBlockID()) && world.getBlockGenerate(j, i).getBlockID() == Block.air.getBlockID()){ //If the block beneath the current cell is grass
					cacti = (int)(Math.random() * 100 + 1); //Decide what plant will be placed									
					space = 1;
					if (cacti <= 30){ //If a cactus is to be placed
						height = (int)(Math.random() * 3 + 2); //Determine the height of the cactus
						if (i-height-space <= 0){
							height = 0;
							space = 0;
						}
						if (world.getBlockGenerate(j, i-height-space).getBlockID() == Block.air.getBlockID() && world.getBlockGenerate(j-1, i).getBlockID() == Block.air.getBlockID() && world.getBlockGenerate(j+1, i).getBlockID() == Block.air.getBlockID()){ //If there is room for the cactus up and to the left/right	
							for (int k = i; k >= i - height; k--){ //Place the cactus
								world.setBlockGenerate(Block.cactus, j, k);
							}
						}					
					}					
				}
			}
		}
	}
	
	/**
	 * Generates a large lump of adminium and stone for the world's base
	 */
	private void generateBase(World world){
		for(int i = 0; i < world.getWidth(); i++){
			for(int k = 0; k < (int)(world.getHeight()); k++){
				if (k >= world.getHeight() - 10){ //In the bottom ten layers
					world.setBlockGenerate(Block.adminium, i, k); //Place adminium
				}
				else if (k >= world.getHeight() - 350){
					world.setBlockGenerate(Block.stone, i, k); //Place stone								
				}
				else{
					world.setBlockGenerate(Block.air, i, k); //place air
				}				
			}
		}
	}
	
	private void generateStone(World world)
	{
		int stone = 0, amount = 0;	
		for(int i = world.getHeight()-10; i > 50; i--){
			for(int k = 8; k < world.getWidth(); k++){
				if (i > (int)(world.getHeight()/3*2)){ //If current cell is in the bottom third of the map
					stone = (int)(Math.random()*3+1); //Chance of spawning rock is 1 in 2
				}
				else{
					stone = (int)(Math.random()*8+1); //Chance of spawning rock is 1 in 6
				}		
				amount = (int)(Math.random()*6+2); //Select how much rock to place			
				if (k - amount <= 0 || k + amount >= world.getWidth()){ //If the amount won't fit on the map
					amount = 0; //Set the amount of extension to zero
				}
				if (stone == 1 && world.getBlockGenerate(k, i+1).isSolid){ //if the spot beneath is solid and a rock is being placed					
					if (world.getBlockGenerate((int)(k+amount), i+1).isSolid || world.getBlockGenerate((int)(k-amount), i+1).isSolid){ //If the spot amount down to the left, or to the right is solid						
						for (int j = -amount; j <= amount; j++){							
							world.setBlockGenerate(Block.stone, k+j, i); //Make the spots rocks
						}
					}					
				}
			}
		}
	}
	
	private void generateDirt(World world){
		int dirt = 0, amount = 0;		
		for(int i = world.getHeight() - 10; i > 50; i--){ //go through the the y-axis of the world
			for(int k = 0; k < world.getWidth(); k++){ //x-axis			
				dirt = (int)(Math.random() * 6 + 1); //select whether dirt is being placed
				amount = (int)(Math.random() * 4 + 1); //Select how much dirt is being placed on either side
				if (world.getBlockGenerate(k, i + 1).getBlockID() == Block.stone.getBlockID()){ //If the block beneath is stone
					dirt = 1; //Place dirt
				}
				if (k - amount <= 0 || k + amount >= world.getWidth()){ //If the amount chosen would take it off the map
					amount = 0; //Do not use an amount
				}				
				if (dirt == 1 && world.getBlockGenerate(k, i + 1).getBlockID() != Block.air.getBlockID()){ //If dirt is being placed and the one beneath is not empty
					if (world.getBlockGenerate(k + amount, i + 1).getBlockID() != Block.air.getBlockID() || world.getBlockGenerate(k - amount, i + 1).getBlockID() != Block.air.getBlockID()){ //If the spot the amount chosen down to the left, or to the right is occupied						
						for (int j = -amount; j <= amount; j++){ //Loop given the amount							
							world.setBlockGenerate(Block.dirt, k + j, i); //Make each space dirt							
						}
					}					
				}
				
			}
		}
	}
			
	private void caves(World world){
		int empty = 0;
		for (int i = world.getHeight() - 8; i > 445; i--){ //Go through the height
			for (int j = 1; j < world.getWidth() - 1; j++){ //go through the width
				empty = (int)(Math.random()*100+1); //Select if the cell should be emptied
				if (empty <= 28){ //If the number is beneath the cutoff
					world.setBlockGenerate(Block.air, j, i); //Empty the cell
					world.setBlockGenerate(Block.air, j+1, i);						
				}
			}
		}	
		cellauto(world); //Run the Celluar Automata function to hollow/smooth out caves
		cellauto(world); //Run the Celluar Automata function to hollow/smooth out caves
		cellauto(world); //Run the Celluar Automata function to hollow/smooth out caves
	}
	
	private void cellauto(World world){
		int solid = 0, choice = 0;
		count ++;
		for (int i = world.getHeight() - 6; i > 200; i--){ //Go through the height
			for (int j = 1; j < world.getWidth() - 1; j++){ //go through the width
				solid = 0; //Reset the solid counter
				//Figure out how many solid blocks there are in a 3x3 area
				for(int k = i - 1; k <= i + 1; k++){ //Height
					for(int l = j - 1; l <= j + 1; l++){ //Width
						if (world.getBlockGenerate(l, k).getBlockID() != Block.air.getBlockID()) solid++; //If the block is solid, add to the count
					}
				}					
				if (solid >= 5 || (solid == 0 && count <= 2)){ //if there is 5 or more walls or if there are 0 walls and it is the first 1 iterations
					if (world.getBlockGenerate(j, i).getBlockID() == Block.air.getBlockID()){ //If the cell is currently empty
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
							if (world.getBlockGenerate(j, i-1).getBlockID() == Block.air.getBlockID()){
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

	private void ores(World world){
		int minchance = 0;
		int ore = 0;
		Block orec = Block.air;
		for (int i = world.getHeight() - 10; i > 1; i--){ //Go through the height
			for (int j = 1; j < world.getWidth() - 3; j++){ //go through the width
				minchance = (int)(Math.random()*1000+1);	//Decide if an ore vein will be placed
				if (minchance >=988 && world.getBlockGenerate(j, i).getBlockID() == Block.stone.getBlockID()){ // if a vein is to be placed
					ore = (int)(Math.random()*100+1); //Determine which ore will be placed
					if (i <= world.getHeight()/10*7){ //if it is placing in the top 7/10's of the map
						if (ore <= 20){ //with a 20% chance
							orec = Block.copper; // Copper
						}
						else if (ore <= 40){ //20% chance
							orec = Block.tin;
						}
						else if (ore <= 45){ //With a 5% chance
							orec = Block.iron; //Iron
						}
						else if (ore <= 65){ //with a 20% chance
							orec = Block.coal; //Coal
						}
						else{ //All other cases
							orec = Block.air; //Don't replace the cell
						}							
					}
					else if (i >= world.getHeight()/10 * 7){ //If it's instead in the bottom 3/10's
						if (ore <= 10){ // a 10% chance
							orec = Block.iron; //Iron
						}
						else if (ore <= 20){ //A 10% chance
							orec = Block.coal; //Coal
						}
						else if (ore <= 25){ //A 5% chance
							orec = Block.silver; //Silver
						}
						else if (ore <= 28){ //a 3% chance
							orec = Block.gold; //Gold
						}
						else if (ore <= 29){ // 1% chance
							orec = Block.diamond; //Diamond
						}
						else{ //All other cases
							orec = Block.air; //do not replace
						}
					}
					if (orec != Block.air){ //If an ore is actually being placed				
						oreplace(world, 15, j, i, orec); //place the vein					
					}
				}
			}
		}
	}
	
	private void oreplace (World world, int am, int j, int i, Block ore){
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

	private void stoneinter (World world){
		int choice = 0;
		for (int i = world.getHeight() - 10; i > 1; i--){ //Go through the height
			for (int j = 1; j < world.getWidth() - 1; j++){ //go through the width
				if (world.getBlockGenerate(j, i-1).getBlockID() != Block.air.getBlockID() && world.getBlockGenerate(j, i-2).getBlockID() != Block.air.getBlockID() && world.getBlockGenerate(j, i-3).getBlockID() != Block.air.getBlockID()){
					choice = (int)(Math.random()*100+1);
					if (choice <= 80) 
						world.setBlockGenerate(Block.stone, j, i);
				}
				if (world.getBlockGenerate(j-1, i).getBlockID() == Block.dirt.getBlockID()) 
					oreplace(world, 3, j-1,i ,Block.dirt);
			}
		}
	}
	
	private void generateBiomes(World world)
	{
		int length;
		int biomeType;
		int baseWidth = 0;
		Biome t_biome;
		int biomeCounter = 0;
		Vector<Biome> vector = new Vector<Biome>();
		while(true) //While There is land without a biome assignment... 
		{
			length = random.nextInt(175) + 100; //how wide is the biome?
			biomeType = getBiomeType(); //what type is it?

			if(length + baseWidth > world.getWidth()) //if the biome exceeds world bounds, ensure it actually doesnt
			{
				length = world.getWidth() - baseWidth; 
			}
			
			t_biome = Biome.getBiomeFromBiomeList(biomeType); //get the correct biome from the Biome class
			t_biome.setBiomeBounds(baseWidth, 0, length, world.getHeight()); //set the bounds			
			vector.add(t_biome);
			biomeCounter++;
			for(int i = baseWidth; i <= baseWidth + length; i++)	//for each column, assign a biome value
			{
				world.biomesByColumn.put((new StringBuilder().append(i).toString()), t_biome.biomeName);
			}
			
			baseWidth += length; 		
			if(baseWidth >= world.getWidth()) //if the right side of the map is reached, break
			{
				break;				
			}			
		}		
				
		for(int i = 0; i < vector.size() - 1; ) //For each biome
		{
			if(vector.get(i).biomeID == vector.get(i + 1).biomeID) //If two biomes are next to each other
			{
				vector.get(i).setBiomeBounds(vector.get(i).x, vector.get(i).y, vector.get(i).width + vector.get(i+1).width, 0);
				vector.remove(i + 1); //Merge them and remove the obsolete one
				i = 0;
				continue;
			}
			i++;
		}
		for(int i = 0; i < vector.size(); i++) //Add them to the world
		{
			System.out.println("Generated Biome " + i + ": " + vector.get(i).biomeName);
			world.biomes.put(""+i, vector.get(i));
		}
				
		world.setTotalBiomes(vector.size());
		System.out.println("Total Biomes: " + world.getTotalBiomes());
	}
	
	/**
	 * Generates a random biome id, from the following list:
	 * <br><br>
	 * <li>Forest = 0
	 * <li>Desert = 1
	 * <li>Arctic = 2
	 * <li>Jungle = NYI
	 * <li>Ocean = NYI
	 * <li>Corruption = NYI
	 * <li>The Hallow = NYI
	 */
	private int getBiomeType()
	{		
		return random.nextInt(3);
	}
	
	/**
	 * Forces the world to generate all the chunks, based on its size. This also
	 * completely clears the chunks, making them completely empty.
	 */
	private void forciblyGenerateChunks(World world)
	{
		int chunkWidth = world.getWidth() / Chunk.getChunkWidth();
		int chunkHeight = world.getHeight() / Chunk.getChunkHeight();
	
		for(int i = 0; i < chunkWidth; i++)
		{
			for(int j = 0; j < chunkHeight; j++)
			{
				world.registerChunk(new Chunk(i, j), i, j);
			}
		}
	}
	
	private final Random random = new Random();
	private int count;
}