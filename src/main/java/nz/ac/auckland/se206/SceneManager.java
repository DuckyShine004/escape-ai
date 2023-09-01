package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  // UI fxml files the app can switch between
  public enum AppUi {
    ROOM,
    MENU,
    OPTIONS,
    CONTROL,
    BREAKER,
    CHAT,
    OFFICE
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
}
