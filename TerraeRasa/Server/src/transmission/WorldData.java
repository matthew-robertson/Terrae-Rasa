package transmission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entities.DisplayableEntity;
import entities.EntityItemStack;
import entities.EntityNPC;
import entities.EntityNPCEnemy;
import entities.EntityProjectile;
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
	
	public void setLists(List<EntityItemStack> itemstacks, List<EntityNPCEnemy> enemies, List<EntityNPC> npcs, List<EntityProjectile> projectiles)
	{
		itemsList = new ArrayList<DisplayableEntity>();
		enemyList = new ArrayList<DisplayableEntity>();
		npcList = new ArrayList<DisplayableEntity>();
		projectileList = new ArrayList<DisplayableEntity>();
		for(EntityItemStack stack : itemstacks)
		{
			itemsList.add(new DisplayableEntity(stack));
		}
		for(EntityNPCEnemy enemy : enemies)
		{
			enemyList.add(new DisplayableEntity(enemy));
		}
		for(EntityNPC npc : npcs)
		{
			npcList.add(new DisplayableEntity(npc));
		}
		for(EntityProjectile projectile : projectiles)
		{
			projectileList.add(new DisplayableEntity(projectile));
		}
	}
}
