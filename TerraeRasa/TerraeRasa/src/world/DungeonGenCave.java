package world;

import blocks.Block;

/**
 * DungeonGenCave aims to create a simple dungeon, consisting of a reasonably open cave with the dimensions (x,y).
 * 
 * @author Matt Robertson
 * @Version 1.0
 * @Since 1.0
 */
public class DungeonGenCave extends WorldGen{
	
	public World generate(World world){
		genBase(world);
		caves(world, 3, world.getWidth() - 6, 3, world.getHeight() - 6, 160);
		cellauto(world, 3, world.getWidth() - 6, 3, world.getHeight() - 6);
		cellauto(world, 3, world.getWidth() - 6, 3, world.getHeight() - 6);
		cellauto(world, 3, world.getWidth() - 6, 3, world.getHeight() - 6);
		
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
	
	private World genBase(World world){
		for (int i = 0; i < world.getWidth(); i++){
			for (int j = 0; j < world.getHeight() - 1; j++){
				if (i == 0 || i == world.getWidth() - 1 || j == 0 || j == world.getWidth() - 1){
					world.setBlockGenerate(Block.adminium, i, j);
				}
				else{
					
					world.setBlockGenerate(Block.stone, i, j);
				}
			}
		}
		return world;
	}
}