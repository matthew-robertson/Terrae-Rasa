package affix;

import java.io.Serializable;

/**
 * Provides the basic infrastructure for creating affixes.
 * In addition to the intializing constructor, this class contains the getters and setters important for functioning.
 * @author Matthew Robertson
 *
 */
public class DisplayableAffix
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String name;
	/**
	 * Base->0
	 * Sturdy->1
	 * Frenzied->2
	 * Destruction->3
	 */
	protected int id;
	/**Indicates the strength of this affix. Default value is 1. */
	private int tier;
	
	/**
	 * The default constructor for Affixes. This is not to be used alone, as it won't actually do anything.
	 * Initializes all of the variables and sets the name, nothing more.
	 * @param name is the name of the affix
	 */
	public DisplayableAffix(String name){
		this.name = name;
		tier = 1;
		this.id = 0;
	}
	
	public DisplayableAffix setName(String name){
		this.name = name;
		return this;
	}
	
	public DisplayableAffix(Affix affix)
	{
		this.name = affix.getName();
		this.id = affix.getID();
		this.tier = affix.getTier();
	}
	
	public String getName(){
		return name;
	}
	
	public int getID()
	{
		return id;
	}
	
	public DisplayableAffix setTier(int tier)
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
}
