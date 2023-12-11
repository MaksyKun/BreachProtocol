package settings;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class SoundUT {
    public enum SoundType {
        CLOSING,
    }
    public static void playSound(SoundType type) {
        URL url = null;
        switch (type) {
            case CLOSING -> url = SoundUT.class.getResource("/closing.wav");
        }
        try {
            // getClass().getSy.getResource("/images/ads/WindowsNavigationStart.wav");
            File soundFile = new File(url.toURI());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start( );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
