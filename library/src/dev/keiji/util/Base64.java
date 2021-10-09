package dev.keiji.util;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Base64 {

    private Base64() {
    }

    private static final int BUCKET_SIZE = 3;

    private static final int BIT_WIDTH = 6;
    private static final int BIT_MASK = 0x3F; // = 00111111

    private static final int INTEGER_LENGTH_IN_BYTES = 4;

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

    private static final char[] TABLE_ENCODE_URL_SAFE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '-', '_',
    };

    private static final int[] TABLE_DECODE = new int['z' + 1];
    private static final int[] TABLE_DECODE_URL_SAFE = new int['z' + 1];

    static {
        // Initialize
        for (int i = 0; i < TABLE_DECODE.length; i++) {
            TABLE_DECODE[i] = -1;
            TABLE_DECODE_URL_SAFE[i] = -1;
        }

        // A -> Z
        for (char c = 'A'; c <= 'Z'; c++) {
            TABLE_DECODE[c] = c - 'A';
            TABLE_DECODE_URL_SAFE[c] = c - 'A';
        }

        // a -> z
        for (char c = 'a'; c <= 'z'; c++) {
            TABLE_DECODE[c] = 26 + (c - 'a');
            TABLE_DECODE_URL_SAFE[c] = 26 + (c - 'a');
        }

        // 0 -> 9
        for (char c = '0'; c <= '9'; c++) {
            TABLE_DECODE[c] = 52 + (c - '0');
            TABLE_DECODE_URL_SAFE[c] = 52 + (c - '0');
        }

        TABLE_DECODE['+'] = 62;
        TABLE_DECODE['/'] = 63;

        TABLE_DECODE_URL_SAFE['-'] = 62;
        TABLE_DECODE_URL_SAFE['_'] = 63;
    }

    public static String encode(byte[] byteArray) {
        return Encoder.encode(byteArray, TABLE_ENCODE);
    }

    public static String encodeUrlSafe(byte[] byteArray) {
        return Encoder.encode(byteArray, TABLE_ENCODE_URL_SAFE);
    }

    public static byte[] decode(String encoded) {
        return Decoder.decode(encoded, TABLE_DECODE);
    }

    public static byte[] decodeUrlSafe(String encoded) {
        return Decoder.decode(encoded, TABLE_DECODE_URL_SAFE);
    }

    private static class Encoder {

        public static String encode(byte[] byteArray, char[] tableEncode) {
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
                    result.append(tableEncode[index]);
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
    }

    private static class Decoder {

        public static byte[] decode(String encoded, int[] tableDecode) {
            if (encoded == null || encoded.length() == 0) {
                return new byte[0];
            }
            if (encoded.length() % 4 != 0) {
                throw new IllegalArgumentException("Decoded string length must be divisible by 4.");
            }

            int padLength = 0;
            if (encoded.indexOf(PAD) >= 0) {
                padLength = encoded.length() - encoded.indexOf(PAD);
            }

            String padStrippedStr = encoded.substring(0, encoded.length() - padLength);
            int actualPadLength = padLength - (padLength / 4 * 4);

            int resultLength = ((padStrippedStr.length() + actualPadLength) * BIT_WIDTH) / 8 - actualPadLength;
            ByteBuffer bb = ByteBuffer.allocate(resultLength)
                    .order(ByteOrder.LITTLE_ENDIAN);

            byte[] bucket = new byte[BUCKET_SIZE];

            byte[] byteArray = padStrippedStr.getBytes(StandardCharsets.US_ASCII);
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

            byte[] chars = new byte[INTEGER_LENGTH_IN_BYTES];

            while ((bis.read(chars, 0, INTEGER_LENGTH_IN_BYTES)) > 0) {
                boolean isLastBucket = bis.available() == 0;

                int bucketValue0 = getTableValue(tableDecode, chars[0]);
                int bucketValue1 = getTableValue(tableDecode, chars[1]);
                int bucketValue2 = getTableValue(tableDecode, chars[2]);
                int bucketValue3 = getTableValue(tableDecode, chars[3]);

                int value = (
                        (bucketValue0 << 18)
                                + (bucketValue1 << 12)
                                + (bucketValue2 << 6)
                                + (bucketValue3)
                );

                readFromValue(value, bucket);
                bb.put(bucket, 0, isLastBucket ? BUCKET_SIZE - actualPadLength : BUCKET_SIZE);

                for (int i = 0; i < chars.length - 1; i++) {
                    chars[i] = 0;
                }
            }

            byte[] bytesArray = bb.array();
            bb.clear();

            return bytesArray;
        }

        private static int getTableValue(int[] tableDecode, byte value) {
            if (value <= 0) {
                return 0;
            }

            char key = (char) value;
            int tableValue = tableDecode[key];
            if (tableValue < 0) {
                throw new IllegalArgumentException(String.format("Detected invalid character %c", key));
            }
            return tableValue;
        }

        private static void readFromValue(int value, byte[] bucket) {
            bucket[0] = (byte) ((value & (0XFF << 16)) >>> 16);
            bucket[1] = (byte) ((value & (0XFF << 8)) >>> 8);
            bucket[2] = (byte) ((value & 0XFF));
        }
    }
}
