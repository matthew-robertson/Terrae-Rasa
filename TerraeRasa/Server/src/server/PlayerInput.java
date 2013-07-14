package server;

import world.World;
import entities.EntityPlayer;
import enums.EnumHardwareInput;

public class PlayerInput {
	private EntityPlayer associatedPlayer;
	private EnumHardwareInput[] inputs;
	private double newX;
	private double newY;
	
	public PlayerInput(EntityPlayer player, EnumHardwareInput[] inputs)
	{
		this.associatedPlayer = player;
		this.inputs = inputs;
	}
	
	public void handle(World world)
	{
		for(EnumHardwareInput input : this.inputs)
		{
			if(input == EnumHardwareInput.MOVE_LEFT)
			{
				associatedPlayer.moveEntityLeft(world);
				associatedPlayer.isFacingRight = false;
				
			}
			else if(input == EnumHardwareInput.MOVE_RIGHT)
			{
				associatedPlayer.moveEntityRight(world);
				associatedPlayer.isFacingRight = true;
				
			}
			else if(input == EnumHardwareInput.JUMP)
			{
				associatedPlayer.tryToJumpAgain(world);
			}			
		}
		newX = associatedPlayer.x;
		newY = associatedPlayer.y;
	}
	
	public int getAssociatedID()
	{
		return associatedPlayer.entityID;
	}
	
	public double newX()
	{
		return newX;
	}
	
	public double newY()
	{
		return newY;
	}
}
