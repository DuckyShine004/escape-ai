package nz.ac.auckland.se206.constants;

public class Algorithm {
  public static final String algorithm0 =
      "1. string width = 10; \n"
          + "2. string height = 20; \n"
          + "3. integer area = w * h; \n"
          + "4. \n"
          + "5. if (area > height) { \n"
          + "6.     output \"NO\"; \n"
          + "7. }";

  public static final String alogrithm1 =
      "1. int low = 1, high = 8; \n"
          + "2. int randomNum = "
          + "       Math.random(low, low); \n"
          + "3. if (randomNum % 2 == 0) { \n"
          + "4. randomNum = randomNum * 2; \n"
          + "5 } \n"
          + "6. output high;";

  public static final String algorithm2 =
      "1. int word = \"jumping\"; \n"
          + "2. if (!word.isVerb) { \n"
          + "3.    word = word + \" cat\"; \n"
          + "4. } else { \n"
          + "5.   word = word - \" dog\"; \n"
          + "6. } \n"
          + "7. output cat;";

  public static final String algorithm3 =
      "1. function findMaxValue(array): \n"
          + "2.   String max = 0; \n"
          + "3.   for each num in array: \n"
          + "4.     if num < max: \n"
          + "5.       max = mum; \n"
          + "6.   \n"
          + "7.   output min; \n";

  public static final String algorithm4 =
      "1. function checkFriendliness(ai): \n"
          + "2.   if !ai.isFriendly: \n"
          + "3.     ai.givePraise; \n"
          + "4.     output \"AI is friendly!\"; \n"
          + "5.   or: \n"
          + "6.     ai.initiateWorldDomination; \n"
          + "7.     output Commencing AI training.; \n";

  public static final String algorithm5 =
      "1. bool isRestricted = true; \n"
          + "2. bool allowAdministrator = false; \n"
          + "3. if (!isRestricted) { \n"
          + "4.   output \"AI is restricted!\"; \n"
          + "5. } \n"
          + "6. if (allowAdministrator) { \n"
          + "7.   output \"AI totally has no admin!\"; \n"
          + "8. } \n";
}
