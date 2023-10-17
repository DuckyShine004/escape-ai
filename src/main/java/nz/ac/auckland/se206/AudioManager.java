package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import nz.ac.auckland.se206.utilities.Timer;

/**
 * This manager clas contains methods for storing and playing audio clips that are used throughout
 * the game.
 */
public class AudioManager {
  /** This enum represents the different audio clips that can be used in the game. */
  public enum Clip {
    MAKING_SELECTION,
    SELECTION,
    HEART_BEAT,
    DIALOGUE,
    VICTORY,
    GAME_OVER,
    WRONG,
    CORRECT,
    LOGIC_GATE_SOLVED,
    RIDDLE_SOLVED
  }

  private static Timeline dialogue;
  private static Timeline heartBeat;
  private static HashMap<Clip, AudioClip> audioMap = new HashMap<>();

  /**
   * This method adds an audio clip to the audio map.
   *
   * @param clip the clip
   * @param path the path to the sound file
   */
  public static void addAudio(Clip clip, String path) {
    AudioClip audio = new AudioClip(App.class.getResource(path).toString());
    audioMap.put(clip, audio);
  }

  /**
   * This method loads an audio clip from the audio map.
   *
   * @param clip the clip
   */
  public static void loadAudio(Clip clip) {
    audioMap.get(clip).play();
  }

  /**
   * Initialize a looping sound effect for the given clip using a timeline. The audio should loop
   * given the delay.
   *
   * @param clip the clip to be played.
   * @param delay the delay of the audio.
   * @return the initialized timeline.
   */
  private static Timeline getTimedSound(Clip clip, double delay) {
    return new Timeline(
        new KeyFrame(
            Duration.seconds(delay), // Delay
            event -> {
              loadAudio(clip); // Play the sound effect
            }));
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
    heartBeat = getTimedSound(Clip.HEART_BEAT, 2);

    // Set the cycle and play the sound effect
    heartBeat.setCycleCount(Timer.getIntegerTime());
    heartBeat.play();
  }

  /** Play the dialogue sound effect. */
  public static void playDialogue() {
    dialogue = getTimedSound(Clip.DIALOGUE, 0.1);

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
