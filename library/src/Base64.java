import java.io.ByteArrayInputStream;

public class Base64 {

    private Base64() {
    }

    private static final int BUCKET_SIZE = 3;

    private static final int BIT_WIDTH = 6;
    private static final int BIT_MASK = 0x3F;

    private static final int INTEGER_LENGTH_IN_BYTES = 4;

    private static final char PAD = '=';

    private static final char[] TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/',
    };

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
                result.append(TABLE[index]);
            }

            for (int i = 0; i < padLength; i++) {
                result.append(PAD);
            }

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
}
