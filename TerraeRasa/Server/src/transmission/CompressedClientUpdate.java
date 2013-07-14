package transmission;

import java.io.Serializable;
import java.util.Vector;

import enums.EnumHardwareInput;

public class CompressedClientUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int playerID;
	public CompressedInventory newInventory = null;
	public String[] commands = { };
	public EnumHardwareInput[] clientInput = { };
}
