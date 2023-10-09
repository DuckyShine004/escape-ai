package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.media.AudioClip;

public class AudioManager {

  public enum Clip {
    MAKING_SELECTION,
    SELECTION
  }

  private static HashMap<Clip, AudioClip> audioMap = new HashMap<>();

  public static void addAudio(Clip clip, String path) {
    AudioClip audio = new AudioClip(App.class.getResource(path).toString());
    audioMap.put(clip, audio);
  }

  public static void loadAudio(Clip clip) {
    audioMap.get(clip).play();
  }
}
