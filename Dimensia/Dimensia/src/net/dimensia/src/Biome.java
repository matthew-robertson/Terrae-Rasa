package net.dimensia.src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Biome implements Serializable, Cloneable
{
	private static final long serialVersionUID = 1L;
	protected int biomeID;
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	protected List<EntityLivingNPCEnemy> spawnableEntites;
	protected String biomeName;
	
	protected Biome(int i, String name)
	{
		spawnableEntites = new ArrayList<EntityLivingNPCEnemy>();
		biomeName = name;
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		biomeID = i;
		if(biomeList[i] != null)
		{
			throw new RuntimeException("Biome Id already exists@ " + i);
		}
		biomeList[i] = this;
	}
		
	/**
	 * Copy constructor, an alternative to clone()
	 * @param biome the biome to duplicate
	 */
	public Biome(Biome biome) 
	{
		this.biomeID = biome.biomeID;
		this.x = biome.x;
		this.y = biome.y;
		this.width = biome.width;
		this.height = biome.height;
		this.spawnableEntites = biome.spawnableEntites;
		this.biomeName = biome.biomeName;
	}
	
	/**
	 * Sets where this biome instance is in the world. 
	 * @param x the starting point horizontally (in blocks)
	 * @param y the starting point vertically (in blocks). Generally 0.
	 * @param w the width of the biome (in blocks)
	 * @param h the height of the biome (in blocks) generally the height of the world
	 * @return a biome of set bounds. this return may or may not be useful.
	 */
	public Biome setBiomeBounds(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		return this;
	}
	
	public final float getX()
	{
		return x;
	}
	
	public final float getY()
	{
		return y;
	}
	
	public final float getWidth()
	{
		return width;
	}
	
	public final float getHeight()
	{
		return height;
	}
	
	public final String getBiomeName()
	{
		return biomeName;
	}
	
	protected void setSpawnableEntities()
	{
	}
	
	/**
	 * Returns a copy of the specified biome. The actual list is private down due to pointer (reference) errors
	 * @param index the index of biomeList to return, also known as the biomeID
	 * @return the biome at the specified index, or null if it doesnt exist
	 */
	public final static Biome getBiomeFromBiomeList(int index)
	{
		return new Biome(biomeList[index]);
	}
	
	private final static Biome biomeList[] = new Biome[3];
	
	/** Biome Declarations **/
	
	public static final Biome forest = new BiomeForest(0, "Forest");
	public static final Biome desert = new BiomeDesert(1, "Desert");
	public static final Biome arctic = new BiomeArctic(2, "Arctic");
	
	
}