package world;

import io.Chunk;

import java.util.Random;


import blocks.Block;

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
public class WorldGenEarth extends WorldGen
{	
	private final Random random = new Random();
	
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
		Block[] placeableOres = {Block.tin, Block.copper, Block.iron, Block.coal, Block.silver, Block.gold, Block.diamond};
		Biome[] biomes = generateBiomes(world);
		assignBiomes(world, biomes);
		count = 0;
		generateBase(world); //Create the stone base for the world
		generateStone(world); //Create a basic shape for stone
		generateDirt(world);
		System.gc();		
		caves(world, 1, world.getWidth() - 1, 445, world.getHeight() - 445, 179);
		cellauto(world, 1, world.getWidth() - 2, 200, world.getHeight() - 206);
		cellauto(world, 1, world.getWidth() - 2, 200, world.getHeight() - 206);
		cellauto(world, 1, world.getWidth() - 2, 200, world.getHeight() - 206);
		stoneinter(world);
		cellauto(world, 1, world.getWidth() - 2, 0, world.getHeight() - 2);
		ores(world, 1, world.getWidth() - 4, 1, world.getHeight() - 11, placeableOres);
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
				if (world.getBlockGenerate(k, j+1).getID() == Block.treebranch.getID()) world.getBlockGenerate(k, j+1).setBitMap(world.getBlockGenerate(k, j+1).getBitMap() + 12);
				
				else if (world.getBlockGenerate(k, j+1).getID() == Block.treetop.getID() || world.getBlockGenerate(k, j+1).getID() == Block.treetopl2.getID() ||
						world.getBlockGenerate(k, j+1).getID() == Block.treetopc1.getID() || world.getBlockGenerate(k, j+1).getID() == Block.treetopc2.getID() ||
						world.getBlockGenerate(k, j+1).getID() == Block.treetopr1.getID() || world.getBlockGenerate(k, j+1).getID() == Block.treetopr2.getID()) world.getBlockGenerate(k, j+1).setBitMap(1);
				
				else if (world.getBlockGenerate(k, j+1).isSolid){ //If there is a solid block with air above
					if (world.getBlockGenerate(k, j).isOveridable){
						world.setBlockGenerate(Block.snowCover, k, j); // If the current block is air or a unneeded plant, replace the block with snow
					}
				}
				if (world.getBlockGenerate(k, j).getID() == Block.grass.getID()) world.getBlockGenerate(k, j).setBitMap(world.getBlockGenerate(k, j).getBitMap() + 16);								
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
				if (world.getBlockGenerate(j, i+1).getID() == Block.grass.getID() && world.getBlockGenerate(j, i).getID() == Block.air.getID()){ //If the block beneath the current cell is grass
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
				if (world.getBlockGenerate(j, i).getID() == Block.dirt.getID()|| world.getBlockGenerate(j, i).getID() == Block.grass.getID()){ //If the current block is dirt
					if (i > world.getHeight() - 390){
						if (j < x + 6 || j > x + w - 6 || i > world.getHeight() - 210){
							sand = (int)(Math.random()*100+1);
						}
						else{
							sand = (int)(Math.random()*50+1);
						}
						if (sand <= 50){
							if (world.getBlockGenerate(j, i+1).getID() != Block.sandstone.getID() && world.getBlockGenerate(j, i+1).getID() != Block.sand.getID()){
								world.setBlockGenerate(Block.sandstone, j, i);
							}
							else{
								world.setBlockGenerate(Block.sand, j, i);	
							}
						}
					}
					else{						
						if (world.getBlockGenerate(j, i+1).getID() != Block.sandstone.getID() && world.getBlockGenerate(j, i+1).getID() != Block.sand.getID()){
							world.setBlockGenerate(Block.sandstone, j, i);							
						}
						else{
							world.setBlockGenerate(Block.sand, j, i);	
						}
					}
				}
				if (i <= 460 && (world.getBlockGenerate(j, i+1).getID() == Block.sand.getID() || world.getBlockGenerate(j, i+1).getID() == Block.sandstone.getID()) && world.getBlockGenerate(j, i).getID() == Block.air.getID()){ //If the block beneath the current cell is grass
					cacti = (int)(Math.random() * 100 + 1); //Decide what plant will be placed									
					space = 1;
					if (cacti <= 30){ //If a cactus is to be placed
						height = (int)(Math.random() * 3 + 2); //Determine the height of the cactus
						if (i-height-space <= 0 || j < 1 || j > world.getWidth() - 1){
							height = 0;
							space = 0;
						}
						if (height != 0){
							if (world.getBlockGenerate(j, i-height-space).getID() == Block.air.getID() && world.getBlockGenerate(j-1, i).getID() == Block.air.getID() && world.getBlockGenerate(j+1, i).getID() == Block.air.getID()){ //If there is room for the cactus up and to the left/right	
								for (int k = i; k >= i - height; k--){ //Place the cactus
									world.setBlockGenerate(Block.cactus, j, k);
								}
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
				if (world.getBlockGenerate(k, i + 1).getID() == Block.stone.getID()){ //If the block beneath is stone
					dirt = 1; //Place dirt
				}
				if (k - amount <= 0 || k + amount >= world.getWidth()){ //If the amount chosen would take it off the map
					amount = 0; //Do not use an amount
				}				
				if (dirt == 1 && world.getBlockGenerate(k, i + 1).getID() != Block.air.getID()){ //If dirt is being placed and the one beneath is not empty
					if (world.getBlockGenerate(k + amount, i + 1).getID() != Block.air.getID() || world.getBlockGenerate(k - amount, i + 1).getID() != Block.air.getID()){ //If the spot the amount chosen down to the left, or to the right is occupied						
						for (int j = -amount; j <= amount; j++){ //Loop given the amount							
							world.setBlockGenerate(Block.dirt, k + j, i); //Make each space dirt							
						}
					}					
				}
				
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
				if (world.getBlockGenerate(j, i-1).getID() != Block.air.getID() && world.getBlockGenerate(j, i-2).getID() != Block.air.getID() && world.getBlockGenerate(j, i-3).getID() != Block.air.getID()){
					choice = (int)(Math.random()*100+1);
					if (choice <= 80) 
						world.setBlockGenerate(Block.stone, j, i);
				}
				if (world.getBlockGenerate(j-1, i).getID() == Block.dirt.getID()) 
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
			for(int j = 0; j < world.getHeight() / world.getHeight(); j++)
			{
				Chunk chunk = world.getChunk(i);
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
}