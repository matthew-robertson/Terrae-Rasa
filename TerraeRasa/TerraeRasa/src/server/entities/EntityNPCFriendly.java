package server.entities;


public class EntityNPCFriendly extends EntityNPC
{
	private static final long serialVersionUID = 1L;

	public EntityNPCFriendly(int i, String name) 
	{
		super(i, name);
		
		if (npcList[i] != null){
			throw new RuntimeException("NPC already exists @" + i);
		}		
		npcList[i] = this;
	}
	
	public static final EntityNPCFriendly[] npcList = new EntityNPCFriendly[5];	
	public static final EntityNPCFriendly test = (EntityNPCFriendly) new EntityNPCFriendly(0, "Test").setIsAlert(true).setBaseSpeed(2.5f).setSpeech(new String[] { "Hai!", "I am a test NPC.", "I can't give you any quests."}).setUpwardJumpHeight(30).setJumpSpeed(6);

}
