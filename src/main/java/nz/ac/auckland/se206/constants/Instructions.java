package nz.ac.auckland.se206.constants;

import java.time.LocalDate;
import java.time.LocalTime;

public class Instructions {
  public static final String instruction =
      "Copyright (C) Evil Corporations \n"
          + "===================================================== \n"
          + "OS: AIOS \n"
          + "Date: "
          + LocalDate.now()
          + "\n"
          + "Time: "
          + LocalTime.now().toString().substring(0, 8)
          + "\n"
          + "Resolution: 720x480 \n"
          + "Terminal: CONTROL \n"
          + "===================================================== \n"
          + "> Running process: password.pscode... \n"
          + "> Retrieving password... \n"
          + "> Critical error! Password is corrupted! \n"
          + "> Resolve errors in password.pscode to proceed! \n"
          + "===================================================== \n"
          + "> Awaiting user response... ";

  public static final double printSpeed = 0.025;
}
