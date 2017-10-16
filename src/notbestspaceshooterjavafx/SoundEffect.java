package notbestspaceshooterjavafx;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundEffect {

    Clip clip;
    File audiofile;

    public SoundEffect(String url) {
        try {
            clip = AudioSystem.getClip();
            audiofile = new File("resources/" + url);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(audiofile);
            clip.open(inputStream);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void playSound() {
        clip.start();
    }
}
