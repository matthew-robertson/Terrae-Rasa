package transmission;

import java.io.Serializable;
import java.util.List;

import utils.WorldText;
import entities.EntityItemStack;
import entities.EntityNPC;
import entities.EntityNPCEnemy;
import entities.EntityProjectile;
import enums.EnumWorldDifficulty;

public class WorldData 
		implements Serializable
{
	//TODO: parts of the world dont send. 
	//public Weather weather;
	//private boolean weatherFinished;
	
	private static final long serialVersionUID = 1L;
	public List<EntityItemStack> itemsList;
	public List<WorldText> temporaryText; 
	public List<EntityNPCEnemy> entityList;
	public List<EntityNPC> npcList;
	public List<EntityProjectile> projectileList;
	public int[] generatedHeightMap;
	public int averageSkyHeight;
	public int totalBiomes;
	public int chunkWidth;
	public int chunkHeight;
	public EnumWorldDifficulty difficulty;
	public String worldName;
	public long worldTime;
	public int width; 
	public int height; 
	public double previousLightLevel;
	public EntityNPCEnemy[] spawnList;
	public boolean lightingUpdateRequired;
}
