package affix;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import passivebonuses.PassiveBonus;
import auras.Aura;

/**
 * Provides the basic infrastructure for creating affixes.
 * In addition to the intializing constructor, this class contains the getters and setters important for functioning.
 * @author Matthew Robertson
 *
 */
public class Affix
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected Random rng;
	private String name;
	private Vector<PassiveBonus> passives;
	private Vector<Aura> auras;
	private boolean prefix;
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
	public Affix(String name){
		this.name = name;
		tier = 1;
		this.id = 0;
		rng = new Random();
		passives = new Vector<PassiveBonus>();
		auras = new Vector<Aura>();
		prefix = true;
	}
	
	public Affix addPassive(PassiveBonus bonus){
		passives.add(bonus);
		return this;
	}
	
	public Affix addAura(Aura aura){
		auras.add(aura);
		return this;
	}
	
	public Affix setName(String name){
		this.name = name;
		return this;
	}
	
	public Affix setPassives(Vector<PassiveBonus> passives){
		this.passives = passives;
		return this;
	}
	
	public Affix setAuras(Vector<Aura> auras){
		this.auras = auras;
		return this;
	}
	
	/**
	 * Sets whether the name of the affix is a Prefix or suffix.
	 * @param flag - true for prefix, false for suffix.
	 * @return the affix
	 */
	public Affix setPrefix(boolean flag){
		prefix = flag;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	public PassiveBonus[] getPassives(){
		PassiveBonus[] bonus = new PassiveBonus[passives.size()];
		passives.copyInto(bonus);
		return bonus;
	}
	
	public Aura[] getAuras(){
		Aura[] aura = new Aura[auras.size()];
		auras.copyInto(aura);
		return aura;
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
}
