package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.utilities.ChatArea;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the breaker room. */
public class BreakerRoomController {
  @FXML private Label lblTime;

  @FXML private ChatArea chatArea;

  private SceneManager sceneManager;

  /** Initialize the breaker room. */
  @FXML
  private void initialize() {
    // add the label to list of labels to be updated.
    Timer.addLabel(lblTime);

    sceneManager = SceneManager.getInstance();
    String chatContent = ChatManager.getChatContent();
    chatArea.setChatContent(chatContent);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the left scene.
   *
   * @throws IOException
   */
  @FXML
  private void onLeftButton() throws IOException {
    sceneManager.setChatContent(chatArea.getChatContent());
    App.initializeOfficeScene();
    App.setUi(AppUi.OFFICE);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the right scene.
   *
   * @throws IOException
   */
  @FXML
  private void onRightButton() throws IOException {
    sceneManager.setChatContent(chatArea.getChatContent());
    App.initializeControlScene();
    App.setUi(AppUi.CONTROL);
  }

  /** Change the scene to the logic puzzle */
  @FXML
  private void onOpenLogicGatePuzzle() {
    App.setUi(AppUi.LOGIC_PUZZLE);
  }
}
