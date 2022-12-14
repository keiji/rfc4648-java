package dev.keiji.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Base16StreamTest {

    private static final byte[] TEST_VECTOR0_DECODED = "".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR0_ENCODED = "".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR1_DECODED = "f".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR1_ENCODED = "66".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR2_DECODED = "fo".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR2_ENCODED = "666F".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR3_DECODED = "foo".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR3_ENCODED = "666F6F".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR4_DECODED = "foob".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR4_ENCODED = "666F6F62".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR5_DECODED = "fooba".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR5_ENCODED = "666F6F6261".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR6_DECODED = "foobar".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR6_ENCODED = "666F6F626172".getBytes(StandardCharsets.US_ASCII);

    @Test
    public void encodeTest0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR0_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR0_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest1() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR1_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR1_ENCODED, baos.toByteArray());
    }


    @Test
    public void encodeTest2() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR2_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR2_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest3() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR3_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR3_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest4() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR4_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR4_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest5() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR5_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR5_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest6() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR6_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR6_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTestException0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        try {
            Base16.encode(bais, null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void encodeTestException1() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Base16.encode(null, baos);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTest0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR0_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR0_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest1() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR1_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR1_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest2() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR2_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR2_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest3() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR3_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR3_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest4() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR4_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR4_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest5() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR5_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR5_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest6() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR6_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base16.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR6_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTestException0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);

        try {
            Base16.decode(bais, null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException1() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Base16.decode(null, baos);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException2() {
        try {
            Base32.decode("666F6F62617");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException3() {
        try {
            Base16.decode("666F6F62617X");
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
