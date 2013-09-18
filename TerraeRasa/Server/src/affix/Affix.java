package affix;

import java.io.Serializable;
import java.util.Random;

import passivebonuses.PassiveBonus;
import auras.Aura;

/**
 * Affix is an abstract base class which provides the basic infrastructure for creating affixes.
 * In addition to the initialising some variables to default values, this class contains the 
 * getters and setters important for functioning. The following methods must be defined by a 
 * subclass:   
 * <ul>
 *  <li> {@link #verifyPowers(double[])} </li>
 *  <li> {@link #getAuras(double[])} </li>
 *  <li> {@link #rollPowers()} </li>
 *  <li> {@link #getPassives(double[])} </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public abstract class Affix
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected Random rng = new Random();
	/** This affix's plaintext name. */
	private String name;
	/** Whether or not this affix applies a prefix. */
	private boolean prefix;
	/** An integer value unique to this affix. */
	private int id;
	/** Indicates the strength of this affix in a non-specific way. Default value is 1. */
	private int tier;
	
	/**
	 * The default constructor for Affixes. This is not to be used alone, as it won't actually do anything.
	 * Initializes all of the variables and sets the name, nothing more.
	 * @param name is the name of the affix
	 * @param id the unique id number for this affix
	 * @param prefix true if this affix applies a prefix
	 */
	protected Affix(final String name, final int id, final boolean prefix){
		this.name = name;
		tier = 1;
		this.id = id;
		this.prefix = prefix;
	}		
	
	/**
	 * Gets the name of this affix.
	 * @return the name of this affix
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets whether or not this affix modifies the render name with a prefix. If true the text goes before
	 * the item's name when setting the render name. IE "PREFIX " + itemName.
	 * @return true if this Affix applies a prefix, otherwise false
	 */
	public boolean getPrefix()
	{
		return prefix;
	}
	
	/**
	 * Gets the ID of this affix, a final value which does not change and can be used to identify
	 * this affix.
	 * @return an integer value unique to this affix
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Sets this affix's tier - a higher tier means something is stronger.
	 * @param tier the new tier of this affix
	 * @return a reference to this affix
	 */
	public Affix setTier(int tier)
	{
		this.tier = tier;
		return this;
	}
	
	/**
	 * Gets this affix's tier - a higher tier means something is stronger.
	 * @return this affix's tier
	 */
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
	
	/**
	 * Rolls for the powers of a given affix. The result will vary by affix.
	 * @return some sort of double[] indicating powers for a given affix.
	 */
	public abstract double[] rollPowers();
	
	/**
	 * Generates a PassiveBonus[] for a given affix, given powers.
	 * @param powers the powers to use when generating this affix
	 * @return a PassiveBonus[] created from the given powers.
	 */
	public abstract PassiveBonus[] getPassives(double[] powers);

	/**
	 * Generates a PassiveBonus[] for a given affix, given powers.
	 * @param powers the powers to use when generating this affix
	 * @return a PassiveBonus[] created from the given powers.
	 */
	public abstract Aura[] getAuras(double[] powers);
	
	/**
	 * Verifies this affix's powers are not illegal. If they are they are forced inside the legal bounds. 
	 * This is to prevent cheating and stupidly overpowered affixes.
	 * @param powers a given power array to check based on this affix
	 */
	public abstract void verifyPowers(double[] powers);
}
