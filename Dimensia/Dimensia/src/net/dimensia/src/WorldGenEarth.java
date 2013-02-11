package net.dimensia.src;

import java.util.Random;

/**
 * WorldGenEarth implements all the features and methods needed to generate a complete 'world map'. 
 * This is stored in chunks, but WorldGenEarth is not directly required to interact with these chunks,
 * that is instead handled by {@link World#getBlockGenerate(int, int)} and {@link World#setBlockGenerate(Block, int, int)}. 
 * This costs some time, but due to the rarity of actually generating a new world, this time should be insignificant overall. 
 * <br><br>
 * 
 * WorldGenEarth exposes only one method: {@link #generate(World)} which is used to completely 
 * generate an entire world. This generation should include:
 * <br><br>
 * <li>Adminium
 * <li>Stone
 * <li>Dirt
 * <li>Caves
 * <li>Ores	 
 * <li>Grass
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
	private final Random random = new Random();
	private int count;
	
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
		System.gc();
		Biome[] biomes = generateBiomes(world);
		assignBiomes(world, biomes);
		count = 0;
		generateBase(world); //Create the stone base for the world
		generateStone(world); //Create a basic shape for stone
		generateDirt(world);
		System.gc();
		caves(world, 100);
		stoneinter(world);
		cellauto(world);
		ores(world);
		System.gc();
				
		Biome biome;
		world.placeGrass(0, world.getWidth(), 0, 460);
				
		for(int i = 0; i < world.getTotalBiomes(); i++){
			biome = biomes[i];			
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
					world.getBlockGenerate(k, j).setBitMap(world.updateBlockBitMap(k, j)); //set the appropriate texture
				}
				
			}
		}
		
		System.gc();
		verifyAirExists(world);
		world.assessForAverageSky();
		System.gc();
		return world;
	}	
	
	/**
	 * Ensures that the world is not solid or null
	 * @param world - current world
	 */
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
	
	/**
	 * Generates an arctic biome by creating a forest, then converting it to snowy varients.
	 * @param world - current world
	 * @param x - the x position of the arctic in the 'world map' (block units, not ortho)
	 * @param w - the width of the arctic in blocks
	 */
	private void generateArctic(World world, int x, int w){
		generateForest(world,x,w);
		for(int j = world.getHeight() - 200; j > 0; j--){ //go through the the y-axis of the world
			for(int k =  x; k < x + w; k++){ //x-axis	
				//Start replacing trees with snow-covered variant
				if (world.getBlockGenerate(k, j+1).getBlockID() == Block.treebranch.getBlockID()) world.getBlockGenerate(k, j+1).setBitMap(world.getBlockGenerate(k, j+1).getBitMap() + 12);
				
				else if (world.getBlockGenerate(k, j+1).getBlockID() == Block.treetop.getBlockID() || world.getBlockGenerate(k, j+1).getBlockID() == Block.treetopl2.getBlockID() ||
						world.getBlockGenerate(k, j+1).getBlockID() == Block.treetopc1.getBlockID() || world.getBlockGenerate(k, j+1).getBlockID() == Block.treetopc2.getBlockID() ||
						world.getBlockGenerate(k, j+1).getBlockID() == Block.treetopr1.getBlockID() || world.getBlockGenerate(k, j+1).getBlockID() == Block.treetopr2.getBlockID()) world.getBlockGenerate(k, j+1).setBitMap(1);
				
				else if (world.getBlockGenerate(k, j+1).isSolid){ //If there is a solid block with air above
					if (world.getBlockGenerate(k, j).isOveridable){
						world.setBlockGenerate(Block.snowCover, k, j); // If the current block is air or a unneeded plant, replace the block with snow
					}
				}
				if (world.getBlockGenerate(k, j).getBlockID() == Block.grass.getBlockID()) world.getBlockGenerate(k, j).setBitMap(world.getBlockGenerate(k, j).getBitMap() + 16);								
			}
		}
	}
	
	/**
	 * Generates a forest biome, with trees, flowers, and tall grass
	 * @param world - current world
	 * @param x - the x position of the forest in the 'world map' (block units, not ortho)
	 * @param w - the width of the forest in blocks
	 */
	private void generateForest(World world, int x, int w){
		int plant = 0;		
		for(int i = world.getHeight() - 20; i > 0 ; i--){ //for the depth
			for(int j = x; j < x + w; j++){ //Throughout the width of the forest
				if (world.getBlockGenerate(j, i+1).getBlockID() == Block.grass.getBlockID() && world.getBlockGenerate(j, i).getBlockID() == Block.air.getBlockID()){ //If the block beneath the current cell is grass
					plant = (int)(Math.random() * 100 + 1); //Decide what plant will be placed									
					int space = 4;
					if (plant <= 40 && i <= 450){ //If a tree is to be placed
						world.growTree(space, j, i);							
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
	 * @param world - current world
	 * @param x - the x position of the desert in the 'world map' (block units, not ortho)
	 * @param w - the width of the desert in blocks
	 */
	private void generateDesert(World world, int x, int w)
	{
		//Add deserts to the world
		int height = 0, space = 0, cacti = 0, sand = 0;
		for(int i = world.getHeight() -200; i > 1 ; i--){ //for the depth
			for(int j = x; j < x + w; j++){ //Throughout the width of the desert
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
						if (i-height-space <= 0 || j < 1 || j > world.getWidth() - 1){
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
	 * Generates a large lump of adminium and stone at the bottom of the world, for the world's base
	 * @param world - current world
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
	
	/**
	 * Creates a surface of dirt for the world
	 * @param world - current world
	 */
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
		
	/**
	 * Adds caves to the world by creating random holes and performing cellular automata to hollow/smooth them
	 * @param world - current world
	 * @param emptyChance - the number to use for determining whether to place a hole or not (random number between 1 and emptyChance, empties cell on 28 or less)
	 */
	private void caves(World world, int emptyChance){
		int empty = 0;
		for (int i = world.getHeight() - 8; i > 445; i--){ //Go through the height
			for (int j = 1; j < world.getWidth() - 1; j++){ //go through the width
				empty = (int)(Math.random()*emptyChance + 1); //Select if the cell should be emptied
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
	
	/**
	 * Compares each cell to the cells around it, determining if it should be empty or solid 
	 * @param world - current world
	 */
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

	/**
	 * Selects places to create viens of ores (copper, tin, iron, coal, silver, gold, diamond)
	 * @param world - current world
	 */
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
	
	/**
	 * Designed to add individual veins of ores
	 * @param world - current world
	 * @param am - addition length of the vein (number from 0 to am -1, + 5 gives total length)
	 * @param j - x location to start the vien
	 * @param i - y location to start the vein
	 * @param ore - which ore to use (copper, tin, iron, coal, silver, gold, diamond)
	 */
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
	
	/**
	 * Creates a base of stone ontop of the stone and adminium of the base. Is later covered by dirt
	 * @param world - current world
	 */
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
	
	private Biome[] generateBiomes(World world)
	{
		Biome[] biomes = new Biome[world.getChunkWidth()];
		Biome t_biome;
		
		for(int i = 0; i < world.getChunkWidth(); i++)
		{
			t_biome = Biome.getBiomeFromBiomeList(getBiomeType()); //get the correct biome from the Biome class
			t_biome.setBiomeBounds(Chunk.getChunkWidth() * i, 0, Chunk.getChunkWidth(), world.getHeight()); //set the bounds			
			biomes[i] = t_biome;						
			System.out.println("Generated Biome " + i + ": " + t_biome.biomeName);
		}		
				
		world.setTotalBiomes(biomes.length);
		System.out.println("Total Biomes: " + world.getTotalBiomes());
		
		return biomes;
	}
	
	public void assignBiomes(World world, Biome[] biomes)
	{
		for(int i = 0; i < world.getWidth() / Chunk.getChunkWidth(); i++)
		{
			for(int j = 0; j < world.getHeight() / Chunk.getChunkHeight(); j++)
			{
				Chunk chunk = world.getChunk(i, j);
				Biome biome = biomes[i];
				chunk.setBiome(biome);
				world.setChunk(chunk, i, j);
			}
		}
		
		System.out.println("[WorldGenEarth]: World biomes assigned to chunks");
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
	 * completely clears the chunks, making them completely empty. Default biome is Biome.forest
	 * @param world - current world
	 */
	private void forciblyGenerateChunks(World world)
	{
		int chunkWidth = world.getWidth() / Chunk.getChunkWidth();
		int chunkHeight = world.getHeight() / Chunk.getChunkHeight();
	
		for(int i = 0; i < chunkWidth; i++)
		{
			for(int j = 0; j < chunkHeight; j++)
			{
				world.registerChunk(new Chunk(Biome.forest, i, j), i, j);
			}
		}
	}
}