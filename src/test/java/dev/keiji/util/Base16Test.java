package dev.keiji.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class Base16Test {

    private static final byte[] TEST_VECTOR0_DECODED = "".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_VECTOR0_ENCODED = "";

    private static final byte[] TEST_VECTOR1_DECODED = "f".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_VECTOR1_ENCODED = "66";

    private static final byte[] TEST_VECTOR2_DECODED = "fo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_VECTOR2_ENCODED = "666F";

    private static final byte[] TEST_VECTOR3_DECODED = "foo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_VECTOR3_ENCODED = "666F6F";

    private static final byte[] TEST_VECTOR4_DECODED = "foob".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_VECTOR4_ENCODED = "666F6F62";

    private static final byte[] TEST_VECTOR5_DECODED = "fooba".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_VECTOR5_ENCODED = "666F6F6261";

    private static final byte[] TEST_VECTOR6_DECODED = "foobar".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_VECTOR6_ENCODED = "666F6F626172";

    @Test
    public void encodeTest0() {
        String result = Base16.encode(TEST_VECTOR0_DECODED);
        Assertions.assertEquals(TEST_VECTOR0_ENCODED, result);
    }

    @Test
    public void encodeTest1() {
        String result = Base16.encode(TEST_VECTOR1_DECODED);
        Assertions.assertEquals(TEST_VECTOR1_ENCODED, result);
    }


    @Test
    public void encodeTest2() {
        String result = Base16.encode(TEST_VECTOR2_DECODED);
        Assertions.assertEquals(TEST_VECTOR2_ENCODED, result);
    }

    @Test
    public void encodeTest3() {
        String result = Base16.encode(TEST_VECTOR3_DECODED);
        Assertions.assertEquals(TEST_VECTOR3_ENCODED, result);
    }

    @Test
    public void encodeTest4() {
        String result = Base16.encode(TEST_VECTOR4_DECODED);
        Assertions.assertEquals(TEST_VECTOR4_ENCODED, result);
    }

    @Test
    public void encodeTest5() {
        String result = Base16.encode(TEST_VECTOR5_DECODED);
        Assertions.assertEquals(TEST_VECTOR5_ENCODED, result);
    }

    @Test
    public void encodeTest6() {
        String result = Base16.encode(TEST_VECTOR6_DECODED);
        Assertions.assertEquals(TEST_VECTOR6_ENCODED, result);
    }

    @Test
    public void encodeTestException0() {
        try {
            String result = Base16.encode(null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTest0() {
        byte[] result = Base16.decode(TEST_VECTOR0_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR0_DECODED, result);
    }

    @Test
    public void decodeTest1() {
        byte[] result = Base16.decode(TEST_VECTOR1_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR1_DECODED, result);
    }

    @Test
    public void decodeTest2() {
        byte[] result = Base16.decode(TEST_VECTOR2_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR2_DECODED, result);
    }

    @Test
    public void decodeTest3() {
        byte[] result = Base16.decode(TEST_VECTOR3_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR3_DECODED, result);
    }

    @Test
    public void decodeTest4() {
        byte[] result = Base16.decode(TEST_VECTOR4_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR4_DECODED, result);
    }

    @Test
    public void decodeTest5() {
        byte[] result = Base16.decode(TEST_VECTOR5_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR5_DECODED, result);
    }

    @Test
    public void decodeTest6() {
        byte[] result = Base16.decode(TEST_VECTOR6_ENCODED);
        Assertions.assertArrayEquals(TEST_VECTOR6_DECODED, result);
    }

    @Test
    public void decodeTestException0() {
        try {
            byte[] result = Base16.decode(null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException1() {
        try {
            byte[] result = Base32.decode("666F6F62617");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException2() {
        try {
            byte[] result = Base16.decode("666F6F62617X");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
