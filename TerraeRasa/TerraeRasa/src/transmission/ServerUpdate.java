package transmission;

import java.io.Serializable;
import java.util.Vector;

public class ServerUpdate
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public Vector<String> values;
	public Vector<SuperCompressedChunk> chunks; 

	public ServerUpdate(Vector<String> values, Vector<SuperCompressedChunk> chunks)
	{
		this.values = values;
		this.chunks = chunks;
	}
	
	public ServerUpdate(String[] values, SuperCompressedChunk[] chunks)
	{
		this.values = new Vector<String>();
		for(String val : values)
		{	
			this.values.add(val);
		}
		this.chunks = new Vector<SuperCompressedChunk>();
		for(SuperCompressedChunk chunk : chunks)
		{
			this.chunks.add(chunk);
		}
	}
}
