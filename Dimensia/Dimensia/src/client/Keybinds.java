package client;

import java.io.Serializable;

import org.lwjgl.input.Keyboard;

/**
 * <code>Keybinds implements Serializable</code> <br>
 * <code>Keybinds</code> defines a class that stores all the customizable keybinds for the game. Any relevant keybinds are listed here 
 * with descriptive names and can then be writen to or read from a file as required as part of the user's preferences.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Keybinds 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** The keybind to open/close the inventory of the player. Default value of Keyboard.KEY_E */
	public int inventoryToggle = Keyboard.KEY_E;
}
