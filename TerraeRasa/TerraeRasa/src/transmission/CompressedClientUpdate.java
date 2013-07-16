package transmission;

import java.io.Serializable;

import enums.EnumHardwareInput;

public class CompressedClientUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int playerID;
	public String[] commands = { };
	public EnumHardwareInput[] clientInput = { };
	public UpdateWithObject[] objectUpdates = { };
}
