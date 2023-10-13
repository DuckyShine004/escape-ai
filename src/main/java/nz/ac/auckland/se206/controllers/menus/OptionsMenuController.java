package nz.ac.auckland.se206.controllers.menus;

import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;

/**
 * The controller for the options menu, which gives the player the option to switch between the
 * different difficulties and time limits.
 */
public class OptionsMenuController {
  @FXML private Pane paReturn;
  @FXML private Pane paOption;
  @FXML private Pane paReturnLeftOverlay;
  @FXML private Pane paReturnRightOverlay;

  @FXML private Line lineEasy;
  @FXML private Line lineHard;
  @FXML private Line lineMedium;
  @FXML private Line lineReturnTop;
  @FXML private Line lineReturnLeft;
  @FXML private Line lineTwoMinutes;
  @FXML private Line lineSixMinutes;
  @FXML private Line lineFourMinutes;
  @FXML private Line lineReturnRight;
  @FXML private Line lineReturnBottom;
  @FXML private Line lineReturnDiagonal;

  @FXML private Label lblEasy;
  @FXML private Label lblHard;
  @FXML private Label lblMedium;
  @FXML private Label lblReturn;
  @FXML private Label lblTwoMinutes;
  @FXML private Label lblSixMinutes;
  @FXML private Label lblFourMinutes;
  @FXML private Label lblEasyComment;
  @FXML private Label lblHardComment;
  @FXML private Label lblMediumComment;

  @FXML private Polygon pgReturnOverlay;

  private int timeIndex = 0;
  private int difficultyIndex = 0;

  private List<Pair<Label, Line>> timeComponents;
  private List<Pair<Label, Line>> difficultyComponents;

  /** Initialize the options menu controller. */
  @FXML
  public void initialize() {
    // Initialize the time components
    initializeTimeComponents();

    // Initialize the difficulty components
    initializeDifficultyComponents();
  }

  /** When the mouse is hovering over the arrows, the overlay appears. */
  @FXML
  private void onArrowEntered(Event event) {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    ((Polygon) event.getSource()).setFill(Color.rgb(97, 219, 224));
  }

  /** When the mouse is not hovering over the arrows, the overlay disappears. */
  @FXML
  private void onArrowExited(Event event) {
    ((Polygon) event.getSource()).setFill(Color.BLACK);
  }

  /** When the mouse is hovering over the pane, the overlay appears (return). */
  @FXML
  private void onReturnPaneEntered() {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    enableReturnOverlay();
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (return). */
  @FXML
  private void onReturnPaneExited() {
    disableReturnOverlay();
  }

  /** When time left arrow is clicked, change the current time to a lower time. */
  @FXML
  private void onTimeLeftArrowClicked() {
    // Play the selection audio
    AudioManager.loadAudio(Clip.SELECTION);

    // Disable the current components for the current index
    disableComponent(timeComponents, timeIndex);

    // Set the next time index
    timeIndex = getPreviousIndex(timeIndex);

    // Enable the new components for the new index
    enableComponent(timeComponents, timeIndex);

    // Set the time based on the current index
    setTime();
  }

  /** When time right arrow is clicked, change the current time to a higher time. */
  @FXML
  private void onTimeRightArrowClicked() {
    // Play the selection audio
    AudioManager.loadAudio(Clip.SELECTION);

    // Disable the current components for the current index
    disableComponent(timeComponents, timeIndex);

    // Set the next time index
    timeIndex = getNextIndex(timeIndex);

    // Enable the new components for the new index
    enableComponent(timeComponents, timeIndex);

    // Set the time based on the current index
    setTime();
  }

  /** When difficulty left arrow is clicked, change the current difficulty to a lower difficulty. */
  @FXML
  private void onDifficultyLeftArrowClicked() {
    // Play the selection audio
    AudioManager.loadAudio(Clip.SELECTION);

    // Disable the current components for the current index
    disableComponent(difficultyComponents, difficultyIndex);

    // Set the next difficulty index
    difficultyIndex = getPreviousIndex(difficultyIndex);

    // Enable the new components for the new index
    enableComponent(difficultyComponents, difficultyIndex);

    // Set the difficulty based on the current index
    setDifficulty();
  }

  /**
   * When difficulty right arrow is clicked, change the current difficulty to a higher difficulty.
   */
  @FXML
  private void onDifficultyRightArrowClicked() {
    // Play the selection audio
    AudioManager.loadAudio(Clip.SELECTION);

    // Disable the current components for the current index
    disableComponent(difficultyComponents, difficultyIndex);

    // Set the next difficulty index
    difficultyIndex = getNextIndex(difficultyIndex);

    // Enable the new components for the new index
    enableComponent(difficultyComponents, difficultyIndex);

    // Set the difficulty based on the current index
    setDifficulty();
  }

  /** When return is clicked, go back to the main menu. */
  @FXML
  private void onReturnPaneClicked() {
    AudioManager.loadAudio(Clip.SELECTION);
    App.setUi(AppUi.MENU);
  }

  /**
   * Check if there is a keyboard event. If there is a keyboard event, handle the event
   * appropriately.
   *
   * @param keyEvent this event is generated when a key is pressed, released, or typed
   */
  @FXML
  private void onKeyPressed(KeyEvent keyEvent) {
    // Check if the escape key has been pressed
    if (keyEvent.getCode() != KeyCode.ESCAPE) {
      return;
    }

    // Play the selection audio and switch back to main menu
    AudioManager.loadAudio(Clip.SELECTION);
    App.setUi(AppUi.MENU);
  }

  /** Initialize the time components. */
  private void initializeTimeComponents() {
    // Initialize the time index
    timeIndex = 0;

    // Initialize an array of list
    timeComponents = new ArrayList<>();

    // Add the pair components to the time components
    timeComponents.add(new Pair<Label, Line>(lblTwoMinutes, lineTwoMinutes));
    timeComponents.add(new Pair<Label, Line>(lblFourMinutes, lineFourMinutes));
    timeComponents.add(new Pair<Label, Line>(lblSixMinutes, lineSixMinutes));
  }

  /** Initialize the difficulty components. */
  private void initializeDifficultyComponents() {
    // Initialize the difficulty index
    difficultyIndex = 0;

    // Initialize an array of list
    difficultyComponents = new ArrayList<>();

    // Add the pair components to the difficulty components
    difficultyComponents.add(new Pair<Label, Line>(lblEasy, lineEasy));
    difficultyComponents.add(new Pair<Label, Line>(lblMedium, lineMedium));
    difficultyComponents.add(new Pair<Label, Line>(lblHard, lineHard));
  }

  /**
   * Return the previous index based on the current index.
   *
   * @param index the current index
   * @return the previous index
   */
  private int getPreviousIndex(int index) {
    return (index == 0 ? 2 : index - 1);
  }

  /**
   * Return the next index based on the current index.
   *
   * @param index the current index
   * @return the next index
   */
  private int getNextIndex(int index) {
    return (index == 2 ? 0 : index + 1);
  }

  /** Set the max time for the game. */
  private void setTime() {
    switch (timeIndex) {
      case 0:
        setTimeTwoMinutes();
        break;
      case 1:
        setTimeFourMinutes();
        break;
      default:
        setTimeSixMinutes();
        break;
    }
  }

  /** Set the game's difficulty. */
  private void setDifficulty() {
    // Reset all comments
    resetDifficultyComments();

    switch (difficultyIndex) {
      case 0:
        setDifficultyEasy();
        break;
      case 1:
        setDifficultyMedium();
        break;
      default:
        setDifficultyHard();
        break;
    }
  }

  /** Set the game's difficulty to easy. */
  private void setDifficultyEasy() {
    // Set the difficulty to easy
    GameState.gameDifficulty = Difficulty.EASY;

    // Set max hints to infinity
    GameState.maxHints = Integer.MAX_VALUE;

    // Set the amount of hints to infinity
    GameState.hintCounter = Integer.MAX_VALUE;

    // Change the difficulty comment label
    lblEasyComment.setVisible(true);
  }

  /** Set the game's difficulty to hard. */
  private void setDifficultyHard() {
    // Set the difficulty to hard
    GameState.gameDifficulty = Difficulty.HARD;

    // Set max hints to zero
    GameState.maxHints = 0;

    // Set the amount of hints to zero
    GameState.hintCounter = 0;

    // Change the difficulty comment label
    lblHardComment.setVisible(true);
  }

  /** Set the game's difficulty to medium. */
  private void setDifficultyMedium() {
    // Set the difficulty to medium
    GameState.gameDifficulty = Difficulty.MEDIUM;

    // Set max hints to 5
    GameState.maxHints = 5;

    // Set the amount of hints to 5
    GameState.hintCounter = 5;

    // Change the difficulty comment label
    lblMediumComment.setVisible(true);
  }

  /** Set the time limit to two minutes. */
  private void setTimeTwoMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 120;
  }

  /** Set the time limit to six minutes. */
  private void setTimeSixMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 360;
  }

  /** Set the time limit to four minutes. */
  private void setTimeFourMinutes() {
    GameState.maxTime = 240;
  }

  /** Enables the return pane overlay. */
  private void enableReturnOverlay() {
    // Change the color of the label
    lblReturn.setTextFill(Color.rgb(3, 7, 9));

    // Change the color of the panes
    paReturnLeftOverlay.setStyle("-fx-background-color: rgb(80, 180, 180);");
    paReturnRightOverlay.setStyle("-fx-background-color: rgb(80, 180, 180);");

    // Change the fill and stroke of the polygon
    pgReturnOverlay.setFill(Color.rgb(80, 180, 180));
    pgReturnOverlay.setStroke(Color.rgb(80, 180, 180));

    // Change the color of the lines
    lineReturnTop.setStroke(Color.rgb(80, 180, 180));
    lineReturnLeft.setStroke(Color.rgb(80, 180, 180));
    lineReturnRight.setStroke(Color.rgb(80, 180, 180));
    lineReturnBottom.setStroke(Color.rgb(80, 180, 180));
    lineReturnDiagonal.setStroke(Color.rgb(80, 180, 180));
  }

  /**
   * Disables all components inside of the input components list for the given index.
   *
   * @param components the components list.
   * @param index the index for component to be disabled.
   */
  private void enableComponent(List<Pair<Label, Line>> components, int index) {
    // Get the label
    Label label = components.get(index).getKey();

    // Get the line
    Line line = components.get(index).getValue();

    // Set the label visible
    label.setVisible(true);

    // Set the stroke and fill for the line
    line.setFill(Color.rgb(248, 84, 84));
    line.setStroke(Color.rgb(248, 84, 84));
  }

  private void disableReturnOverlay() {
    // Change the color of the label
    lblReturn.setTextFill(Color.rgb(80, 180, 183));

    // Change the color of the panes
    paReturnLeftOverlay.setStyle("-fx-background-color: rgb(3, 7, 9);");
    paReturnRightOverlay.setStyle("-fx-background-color: rgb(3, 7, 9);");

    // Change the fill and stroke of the polygon
    pgReturnOverlay.setFill(Color.rgb(3, 7, 9));
    pgReturnOverlay.setStroke(Color.rgb(3, 7, 9));

    // Change the color of the lines
    lineReturnTop.setStroke(Color.rgb(248, 84, 84));
    lineReturnLeft.setStroke(Color.rgb(248, 84, 84));
    lineReturnRight.setStroke(Color.rgb(248, 84, 84));
    lineReturnBottom.setStroke(Color.rgb(248, 84, 84));
    lineReturnDiagonal.setStroke(Color.rgb(248, 84, 84));
  }

  /**
   * Disables all components inside of the input components list for the given index.
   *
   * @param components the components list.
   * @param index the index for component to be disabled.
   */
  private void disableComponent(List<Pair<Label, Line>> components, int index) {
    // Get the label
    Label label = components.get(index).getKey();

    // Get the line
    Line line = components.get(index).getValue();

    // Set the label invisible
    label.setVisible(false);

    // Set the stroke and fill for the line
    line.setFill(Color.rgb(101, 40, 40));
    line.setStroke(Color.rgb(101, 40, 40));
  }

  /**
   * Reset all difficulty comments. These comments provide the player some insight for the
   * difficulty picked.
   */
  private void resetDifficultyComments() {
    lblEasyComment.setVisible(false);
    lblHardComment.setVisible(false);
    lblMediumComment.setVisible(false);
  }
}
