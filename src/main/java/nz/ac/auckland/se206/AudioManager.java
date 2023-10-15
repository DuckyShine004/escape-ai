package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
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

  private static Timeline getTimedSound(Timeline timeline, Clip clip, double delay) {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(delay),
                event -> {
                  loadAudio(clip);
                }));

    return timeline;
  }

  public static boolean isDialoguePlaying() {
    return dialogue.getStatus() == Timeline.Status.PAUSED;
  }

  public static void playHeartBeat() {
    // Initialize the heart beat sound effect
    heartBeat = getTimedSound(heartBeat, Clip.HEART_BEAT, 2);

    // Set the cycle and play the sound effect
    heartBeat.setCycleCount(Timer.getIntegerTime());
    heartBeat.play();
  }

  public static void playDialogue() {
    dialogue = getTimedSound(dialogue, Clip.DIALOGUE, 0.1);

    // Set the cycle and play the sound effect
    dialogue.setCycleCount(Timeline.INDEFINITE);
    dialogue.play();
  }

  public static void pauseDialogue() {
    dialogue.pause();
  }

  public static void resumeDialogue() {
    dialogue.play();
  }

  public static void stopHeartBeat() {
    heartBeat.stop();
  }

  public static void stopDialogue() {
    dialogue.stop();
  }
}
