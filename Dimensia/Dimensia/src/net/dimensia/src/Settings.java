package net.dimensia.src;

import java.io.Serializable;


public class Settings 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public boolean gamePaused = false;
	public boolean menuOpen = true;
	public float volume = 0.5f;
	public boolean smoothLighting = false;
	public boolean weatherEnabled = true;

	public boolean autosave = true;
	public int autosaveMinutes = 10;
	
	public boolean vsyncEnabled = false;
	
	public Keybinds keybinds = new Keybinds();
	
}
