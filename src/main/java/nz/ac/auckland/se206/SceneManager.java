package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  // UI fxml files the app can switch between
  public enum AppUi {
    MENU,
    OPTIONS,
    CONTROL,
    DECRYPTION,
    BREAKER,
    TERMINAL,
    LOGIC_PUZZLE, // logic gate puzzle in Breaker Room
    OFFICE,
    WINNING,
    LOSING
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

    sceneMap.remove(AppUi.OFFICE);

    sceneMap.remove(AppUi.BREAKER);
    sceneMap.remove(AppUi.LOGIC_PUZZLE);

    sceneMap.remove(AppUi.CONTROL);
    sceneMap.remove(AppUi.DECRYPTION);

    // reinitalizes the puzzle scenes
    App.initalizePuzzleScenes();
  }
}
