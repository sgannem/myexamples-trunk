package com.xyz;

/**
 * Utility methods.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@SuppressWarnings("MagicNumber")
public class Utils {

  private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
  private static final int RADIX_HEX = 16;

  private static final String PARAMETER_NAME_STRING = "parameterName";
  private static final String EXCEPTION_MESSAGE_PARAMETER_NOT_NULL = "The parameter %s must not be null.";
  private static final String EXCEPTION_MESSAGE_PARAMETER_NOT_SET = "The parameter %s is not set.";
  private static final String EXCEPTION_MESSAGE_PARAMETER_NEGATIVE = "The parameter %s must not be negative.";
  private static final String EXCEPTION_MESSAGE_LENGTH_AND_OFFSET_MUST_NOT_EXCEED_ARRAY_LENGTH = "The value (offset + length) must not "
      + "exceed the length of the array.";


  private Utils() {
    // empty
  }

  /**
   * Converts an array of bytes into a {@link String} representing the hexadecimal values of each byte in order.<br> <i>Note</i>:
   * Implementation took from <i>Apache commons</i>.
   *
   * @param array a byte[] to convert to Hex characters
   * @return a {@link String} containing hexadecimal representation
   */
  public static String getHexString(final byte... array) {
    final int length = array.length;
    final char[] out = new char[length << 1];

    for (int i = 0, j = 0; i < length; i++, j += 2) {
      out[j] = DIGITS_UPPER[(0xF0 & array[i]) >>> 4];
      out[j + 1] = DIGITS_UPPER[0x0F & array[i]];
    }

    return String.valueOf(out);
  }

  /**
   * Converts an array of bytes into a {@link String} representing the hexadecimal values of each byte in order.<br> <i>Note</i>:
   * Implementation took from <i>Apache commons</i>.
   *
   * @param array  a byte[] to convert to Hex characters
   * @param offset offset within the byte[]
   * @param length length to be converted
   * @return a {@link String} containing hexadecimal representation
   */
  public static String getHexString(final byte[] array, final int offset, final int length) {
    // sanity checks
    checkParameterNotNull(array, "array");
    checkParameterNotNegative(offset, "offset");
    checkParameterNotNegative(length, "length");

    if (array.length < (offset + length)) {
      throw new IllegalArgumentException(EXCEPTION_MESSAGE_LENGTH_AND_OFFSET_MUST_NOT_EXCEED_ARRAY_LENGTH);
    }

    final char[] out = new char[length << 1];

    for (int i = offset, j = 0; i < (offset + length); i++, j += 2) {
      out[j] = DIGITS_UPPER[(0xF0 & array[i]) >>> 4];
      out[j + 1] = DIGITS_UPPER[0x0F & array[i]];
    }

    return String.valueOf(out);
  }

  /**
   * Returns a byte[] of the passed hex string.
   *
   * @param hexStr hex string to convert
   * @return byte[] representation of the hex string
   */
  public static byte[] getByteArray(final String hexStr) {
    if ((hexStr.length() % 2) != 0) {
      throw new IllegalArgumentException("the length of the parameter 'hexStr' must be a multiple of 2");
    }

    final byte[] bArray = new byte[hexStr.length() / 2];

    for (int i = 0; i < (hexStr.length() / 2); i++) {
      int off = 2 * i;

      final byte firstNibble = Byte.parseByte(hexStr.substring(off, ++off), RADIX_HEX); // [x,y]
      final byte secondNibble = Byte.parseByte(hexStr.substring(off, ++off), RADIX_HEX);

      final int finalByte = (secondNibble) | (firstNibble << 4);
      bArray[i] = (byte) finalByte;
    }
    return bArray;
  }

  /**
   * Method to create a byte array with 2 elements out of an integer value.
   *
   * @param param integer value to be packed into a byte array
   * @return byte array with the values of the integer.
   */
  public static byte[] getTwoBytesArray(final int param) {
    final byte[] result = new byte[2];
    result[0] = (byte) ((param >> 8) & 0xFF);
    result[1] = (byte) (param & 0xFF);
    return result;
  }

  /**
   * Method to create a byte array with 3 elements out of an integer value.
   *
   * @param param integer value to be packed into a byte array
   * @return byte array with the values of the integer.
   */
  public static byte[] getThreeBytesArray(final int param) {
    final byte[] result = new byte[3];
    result[0] = (byte) ((param >> 16) & 0xFF);
    result[1] = (byte) ((param >> 8) & 0xFF);
    result[2] = (byte) (param & 0xFF);
    return result;
  }

  /**
   * This method inverts the byte order of the passed {@code byte[]}. E.g. when passing 0x8be9edb5, the result will be 0xb5ede98b.
   *
   * @param data data bytes to be flipped
   * @return the flipped data bytes
   */
  public static byte[] flipByteOrder(final byte[] data) {
    final byte[] result = new byte[data.length];

    for (int i = 0; i < data.length; i++) {
      result[i] = data[data.length - 1 - i];
    }

    return result;
  }

  /**
   * Returns the current time in milli seconds.
   *
   * @return current time
   */
  public static long now() {
    return System.currentTimeMillis();
  }

  /**
   * This method compares the content of two byte arrays. It returns {@code true} if, and only if the arrays are not {@code null} and the
   * contents beginning at the specified offsets are equal for the specified length.
   *
   * @param a1       first array
   * @param offsetA1 offset in the first array
   * @param a2       second array
   * @param offsetA2 offset in the second array
   * @param length   length to compare over
   * @return {@code true} if the compared part is equal, otherwise {@code false}
   */
  public static boolean arrayCompare(final byte[] a1, final int offsetA1, final byte[] a2, final int offsetA2, final int length) {
    if (areArrayCompareParametersInvalid(a1, a2, offsetA1, offsetA2, length)) {
      return false;
    }

    for (int i = 0; i < length; i++) {
      if (a1[offsetA1 + i] != a2[offsetA2 + i]) {
        return false;
      }
    }

    return true;
  }

  @SuppressWarnings("squid:MethodCyclomaticComplexity")
  private static boolean areArrayCompareParametersInvalid(final byte[] a1, final byte[] a2, final int offsetA1, final int offsetA2, final
  int length) {
    if ((a1 == null) || (a2 == null)) {
      return true;
    }

    if ((offsetA1 < 0) || (offsetA2 < 0) || (length < 0)) {
      return true;
    }

    return areOffsetsExceedingTotalLen(a1, a2, offsetA1, offsetA2, length);
  }

  private static boolean areOffsetsExceedingTotalLen(final byte[] a1, final byte[] a2, final int offsetA1, final int offsetA2, final int
      length) {
    final boolean offsetA1ExceedsTotalLen = offsetA1 >= a1.length;
    final boolean offsetA2ExceedsTotalLen = offsetA2 >= a2.length;

    if (offsetA1ExceedsTotalLen || offsetA2ExceedsTotalLen) {
      return true;
    }

    final boolean offsetA1PlusLenExceedsTotalLen = (offsetA1 + length) > a1.length;
    final boolean offsetA2PlusLenExceedsTotalLen = (offsetA2 + length) > a2.length;

    return offsetA1PlusLenExceedsTotalLen || offsetA2PlusLenExceedsTotalLen;
  }

  public static boolean hasApduStatusWord(final byte[] apdu, final byte[] statusWord) {
    checkParameterNotNull(apdu, "apdu");
    checkParameterNotNull(statusWord, "statusWord");

    assert (apdu.length >= 2);
    assert (statusWord.length == 2);

    return arrayCompare(apdu, apdu.length - 2, statusWord, 0, 2);
  }

  /**
   * If {@code parameter} is {@code null}, a {@link IllegalArgumentException} is thrown.
   *
   * @param parameterValue the parameter value
   * @param parameterName  the name of the parameter
   * @param <T>            the type of the parameter value and the return value
   * @return if no exception is thrown, {@code parameterValue} is returned
   * @throws IllegalArgumentException if parameter is {@code null}
   */
  public static <T> T checkParameterNotNull(final T parameterValue, final String parameterName) throws IllegalArgumentException {
    if (parameterName == null) {
      throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE_PARAMETER_NOT_NULL, PARAMETER_NAME_STRING));
    }

    if (parameterValue == null) {
      throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE_PARAMETER_NOT_SET, parameterName));
    }

    return parameterValue;
  }

  /**
   * If {@code parameterValue} is {@code negative}, an {@link IllegalArgumentException} is thrown.
   *
   * @param parameterValue the parameter value
   * @param parameterName  the name of the parameter
   * @return if no exception is thrown, {@code parameterValue} is returned
   * @throws IllegalArgumentException if parameter is negative
   */
  public static int checkParameterNotNegative(final int parameterValue, final String parameterName) throws IllegalArgumentException {
    if (parameterName == null) {
      throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE_PARAMETER_NOT_NULL, PARAMETER_NAME_STRING));
    }

    if (parameterValue < 0) {
      throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE_PARAMETER_NEGATIVE, PARAMETER_NAME_STRING));
    }

    return parameterValue;
  }
}
