package client;

import java.util.Vector;

import transmission.ServerUpdate;
import world.World;
import entities.EntityPlayer;
import enums.EnumHardwareInput;

public class EngineLock 
{
	private Vector<EnumHardwareInput> hardwareInput = new Vector<EnumHardwareInput>();
	private GameEngine engine;
	private Vector<ServerUpdate> serverUpdates = new Vector<ServerUpdate>();
	
	public EngineLock(GameEngine engine)
	{
		this.engine = engine;
	}
	
	public synchronized void addHardwareInput(Vector<EnumHardwareInput> newInput)
	{
		hardwareInput.addAll(newInput);
	}
	
	//Deletes too!
	public synchronized EnumHardwareInput[] yieldHardwareInput()
	{
		EnumHardwareInput[] inputs = new EnumHardwareInput[hardwareInput.size()];
		hardwareInput.copyInto(inputs);
		hardwareInput.clear();
		return inputs;
	}
	
	public synchronized void addUpdate(ServerUpdate update)
	{
		serverUpdates.add(update);
	}
	
	//Deletes too
	public synchronized ServerUpdate[] yieldServerUpdates()
	{
		ServerUpdate[] updates = new ServerUpdate[serverUpdates.size()];
		serverUpdates.copyInto(updates);
		serverUpdates.clear();
		return updates;
	}
	
	public synchronized boolean hasUpdates()
	{
		return serverUpdates.size() > 0;
	}
	
	public synchronized boolean hasHardwareInputs()
	{
		return hardwareInput.size() > 0;
	}
		
	public synchronized EntityPlayer getPlayer()
	{
		return engine.getPlayer();
	}
	
	public synchronized void setWorld(World world)
	{
		engine.setWorld(world);
		GameEngine.flagAsMPPlayable();
	}
}
