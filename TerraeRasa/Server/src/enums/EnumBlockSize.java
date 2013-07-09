package enums;

public enum EnumBlockSize 
{
	ONEBYONE(1),
	ONEBYTWO(2),
	ONEBYTHREE(3),
	TWOBYONE(4),
	TWOBYTWO(5),
	TWOBYTHREE(6),
	THREEBYONE(7),
	THREEBYTWO(8),
	THREEBYTHREE(9);

	private int metaValue;
	
	EnumBlockSize(int i)
	{
		metaValue = i;
	}
	
	public int getMetaValue()
	{
		return metaValue;
	}
}
