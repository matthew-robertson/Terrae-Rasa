package net.dimensia.src;

public class EntityNPC extends EntityLiving implements Cloneable {
	public EntityNPC (int i, String s){
		super();
		name = s;
		npcID = i;
		blockWidth = 2;
		blockHeight = 3;
		textureWidth = 16;
		textureHeight = 16;
		maxHealth = 1;
		health = 1;
		width = 12;
		height = 18;
		ticksSinceLastWander = 0;
		baseSpeed = 2.5f;
		speech = new String[10];
		
		if (npcList[i] != null){
			throw new RuntimeException("NPC already exists @" + i);
		}		
		npcList[i] = this;
	}
	
	/**
	 * Overrides Object.clone() to provide cloning functionality to EntityNPC.
	 * Creates a deep copy of the EntityNPC instance on which the method is 
	 * called.
	 * @return a deep copy of the EntityNPC
	 */
	public EntityNPC clone()
	{
		try
		{
			return (EntityNPC) super.clone();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//Ai fucntions to go in a new AIManager later
	
	public void applyAI(World world){
		AIManager.AIWander(world, this);
	}
	
	public void onPlayerNear(){
		i++;
		if (i >= speech.length){
			i = 0;
		}
		else if (speech[i] == null){
			i = 0;
		}
		AIManager.AISpeech(speech[i]);
	}		
	
	//Setters
	
	protected EntityNPC setName(String s){
		name = s;
		return this;
	}
	
	protected EntityNPC setWidthandHeight(int x, int y)
	{
		width = x;
		height = y;
		return this;
	}
	
	protected EntityNPC setTexture(Texture t){
		texture = t;
		return this;
	}
	
	protected EntityNPC setSpeech(String[] s){
		speech = s;
		return this;
	}
	
	protected EntityNPC setMaxHealth(int i)
	{
		maxHealth = i;
		health = i;
		return this;
	}
		
	protected EntityNPC setWorldDimensions(int i, int j)
	{
		width = i;
		height = j;
		return this;
	}
	
	protected EntityNPC setBlockDimensions(int i, int j)
	{
		blockWidth = i;
		blockHeight = j;
		return this;
	}
	
	protected EntityNPC setBlockAndWorldDimensions(int i, int j)
	{
		setWorldDimensions(i * 6, j * 6);
		setBlockDimensions(i, j);
		return this;
	}
	
	protected EntityNPC setIconIndex(int i, int j)
	{
		iconIndex = i * 16 + j;
		return this;
	}
	
	protected EntityNPC setTextureDimensions(int i, int j)
	{
		textureWidth = i;
		textureHeight = j;
		return this;
	}
	
	protected EntityNPC setBaseSpeed(float f)
	{
		baseSpeed = f;
		return this;
	}
	
	public EntityNPC setUpwardJumpHeight(int i){
		upwardJumpHeight = i;
		return this;
	}
	
	public EntityNPC setJumpSpeed(float f){
		jumpSpeed = f;
		return this;
	}
	
	public EntityNPC setIsAlert(boolean bool){
		alert = bool;
		return this;
	}
	
	//getters
	
	public String getName(){
		return name;
	}
	
	public float getWidth()
	{
		return width;
	}
	
	public float getHeight()
	{
		return height; 
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
	public float getBlockWidth()
	{
		return blockWidth;
	}
	
	public float getBlockHeight()
	{
		return blockHeight;
	}
	
	public float getUpwardJumpHeight(){
		return upwardJumpHeight;
	}
	
	public float getJumpSpeed(){
		return jumpSpeed;
	}
	
	private EntityNPC[] npcList = new EntityNPC[5];
	//npc declarations
	public static EntityNPC test = new EntityNPC(0, "Test").setIsAlert(true).setTexture(Render.dino).setBaseSpeed(1.0f).setUpwardJumpHeight(30).setJumpSpeed(6).setSpeech(new String[] { "Hai!", "I am a test NPC.", "I can't give you any quests."});
	
	
	protected String name;
	protected int npcID;
	protected int iconIndex;
	protected Texture texture;
	protected String[] speech;
	protected int i;
}
