package platformer.audio;

import javafx.scene.media.AudioClip;
import platformer.ninjamenu.NinjaMenuApp;

import javax.sound.sampled.*;
import java.io.File;

public class ClipPlayer {

    private AudioClip clip;

    public ClipPlayer(String s) {

        try {

            clip = new AudioClip("file:src"+s);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void play() {
        clip.play((!NinjaMenuApp.isSFXMute ? 1 : 0) * NinjaMenuApp.masterVolume);
    }

}














