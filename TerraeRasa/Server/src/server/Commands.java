package server;

public class Commands 
{
	// -- req world and player?
	public static void processCommand(String command)
	{
		String[] commandComponents = command.split(" ");
		//Everyone or above
		if(command.startsWith("/help"))
		{
			
		}
		if(command.startsWith("/kill"))
		{
			
		}
		if(command.startsWith("/whisper"))
		{
			
		}
		//Mod or above
		if(command.startsWith("/teleport"))
		{
			if(commandComponents.length == 3)
			{
				//player player tp
			}
			else if(commandComponents.length == 4)
			{
				//player <x,y> tp
			}
		}
		if(command.startsWith("/time-set"))
		{
			
		}
		if(command.startsWith("/mod"))
		{
			
		}
		if(command.startsWith("/unmod"))
		{
			
		}
		if(command.startsWith("/kick"))
		{
			
		}
		if(command.startsWith("/ban"))
		{
			
		}
		if(command.startsWith("/unban"))
		{
			
		}
		if(command.startsWith("/stop"))
		{
			
		}
		if(command.startsWith("/save-world"))
		{
			
		}
		if(command.startsWith("/whitelist"))
		{
			
		}
		//Admin only
		if(command.startsWith("/admin"))
		{

		}
		if(command.startsWith("/unadmin"))
		{
			
		}
		if(command.startsWith("/heal"))
		{
			
		}
		if(command.startsWith("/mana"))
		{
			
		}
		if(command.startsWith("/special"))
		{
			
		}
		if(command.startsWith("/give"))
		{
			
		}
		if(command.startsWith("/weather"))
		{
			//off and on
		}
		if(command.startsWith("/effect"))
		{
			
		}
		if(command.startsWith("/affix"))
		{
			
		}
		if(command.startsWith("/say"))
		{
			
		}

		
		
	}
	
	
	
	
	
}
