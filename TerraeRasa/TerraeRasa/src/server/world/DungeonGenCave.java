package server.world;

import blocks.Block;

/**
 * DungeonGenCave aims to create a simple dungeon, consisting of a reasonably open cave with the dimensions (x,y).
 * 
 * @author Matt Robertson
 * @Version 1.0
 * @Since 1.0
 */
public class DungeonGenCave extends WorldGen{
	
	public WorldServerEarth generate(WorldServerEarth world, int xLoc, int width, int yLoc, int depth){
		genBase(world, xLoc, width, yLoc, depth);
		caves(world, xLoc + 3, (xLoc + width)- 6, yLoc + 3, (yLoc + depth) - 6, 160);
		cellauto(world, xLoc + 3, (xLoc + width)- 6, yLoc + 3, (yLoc + depth) - 6);
		cellauto(world, xLoc + 3, (xLoc + width)- 6, yLoc + 3, (yLoc + depth) - 6);
		cellauto(world, xLoc + 3, (xLoc + width)- 6, yLoc + 3, (yLoc + depth) - 6);
		
		for(int j = world.getHeight() - 1; j > 0; j--){ //go through the the y-axis of the world
			for(int k = 1; k < world.getWidth() - 1; k++){ //x-axis
				if (world.getBlock(k,j).isSolid){
					world.setBitMap(k, j, world.updateBlockBitMap(k, j)); //set the appropriate texture
				}
				
			}
		}
		
		System.gc();
		verifyAirExists(world, xLoc, (xLoc + width), yLoc, (yLoc + depth));
		world.assessForAverageSky();
		System.gc();
		
		return world;
	}
	
	/**
	 * Generates a shell for the dungeon
	 * @param world - current world
	 * @param x - x-position to begin placing
	 * @param w - width of the area
	 * @param y - y-position to begin placing
	 * @param h - depth of the area
	 * @return
	 */
	private WorldServerEarth genBase(WorldServerEarth world, int x, int w, int y, int h){
		for (int i = x; i < (x + w); i++){
			for (int j = y; j < (y + h) - 1; j++){
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