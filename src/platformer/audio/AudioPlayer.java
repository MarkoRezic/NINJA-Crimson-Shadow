package platformer.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
	
	private Media media;
	public MediaPlayer mediaPlayer;
	
	public AudioPlayer(String s) {
		
		try {

			media = new Media(getClass().getResource(s).toExternalForm());
			mediaPlayer = new MediaPlayer(media);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void play() {
		mediaPlayer.play();
	}

	public void playLoop() {
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		play();
	}
	
	public void stop() {
		mediaPlayer.stop();
	}
	
}














