import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Base64 {

    private Base64() {
    }

    private static final int BUCKET_SIZE = 3;

    private static final int BIT_WIDTH = 6;
    private static final int BIT_MASK = 0x3F;

    private static final int INTEGER_LENGTH_IN_BYTES = 4;

    private static final int MAX_PAD_LENGTH = 2;

    private static final char PAD = '=';

    private static final char[] TABLE_ENCODE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/',
    };

    private static final HashMap<Character, Integer> MAP_DECODE = new HashMap();

    static {
        // Ignore 0
        MAP_DECODE.put((char) 0, 0);

        // A -> Z
        for (char c = 'A'; c <= 'Z'; c++) {
            MAP_DECODE.put(c, c - 'A');
        }

        // a -> z
        for (char c = 'a'; c <= 'z'; c++) {
            MAP_DECODE.put(c, 26 + (c - 'a'));
        }

        // 0 -> 9
        for (char c = '0'; c <= '9'; c++) {
            MAP_DECODE.put(c, 52 + (c - '0'));
        }

        MAP_DECODE.put('+', 62);
        MAP_DECODE.put('/', 63);
    }

    public static String encode(byte[] byteArray) {
        StringBuilder result = new StringBuilder();

        byte[] bucket = new byte[INTEGER_LENGTH_IN_BYTES];

        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

        int len;
        while ((len = bis.read(bucket, 0, BUCKET_SIZE)) > 0) {
            int padLength = 0;
            if (len < BUCKET_SIZE) {
                padLength = BUCKET_SIZE - len;
            }

            int value = getIntFromBucket(bucket);

            int targetLength = INTEGER_LENGTH_IN_BYTES - padLength;
            for (int i = 0; i < targetLength; i++) {
                int shift = BIT_WIDTH * (INTEGER_LENGTH_IN_BYTES - 1 - i);
                int mask = BIT_MASK << shift;
                int index = (value & mask) >>> shift;
                result.append(TABLE_ENCODE[index]);
            }

            for (int i = 0; i < padLength; i++) {
                result.append(PAD);
            }

            // Clear bucket.
            for (int i = 0; i < bucket.length - 1; i++) {
                bucket[i] = 0;
            }
        }

        return result.toString();
    }

    private static int getIntFromBucket(byte[] bucket) {
        return (
                byteToInt(bucket[0]) << 16)
                + (byteToInt(bucket[1]) << 8)
                + (byteToInt(bucket[2])
        );
    }

    private static int byteToInt(byte bucketValue) {
        return ((int) bucketValue) & 0xFF;
    }

    public static byte[] decode(String decoded) {
        if (decoded == null || decoded.length() == 0) {
            return new byte[0];
        }

        int padLength = 0;
        if (decoded.indexOf(PAD) >= 0) {
            padLength = decoded.length() - decoded.indexOf(PAD);
        }

        int resultLength = (decoded.length() * BIT_WIDTH) / 8 - padLength;
        ByteBuffer bb = ByteBuffer.allocate(resultLength)
                .order(ByteOrder.LITTLE_ENDIAN);

        String padStrippedStr = decoded.substring(0, decoded.length() - padLength);

        byte[] bucket = new byte[BUCKET_SIZE];

        byte[] byteArray = padStrippedStr.getBytes(StandardCharsets.US_ASCII);
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

        byte[] chars = new byte[INTEGER_LENGTH_IN_BYTES];

        while ((bis.read(chars, 0, INTEGER_LENGTH_IN_BYTES)) > 0) {
            boolean isLastBucket = bis.available() == 0;

            int bucketValue0 = MAP_DECODE.get(intToChar(chars[0]));
            int bucketValue1 = MAP_DECODE.get(intToChar(chars[1]));
            int bucketValue2 = MAP_DECODE.get(intToChar(chars[2]));
            int bucketValue3 = MAP_DECODE.get(intToChar(chars[3]));

            int value = (
                    (bucketValue0 << 18)
                            + (bucketValue1 << 12)
                            + (bucketValue2 << 6)
                            + (bucketValue3)
            );

            readFromValue(value, bucket);
            bb.put(bucket, 0, isLastBucket ? BUCKET_SIZE - padLength : BUCKET_SIZE);

            for (int i = 0; i < chars.length - 1; i++) {
                chars[i] = 0;
            }
        }

        byte[] bytesArray = bb.array();
        bb.clear();

        return bytesArray;
    }

    private static void readFromValue(int value, byte[] bucket) {
        bucket[0] = (byte) ((value & (0XFF << 16)) >>> 16);
        bucket[1] = (byte) ((value & (0XFF << 8)) >>> 8);
        bucket[2] = (byte) ((value & 0XFF));
    }

    private static char intToChar(byte byteValue) {
        return (char) (((char) byteValue) & 0xFF);
    }
}
