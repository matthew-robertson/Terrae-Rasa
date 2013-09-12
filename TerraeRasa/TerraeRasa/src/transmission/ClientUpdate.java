package transmission;

import java.util.Vector;

import enums.EnumHardwareInput;

public class ClientUpdate 
{
	private Vector<String> commands = new Vector<String>();
	private Vector<EnumHardwareInput> clientInput = new Vector<EnumHardwareInput>();
	private Vector<UpdateWithObject> objectUpdates = new Vector<UpdateWithObject>();
	
	public ClientUpdate()
	{
	}
		
	public void addCommand(String com)
	{
		if(com.equals(null) || com == null)
		{
			System.out.println("Null command issued");
		}
		this.commands.add(com);
	}
	
	public String[] getCommands()
	{
		String[] vals = new String[commands.size()];
		commands.copyInto(vals);
		return vals;
	}
	
	public Vector<String> getCommandsVector()
	{
		return commands;
	}
	
	public void addInput(EnumHardwareInput input)
	{
		clientInput.add(input);
	}
	
	public EnumHardwareInput[] getHardwareInput()
	{
		EnumHardwareInput[] updates = new EnumHardwareInput[clientInput.size()];
		clientInput.copyInto(updates);
		return updates;
	}
	
	public void addObjectUpdate(UpdateWithObject update)
	{
		objectUpdates.add(update);
	}
	
	public UpdateWithObject[] getObjectUpdates()
	{
		UpdateWithObject[] updates = new UpdateWithObject[objectUpdates.size()];
		objectUpdates.copyInto(updates);
		return updates;
	}
}
