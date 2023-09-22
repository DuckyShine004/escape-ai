package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Interactions;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the breaker room. */
public class BreakerRoomController extends RoomController{
  @FXML private Pane paBreaker;
  @FXML private Pane paCircuitBox;

  @FXML private Label lblTime;

  @FXML private Button btnHint;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;

  /** Initialize the breaker room. */
  @FXML
  private void initialize() {
    // Add the label to list of labels to be updated
    Timer.addLabel(lblTime);

    // Add the text area and text field to the list of chat components
    ChatManager.addChatComponents(taChat, tfChat);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the left scene.
   *
   * @throws IOException
   */
  @FXML
  private void onLeftButton() throws IOException {
    App.setUi(AppUi.OFFICE);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the right scene.
   *
   * @throws IOException
   */
  @FXML
  private void onRightButton() throws IOException {
    App.setUi(AppUi.CONTROL);
  }

  @FXML
  private void onCircuitBoxEntered() {
    paCircuitBox.setOpacity(GameState.overlayCapacity);
  }

  @FXML
  private void onCircuitBoxExited() {
    paCircuitBox.setOpacity(0);
  }

  /** Change the scene to the logic puzzle */
  @FXML
  private void onCircuitBoxClicked() {
    // We should not give anymore hints for clicking on the circuit box
    Interactions.isCircuitBoxClicked = true;

    // Switch to the logic gate puzzle
    App.setUi(AppUi.LOGIC_PUZZLE);
  }

  @FXML
  public void onHintClicked(MouseEvent mouseEvent) {
    // If the circuit box has not been clicked on yet
    if (!Interactions.isCircuitBoxClicked) {
      ChatManager.getUserHint(false);
      return;
    }

    // Disable the hints button
    btnHint.setDisable(true);

    // Tell the player that the room has been completed
    ChatManager.getUserHint(true);
  }
}
