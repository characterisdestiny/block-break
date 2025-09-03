package blockbreaker;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    public static void play(String soundFilePath) {
        new Thread(() -> {
            try {
                URL soundURL = Sound.class.getClassLoader().getResource(soundFilePath);
                if (soundURL == null) {
                    System.out.println(" 사운드 파일을 찾을 수 없음: " + soundFilePath);
                    return;
                }

                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println(" 사운드 재생 오류: " + e.getMessage());
            }
        }).start();
    }
}
