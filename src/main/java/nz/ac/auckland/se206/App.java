package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.utilities.KeyEventsHandler;
import nz.ac.auckland.se206.utilities.Timer;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  // input enum for the ui the app is changing to
  public static void setUi(AppUi newUi) {
    // scene.setRoot
    // get the Parent for that Ui
    scene.setRoot(SceneManager.getUi(newUi));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Loads the requested font. The method expects that the file is located in
   * "src/main/resources/fonts".
   *
   * @param font the name of the font file (without extension).
   * @param size the size of the font.
   */
  private static void loadFont(final String font, final int size) {
    Font.loadFont(App.class.getResourceAsStream("/fonts/" + font + ".ttf"), size);
  }

  /*
   * This method will initalize the scenes, by storing instatnces of the loaded fxmls in SceneManager
   * @throws IOException if fxml is not found
   */
  private static void initalizeScenes() throws IOException {
    // initialize the timer
    Timer.initialize();

    // main menue
    SceneManager.addAppUi(AppUi.MENU, loadFxml("menus/menu"));

    // options in main menu
    SceneManager.addAppUi(AppUi.OPTIONS, loadFxml("menus/options"));

    // terminal screen
    SceneManager.addAppUi(AppUi.TERMINAL, loadFxml("menus/terminal"));

    // winning screen
    SceneManager.addAppUi(AppUi.WINNING, loadFxml("menus/winning"));

    // losing screen
    SceneManager.addAppUi(AppUi.LOSING, loadFxml("menus/losing"));

    // remaining puzzle scenes
    initalizePuzzleScenes();
  }

  protected static void initalizePuzzleScenes() throws IOException {
    // office room
    SceneManager.addAppUi(AppUi.OFFICE, loadFxml("rooms/office"));

    // control room
    SceneManager.addAppUi(AppUi.CONTROL, loadFxml("rooms/control"));

    // breaker room
    SceneManager.addAppUi(AppUi.BREAKER, loadFxml("rooms/breaker"));

    // decryption puzzle in control room
    SceneManager.addAppUi(AppUi.DECRYPTION, loadFxml("puzzles/decryption"));

    // logic gate puzzle in breaker room
    SceneManager.addAppUi(AppUi.LOGIC_PUZZLE, loadFxml("puzzles/logicGates"));
  }

  /**
   * This method will initialize fonts. Like with the FXML files, fonts have to be loaded as well.
   */
  private void initializeFonts() {
    // load the terminal font
    loadFont("terminal", 23);
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // add scenes to sceneManager
    initalizeScenes();

    // initialize fonts
    initializeFonts();

    // Create an instance of KeyEventHandler
    KeyEventsHandler keyEventsHandler = new KeyEventsHandler();

    // set first scene to display
    scene = new Scene(SceneManager.getUi(AppUi.LOGIC_PUZZLE), 720, 480);

    // place scene onto stage
    stage.setScene(scene);

    // show scene
    stage.show();

    // request control focus
    scene.getRoot().requestFocus();

    // Add the KeyEventHandler to the primary scene
    scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEventsHandler);
  }
}
