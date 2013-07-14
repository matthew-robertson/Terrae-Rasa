package transmission;

import java.io.Serializable;

import entities.Entity;

public class EntityUpdate 	
		implements Serializable
{
	private static final long serialVersionUID = 1L;

	public Entity updatedEntity; //set null if remove
	//'a' = add; 'c' = change(basically replace); 'r' = remove
	public char action;
	/**1-enemy; 2-friendly; 3-itemstack; 4-projectile, 5- player */
	public byte type;
	public int entityID;
	
}

/**
So... the position update by id seems to work. Next step is updating the entities server side. Spawning, etc.
-> send to client
-> update positions
-> remove/completely reupdate if needed




*/