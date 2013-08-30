package transmission;

import java.io.Serializable;

import utils.Position;
import world.Biome;
import world.Weather;


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
	public Position[] lightSources;
	public Weather weather;
}
