package dev.keiji.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Base64Test {

    private static byte[] TEST_VECTOR0_DECODED = "".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR0_ENCODED = "";

    private static byte[] TEST_VECTOR1_DECODED = "f".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR1_ENCODED = "Zg==";

    private static byte[] TEST_VECTOR2_DECODED = "fo".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR2_ENCODED = "Zm8=";

    private static byte[] TEST_VECTOR3_DECODED = "foo".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR3_ENCODED = "Zm9v";

    private static byte[] TEST_VECTOR4_DECODED = "foob".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR4_ENCODED = "Zm9vYg==";

    private static byte[] TEST_VECTOR5_DECODED = "fooba".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR5_ENCODED = "Zm9vYmE=";

    private static byte[] TEST_VECTOR6_DECODED = "foobar".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR6_ENCODED = "Zm9vYmFy";

    @Test
    public void encodeTest0() {
        String result = Base64.encode(TEST_VECTOR0_DECODED);
        Assertions.assertEquals(TEST_VECTOR0_ENCODED, result);
    }

    @Test
    public void encodeTest1() {
        String result = Base64.encode(TEST_VECTOR1_DECODED);
        Assertions.assertEquals(TEST_VECTOR1_ENCODED, result);
    }


    @Test
    public void encodeTest2() {
        String result = Base64.encode(TEST_VECTOR2_DECODED);
        Assertions.assertEquals(TEST_VECTOR2_ENCODED, result);
    }

    @Test
    public void encodeTest3() {
        String result = Base64.encode(TEST_VECTOR3_DECODED);
        Assertions.assertEquals(TEST_VECTOR3_ENCODED, result);
    }

    @Test
    public void encodeTest4() {
        String result = Base64.encode(TEST_VECTOR4_DECODED);
        Assertions.assertEquals(TEST_VECTOR4_ENCODED, result);
    }

    @Test
    public void encodeTest5() {
        String result = Base64.encode(TEST_VECTOR5_DECODED);
        Assertions.assertEquals(TEST_VECTOR5_ENCODED, result);
    }

    @Test
    public void encodeTest6() {
        String result = Base64.encode(TEST_VECTOR6_DECODED);
        Assertions.assertEquals(TEST_VECTOR6_ENCODED, result);
    }

    @Test
    public void encodeTestException0() {
        try {
            String result = Base64.encode(null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTest0() {
        byte[] result = Base64.decode(TEST_VECTOR0_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR0_DECODED, result);
    }

    @Test
    public void decodeTest1() {
        byte[] result = Base64.decode(TEST_VECTOR1_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR1_DECODED, result);
    }

    @Test
    public void decodeTest2() {
        byte[] result = Base64.decode(TEST_VECTOR2_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR2_DECODED, result);
    }

    @Test
    public void decodeTest3() {
        byte[] result = Base64.decode(TEST_VECTOR3_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR3_DECODED, result);
    }

    @Test
    public void decodeTest4() {
        byte[] result = Base64.decode(TEST_VECTOR4_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR4_DECODED, result);
    }

    @Test
    public void decodeTest5() {
        byte[] result = Base64.decode(TEST_VECTOR5_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR5_DECODED, result);
    }

    @Test
    public void decodeTest6() {
        byte[] result = Base64.decode(TEST_VECTOR6_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR6_DECODED, result);
    }

    private static byte[] TEST_VECTOR7_DECODED = "fooba".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR7_ENCODED = "Zm9vYmE=====";

    @Test
    public void decodeTest7() {
        byte[] result = Base64.decode(TEST_VECTOR7_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR7_DECODED, result);
    }

    private static String TEST_VECTOR_URL_SAFE_ENCODED_NO_PAD = "Sb9Zwy4aBtjQRH_5QqKG3Q7r_tkWPN4E1zUWKKPAUWsDBqlUBOSbyZ1VgIxecCZTR2D1HqyfupErvQYI7wsogiJpchNadJU1S_hXkV6CHLv-hELxQWhxNuWJrIsOEsa-mLnqczAwe7iWHH9SyHwTcUbNj3ZaigZIM-eMn9zgpzGPlzfnY4XrT6cS8abz3O6S2FhosKlbP15t17ExkQwq5Nued8BdsMOjMrE6fjU-7CDEu66k_HOpuaCUvKObH6qTf6NBVM12cQ7WvRNuxt-bRENh3kniEb_qftNYD-Gyt3b1wC-hXY4YswR9JlzHPl5bBxXGHk98iAWdAQbK67IDsQ";
    private static String TEST_VECTOR_URL_SAFE_ENCODED = "Sb9Zwy4aBtjQRH_5QqKG3Q7r_tkWPN4E1zUWKKPAUWsDBqlUBOSbyZ1VgIxecCZTR2D1HqyfupErvQYI7wsogiJpchNadJU1S_hXkV6CHLv-hELxQWhxNuWJrIsOEsa-mLnqczAwe7iWHH9SyHwTcUbNj3ZaigZIM-eMn9zgpzGPlzfnY4XrT6cS8abz3O6S2FhosKlbP15t17ExkQwq5Nued8BdsMOjMrE6fjU-7CDEu66k_HOpuaCUvKObH6qTf6NBVM12cQ7WvRNuxt-bRENh3kniEb_qftNYD-Gyt3b1wC-hXY4YswR9JlzHPl5bBxXGHk98iAWdAQbK67IDsQ==";
    private static byte[] TEST_VECTOR_URL_SAFE_DECODED = new byte[]{73, -65, 89, -61, 46, 26, 6, -40, -48, 68, 127, -7, 66, -94, -122, -35, 14, -21, -2, -39, 22, 60, -34, 4, -41, 53, 22, 40, -93, -64, 81, 107, 3, 6, -87, 84, 4, -28, -101, -55, -99, 85, -128, -116, 94, 112, 38, 83, 71, 96, -11, 30, -84, -97, -70, -111, 43, -67, 6, 8, -17, 11, 40, -126, 34, 105, 114, 19, 90, 116, -107, 53, 75, -8, 87, -111, 94, -126, 28, -69, -2, -124, 66, -15, 65, 104, 113, 54, -27, -119, -84, -117, 14, 18, -58, -66, -104, -71, -22, 115, 48, 48, 123, -72, -106, 28, 127, 82, -56, 124, 19, 113, 70, -51, -113, 118, 90, -118, 6, 72, 51, -25, -116, -97, -36, -32, -89, 49, -113, -105, 55, -25, 99, -123, -21, 79, -89, 18, -15, -90, -13, -36, -18, -110, -40, 88, 104, -80, -87, 91, 63, 94, 109, -41, -79, 49, -111, 12, 42, -28, -37, -98, 119, -64, 93, -80, -61, -93, 50, -79, 58, 126, 53, 62, -20, 32, -60, -69, -82, -92, -4, 115, -87, -71, -96, -108, -68, -93, -101, 31, -86, -109, 127, -93, 65, 84, -51, 118, 113, 14, -42, -67, 19, 110, -58, -33, -101, 68, 67, 97, -34, 73, -30, 17, -65, -22, 126, -45, 88, 15, -31, -78, -73, 118, -11, -64, 47, -95, 93, -114, 24, -77, 4, 125, 38, 92, -57, 62, 94, 91, 7, 21, -58, 30, 79, 124, -120, 5, -99, 1, 6, -54, -21, -78, 3, -79};

    @Test
    public void decodeUrlSafeTest() {
        byte[] result = Base64.decodeUrlSafe(TEST_VECTOR_URL_SAFE_ENCODED_NO_PAD);
        Assertions.assertArrayEquals(TEST_VECTOR_URL_SAFE_DECODED, result);
    }

    @Test
    public void encodeUrlSafeTest() {
        String result = Base64.encodeUrlSafe(TEST_VECTOR_URL_SAFE_DECODED);
        Assertions.assertEquals(TEST_VECTOR_URL_SAFE_ENCODED, result);
    }

    @Test
    public void decodeTestException0() {
        try {
            byte[] result = Base64.decode(null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException1() {
        try {
            byte[] result = Base64.decode("Zm9vYmE==");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException2() {
        try {
            byte[] result = Base64.decode("Zm9vYm.=");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void defaultRandomEncodeTest() {
        Random rand = new Random();

        for (int i = 0; i < 1024; i++) {
            byte[] testData = new byte[rand.nextInt(1024)];
            rand.nextBytes(testData);

            byte[] expectedResult = java.util.Base64.getEncoder().encode(testData);
            byte[] actualResult = Base64.encode(testData).getBytes(StandardCharsets.US_ASCII);
            Assertions.assertArrayEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void urlSafeRandomEncodeTest() {
        Random rand = new Random();

        for (int i = 0; i < 1024; i++) {
            byte[] testData = new byte[rand.nextInt(1024)];
            rand.nextBytes(testData);

            byte[] expectedResult = java.util.Base64.getUrlEncoder().encode(testData);
            byte[] actualResult = Base64.encodeUrlSafe(testData).getBytes(StandardCharsets.US_ASCII);
            Assertions.assertArrayEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void defaultRandomDecodeTest() {
        Random rand = new Random();

        for (int i = 0; i < 1024; i++) {
            byte[] testData = new byte[rand.nextInt(1024)];
            rand.nextBytes(testData);

            byte[] encoded = java.util.Base64.getEncoder().encode(testData);
            byte[] actualResult = Base64.decode(new String(encoded, StandardCharsets.US_ASCII));
            Assertions.assertArrayEquals(testData, actualResult);
        }
    }

    @Test
    public void urlSafeRandomDecodeTest() {
        Random rand = new Random();

        for (int i = 0; i < 1024; i++) {
            byte[] testData = new byte[rand.nextInt(1024)];
            rand.nextBytes(testData);

            byte[] encoded = java.util.Base64.getUrlEncoder().encode(testData);
            byte[] actualResult = Base64.decodeUrlSafe(new String(encoded, StandardCharsets.US_ASCII));
            Assertions.assertArrayEquals(testData, actualResult);
        }
    }
}
