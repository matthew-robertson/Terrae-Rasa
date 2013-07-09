package server;

public class GameEngineThread extends Thread
{
	GameEngine gameEngine;
	
	public GameEngineThread(GameEngine engine)
	{
		this.gameEngine = engine;
	}
	
	public void run()
	{
		gameEngine.init();
		gameEngine.run();		
	}
}
