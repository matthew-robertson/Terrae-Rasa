package transmission;

import java.io.Serializable;
import java.util.Vector;

public class CompressedServerUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String[] values;
	public SuperCompressedChunk[] chunks; 
	//public CompressedPlayer player;
	//public PositionUpdate update;
	public EntityUpdate[] entityUpdates;
	public PositionUpdate[] positionUpdates;
	public BlockUpdate[] blockUpdates;
	public StatUpdate[] statUpdates;
	public UpdateWithObject[] objectUpdates;
}
