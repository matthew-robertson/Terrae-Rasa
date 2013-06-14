package Audio;

import java.io.IOException;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import client.Dimensia;

import world.World;

public class Sound{
	//Music
	public static Audio bgmMain;
	public static Audio bgmAlternate;
	
	//Sound effects
	public static Audio playerDeath;
	public static Audio menuSelect;
	public static Audio itemCraft;
	public static Audio death;
	public static Audio hitArrow;
	public static Audio jump;
	
	public static void initializeAudio(World world){
		try{
			
			bgmMain = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/BGMMain.ogg"));
			bgmAlternate = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/BGMAlternate.ogg"));
			
			playerDeath = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/PlaceholderPlayerDeath.ogg"));
			menuSelect = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/MenuSelect.ogg"));
			itemCraft = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/NewItemCraft.ogg"));
			death = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/Death.ogg"));
			hitArrow = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/HitArrow.ogg"));
			jump = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(Dimensia.getBasePath() + "/Resources/Audio/Jump.ogg"));
			
		}catch(IOException e){		
			e.printStackTrace();
		}
	}
	
}