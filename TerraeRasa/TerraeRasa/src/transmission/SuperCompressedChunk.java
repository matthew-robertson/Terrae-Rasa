package transmission;

import world.Biome;


public class SuperCompressedChunk 
{
	public Biome biome;
	public float[][] light;
	public float[][] diffuseLight;
	public float[][] ambientLight;
	public SuperCompressedBlock[][] backWalls;
	public SuperCompressedBlock[][] blocks;
	public int x;
	public boolean wasChanged;
	public static final int CHUNK_WIDTH = 100;
	public int height;
	public boolean lightUpdated;
	public boolean flaggedForLightingUpdate;
		
}
