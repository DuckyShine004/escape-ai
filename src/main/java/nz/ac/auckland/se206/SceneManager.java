package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import javafx.scene.Parent;
import nz.ac.auckland.se206.constants.GameState;

public class SceneManager {
  // UI fxml files the app can switch between
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

  // add root to the hashmap by ui key
  public static void addAppUi(AppUi ui, Parent root) {
    sceneMap.put(ui, root);
  }

  // get root from ui key
  public static Parent getUi(AppUi ui) {
    return sceneMap.get(ui);
  }

  /**
   * This method will be called when the level needs to be reset, This method will remove all rooms
   * from the scene manager, and put them back in reinitailzed
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
