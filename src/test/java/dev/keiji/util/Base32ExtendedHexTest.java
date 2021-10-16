package dev.keiji.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class Base32ExtendedHexTest {

    private static byte[] TEST_VECTOR0_DECODED = "".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR0_ENCODED = "";

    private static byte[] TEST_VECTOR1_DECODED = "f".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR1_ENCODED = "CO======";

    private static byte[] TEST_VECTOR2_DECODED = "fo".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR2_ENCODED = "CPNG====";

    private static byte[] TEST_VECTOR3_DECODED = "foo".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR3_ENCODED = "CPNMU===";

    private static byte[] TEST_VECTOR4_DECODED = "foob".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR4_ENCODED = "CPNMUOG=";

    private static byte[] TEST_VECTOR5_DECODED = "fooba".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR5_ENCODED = "CPNMUOJ1";

    private static byte[] TEST_VECTOR6_DECODED = "foobar".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR6_ENCODED = "CPNMUOJ1E8======";

    @Test
    public void encodeTest0() {
        String result = Base32.encodeExtendedHex(TEST_VECTOR0_DECODED);
        Assertions.assertEquals(TEST_VECTOR0_ENCODED, result);
    }

    @Test
    public void encodeTest1() {
        String result = Base32.encodeExtendedHex(TEST_VECTOR1_DECODED);
        Assertions.assertEquals(TEST_VECTOR1_ENCODED, result);
    }


    @Test
    public void encodeTest2() {
        String result = Base32.encodeExtendedHex(TEST_VECTOR2_DECODED);
        Assertions.assertEquals(TEST_VECTOR2_ENCODED, result);
    }

    @Test
    public void encodeTest3() {
        String result = Base32.encodeExtendedHex(TEST_VECTOR3_DECODED);
        Assertions.assertEquals(TEST_VECTOR3_ENCODED, result);
    }

    @Test
    public void encodeTest4() {
        String result = Base32.encodeExtendedHex(TEST_VECTOR4_DECODED);
        Assertions.assertEquals(TEST_VECTOR4_ENCODED, result);
    }

    @Test
    public void encodeTest5() {
        String result = Base32.encodeExtendedHex(TEST_VECTOR5_DECODED);
        Assertions.assertEquals(TEST_VECTOR5_ENCODED, result);
    }

    @Test
    public void encodeTest6() {
        String result = Base32.encodeExtendedHex(TEST_VECTOR6_DECODED);
        Assertions.assertEquals(TEST_VECTOR6_ENCODED, result);
    }

    @Test
    public void encodeTestException0() {
        try {
            String result = Base32.encodeExtendedHex(null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTest0() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR0_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR0_DECODED, result);
    }

    @Test
    public void decodeTest1() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR1_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR1_DECODED, result);
    }

    @Test
    public void decodeTest2() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR2_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR2_DECODED, result);
    }

    @Test
    public void decodeTest3() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR3_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR3_DECODED, result);
    }

    @Test
    public void decodeTest4() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR4_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR4_DECODED, result);
    }

    @Test
    public void decodeTest5() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR5_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR5_DECODED, result);
    }

    @Test
    public void decodeTest6() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR6_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR6_DECODED, result);
    }

    private static byte[] TEST_VECTOR7_DECODED = "foobar".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR7_ENCODED = "CPNMUOJ1E8======================";

    @Test
    public void decodeTest7() {
        byte[] result = Base32.decodeExtendedHex(TEST_VECTOR7_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR7_DECODED, result);
    }

    @Test
    public void decodeTestException0() {
        try {
            byte[] result = Base32.decodeExtendedHex(null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException1() {
        try {
            byte[] result = Base32.decodeExtendedHex("CPNMUOJ1E8=======");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException2() {
        try {
            byte[] result = Base32.decodeExtendedHex("CPNMUOJ.E8======");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
