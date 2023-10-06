package nz.ac.auckland.se206.utilities;

import java.util.Random;

public class Number {
  private static final int low = 0;
  private static final int high = 100;

  private static final Random randomNumberGenerator = new Random();

  /**
   * Generate and return a random number between low and high, where the low and high values are
   * constant. This is used for the pseudocode algorithms.
   *
   * @return a random number between low and high.
   */
  public static int getRandomNumber() {
    return getRandomNumber(low, high);
  }

  /**
   * Generate and return a random number between the input low and high range. High is excluded from
   * the range.
   *
   * @param low the minimum range.
   * @param high the maximum range.
   * @return a random number between low and high.
   */
  public static int getRandomNumber(int low, int high) {
    return randomNumberGenerator.nextInt(high - low) + low;
  }
}
