package com.SnakeGame.Core;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
/**
 * This class is responsible for reading and decoding audio files that will be used in game
 * the methods specified within this class can be called along with every event ocurring within
 * the games framework
 * @author Eudy Contreras
 *
 */
public class GameAudioManager {
	
	private Clip soundClip;
	private FloatControl gainControl;
	public GameAudioManager(){
		
	}
	/**
	 * Constructor which takes a path to an audio file which will then 
	 * be process and converted into a java clip file. The audio file 
	 * will later be able to be manipulated using the below non static methods
	 * @param path
	 */
	public GameAudioManager(String path){	
		try{
			InputStream audioSource = getClass().getResourceAsStream(path);
			InputStream bufferedInput = new BufferedInputStream(audioSource);
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(bufferedInput);
			AudioFormat baseFormat = audioInput.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, baseFormat.getSampleRate(),16, baseFormat.getChannels(), baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
			AudioInputStream audioInputD = AudioSystem.getAudioInputStream(decodeFormat, audioInput);		
			soundClip = AudioSystem.getClip();
			soundClip.open(audioInputD);
			gainControl = (FloatControl)soundClip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Method used to process and return and audio file which is ready to be played
	 * by the system. This method can be used to precreate decoded audio files.
	 * @param path: the path to the audio file to be played
	 * @return
	 */
	public static Clip processAudioFile (String path){	
		Clip newSound = null;
		try{
			InputStream audioSource = GameAudioManager.class.getResourceAsStream(path);
			InputStream bufferedInput = new BufferedInputStream(audioSource);
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(bufferedInput);
			AudioFormat baseFormat = audioInput.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, baseFormat.getSampleRate(),16, baseFormat.getChannels(), baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
			AudioInputStream audioInputD = AudioSystem.getAudioInputStream(decodeFormat, audioInput);		
			newSound = AudioSystem.getClip();
			newSound.open(audioInputD);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return  newSound;
	}
	/**
	 * This methods plays the audio once 
	 * if the method is recalled it will first 
	 * stop the audio from playing and play it from 
	 * the beginning
	 * @param soundClip
	 */
	public static void playAudio(Clip soundClip){
		if(soundClip == null)
			return;
		GameAudioManager.stopAudio(soundClip);
		soundClip.setFramePosition(0);
		while(!soundClip.isRunning()){
			soundClip.start();
		}
	}
	/**
	 * This methods plays the audio once 
	 * if the method is recalled it will first 
	 * stop the audio from playing and play it from 
	 * the beginning
	 */
	public void playAudio(){
		if(soundClip == null)
			return;
		stopAudio();
		soundClip.setFramePosition(0);
		while(!soundClip.isRunning()){
			soundClip.start();
		}
	}
	/**
	 * Method used to stop an audio file from playing as
	 * @param soundClip
	 */
	public static void stopAudio(Clip soundClip){
		if(soundClip.isRunning())
			soundClip.stop();
			
	}
	/*
	 * Method used to stop an audio file from playing as
	 */
	public void stopAudio(){
		if(soundClip.isRunning())
			soundClip.stop();
			
	}
	/**
	 * Method used to dispose of an audio file. Should only be used 
	 * when the audio will no longer be used 
	 * @param soundClip
	 */
	public static void disposeAudio(Clip soundClip){
		GameAudioManager.stopAudio(soundClip);
		soundClip.drain();
		soundClip.close();
	}
	/**
	 * Method used to dispose of an audio file. Should only be used 
	 * when the audio will no longer be used 
	 */
	public void disposeAudio(){
		stopAudio();
		soundClip.drain();
		soundClip.close();
	}
	/**
	 * Method used to make an audio file enter constant loop 
	 * which will only end when the stop method is called
	 * @param soundClip
	 */
	public static void loopAudio(Clip soundClip){
		soundClip.loop(Clip.LOOP_CONTINUOUSLY);
		while(!soundClip.isRunning()){
			soundClip.start();
		}
	}
	/**
	 * Method used to make an audio file enter constant loop 
	 * which will only end when the stop method is called
	 */
	public void loopAudio(){
		soundClip.loop(Clip.LOOP_CONTINUOUSLY);
		while(!soundClip.isRunning()){
			soundClip.start();
		}
	}
	/**
	 * method used to see if the given audio file 
	 * is currently running
	 * @param soundClip
	 * @return
	 */
	public static boolean audioIsRunning(Clip soundClip){		
		return soundClip.isRunning();
	}
	/**
	 * method used to see if the given audio file 
	 * is currently running
	 * @return
	 */
	public boolean audioIsRunning(){		
		return soundClip.isRunning();
	}
	/**
	 * Sets the volume of the audio file 
	 * @param volume: max: 1.0f min: 0  
	 * @param soundClip
	 */
	public static void setAudioVolume( float volume, Clip soundClip){
		FloatControl gainControl;
		gainControl = (FloatControl)soundClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume);
	}
	/**
	 * Sets the volume of the audio file 
	 * @param volume: max: 1.0f min: 0  
	 */
	public void setAudioVolume( float volume){
		gainControl.setValue(volume);
	}

}
