package auras;

import java.io.Serializable;
import java.util.Vector;

/**
 * An aura container holds an array of auras for further use. For example, this is useful when 
 * reversing auras that come from an armour set. Auras cannot be changed after being added to 
 * an Aura Container, but can be copied to a new aura container. This restriction is to prevent
 * corruption or weird side effects which make auras last forever.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraContainer 
		 implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** A collection of auras to be used at some point later on. There's a good chance this belong to some piece of armour. */
	private Aura[] auras;
	
	/**
	 * Constructs a new AuraContainer with the specified auras.
	 * @param auras the auras for this container
	 */
	public AuraContainer(Aura[] auras)
	{
		this.auras = auras;
	}
	
	/**
	 * Constructs a new AuraContainer with the specified auras. This version of the constructor 
	 * accepts a Vector instead of an Array.
	 * @param allAuras a Vector of Auras that will be stored in the AuraContainer
	 */
	public AuraContainer(Vector<Aura> allAuras) 
	{
		this.auras = new Aura[allAuras.size()];
		allAuras.copyInto(this.auras);
	}

	/**
	 * Gets the aura at the given index. This will throw an exception if out of bounds.
	 * @param index the index of the aura to get
	 * @return the aura at the given index
	 */
	public Aura getAtIndex(int index)
	{
		return auras[index];
	}
	
	/**
	 * Gets an array with all the auras.
	 * @return all the auras in this container
	 */
	public Aura[] getAll()
	{
		return auras;
	}
	
	/**
	 * Gets the total number of auras.
	 * @return the total number of auras
	 */
	public int length()
	{
		return auras.length;
	}
}

