package enums;

public enum EnumEventType 
{
	EVENT_BLOCK_BREAK(1),
	EVENT_BLOCK_PLACE(2),
	EVENT_BLOCK_BREAK_LIGHT(3),
	EVENT_BLOCK_PLACE_LIGHT(4);
	
	public int value;
	
	EnumEventType(int val)
	{
		this.value = val;
	}
}
