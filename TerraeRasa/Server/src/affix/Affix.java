package affix;

import java.util.Random;

import passivebonuses.PassiveBonus;
import auras.Aura;

/**
 * Provides the basic infrastructure for creating affixes.
 * In addition to the intializing constructor, this class contains the getters and setters important for functioning.
 * @author Matthew Robertson
 *
 */
public abstract class Affix
{
	protected Random rng = new Random();
	private String name;
	private boolean prefix;
	private int id;
	/** Indicates the strength of this affix in a non-specific way. Default value is 1. */
	private int tier;
	
	/**
	 * The default constructor for Affixes. This is not to be used alone, as it won't actually do anything.
	 * Initializes all of the variables and sets the name, nothing more.
	 * @param name is the name of the affix
	 */
	protected Affix(final String name, final int id, final boolean prefix){
		this.name = name;
		tier = 1;
		this.id = id;
		this.prefix = prefix;
	}		
	
	public String getName(){
		return name;
	}
	
	public boolean getPrefix(){
		return prefix;
	}
	
	public int getID()
	{
		return id;
	}
	
	public Affix setTier(int tier)
	{
		this.tier = tier;
		return this;
	}
	
	public int getTier()
	{
		return tier;
	}
	
	/**
	 * Yields a string in the form of "AFFIX_ID" + " " + "Tier". <b>This does not include the actual roll of the stat just the affix and tier.</b>
	 * @return a string containing an affix's ID then TIER which is useful for crafting
	 */
	public String getCraftingID()
	{
		return "" + id + " " + tier;
	}
	
	public abstract double[] rollPowers();
	
	public abstract PassiveBonus[] getPassives(double[] powers);
	
	public abstract Aura[] getAuras(double[] powers);
	
	public abstract void verifyPowers(double[] powers);
}
