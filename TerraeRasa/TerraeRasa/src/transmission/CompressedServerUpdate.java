package transmission;

import java.io.Serializable;

public class CompressedServerUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String[] values;
	public SuperCompressedChunk[] chunks; 
	public CompressedPlayer player;
	public PositionUpdate update;
	
	public CompressedServerUpdate()
	{
	}
}
