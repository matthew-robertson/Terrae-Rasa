package auras;

import java.io.Serializable;

/**
 * DisplayableAura is a data structure used to display an Aura client side.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class DisplayableAura
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** A number unique to this DisplayableAura, used to distinguish it from other DisplayableAura. */
	private final long id;
	/** The description of this DisplayableAura. */
	protected String description;
	
	/**
	 * Constructs a new DisplayableAura from an Aura
	 * @param aura the Aura to use in the construction of this DisplayableAura
	 */
	protected DisplayableAura(Aura aura)
	{
		this.id = aura.getID();
		this.description = aura.toString();
	}

	/**
	 * Gets the description of this DisplayableAura.
	 * @return the description of this DisplayableAura
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Returns the id of this DisplayableAura - a unique value to this DisplayableAura.
	 * @return a value unique to this Aura
	 */
	public final long getID()
	{
		return id;
	}
	
	/**
	 * Overrides Object.toString() to accurately describe this DisplayableAura in text form.
	 */
	public String toString()
	{
		return description;
	}
}
