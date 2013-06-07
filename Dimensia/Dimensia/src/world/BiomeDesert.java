package world;

import java.io.Serializable;

public class BiomeDesert extends Biome implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public BiomeDesert(int i, String name) 
	{
		super(i, name);
	}

	public void setSpawnableEntities()
	{
		//spawnableEntites.add();
	}
}