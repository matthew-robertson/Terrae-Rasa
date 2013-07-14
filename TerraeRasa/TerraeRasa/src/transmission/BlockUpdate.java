package transmission;

import java.io.Serializable;

public class BlockUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public SuperCompressedBlock block;
	public int x;
	public short y;	
	/** 0 = front; 1 = back*/
	public byte type;
	
	public BlockUpdate()
	{
		this.type = 0;
	}
}
