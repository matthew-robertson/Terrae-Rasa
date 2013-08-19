package audio;

import java.io.IOException;
import java.util.Hashtable;

import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import client.Settings;
import client.TerraeRasa;

public class SoundEngine{
	
	protected static Hashtable<String, Song> musicDictionary = new Hashtable<String, Song>();
	protected static Hashtable<String, Audio> soundDictionary = new Hashtable<String, Audio>();
	private static Song currentMusic;
	private static Song oldMusic;
	private static Settings settings;
	private static boolean initialized = false;
	
	private SoundEngine() {
	}
	
	public static void initialize(Settings clientSettings){
		//Guard against extra initializations
		if(initialized) {
			return;
		}
		initialized = true;
		settings = clientSettings;
		
		/**
		 * Initialize music
		 */
		musicDictionary.put("Main Music", loadMusic("/Music/BGMMain.ogg", "Main Music"));
		musicDictionary.put("Pause Music", loadMusic("/Music/BGMPause.ogg", "Pause Music"));
		musicDictionary.put("Placeholder Music", loadMusic("/Music/PlaceholderBGM.ogg", "PlaceholderMusic"));
		
		/**
		 * Initialize sound effects
		 */
		soundDictionary.put("Player Death", loadSound("/Sound/PlaceholderPlayerDeath.ogg"));
		soundDictionary.put("Generic Hit", loadSound("/Sound/PlayerHit.ogg"));
		soundDictionary.put("Menu Button Press", loadSound("/Sound/MenuSelect.ogg"));
		soundDictionary.put("Player Jump", loadSound("/Sound/Jump.ogg"));
		soundDictionary.put("New Item", loadSound("/Sound/NewItemCraft.ogg"));
		soundDictionary.put("Axe Hit", loadSound("/Sound/Axe.ogg"));
		soundDictionary.put("Pick Hit", loadSound("/Sound/Pick.ogg"));
		soundDictionary.put("Sword Hit",  loadSound("/Sound/Sword.ogg"));
		soundDictionary.put("Potion Drink 1", loadSound("/Sound/PotionDrink.ogg"));
		soundDictionary.put("Potion Drink 2", loadSound("/Sound/PotionDrinkAlt.ogg"));
	}
	
	/**
	 * Loads a given music file.
	 * @param url - URL inside the Audio folder in appdata to locate the file.
	 * @return the given music file.
	 */
	private static Song loadMusic(String url, String name){
		try{
			return new Song(TerraeRasa.getBasePath() + "/Resources/Audio" + url, name);
		}catch(SlickException e){
			System.out.println("Failed to load at: " + TerraeRasa.getBasePath() + url);
			System.out.println("Please either reinstall or let Alec or Matt know about this issue.");
			return null;
		}
	}
	
	/**
	 * Load a given audio file of the type .ogg.
	 * Used for sound-effects.
	 * @param url - URL for the file in the Audio folder in appdata
	 * @return - a sound-effect.
	 */
	private static Audio loadSound(String url){
		try{
			return AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(TerraeRasa.getBasePath() + "/Resources/Audio" + url));
		}catch(IOException e){
			System.out.println("Failed to load at: " + TerraeRasa.getBasePath() + url);
			System.out.println("Please either reinstall or let Alec or Matt know about this issue.");
			return null;
		}
	}
	
	/**
	 * Plays the current music at the given volume.
	 * @param gain - volume to play at. Typically, but not always the settings volume.
	 */
	public static void playCurrentMusic(){
		if (currentMusic != oldMusic){
			currentMusic.loop();
			oldMusic = currentMusic;
		}
	}
	
	/**
	 * Play a sound effect at 100% volume
	 * @param s - key used to locate the sound
	 */
	public static void playSoundEffect(String s){
		if (settings.soundVolume >= 0.01){
			if (soundDictionary.containsKey(s)){
				soundDictionary.get(s).playAsSoundEffect(1f, (float) settings.soundVolume, false);
			}
			else {
				System.out.println("This.... should never happen. Please initialize your sounds before playing them");
			}
		}
	}
	
	/**
	 * Play a soundEffect, given a locator string, a pitch, and a gain.
	 * @param s - the key used to locate the effect
	 * @param perentage - scales the sounds volume based off the settings
	 */
	public static void playSoundEffect(String s, double percentage){
		if (settings.soundVolume >= 0.01){
			if(soundDictionary.containsKey(s)){
				soundDictionary.get(s).playAsSoundEffect(1f, (float) (settings.soundVolume * percentage), false);
			}
			else {
				System.out.println("This.... should never happen. Please initialize your sounds before playing them. >" + s + "<");
			}
		}
	}
	
	/**
	 * Play a soundEffect, given a locator string, a pitch, and a gain.
	 * @param s - the key used to locate the effect
	 * @param pitch - pitch at which to play said effect
	 */
	public static void playSoundEffect(String s, float pitch){
		if (settings.soundVolume >= 0.01){ 
			if (soundDictionary.containsKey(s)){
				soundDictionary.get(s).playAsSoundEffect(pitch, (float) settings.soundVolume, false);
			}
			else {
				System.out.println("This.... should never happen. Please initialize your sounds before playing them. >" + s + "<");
			}
		}
	}
	
	/**
	 * Cleanup. Not much to say here
	 */
	public static void destroy(){
		AL.destroy();
	}
	
	/**
	 * Sets the current music.
	 */
	public static void setCurrentMusic(String music){
		if (currentMusic != null && currentMusic != musicDictionary.get(music)){
			currentMusic.fade(100, 0, true);
			musicDictionary.get(music).savePosition();
		}	
		
		currentMusic = musicDictionary.get(music);
		playCurrentMusic();			
	}
	
	/**
	 * Sets the volume.
	 * @param f
	 */
	public static void setVolume(float f){
				
		if (settings.musicVolume < 0.01){
			currentMusic.setVolume(0);
		}
		else{
			currentMusic.setVolume(f);
		}
	}
	
	/**
	 * Gets the current music that is playing.
	 */
	public static Song getCurrentMusic(){
		return currentMusic;
	}
	
	/**
	 * Gets the old music that was previously playing.
	 * @return
	 */
	public static Song getOldMusic(){
		return oldMusic;
	}
	
	/**
	 * Gets the volume.
	 * @return
	 */
	public static float getVolume(){
		return currentMusic.getVolume();
	}	
}