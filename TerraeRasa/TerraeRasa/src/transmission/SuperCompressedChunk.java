package transmission;

import java.io.Serializable;

import world.Biome;


public class SuperCompressedChunk 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public Biome biome;
	public SuperCompressedBlock[][] backWalls;
	public SuperCompressedBlock[][] blocks;
	public int x;
	public boolean wasChanged;
	public static final int CHUNK_WIDTH = 100;
	public int height;		
}
