package transmission;

import java.io.Serializable;
import java.util.List;

import entities.DisplayableEntity;
import enums.EnumWorldDifficulty;

public class WorldData 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public List<DisplayableEntity> itemsList;
	public List<DisplayableEntity> enemyList;
	public List<DisplayableEntity> npcList;
	public List<DisplayableEntity> projectileList;
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
	public boolean lightingUpdateRequired;
	public TransmittablePlayer[] otherplayers;
}
