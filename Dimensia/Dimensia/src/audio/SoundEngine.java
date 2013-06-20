package audio;

import java.io.IOException;
import java.util.Hashtable;

import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import client.Dimensia;
import client.Settings;

public class SoundEngine{
	
	protected Hashtable<String, Song> musicDictionary = new Hashtable<String, Song>();
	protected Hashtable<String, Audio> soundDictionary = new Hashtable<String, Audio>();
	
	protected Song currentMusic;
	protected Song oldMusic;
	protected Settings settings;
	
	public SoundEngine(Settings settings){
		initializeAudio();
		this.settings = settings;
	}
	
	public void initializeAudio(){
		/**
		 * Initialize music
		 */
		musicDictionary.put("Main Music", this.loadMusic("/Music/BGMMain.ogg", "Main Music"));
		musicDictionary.put("Pause Music", this.loadMusic("/Music/BGMPause.ogg", "Pause Music"));
		musicDictionary.put("Placeholder Music", this.loadMusic("/Music/PlaceholderBGM.ogg", "PlaceholderMusic"));
		
		/**
		 * Initialize sound effects
		 */
		soundDictionary.put("Player Death", this.loadSound("/Sound/PlaceholderPlayerDeath.ogg"));
		soundDictionary.put("Generic Hit", this.loadSound("/Sound/PlayerHit.ogg"));
		soundDictionary.put("Menu Button Press", this.loadSound("/Sound/MenuSelect.ogg"));
		soundDictionary.put("Player Jump", this.loadSound("/Sound/Jump.ogg"));
		soundDictionary.put("New Item", this.loadSound("/Sound/NewItemCraft.ogg"));
		soundDictionary.put("Axe Hit", this.loadSound("/Sound/Axe.ogg"));
		soundDictionary.put("Pick Hit", this.loadSound("/Sound/Pick.ogg"));
	}
	
	/**
	 * Loads a given music file.
	 * @param url - URL inside the Audio folder in appdata to locate the file.
	 * @return the given music file.
	 */
	private Song loadMusic(String url, String name){
		try{
			return new Song(Dimensia.getBasePath() + "/Resources/Audio" + url, name);
		}catch(SlickException e){
			System.out.println("Failed to load at: " + Dimensia.getBasePath() + url);
			System.out.println("I really hope you are Matt or Alec. If not, please let them know.");
			return null;
		}
	}
	
	/**
	 * Load a given audio file of the type .ogg.
	 * Used for sound-effects.
	 * @param url - URL for the file in the Audio folder in appdata
	 * @return - a sound-effect.
	 */
	private Audio loadSound(String url){
		try{
			return AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio" + url));
		}catch(IOException e){
			System.out.println("Failed to load at: " + Dimensia.getBasePath() + url);
			System.out.println("I really hope you are Matt or Alec. If not, please let them know.");
			return null;
		}
	}
	
	/**
	 * Plays the current music at the given volume.
	 * @param gain - volume to play at. Typically, but not always the settings volume.
	 */
	public void playCurrentMusic(){
		if (this.currentMusic != this.oldMusic){
			//currentMusic.setPosition(currentMusic.getSavedPosition());
			currentMusic.loop();
			this.oldMusic = this.currentMusic;
		}
	}
	
	/**
	 * Play a sound effect at 100% volume
	 * @param s - key used to locate the sound
	 */
	public void playSoundEffect(String s){
		if (soundDictionary.containsKey(s)){
			soundDictionary.get(s).playAsSoundEffect(1f, (float) settings.volume, false);
		}
		else {
			System.out.println("This.... should never happen. Please initialize your sounds before playing them");
		}
	}
	
	/**
	 * Play a soundEffect, given a locator string, a pitch, and a gain.
	 * @param s - the key used to locate the effect
	 * @param pitch - pitch at which to play said effect
	 * @param gain - volume to use for the effect. Typically the volume given by settings
	 */
	public void playSoundEffect(String s, double percentage){
		if (soundDictionary.containsKey(s)){
			soundDictionary.get(s).playAsSoundEffect(1f, (float) (settings.volume * percentage), false);
		}
		else {
			System.out.println("This.... should never happen. Please initialize your sounds before playing them");
		}
	}
	
	/**
	 * Play a soundEffect, given a locator string, a pitch, and a gain.
	 * @param s - the key used to locate the effect
	 * @param pitch - pitch at which to play said effect
	 * @param gain - volume to use for the effect. Typically the volume given by settings
	 */
	public void playSoundEffect(String s, float pitch){
		if (soundDictionary.containsKey(s)){
			soundDictionary.get(s).playAsSoundEffect(pitch, (float) settings.volume, false);
		}
		else {
			System.out.println("This.... should never happen. Please initialize your sounds before playing them");
		}
	}
	
	/**
	 * Cleanup. Not much to say here
	 */
	public void destroy(){
		AL.destroy();
	}
	
	/**
	 * setters
	 */
	public void setCurrentMusic(String music){
		if (currentMusic != null && currentMusic != musicDictionary.get(music)){
			currentMusic.fade(100, 0, true);
			musicDictionary.get(currentMusic.getName()).savePosition();
		}	
		
		this.currentMusic = musicDictionary.get(music);
		playCurrentMusic();			
	}
	
	public void setVolume(float f){
		currentMusic.setVolume(f);
	}
	
	/**
	 * getters
	 */
	public Song getCurrentMusic(){
		return currentMusic;
	}
	
	public Song getOldMusic(){
		return oldMusic;
	}
	
	public float getVolume(){
		return currentMusic.getVolume();
	}
	
	
}