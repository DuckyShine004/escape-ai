package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import javafx.scene.Parent;
import nz.ac.auckland.se206.constants.GameState;

/**
 * This manager class contains methods for storing and switching between the different scenes in the
 * game.
 */
public class SceneManager {
  /**
   * This enum represents the different scenes in the game.
   */
  public enum AppUi {
    MENU,
    OPTIONS,
    BACKSTORY,
    CONTROL,
    DECRYPTION,
    BREAKER,
    TERMINAL,
    LOGIC_PUZZLE, // logic gate puzzle in Breaker Room
    OFFICE,
    WINNING,
    LOSING,
    RIDDLE // riddle room is not in sceneMap
  }

  // stores the scenes
  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  /**
   * This method will switch the scene to the scene specified by the ui key.
   *
   * @param ui the ui key
   * @param root the root of the scene
   */
  public static void addAppUi(AppUi ui, Parent root) {
    sceneMap.put(ui, root);
  }

  /**
   * This method will return the scene specified by the ui key.
   *
   * @param ui the ui key
   */
  public static Parent getUi(AppUi ui) {
    return sceneMap.get(ui);
  }

  /**
   * This method will be called when the level needs to be reset, This method will remove all rooms
   * from the scene manager, and put them back in reinitailzed.
   */
  public static void onResetLevel() throws IOException {

    System.out.println("RESETTING PUZZLE ROOMS");

    GameState.currentRoom = AppUi.MENU;

    // will trigger exception on first time through each time
    try {
      sceneMap.remove(AppUi.OFFICE);
      sceneMap.remove(AppUi.BREAKER);
      sceneMap.remove(AppUi.CONTROL);
      sceneMap.remove(AppUi.TERMINAL);
    } catch (Exception e) {
      System.out.println("scenes don't exist yet");
    }

    sceneMap.remove(AppUi.LOGIC_PUZZLE);
    sceneMap.remove(AppUi.DECRYPTION);

    // reinitalizes the puzzle scenes
    App.initalizePuzzleScenes();
  }
}
