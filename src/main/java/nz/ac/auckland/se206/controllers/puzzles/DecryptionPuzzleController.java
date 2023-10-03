package nz.ac.auckland.se206.controllers.puzzles;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.Algorithm;
import nz.ac.auckland.se206.constants.Description;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;
import nz.ac.auckland.se206.constants.Sequence;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the decryption puzzle scene. */
public class DecryptionPuzzleController {
  @FXML private Pane paLine0;
  @FXML private Pane paLine1;
  @FXML private Pane paLine2;
  @FXML private Pane paLine3;
  @FXML private Pane paLine4;
  @FXML private Pane paLine5;
  @FXML private Pane paLine6;
  @FXML private Pane paLine7;
  @FXML private Pane paLine8;

  @FXML private Pane paBack;
  @FXML private Pane paHint;
  @FXML private Pane paEmpty;
  @FXML private Pane paAnalyze;
  @FXML private Pane paPassword;
  @FXML private Pane paDecryption;
  @FXML private Pane paMatrixRain;
  @FXML private Pane paBackOverlay;
  @FXML private Pane paEmptyComponent;
  @FXML private Pane paEmptyComponentBar;
  @FXML private Pane paPasswordComponent;

  @FXML private Line lineVertical;

  @FXML private Label lblTime;
  @FXML private Label lblError;
  @FXML private Label lblEmpty;
  @FXML private Label lblMemory;
  @FXML private Label lblSequence;
  @FXML private Label lblPassword;
  @FXML private Label lblHintCounter;
  @FXML private Label lblPrefixSequence;

  @FXML private Canvas cvsMatrixRain;

  @FXML private Polygon pgEmptyLeft;
  @FXML private Polygon pgEmptyRight;
  @FXML private Polygon pgPasswordLeft;
  @FXML private Polygon pgPasswordRight;
  @FXML private Polygon pgEmptyComponent;
  @FXML private Polygon pgPasswordComponent;

  @FXML private TextArea taChat;
  @FXML private TextArea taAlgorithm;
  @FXML private TextArea taDescription;

  private boolean[] isLineSelected;

  private Pane[] paLines;
  private Pane[] paLineOverlays;

  private int hintIndex;
  private int pseudocodeIndex;
  private int pseudocodeLines;

  private double memoryUsed;

  private boolean isPasswordTabOpen;

  private String sequence;
  private String algorithm;
  private String pseudocode;
  private String description;

  private ChatCompletionRequest gptRequest;

  private TextToSpeech tts;

  /** Initializes the decryption puzzle. */
  @FXML
  private void initialize() throws Exception {
    // Password tab is initially open
    isPasswordTabOpen = true;

    // get the game state instance of tts
    this.tts = GameState.tts;

    // Add the label to list of labels to be updated
    Timer.addLabel(lblTime);

    // Initialize the memory component
    initializeMemory();

    // Initialize the matrix rain
    initializeMatrixRain();

    // Initialize the pseudocode and algorithms
    initializePseudocode();

    // Intialize the puzzle components
    initializePuzzleComponents();
  }

  /** When the mouse is hovering over the pane, the overlay appears (hint). */
  @FXML
  private void onHintPaneEntered() {
    paHint.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (hint). */
  @FXML
  private void onHintPaneExited() {
    paHint.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When the mouse is hovering over the pane, the overlay appears (analyze). */
  @FXML
  private void onAnalyzeEntered() {
    paAnalyze.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (analyze). */
  @FXML
  private void onAnalyzeExited() {
    paAnalyze.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When the mouse is hovering over the pane, the overlay appears (line). */
  @FXML
  private void onLinePaneEntered(Event event) {
    // Retrieve the line's index
    int lineIndex = getLineIndex(event);

    // Check if the puzzle has been solved
    if (GameState.isDecryptionSolved) {
      return;
    }

    // Check if the index is greater than the amount of lines
    if (lineIndex >= pseudocodeLines) {
      return;
    }

    // Check if the line has already been selected
    if (isLineSelected[lineIndex]) {
      return;
    }

    // Set the line visible
    paLines[lineIndex].setOpacity(1);

    // Set the line overlay visible
    paLineOverlays[lineIndex].setVisible(true);
  }

  /** When the mouse is hovering over the pane, the overlay appears (line). */
  @FXML
  private void onLinePaneExited(Event event) {
    // Retrieve the line's index
    int lineIndex = getLineIndex(event);

    // Check if the index is greater than the amount of lines
    if (lineIndex >= pseudocodeLines) {
      return;
    }

    // Check if the line has already been selected
    if (isLineSelected[lineIndex]) {
      return;
    }

    // Set the line overlay invisible
    paLines[lineIndex].setOpacity(0);

    // Set the line overlay invisible
    paLineOverlays[lineIndex].setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (back). */
  @FXML
  private void onBackPaneEntered() {
    paBack.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (back). */
  @FXML
  private void onBackPaneExited() {
    paBack.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When the mouse is hovering over the pane, the overlay appears (empty). */
  @FXML
  private void onEmptyEntered() {
    enableEmptyTabComponents();
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (empty). */
  @FXML
  private void onEmptyExited() {
    // Check if the empty tab is already opened
    if (!isPasswordTabOpen) {
      return;
    }

    // disable empty tab components
    disableEmptyTabComponents();
  }

  /** When the mouse is hovering over the pane, the overlay appears (password). */
  @FXML
  private void onPasswordEntered() {
    enablePasswordTabComponents();
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (password). */
  @FXML
  private void onPasswordExited() {
    // Check if the password tab is already open
    if (isPasswordTabOpen) {
      return;
    }

    // disable password tab components
    disablePasswordTabComponents();
  }

  /** When hint is clicked, give the user a hint. */
  @FXML
  private void onHintPaneClicked() {
    // Check if the puzzle has been solved
    if (GameState.isDecryptionSolved) {
      return;
    }

    // If the difficulty is hard, ignore user.
    if (GameState.gameDifficulty == Difficulty.HARD) {
      return;
    }

    // If the number of remaining hints is zero
    if (GameState.hintCounter == 0) {
      return;
    }

    // Get a user hint
    getUserHint();
  }

  /** When analyze pane is pressed, check if the user's sequence is corrrect. */
  @FXML
  private void onAnalyzeClicked() {
    // Check if the puzzle has been solved
    if (GameState.isDecryptionSolved) {
      return;
    }

    // Handle the user's sequence based on whether it is correct or not
    if (lblSequence.getText().equals(sequence)) {
      handleCorrectUserSequence();
    } else {
      handleIncorrectUserSequence();
    }
  }

  @FXML
  private void onLinePaneClicked(Event event) {
    // Retrieve the line's index
    int lineIndex = getLineIndex(event);

    // Check if the puzzle has been solved
    if (GameState.isDecryptionSolved) {
      return;
    }

    // Check if the index is greater than the amount of lines
    if (lineIndex >= pseudocodeLines) {
      return;
    }

    // Check if the user can click on anymore lines
    if (getLinesSelected() == GameState.maxSequence && !isLineSelected[lineIndex]) {
      return;
    }

    // Get the next opacity for the line
    double nextLineOpacity = (isLineSelected[lineIndex] ? 0 : 1);

    // Toggle the current sequence index
    isLineSelected[lineIndex] = !isLineSelected[lineIndex];

    // Toggle the visibility of the line
    paLines[lineIndex].setOpacity(nextLineOpacity);

    // Toggle the visibility of the line overlay
    paLineOverlays[lineIndex].setVisible(isLineSelected[lineIndex]);

    // Set the sequence entered by the user
    setUserSequence();
  }

  /** When back is clicked, go back to previous section (control room). */
  @FXML
  private void onBackPaneClicked() {
    App.setUi(AppUi.TERMINAL);
  }

  @FXML
  private void onEmptyClicked() {
    // Empty tab is now open
    isPasswordTabOpen = false;

    // Set the empty label to 'opened tab' color
    lblEmpty.setTextFill(Color.rgb(80, 170, 255));

    // Set the password label to 'closed tab' color
    lblPassword.setTextFill(Color.rgb(255, 255, 255));

    // Disable password pane components
    disablePasswordComponents();

    // Enable empty pane components
    enableEmptyComponents();
  }

  @FXML
  private void onPasswordClicked() {
    // Password tab is now open
    isPasswordTabOpen = true;

    // Set the empty label to 'closed tab' color
    lblEmpty.setTextFill(Color.rgb(255, 255, 255));

    // Set the password label to 'opened tab' color
    lblPassword.setTextFill(Color.rgb(80, 170, 255));

    // Enable password pane components
    enablePasswordComponents();

    // Disable empty pane components
    disableEmptyComponents();
  }

  /**
   * Initialize GPT. Set the tokens and create a new instance of GPT request.
   *
   * <p>Note: I would love to be able to name this method 'initializeGPT'. Unfortunately, we are not
   * allowed to have acronyms as method names as per the naming convention.
   */
  private void initializeChat() {
    // initialize an instance of GPT request
    gptRequest = new ChatCompletionRequest();

    // set the 'n' parameter for the request -> has to be '1'
    gptRequest.setN(1);

    // set the temperature for the request -> [0,2]
    gptRequest.setTemperature(1.4);

    // set the 'top p' value for the request -> [0,1]
    gptRequest.setTopP(0.5);

    // set the max tokens -> has to be at least '1'
    gptRequest.setMaxTokens(100);
  }

  /** Initialize the memory grid (for visual effects). */
  private void initializeMemory() {
    // Initialize memory used to zero
    memoryUsed = 0;

    // Create a grid of memory cells
    for (double y = 0; y < 5; y++) {
      for (double x = 0; x < 15; x++) {
        setMemoryLocation(x, y, 5, 5);
      }
    }

    // Recalculate memory used using ratio
    memoryUsed = getMemoryUsed();

    // Set the memory used text
    lblMemory.setText("USING " + memoryUsed + " OUT OF 32 GiB");
  }

  /** Intialize the matrix rain for the decryption puzzle. */
  private void initializeMatrixRain() {
    new AnimationTimer() {
      // Keep track of when the previous fram was called
      long lastTimerCall = 0;

      // Initialize the font size
      int fontSize = 16;

      // Get the width and height of the matrix rain canvas
      int width = (int) cvsMatrixRain.getWidth();
      int height = (int) cvsMatrixRain.getHeight();

      // Get the number of columns for the matrix pane
      int columns = (int) Math.floor(cvsMatrixRain.getWidth() / fontSize);

      // Initialize array to store each rain drop's last position
      int[] verticalPositions = new int[columns];

      // Nanoseconds in a millisecond and get 50ms
      long nanoseconds = 1000000;
      long animationDelay = nanoseconds * 100;

      // Initialize instance of random method
      Random random = new Random();

      // Get the graphic context of the matrix rain canvas
      GraphicsContext gcMatrixRain = cvsMatrixRain.getGraphicsContext2D();

      public void handle(long now) {
        // Check if the current frame is drawing the matrix rain
        if (now > lastTimerCall + animationDelay) {
          lastTimerCall = now;

          // Add a fade out effect for the text
          gcMatrixRain.setFill(Color.web("#1d1e2530"));
          gcMatrixRain.fillRect(0, 0, width, height);

          // Draw the text onto the matrix rain canvas
          gcMatrixRain.setFill(Color.web("00ff00"));
          gcMatrixRain.setFont(new Font("monospace", 16));

          for (int i = 0; i < columns; i++) {
            // Get a random text for the matrix rain drop
            String currentText = getRandomCharacter(random);

            // X coordinate to draw from left to right (each column)
            double horizontalPosition = (i * fontSize) + (fontSize / 2);

            // Y coordinate is based on the value previously stored
            int verticalPosition = verticalPositions[i];

            // Draw a character with an opaque color
            gcMatrixRain.fillText(currentText, horizontalPosition, verticalPosition);

            // Restart the current rain drop if the drop reaches the bottom of the canvas
            if (verticalPosition > 100 + (Math.random() * 10000)) {
              verticalPositions[i] = 0;
            } else {
              verticalPositions[i] = verticalPosition + fontSize;
            }
          }
        }
      }
    }.start();
  }

  /**
   * Initialize pseudocode instances. The method should get a 'random' pseudocode each round.
   *
   * @throws Exception thrown when there is an error initializing pseudocode instances.
   */
  private void initializePseudocode() throws Exception {
    // Hint index is initially zero
    hintIndex = 0;

    // Get a random pseudo code
    pseudocodeIndex = (int) (Math.random() * GameState.maxPseudocodes);

    // Initialize the sequence
    intializeSequence();

    // Initialize the description and algorithm
    initializeDescription();
    initializeAlgorithm();

    // Get the number of lines for the pseudocode
    pseudocodeLines = getPseudocodeLines();

    // Append the description to the text area
    taDescription.appendText(description);

    // Append the algorithm to the text area
    taAlgorithm.appendText(algorithm);

    // Get the pseudocode in string form
    pseudocode = description + algorithm;
  }

  private void initializePuzzleComponents() {
    // Initialize the sequence indice array
    isLineSelected = new boolean[9];

    // Initialize the pseudocode lines array
    paLines = new Pane[pseudocodeLines];

    // Initialize the line overlays array
    paLineOverlays = new Pane[pseudocodeLines];

    // Retrieve the line and overlay pane components
    for (int line = 0; line < pseudocodeLines; line++) {
      paLines[line] = (Pane) paDecryption.lookup("#paLine" + line);
      paLineOverlays[line] = (Pane) paDecryption.lookup("#paLineOverlay" + line);
    }

    // Change the cursor for un-touchable lines
    for (int line = pseudocodeLines; line < 9; line++) {
      Pane paLine = (Pane) paDecryption.lookup("#paLine" + line);
      paLine.setCursor(Cursor.DEFAULT);
    }

    // Change the line height to match the number of algorithm lines present
    lineVertical.setEndY((22 * pseudocodeLines) - 1.5);
  }

  /**
   * Get the string sequence for the corresponding random pseudocode index.
   *
   * @return the string value of the sequence.
   * @throws Exception throw when class or field name is not found.
   */
  private void intializeSequence() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "sequence" + Integer.toString(pseudocodeIndex);

    // create an object of 'Sequence'
    Sequence sequence = new Sequence();

    // create a runtime reference to the class, 'Sequence'
    Class<?> cls = sequence.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    this.sequence = (String) fld.get(sequence);
  }

  /**
   * Get the string algorithm code snippet for the corresponding random pseudocode index.
   *
   * @return the string value of the algorithm code snippet.
   * @throws Exception throw when class or field name is not found.
   */
  private void initializeAlgorithm() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "algorithm" + Integer.toString(pseudocodeIndex);

    // create an object of 'Algorithm'
    Algorithm algorithm = new Algorithm();

    // create a runtime reference to the class, 'Algorithm'
    Class<?> cls = algorithm.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    this.algorithm = (String) fld.get(algorithm);
  }

  /**
   * Get the string description for the corresponding random pseudocode index.
   *
   * @return the string value of the description.
   * @throws Exception throw when class or field name is not found.
   */
  private void initializeDescription() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "description" + Integer.toString(pseudocodeIndex);

    // create an object of 'Description'
    Description description = new Description();

    // create a runtime reference to the class, 'Description'
    Class<?> cls = description.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    this.description = (String) fld.get(description);
  }

  /**
   * Generate a response from GPT.
   *
   * @param entityMessage the chat message to be sent to GPT.
   */
  private void getChatResponse(ChatMessage entityMessage, boolean isHint) {
    // add user input to GPT's user input history
    gptRequest.addMessage(entityMessage);

    // create a concurrent task for handling GPT response
    Task<Void> gptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Set GPT's response
            setChatResponse();
            return null;
          }
        };

    // If the task is running, disable certain components
    gptTask.setOnRunning(
        event -> {
          disableComponents();
        });

    // If the task succeeds, then enable components
    gptTask.setOnSucceeded(
        event -> {
          enableComponents();
        });

    // If the task fails, then enable components
    gptTask.setOnFailed(
        event -> {
          enableComponents();
        });

    // Create a thread to handle GPT concurrency
    Thread gptThread = new Thread(gptTask);

    // Start the thread
    gptThread.start();
  }

  /** Generate a GPT response. GPT should give a hint for the current pseudocode. */
  private void getUserHint() {
    // Initialize a new instance of gpt
    initializeChat();

    // Update the hint counter
    HintManager.updateHintCounter();

    // Get the incorrect line number for the following pseudocode and hint index
    int lineNumber = Integer.valueOf(sequence.charAt(hintIndex));

    // Get the hint prompt for GPT to analyze and generate a response
    String hint = GptPromptEngineering.getDecryptionHint(pseudocode, lineNumber);

    // Initialize a user hint message compatible for GPT to analyze
    ChatMessage userHintMessage = new ChatMessage("assistant", hint);

    // Get GPT's response
    getChatResponse(userHintMessage, true);

    // Update the hint index
    hintIndex = (hintIndex + 1) % GameState.maxSequence;
  }

  private double getMemoryUsed() {
    // Get the ratio of memory used
    memoryUsed = (memoryUsed / 75) * 32;

    // Get the memory used to 2 decimal places
    String roundedMemoryUsed = String.format("%.2f", memoryUsed);

    return Double.parseDouble(roundedMemoryUsed);
  }

  private Color getRandomColor() {
    Color green = Color.rgb(130, 240, 130);
    Color gray = Color.rgb(56, 57, 63);

    boolean isColorGreen = Math.random() < 0.5f;

    if (isColorGreen) {
      memoryUsed++;
    }

    return (isColorGreen ? green : gray);
  }

  private String getRandomCharacter(Random random) {
    IntStream characterStream = random.ints(12353, 12380);

    char character = (char) characterStream.findFirst().getAsInt();

    return Character.toString(character);
  }

  private int getPseudocodeLines() {
    IntStream characterStream = algorithm.chars();

    return (int) characterStream.filter(character -> character == '\n').count() + 1;
  }

  private int getLineIndex(Event event) {
    String index = ((Node) event.getSource()).getId();

    return Integer.parseInt(index.substring(index.length() - 1));
  }

  private int getLinesSelected() {
    int count = 0;

    for (boolean isSelected : isLineSelected) {
      count += (isSelected ? 1 : 0);
    }

    return count;
  }

  /**
   * Set the chat response from GPT. This includes printing the response to the text area.
   *
   * @throws Exception thrown when we fail to retrieve a response from GPT.
   */
  private void setChatResponse() throws Exception {
    // Get GPT's response
    ChatCompletionResult gptResult = gptRequest.execute();

    // Get GPT's choice
    Choice gptChoice = gptResult.getChoices().iterator().next();

    // Get GPT's chat message
    ChatMessage gptMessage = gptChoice.getChatMessage();

    // Get the content of gpt's message in the form of a string
    String gptOutput = gptMessage.getContent();

    // Append the result to the text area
    taChat.appendText("ai> " + gptOutput + "\n\n");

    // Make text-to-speech read GPT's output
    tts.speak(gptOutput, AppUi.DECRYPTION);
  }

  private void setMemoryLocation(double x, double y, double w, double h) {
    // Initialize the offsets
    double horizontalOffset = 5;
    double verticalOffset = 85;

    // Intialize the padding
    double padding = 5;

    // Create a rectangle component
    Rectangle memory = new Rectangle(w, h);

    // Get a random color
    Color color = getRandomColor();

    // Set the location of the rectangle
    memory.setTranslateX((x * (w + padding)) + horizontalOffset);
    memory.setTranslateY((y * (h + padding)) + verticalOffset);

    // Set the color of the rectangle
    memory.setFill(color);

    // Set the style of the rectangle
    setRectangleStyle(memory);

    // Add the rectangle to the scene
    paDecryption.getChildren().add(memory);
  }

  private void setUserSequence() {
    // Initialize a new sequence
    String newSequence = "";

    // Get which line numbers are selected by the user
    for (int line = 0; line < pseudocodeLines; line++) {
      if (isLineSelected[line]) {
        newSequence += line + 1;
      }
    }

    // Update the old user's sequence
    lblSequence.setText(newSequence);
  }

  private void setRectangleStyle(Rectangle rectangle) {
    // Initialize a glow for the rectangle
    Glow glow = new Glow();
    glow.setLevel(0.7);

    // Set the glow effect for the rectangle
    rectangle.setEffect(glow);
  }

  /** Update labels when player solves the puzzle. */
  private void setLabelsSolved() {
    // There are now zero errors
    lblError.setText("0 errors");

    // Change the color of the error label
    lblError.setTextFill(Color.rgb(130, 255, 90));

    // Change the color of the sequence labels
    lblSequence.setTextFill(Color.rgb(130, 255, 90));
    lblPrefixSequence.setTextFill(Color.rgb(130, 255, 90));
  }

  private void setPaneEntered(Pane pane) {
    pane.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  private void setPaneExited(Pane pane) {
    pane.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  private void setPolygonEntered(Polygon polygon) {
    polygon.setStroke(Color.rgb(29, 30, 37));
    polygon.setFill(Color.rgb(29, 30, 37));
  }

  private void setPolygonExited(Polygon polygon) {
    polygon.setStroke(Color.rgb(20, 20, 23));
    polygon.setFill(Color.rgb(20, 20, 23));
  }

  /** Enable components when a task is finished. */
  private void enableComponents() {
    paHint.setDisable(false);
  }

  /** Enable empty pane components. */
  private void enableEmptyComponents() {
    // Enable empty tab components
    enableEmptyTabComponents();

    // Set the matrix rain visible
    cvsMatrixRain.setVisible(true);

    // Set all components in empty pane visible
    paEmptyComponent.setVisible(true);
    pgEmptyComponent.setVisible(true);
    paEmptyComponentBar.setVisible(true);

    // Enable all components in empty pane
    paEmptyComponent.setDisable(false);
    pgEmptyComponent.setDisable(false);
    paEmptyComponentBar.setDisable(false);
  }

  /** Enable password pane components. */
  private void enablePasswordComponents() {
    // Enable password tab components
    enablePasswordTabComponents();

    // Set all components in password pane visible
    paPasswordComponent.setVisible(true);
    pgPasswordComponent.setVisible(true);

    // Enable all components in password pane
    paPasswordComponent.setDisable(false);
    pgPasswordComponent.setDisable(false);
  }

  /** Enable empty tab components. */
  private void enableEmptyTabComponents() {
    // Set entered color for the pane
    setPaneEntered(paEmpty);

    // Set entered colors for the polygons
    setPolygonEntered(pgEmptyLeft);
    setPolygonEntered(pgEmptyRight);
  }

  /** Enable password tab components. */
  private void enablePasswordTabComponents() {
    // Set entered color for the pane
    setPaneEntered(paPassword);

    // Set entered colors for the polygons
    setPolygonEntered(pgPasswordLeft);
    setPolygonEntered(pgPasswordRight);
  }

  /** Disable components when a task is running. */
  private void disableComponents() {
    paHint.setDisable(true);
  }

  /** Disable empty pane components. */
  private void disableEmptyComponents() {
    // Disable empty tab components
    disableEmptyTabComponents();

    // Set the matrix rain invisible
    cvsMatrixRain.setVisible(false);

    // Set all components in empty pane invisible
    paEmptyComponent.setVisible(false);
    pgEmptyComponent.setVisible(false);
    paEmptyComponentBar.setVisible(false);

    // Disable all components in empty pane
    paEmptyComponent.setDisable(true);
    pgEmptyComponent.setDisable(true);
    paEmptyComponentBar.setDisable(true);
  }

  /** Disable password pane components. */
  private void disablePasswordComponents() {
    // Disable password tab components
    disablePasswordTabComponents();

    // Set all components in password pane invisible
    paPasswordComponent.setVisible(false);
    pgPasswordComponent.setVisible(false);

    // Disable all components in password pane
    paPasswordComponent.setDisable(true);
    pgPasswordComponent.setDisable(true);
  }

  /** Disable empty tab components. */
  private void disableEmptyTabComponents() {
    // Set exited color for the pane
    setPaneExited(paEmpty);

    // Set exited colors for the polygons
    setPolygonExited(pgEmptyLeft);
    setPolygonExited(pgEmptyRight);
  }

  /** Disable password tab components. */
  private void disablePasswordTabComponents() {
    // Set exited color for the pane
    setPaneExited(paPassword);

    // Set exited colors for the polygons
    setPolygonExited(pgPasswordLeft);
    setPolygonExited(pgPasswordRight);
  }

  /** Handle the event when user correctly inputs the sequence. */
  private void handleCorrectUserSequence() {
    // Set cursor to default for all line panes
    for (Pane pane : paLines) {
      pane.setCursor(Cursor.DEFAULT);
    }

    // Update all labels associated with solving the puzzle
    setLabelsSolved();

    // The decryption puzzle is now solved
    GameState.isDecryptionSolved = true;
  }

  /** Handle the event when user incorrectly inputs the sequence. */
  private void handleIncorrectUserSequence() {
    System.out.println("INCORRECT");
  }
}
