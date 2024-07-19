package PruebasIniciales;

import javax.sound.sampled.*;
import java.io.IOException;

class SoundUtils {
    public static Clip playSound(String soundFile) {
        Clip clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundUtils.class.getResource(soundFile));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return clip;
    }
}


