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
