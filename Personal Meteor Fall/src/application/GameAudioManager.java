package application;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class GameAudioManager {
	
	private Clip soundClip;
	private FloatControl gainControl;
	
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
	public void playAudio(){
		if(soundClip == null)
			return;
		stopAudio();
		soundClip.setFramePosition(0);
		while(!soundClip.isRunning()){
			soundClip.start();
		}
	}
	public void stopAudio(){
		if(soundClip.isRunning())
			soundClip.stop();
			
	}
	public void disposeAudio(){
		stopAudio();
		soundClip.drain();
		soundClip.close();
	}
	public void loopAudio(){
		soundClip.loop(Clip.LOOP_CONTINUOUSLY);
		while(!soundClip.isRunning()){
			soundClip.start();
		}
	}
	public boolean audioIsRunning(){		
		return soundClip.isRunning();
	}
	public void setAudioVolume( float volume){
		gainControl.setValue(volume);
	}

}
