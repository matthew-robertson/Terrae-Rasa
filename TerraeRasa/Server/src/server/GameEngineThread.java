package server;

public class GameEngineThread extends Thread
{
	GameEngine gameEngine;
	
	public GameEngineThread(GameEngine engine)
	{
		setDaemon(true);
		this.gameEngine = engine;
	}
	
	public void run()
	{
		gameEngine.run();		
	}
}
