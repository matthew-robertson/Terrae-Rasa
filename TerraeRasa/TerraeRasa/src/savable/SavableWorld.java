package savable;

import java.util.List;

import server.entities.EntityItemStack;

import enums.EnumWorldDifficulty;

public class SavableWorld 
{
	public int width;
	public int height;
	public int chunkWidth;
	public int chunkHeight;
	public int averageSkyHeight;
	public int[] generatedHeightMap;
	public long worldTime;
	public String worldName;
	public EnumWorldDifficulty difficulty;
	public List<EntityItemStack> itemsList;
	
}
