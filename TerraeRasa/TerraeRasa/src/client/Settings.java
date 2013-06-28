package client;


public class Settings 
		 
{
	public boolean gamePaused = false;
	/** A boolean which indicates whether or not the ingame pause menu is active. (True = paused) */
	public boolean menuOpen = true;
	public double soundVolume = 0.5;
	public double musicVolume = 0.5;
	public boolean smoothLighting = false;
	public boolean weatherEnabled = true;

	public boolean autosave = false;
	public int autosaveMinutes = 10;
	
	public boolean vsyncEnabled = false;
	
	public Keybinds keybinds = new Keybinds();
	
}
