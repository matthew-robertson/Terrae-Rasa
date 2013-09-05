package transmission;
import java.io.Serializable;

public class CompressedServerUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String[] values;
//	public SuperCompressedChunk[] chunks; 
	public EntityUpdate[] entityUpdates;
	public PositionUpdate[] positionUpdates;
	public BlockUpdate[] blockUpdates;
	public StatUpdate[] statUpdates;
	public UpdateWithObject[] objectUpdates;

	public CompressedServerUpdate()
	{
		this.values = new String[0];
//		this.chunks = new SuperCompressedChunk[0];
		this.entityUpdates = new EntityUpdate[0];
		this.positionUpdates = new PositionUpdate[0];
		this.blockUpdates = new BlockUpdate[0];
		this.statUpdates = new StatUpdate[0];
		this.objectUpdates = new UpdateWithObject[0];
	}
}