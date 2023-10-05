package nz.ac.auckland.se206.constants;

import java.time.LocalDate;
import java.time.LocalTime;

public class Instructions {
  public static final String bootup =
      "Copyright (C) Evil Corporations \n"
          + "================================================ \n"
          + "OS: "
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
          + "================================================ \n"
          + "~> Running process: password file \n"
          + "~> Retrieving password... \n"
          + "~> Critical error! Password file is corrupted! \n"
          + "~> Resolve ALL errors in password file! \n"
          + "================================================ \n"
          + "~> Awaiting user response... ";

  public static final String instructions =
      "Copyright (C) Evil Corporations \n"
          + "================================================ \n"
          + "~> Analyzing password file... \n"
          + "~> Errors detected: 4 \n"
          + "================================================ \n"
          + "INSTRUCTIONS \n"
          + "================================================ \n"
          + "~> Detect ALL errors in the code... \n"
          + "~> Select ALL incorrect lines... \n"
          + "~> Sequence is read from TOP TO BOTTOM... \n"
          + "================================================ \n"
          + "~> Awaiting user response... ";

  public static final String correctSequence =
      "Searching sequence database... \n\n" + "Results found: 1 \n\n" + "Sequence is correct!";

  public static final String incorrectSequence =
      "Searching sequence database... \n\n" + "Results found: 0 \n\n" + "Sequence is incorrect!";

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
