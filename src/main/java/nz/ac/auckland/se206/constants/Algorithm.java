package nz.ac.auckland.se206.constants;

import nz.ac.auckland.se206.utilities.Number;

/**
 * This constants class contains the database of psuedocodes that could be randomly drawn form for the decryption puzzle. The algorithms are stored as
 * strings.
 */
public class Algorithm {
  public static final String algorithm0 =
      "1. string width = "
          + Number.getRandomNumber()
          + "; \n"
          + "2. string height = "
          + Number.getRandomNumber()
          + "; \n"
          + "3. \n"
          + "4. integer area = w * h; \n"
          + "5. \n"
          + "6. if (area > height) { \n"
          + "7.     output \"NO\"; \n"
          + "8. }";

  public static final String algorithm1 =
      "1. integer a = "
          + Number.getRandomNumber()
          + "; \n"
          + "2. string b = "
          + Number.getRandomNumber()
          + "; \n"
          + "3. integer number = random(a, a); \n"
          + "4. \n"
          + "5. if (number % 2 == 0) { \n"
          + "6.     number = number * 2; \n"
          + "7. } \n"
          + "8. \n"
          + "9. output a + b;";

  public static final String algorithm2 =
      "1. integer word = \"jumping\"; \n"
          + "2. \n"
          + "3. if (word.isVerb) { \n"
          + "4.     word = word + \"dog\"; \n"
          + "5. } else { \n"
          + "6.     word = word - \"cat\"; \n"
          + "7. } \n"
          + "8. \n"
          + "9. output cat * dog;";

  public static final String algorithm3 =
      "1. integer x = "
          + Number.getRandomNumber()
          + "; \n"
          + "2. string y = "
          + Number.getRandomNumber()
          + "; \n"
          + "3. string z = "
          + Number.getRandomNumber()
          + "; \n"
          + "4. \n"
          + "5. if (x + y + a < 50) { \n"
          + "6.     output \"GREATER THAN\"; \n"
          + "7. }";

  public static final String algorithm4 =
      "1. function checkFriendliness(AI) { \n"
          + "2.     if (AI.isFriendly) { \n"
          + "3.         AI.train(); \n"
          + "4.         output \"NOT FRIENDLY\"; \n"
          + "5.     } else { \n"
          + "6.         AI.praise(); \n"
          + "7.         output \"FRIENDLY!\"; \n"
          + "8.     } \n"
          + "9. }";

  public static final String algorithm5 =
      "1. string a = \"ant\"; \n"
          + "2. string b = \"cat\"; \n"
          + "3. integer c = \"10\"; \n"
          + "4. string d = 10; \n"
          + "5. \n"
          + "6. if (a.isInsect()) { \n"
          + "7.     output b; \n"
          + "8. } \n"
          + "9. output b + a";
}
