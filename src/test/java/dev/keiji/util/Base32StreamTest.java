package dev.keiji.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Base32StreamTest {

    private static final byte[] TEST_VECTOR0_DECODED = "".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR0_ENCODED = "".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR1_DECODED = "f".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR1_ENCODED = "MY======".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR2_DECODED = "fo".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR2_ENCODED = "MZXQ====".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR3_DECODED = "foo".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR3_ENCODED = "MZXW6===".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR4_DECODED = "foob".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR4_ENCODED = "MZXW6YQ=".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR5_DECODED = "fooba".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR5_ENCODED = "MZXW6YTB".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] TEST_VECTOR6_DECODED = "foobar".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR6_ENCODED = "MZXW6YTBOI======".getBytes(StandardCharsets.US_ASCII);

    @Test
    public void encodeTest0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR0_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR0_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest1() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR1_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR1_ENCODED, baos.toByteArray());
    }


    @Test
    public void encodeTest2() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR2_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR2_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest3() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR3_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR3_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest4() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR4_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR4_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest5() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR5_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR5_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTest6() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR6_DECODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.encode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR6_ENCODED, baos.toByteArray());
    }

    @Test
    public void encodeTestException0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        try {
            Base32.encode(bais, null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void encodeTestException1() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Base32.encode(null, baos);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTest0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR0_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR0_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest1() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR1_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR1_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest2() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR2_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR2_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest3() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR3_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR3_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest4() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR4_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR4_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest5() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR5_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR5_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTest6() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR6_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR6_DECODED, baos.toByteArray());
    }

    private static final byte[] TEST_VECTOR7_DECODED = "foobar".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] TEST_VECTOR7_ENCODED = "MZXW6YTBOI======================".getBytes(StandardCharsets.US_ASCII);

    @Test
    public void decodeTest7() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_VECTOR7_ENCODED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base32.decode(bais, baos);
        Assertions.assertArrayEquals(TEST_VECTOR7_DECODED, baos.toByteArray());
    }

    @Test
    public void decodeTestException0() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        try {
            Base32.decode(bais, null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException1() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Base32.decode(null, baos);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void decodeTestException2() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream("MZXW6YTB.OI=====".getBytes(StandardCharsets.US_ASCII));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Base32.decode(bais, baos);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
