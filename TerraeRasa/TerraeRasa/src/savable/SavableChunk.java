package savable;

import java.io.Serializable;
import java.util.Vector;

import utils.Position;

/**
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public class SavableChunk 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int biomeID;
	public SavableBlock[][] backWalls;
	public SavableBlock[][] blocks;
	public int x;
	public boolean wasChanged;
	public int height;
	public Vector<Position> lightPositions;
}