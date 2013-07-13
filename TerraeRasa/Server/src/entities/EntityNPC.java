package entities;

import utils.AIManager;
import world.World;

public class EntityNPC extends EntityLiving 
{
	private static final long serialVersionUID = 1L;

	protected String name;
	protected int npcID;
	protected int iconX;
	protected int iconY;
	protected String[] speech;
	protected int speechIndex;
	public int ticksSinceLastWander;
	public int ticksSinceLastProjectile;
	public int wanderLeft;
	public int wanderRight;
	public boolean alert;	
	
	/**
	 * Creates a new EntityNPC, assigning it an ID and name.
	 * @param id the EntityNPC's id
	 * @param name the EntityNPC's name
	 */
	public EntityNPC (int id, String name)
	{
		super();
		this.name = name;
		npcID = id;
		blockWidth = 2;
		blockHeight = 3;
		textureWidth = 16;
		textureHeight = 16;
		maxHealth = 1;
		health = 1;
		width = 12;
		height = 18;
		maxHeightFallenSafely = 144;
		ticksSinceLastWander = 0;
		setBaseSpeed(2.5f);
		speech = new String[10];
		wanderLeft = 0;		
		wanderRight = 0;		
		alert = false;	
	}
	
	/**
	 * Creates a deep copy of this EntityNPC, calling all super class copy constructors.
	 * @param entity the entity to make a deep copy of
	 */
	public EntityNPC(EntityNPC entity)
	{
		super(entity);
		this.name = entity.name;
		this.npcID = entity.npcID;
		this.iconX = entity.iconX;
		this.iconY = entity.iconY;
		this.speech = entity.speech;
		this.speechIndex = entity.speechIndex;
		this.ticksSinceLastWander = entity.ticksSinceLastWander;
		this.ticksSinceLastProjectile = entity.ticksSinceLastProjectile;
		this.wanderLeft = entity.wanderLeft;
		this.wanderRight = entity.wanderRight;
		this.alert = entity.alert;
	}
		
	/**
	 * Basic method to control AI. If the NPC isn't stunned, will chase the player if alert. Will wander otherwise. Gravity is applied 
	 * after any actions are taken
	 * @param world - current world
	 * @param target - entity to chase/retreat from
	 */
	public void applyAI(World world, EntityPlayer player, EntityPlayer target){
		if(!isStunned()){
			if (alert){
				AIManager.AIChaseAndRetreat(world, player, this, target, true );
			}
			else {
				AIManager.AIWander(world, player, this);
			}
		}
		
		applyGravity(world);	
	}	
	
	/**
	 * triggers when the actor approaches a player.
	 * Currently only makes them speak and rotates through their lines
	 */
	public void onPlayerNear()
	{
		speechIndex++;
		if (speechIndex >= speech.length){
			speechIndex = 0;
		}
		else if (speech[speechIndex] == null){
			speechIndex = 0;
		}
		AIManager.AISpeech(speech[speechIndex]);
	}		
		
	protected EntityNPC setName(String s){
		name = s;
		return this;
	}
	
	protected EntityNPC setSpeech(String[] s){
		speech = s;
		return this;
	}
	
	protected EntityNPC setIconIndex(int x, int y)
	{
		iconX = x;
		iconY = y;
		return this;
	}
	
	public EntityNPC setBaseSpeed(double f)
	{
		baseSpeed = f;
		return this;
	}	
		
	public EntityNPC setIsAlert(boolean bool){
		alert = bool;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Checks if a jump is required to progress
	 * @param world - current world
	 * @param direction - true if moving right, false if left
	 * @return true if jump is needed, false if not
	 */
	public int isJumpRequired(World world, boolean direction, boolean up){
		double heightCheck;
		
		if ((alert) || (up && !alert)){
			heightCheck = getUpwardJumpHeight() / 6;
		}
		else {
			heightCheck = blockHeight;
		}
		
		//If checking the right side
		if (direction){
			//Check all the blocks in a line up to the npc's block height if any are solid, return true
			for (int i = 0; i <= heightCheck; i++){
					if (world.getBlockGenerate((int)(x + width) / 6, (int)(y + height) / 6 - 1 - i).getIsSolid()){
						if (up){
							return i;
						}
						else return -1;
					}
			}
		}
		//if checking the left side
		else if (!direction){
			//Check all the blocks in a line up to the npc's block height if any are solid, return true
			for (int i = 0; i <= heightCheck; i++){						
				 if (world.getBlockGenerate((int)(x) / 6 - 1, (int)(y + height) / 6 - 1 - i).getIsSolid()){
					if (up){
						return i;
					}
					else return -1;
				 }
			}
		}
		//Else return false
		return -1;
	}
	
	/**
	 * Checks to see if a jump is possible on the given side
	 * @param world - current world
	 * @param direction - true if moving right, false if left
	 * @return true if jump is possible, false if not
	 */
	public boolean isJumpPossibleAndNeeded(World world, boolean direction, boolean up){
		int start = isJumpRequired(world, direction, up);
		if (start != -1){
			int maxBlockSpace = 0;
			int blockSpace = 0;
			//If checking the right side
			if (direction){
				maxBlockSpace = 0;
				blockSpace = 0;
				//Check all the blocks in a line up to the maximum jump height + the npc's block height
				for (int i = start; i < (getUpwardJumpHeight() + height) / 6; i++){
					 if (!world.getBlockGenerate((int)(x + width) / 6, (int)(y + height) / 6 - 1 - i).getIsSolid()){
						 blockSpace++;
						 if (blockSpace > maxBlockSpace){
							 maxBlockSpace = blockSpace;
						 }
					 }
					 else{
						 blockSpace = 0;
					 }
					 if (maxBlockSpace >= blockHeight){
						  return true;
					 }
				}
			}
			//if checking the left side
			else if (!direction){
				maxBlockSpace = 0;
				blockSpace = 0;
				//Check all the blocks in a line up to the maximum jump height + the npc's block height
				for (int i = start; i < (getUpwardJumpHeight() + height) / 6; i++){
					 if (!world.getBlockGenerate((int)(x) / 6 - 1, (int)(y + height) / 6 - 1 - i).getIsSolid()){
						 blockSpace++;
						 if (blockSpace > maxBlockSpace){
							 maxBlockSpace = blockSpace;
						 }
					 }
					 else{
						 blockSpace = 0;
					 }
					 if (maxBlockSpace >= blockHeight){
						 return true;
					 }
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if it is safe for the actor to move in the provided direction
	 * @param world - the current world
	 * @param direction - true if attempting to move right, false if moving left
	 * @return - true if it's safe to walk (a block is solid and the actor will land on it, up to it's maximum safe fall distance down, and it's width wide)
	 */
	public boolean isWalkingSafe(World world, boolean direction){
		//If checking the right side
		if (direction){
			//Check all the blocks in a line up to the npc's block width if any are solid, return true
			for (int i = 0; i <= blockWidth; i++){
				for (int j = 0; j <= (getMaxHeightFallenSafely() / 6); j++)
				if (world.getBlockGenerate((int)(x + width) / 6 - 1 + i, (int)(y + height) / 6 + j).getIsSolid()){
					return true;							 
				}
			}
		}
		
		//if checking the left side
		else if (!direction){
			//Check all the blocks in a line up to the npc's block width if any are solid, return true
			for (int i = 0; i <= blockWidth; i++){						
				for (int j = 0; j <= (getMaxHeightFallenSafely() / 6); j++){ 
					if (world.getBlockGenerate((int)(x) / 6 - i, (int)(y + height) / 6 + j).getIsSolid()){
						return true;
					}
				 }
			}
		}
		//Else return false
		return false;
	}

}
