package nz.ac.auckland.se206.constants;

import java.time.LocalDate;
import java.time.LocalTime;

public class Instructions {
  public static final String bootup =
      "OS: "
          + System.getProperty("os.name")
          + "\n"
          + "Date: "
          + LocalDate.now()
          + "\n"
          + "Time: "
          + LocalTime.now().toString().substring(0, 8)
          + "\n"
          + "Resolution: 720x480 \n"
          + "Terminal: ctrl-term 4.4.0 \n"
          + "==================================== \n"
          + "~> Click NEXT to proceed... ";

  public static final String instructions =
      "~> There are 4 errors in the code... \n"
          + "~> Select ALL incorrect lines... \n"
          + "==================================== \n"
          + "~> Click NEXT to proceed... ";

  public static final String correctSequence =
      "Searching sequence database... \n\n" + "Results found: 1 \n\n" + "Sequence is correct!";

  public static final String incorrectSequence =
      "Searching sequence database... \n\n" + "Results found: 0 \n\n" + "Sequence is incorrect!";

  public static final String allPuzzleSolved =
      "File debugged correctly...\n\n"
          + "Retrieving control keyboard status...\n\n"
          + "Status: accessible";

  public static final String decryptionPuzzleSolved =
      "File debugged correctly... \n\n" + "You can now exit...\n\n" + "Other tasks await...";

  public static final String emptySequence =
      "Critical error: no sequence detected! \n\n"
          + "Click a line number to create a sequence... \n\n"
          + "Analyze the code carefully...";

  public static final String wrongTabOpened =
      "Critical error: wrong tab opened... \n\n" + "Please navigate to the 'password' tab...";

  public static final String gettingHint =
      "Analyzing pseudocode... \n\n"
          + "Searching for errors... \n\n"
          + "Running code analyzer... \n\n";

  public static final double printSpeed = 0.010;

  public static boolean isInstructionsPrinted = false;
}
