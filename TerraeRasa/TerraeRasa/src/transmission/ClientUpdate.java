package transmission;

import java.util.Vector;

import enums.EnumHardwareInput;

public class ClientUpdate {
	public Vector<String> commands = new Vector<String>();
	public Vector<EnumHardwareInput> clientInput = new Vector<EnumHardwareInput>();
	public Vector<UpdateWithObject> objectUpdates = new Vector<UpdateWithObject>();
	
	public ClientUpdate()
	{
	}
		
	public synchronized void addCommand(String com)
	{
		if(com.equals(null) || com == null)
		{
			System.out.println("Null command issued");
		}
		this.commands.add(com);
	}
	
	public synchronized String[] getCommands()
	{
		String[] vals = new String[commands.size()];
		commands.copyInto(vals);
		return vals;
	}
	
	public synchronized void addInput(EnumHardwareInput input)
	{
		clientInput.add(input);
	}
	
	public synchronized EnumHardwareInput[] getHardwareInput()
	{
		EnumHardwareInput[] updates = new EnumHardwareInput[clientInput.size()];
		clientInput.copyInto(updates);
		return updates;
	}
	
	public synchronized void addObjectUpdate(UpdateWithObject update)
	{
		objectUpdates.add(update);
	}
	
	public synchronized UpdateWithObject[] getObjectUpdates()
	{
		UpdateWithObject[] updates = new UpdateWithObject[objectUpdates.size()];
		objectUpdates.copyInto(updates);
		return updates;
	}
}
