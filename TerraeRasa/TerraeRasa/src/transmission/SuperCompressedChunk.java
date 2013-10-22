package transmission;

import java.io.Serializable;

import utils.Position;
import utils.WeatherData;
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
	public Position[] lightSources;
	public WeatherData weatherData;
}
