/*
 * Copyright (C) 2021 ARIYAMA Keiji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.keiji.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Utilities for encoding and decoding the Base32 representation of binary data.
 * <p>
 * See RFC https://datatracker.ietf.org/doc/html/rfc4648
 * Special thanks to http://www5d.biglobe.ne.jp/stssk/rfc/rfc4648j.html
 */
public class Base32 {

    private Base32() {
    }

    private static final int PLAIN_DATA_LENGTH_IN_BYTES = 5;
    private static final int ENCODED_LENGTH_IN_BYTES = 8;

    private static final char PAD = '=';

    private static final char[] TABLE_ENCODE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '2', '3', '4', '5', '6', '7',
    };

    private static final char[] TABLE_ENCODE_EXTENDED_HEX = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
    };

    private static final int[] TABLE_DECODE = new int[1 << 7];
    private static final int[] TABLE_DECODE_EXTENDED_HEX = new int[1 << 7];

    static {
        // Initialize
        Arrays.fill(TABLE_DECODE, -1);
        Arrays.fill(TABLE_DECODE_EXTENDED_HEX, -1);

        // build reverse lookup tables
        for (int i = 0; i < TABLE_ENCODE.length; i++) {
            TABLE_DECODE[TABLE_ENCODE[i]] = i;
        }
        for (int i = 0; i < TABLE_ENCODE_EXTENDED_HEX.length; i++) {
            TABLE_DECODE_EXTENDED_HEX[TABLE_ENCODE_EXTENDED_HEX[i]] = i;
        }
    }

    /**
     * Base32-encode the given data and return a newly allocated String with the result.
     *
     * @param input the data to encode
     */
    public static String encode(byte[] input) {
        return Encoder.encode(input, TABLE_ENCODE);
    }

    /**
     * Decode the Base32-encoded data in input and return the data in a new byte array.
     *
     * @param input the data to decode
     */
    public static byte[] decode(String input) {
        return Decoder.decode(input, TABLE_DECODE);
    }

    /**
     * Extended Hex Base32-encode the given data and return a newly allocated String with the result.
     *
     * @param input the data to encode
     */
    public static String encodeExtendedHex(byte[] input) {
        return Encoder.encode(input, TABLE_ENCODE_EXTENDED_HEX);
    }

    /**
     * Decode the Extended Hex Base32-encoded data in input and return the data in a new byte array.
     *
     * @param input the data to decode
     */
    public static byte[] decodeExtendedHex(String input) {
        return Decoder.decode(input, TABLE_DECODE_EXTENDED_HEX);
    }

    private static class Encoder {
        private static final int BIT_WIDTH = 5;
        private static final long BIT_MASK = 0x1F; // = 00011111

        public static String encode(byte[] input, char[] tableEncode) {
            if (input == null) {
                throw new IllegalArgumentException("Input data must not be null.");
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(input);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            encode(bis, bos, tableEncode);

            try {
                return bos.toString(StandardCharsets.US_ASCII.name());
            } catch (UnsupportedEncodingException e) {
                return bos.toString();
            }
        }

        private static void encode(
                ByteArrayInputStream byteArrayInputStream,
                ByteArrayOutputStream byteArrayOutputStream,
                char[] tableEncode
        ) {
            byte[] plainData = new byte[PLAIN_DATA_LENGTH_IN_BYTES];
            byte[] encodedData = new byte[ENCODED_LENGTH_IN_BYTES];

            int len;
            while ((len = byteArrayInputStream.read(plainData, 0, PLAIN_DATA_LENGTH_IN_BYTES)) > 0) {
                int resultBucketInBit = len * 8;
                int resultBucketLength = resultBucketInBit / BIT_WIDTH + (resultBucketInBit % BIT_WIDTH > 0 ? 1 : 0);
                int padLength = ENCODED_LENGTH_IN_BYTES - resultBucketLength;

                long value = getLongFromBucket(plainData);

                encodedData[0] = (byte) tableEncode[getIndex(value, 35)];
                encodedData[1] = (byte) tableEncode[getIndex(value, 30)];
                encodedData[2] = (byte) tableEncode[getIndex(value, 25)];
                encodedData[3] = (byte) tableEncode[getIndex(value, 20)];
                encodedData[4] = (byte) tableEncode[getIndex(value, 15)];
                encodedData[5] = (byte) tableEncode[getIndex(value, 10)];
                encodedData[6] = (byte) tableEncode[getIndex(value, 5)];
                encodedData[7] = (byte) tableEncode[getIndex(value, 0)];

                byteArrayOutputStream.write(encodedData, 0, ENCODED_LENGTH_IN_BYTES - padLength);

                for (int i = 0; i < padLength; i++) {
                    byteArrayOutputStream.write((byte) PAD);
                }

                // Clear plainData.
                Arrays.fill(plainData, (byte) 0);
            }
        }

        private static byte getIndex(long value, int shift) {
            return (byte) ((value & BIT_MASK << shift) >>> shift);
        }

        private static long getLongFromBucket(byte[] bucket) {
            return (
                    byteToLong(bucket[0]) << 32)
                    + (byteToLong(bucket[1]) << 24)
                    + (byteToLong(bucket[2]) << 16)
                    + (byteToLong(bucket[3]) << 8)
                    + (byteToLong(bucket[4])
            );
        }

        private static long byteToLong(byte bucketValue) {
            return ((long) bucketValue) & 0xFF;
        }
    }

    private static class Decoder {

        public static byte[] decode(String input, int[] tableDecode) {
            if (input == null) {
                throw new IllegalArgumentException("Input string must not be null.");
            }
            if (input.length() == 0) {
                return new byte[0];
            }
            if (input.length() % 8 != 0) {
                throw new IllegalArgumentException("Input string length must be divisible by 8.");
            }

            int padLength = 0;
            if (input.indexOf(PAD) >= 0) {
                padLength = input.length() - input.indexOf(PAD);
            }

            String padStrippedStr = input.substring(0, input.length() - padLength);

            ByteArrayInputStream bis = new ByteArrayInputStream(padStrippedStr.getBytes(StandardCharsets.US_ASCII));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            decode(bis, bos, tableDecode);
            return bos.toByteArray();
        }

        private static void decode(
                ByteArrayInputStream byteArrayInputStream,
                ByteArrayOutputStream byteArrayOutputStream,
                int[] tableDecode
        ) {
            byte[] plainData = new byte[PLAIN_DATA_LENGTH_IN_BYTES];
            byte[] encodedData = new byte[ENCODED_LENGTH_IN_BYTES];

            int len;
            while ((len = byteArrayInputStream.read(encodedData, 0, ENCODED_LENGTH_IN_BYTES)) > 0) {
                long bucketValue0 = getTableValue(tableDecode, encodedData[0]);
                long bucketValue1 = getTableValue(tableDecode, encodedData[1]);
                long bucketValue2 = getTableValue(tableDecode, encodedData[2]);
                long bucketValue3 = getTableValue(tableDecode, encodedData[3]);
                long bucketValue4 = getTableValue(tableDecode, encodedData[4]);
                long bucketValue5 = getTableValue(tableDecode, encodedData[5]);
                long bucketValue6 = getTableValue(tableDecode, encodedData[6]);
                long bucketValue7 = getTableValue(tableDecode, encodedData[7]);

                long value = (
                        (bucketValue0 << 35)
                                + (bucketValue1 << 30)
                                + (bucketValue2 << 25)
                                + (bucketValue3 << 20)
                                + (bucketValue4 << 15)
                                + (bucketValue5 << 10)
                                + (bucketValue6 << 5)
                                + (bucketValue7)
                );

                readFromValue(value, plainData);

                int resultBucketInBit = len * 5;
                int resultBucketLength = resultBucketInBit / 8;

                byteArrayOutputStream.write(plainData, 0, resultBucketLength);

                Arrays.fill(encodedData, (byte) 0);
            }
        }

        private static long getTableValue(int[] tableDecode, byte value) {
            if (value <= 0) {
                return 0;
            }

            char key = (char) value;
            int tableValue = tableDecode[key];
            if (tableValue < 0) {
                throw new IllegalArgumentException(String.format("Invalid character %c detected.", key));
            }
            return tableValue;
        }

        private static void readFromValue(long value, byte[] bucket) {
            bucket[0] = (byte) ((value & (0XFFL << 32)) >>> 32);
            bucket[1] = (byte) ((value & (0XFFL << 24)) >>> 24);
            bucket[2] = (byte) ((value & (0XFFL << 16)) >>> 16);
            bucket[3] = (byte) ((value & (0XFFL << 8)) >>> 8);
            bucket[4] = (byte) ((value & 0XFF));
        }
    }
}
