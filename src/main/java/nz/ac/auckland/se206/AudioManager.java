package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import nz.ac.auckland.se206.constants.Instructions;
import nz.ac.auckland.se206.utilities.Timer;

public class AudioManager {
  private static Timeline dialogue;
  private static Timeline heartBeat;

  public enum Clip {
    MAKING_SELECTION,
    SELECTION,
    HEART_BEAT,
    DIALOGUE
  }

  private static HashMap<Clip, AudioClip> audioMap = new HashMap<>();

  public static void addAudio(Clip clip, String path) {
    AudioClip audio = new AudioClip(App.class.getResource(path).toString());
    audioMap.put(clip, audio);
  }

  public static void loadAudio(Clip clip) {
    audioMap.get(clip).play();
  }

  private static void initializeTimedSound(
      Timeline timeline, Clip clip, double delay, int duration) {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(delay),
                event -> {
                  loadAudio(clip);
                }));

    // Play the heart beat sound effect for the remaining time
    timeline.setCycleCount(duration);
  }

  public static void playHeartBeat() {
    initializeTimedSound(heartBeat, Clip.HEART_BEAT, 2, Timer.getIntegerTime());
    heartBeat.play();
  }

  public static void playDialogue() {
    initializeTimedSound(dialogue, Clip.DIALOGUE, Instructions.printSpeed, 0);
    dialogue.play();
  }

  public static void stopHeartBeat() {
    heartBeat.stop();
  }

  public static void stopDialogue() {
    dialogue.stop();
  }
}
