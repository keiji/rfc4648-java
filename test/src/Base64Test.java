import com.sun.tools.corba.se.idl.InvalidArgument;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

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
        Assert.assertEquals(TEST_VECTOR0_ENCODED, result);
    }

    @Test
    public void encodeTest1() {
        String result = Base64.encode(TEST_VECTOR1_DECODED);
        Assert.assertEquals(TEST_VECTOR1_ENCODED, result);
    }


    @Test
    public void encodeTest2() {
        String result = Base64.encode(TEST_VECTOR2_DECODED);
        Assert.assertEquals(TEST_VECTOR2_ENCODED, result);
    }

    @Test
    public void encodeTest3() {
        String result = Base64.encode(TEST_VECTOR3_DECODED);
        Assert.assertEquals(TEST_VECTOR3_ENCODED, result);
    }

    @Test
    public void encodeTest4() {
        String result = Base64.encode(TEST_VECTOR4_DECODED);
        Assert.assertEquals(TEST_VECTOR4_ENCODED, result);
    }

    @Test
    public void encodeTest5() {
        String result = Base64.encode(TEST_VECTOR5_DECODED);
        Assert.assertEquals(TEST_VECTOR5_ENCODED, result);
    }

    @Test
    public void encodeTest6() {
        String result = Base64.encode(TEST_VECTOR6_DECODED);
        Assert.assertEquals(TEST_VECTOR6_ENCODED, result);
    }

    @Test
    public void decodeTest0() {
        byte[] result = Base64.decode(TEST_VECTOR0_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR0_DECODED, result);
    }

    @Test
    public void decodeTest1() {
        byte[] result = Base64.decode(TEST_VECTOR1_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR1_DECODED, result);
    }

    @Test
    public void decodeTest2() {
        byte[] result = Base64.decode(TEST_VECTOR2_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR2_DECODED, result);
    }

    @Test
    public void decodeTest3() {
        byte[] result = Base64.decode(TEST_VECTOR3_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR3_DECODED, result);
    }

    @Test
    public void decodeTest4() {
        byte[] result = Base64.decode(TEST_VECTOR4_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR4_DECODED, result);
    }

    @Test
    public void decodeTest5() {
        byte[] result = Base64.decode(TEST_VECTOR5_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR5_DECODED, result);
    }

    @Test
    public void decodeTest6() {
        byte[] result = Base64.decode(TEST_VECTOR6_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR6_DECODED, result);
    }

    private static byte[] TEST_VECTOR7_DECODED = "fooba".getBytes(StandardCharsets.US_ASCII);
    private static String TEST_VECTOR7_ENCODED = "Zm9vYmE=====";

    @Test
    public void decodeTest7() {
        byte[] result = Base64.decode(TEST_VECTOR7_ENCODED);
        Assert.assertArrayEquals(TEST_VECTOR7_DECODED, result);
    }

    @Test
    public void decodeTestException() {
        try {
            byte[] result = Base64.decode("Zm9vYmE==");
            Assert.fail();
        } catch (IllegalArgumentException exception) {
        }
    }
}
