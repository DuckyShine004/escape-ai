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

  /**
   * Add audio to the audio map.
   *
   * @param clip the clip
   * @param path the path to the sound file
   */
  public static void addAudio(Clip clip, String path) {
    AudioClip audio = new AudioClip(App.class.getResource(path).toString());
    audioMap.put(clip, audio);
  }

  /**
   * Loads an audio clip and plays it.
   *
   * @param clip the clip
   */
  public static void loadAudio(Clip clip) {
    audioMap.get(clip).play();
  }

  /**
   * Initialize a looping sound effect.
   *
   * @param timeline the timeline to be set
   * @param clip the clip
   * @param delay the delay
   * @return the initialized timeline
   */
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

  /**
   * Check if the dialogue sound effect is playing.
   *
   * @return boolean value based on whether dialgoue is playing
   */
  public static boolean isDialoguePlaying() {
    return dialogue.getStatus() == Timeline.Status.PAUSED;
  }

  /** Play the heart beat sound effect. */
  public static void playHeartBeat() {
    // Initialize the heart beat sound effect
    heartBeat = getTimedSound(heartBeat, Clip.HEART_BEAT, 2);

    // Set the cycle and play the sound effect
    heartBeat.setCycleCount(Timer.getIntegerTime());
    heartBeat.play();
  }

  /** Play the dialogue sound effect. */
  public static void playDialogue() {
    dialogue = getTimedSound(dialogue, Clip.DIALOGUE, 0.1);

    // Set the cycle and play the sound effect
    dialogue.setCycleCount(Timeline.INDEFINITE);
    dialogue.play();
  }

  /** Pause the dialogue sound effect. */
  public static void pauseDialogue() {
    dialogue.pause();
  }

  /** Resume playing the dialogue sound effect. */
  public static void resumeDialogue() {
    dialogue.play();
  }

  /** Stop the heart beat sound effect. */
  public static void stopHeartBeat() {
    heartBeat.stop();
  }

  /** Completely stop playing the dialogue sound effect. */
  public static void stopDialogue() {
    dialogue.stop();
  }
}
