package nz.ac.auckland.se206.controllers.puzzles;

import java.lang.reflect.Field;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.Algorithm;
import nz.ac.auckland.se206.constants.Declaration;
import nz.ac.auckland.se206.constants.Description;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Sequence;

/** Controller class for the decryption puzzle scene. */
public class DecryptionPuzzleController {
  @FXML private Label lblSequence;

  @FXML private Button btn1;
  @FXML private Button btn2;
  @FXML private Button btn3;
  @FXML private Button btn4;
  @FXML private Button btn5;
  @FXML private Button btn6;
  @FXML private Button btn7;
  @FXML private Button btn8;
  @FXML private Button btn9;

  @FXML private Button btnBack;
  @FXML private Button btnClear;
  @FXML private Button btnEnter;
  @FXML private Button btnBackSpace;

  @FXML private TextArea taAlgorithm;
  @FXML private TextArea taDescription;
  @FXML private TextArea taDeclaration;

  private int psuedocodeIndex;

  private String sequence;
  private String algorithm;
  private String description;
  private String declaration;

  /** Initializes the decryption puzzle. */
  @FXML
  private void initialize() throws Exception {
    initializePseudocode();
  }

  /** On mouse clicked, if the button is pressed, then switch to the control room scene. */
  @FXML
  private void onBackButton() {
    App.setUi(AppUi.CONTROL);
  }

  /** On mouse clicked, if the button is pressed, clear the player's current sequence input. */
  @FXML
  private void onClearButton() {
    lblSequence.setText("");
  }

  /** On mouse clicked, if the button is pressed, remove the last digit from the sequence. */
  @FXML
  private void onBackSpaceButton() {
    int length = lblSequence.getText().length();

    // check if the input is already zero
    if (length == 0) {
      return;
    }

    lblSequence.setText(lblSequence.getText().substring(0, length - 1));
  }

  @FXML
  private void onEnterButton() {
    if (lblSequence.getText().equals(sequence)) {
      System.out.println("CORRECT");
    }
  }

  /** On mouse clicked, if a number button is pressed, add the number to the sequence. */
  @FXML
  private void onNumberButton(ActionEvent event) {
    // check if the player's input is too long
    if (lblSequence.getText().length() == GameState.maxSequence) {
      return;
    }

    lblSequence.setText(lblSequence.getText() + getButtonIndex(event));
  }

  /**
   * Initializes the pseudocode snippets. A random pseudocode index will be picked at run time, then
   * the text areas will be set up for the corresponding code snippets.
   *
   * @throws Exception throw when class or field name is not found.
   */
  private void initializePseudocode() throws Exception {
    // get a random pseudocode index
    psuedocodeIndex = (int) (Math.random() * GameState.maxPseudocodes);

    // get the sequence for the corresponding psuedocode snippet
    sequence = getSequence();

    // get the psuedocode snippets
    algorithm = getAlgorithm();
    description = getDescription();
    declaration = getDeclaration();

    // set up the text areas
    taAlgorithm.appendText(algorithm);
    taDescription.appendText(description);
    taDeclaration.appendText(declaration);
  }

  /**
   * On mouse clicked, return the index of the button pressed.
   *
   * @param event the event representing the type of action.
   * @return the button index of the button pressed.
   */
  private String getButtonIndex(ActionEvent event) {
    String buttonIndex = ((Control) event.getSource()).getId();

    return buttonIndex.substring(buttonIndex.length() - 1);
  }

  /**
   * Get the string sequence for the corresponding random pseudocode index.
   *
   * @return the string value of the sequence.
   * @throws Exception throw when class or field name is not found.
   */
  private String getSequence() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "sequence" + Integer.toString(psuedocodeIndex);

    // create an object of 'Sequence'
    Sequence sequence = new Sequence();

    // create a runtime reference to the class, 'Sequence'
    Class<?> cls = sequence.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    String value = (String) fld.get(sequence);

    return value;
  }

  /**
   * Get the string algorithm code snippet for the corresponding random pseudocode index.
   *
   * @return the string value of the algorithm code snippet.
   * @throws Exception throw when class or field name is not found.
   */
  private String getAlgorithm() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "algorithm" + Integer.toString(psuedocodeIndex);

    // create an object of 'Algorithm'
    Algorithm algorithm = new Algorithm();

    // create a runtime reference to the class, 'Algorithm'
    Class<?> cls = algorithm.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    String value = (String) fld.get(algorithm);

    return value;
  }

  /**
   * Get the string description for the corresponding random pseudocode index.
   *
   * @return the string value of the description.
   * @throws Exception throw when class or field name is not found.
   */
  private String getDescription() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "description" + Integer.toString(psuedocodeIndex);

    // create an object of 'Description'
    Description description = new Description();

    // create a runtime reference to the class, 'Description'
    Class<?> cls = description.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    String value = (String) fld.get(description);

    return value;
  }

  /**
   * Get the string declaration code snippet for the corresponding random pseudocode index.
   *
   * @return the string value of the declaration code snippet.
   * @throws Exception throw when class or field name is not found.
   */
  private String getDeclaration() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "declaration" + Integer.toString(psuedocodeIndex);

    // create an object of 'Declaration'
    Declaration declaration = new Declaration();

    // create a runtime reference to the class, 'Declaration'
    Class<?> cls = declaration.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    String value = (String) fld.get(declaration);

    return value;
  }
}
