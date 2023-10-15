package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import nz.ac.auckland.se206.utilities.Timer;

public class AudioManager {
  private static Timeline heartBeat;

  public enum Clip {
    MAKING_SELECTION,
    SELECTION,
    HEART_BEAT
  }

  private static HashMap<Clip, AudioClip> audioMap = new HashMap<>();

  public static void addAudio(Clip clip, String path) {
    AudioClip audio = new AudioClip(App.class.getResource(path).toString());
    audioMap.put(clip, audio);
  }

  public static void loadAudio(Clip clip) {
    audioMap.get(clip).play();
  }

  public static void playHeartBeat() {
    heartBeat =
        new Timeline(
            new KeyFrame(
                Duration.seconds(2),
                event -> {
                  loadAudio(Clip.HEART_BEAT);
                }));

    // Play the heart beat sound effect for the remaining time
    heartBeat.setCycleCount(Timer.getIntegerTime());
    heartBeat.play();
  }

  public static void stopHeartBeat() {
    heartBeat.stop();
  }
}
