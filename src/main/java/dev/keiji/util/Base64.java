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
 * Utilities for encoding and decoding the Base64 representation of binary data.
 *
 * See RFC https://datatracker.ietf.org/doc/html/rfc4648
 * Special thanks to http://www5d.biglobe.ne.jp/stssk/rfc/rfc4648j.html
 */
public class Base64 {

    private Base64() {
    }

    private static final int BIT_WIDTH = 6;

    private static final int PLAIN_DATA_LENGTH_IN_BYTES = 3;
    private static final int ENCODED_DATA_LENGTH_IN_BYTES = 4;

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

    private static final int[] TABLE_DECODE = new int[1 << 7];
    private static final int[] TABLE_DECODE_URL_SAFE = new int[1 << 7];

    static {
        // Initialize
        Arrays.fill(TABLE_DECODE, -1);
        Arrays.fill(TABLE_DECODE_URL_SAFE, -1);

        // build reverse lookup tables
        for (int i = 0; i < TABLE_ENCODE.length; i++) {
            TABLE_DECODE[TABLE_ENCODE[i]] = i;
            TABLE_DECODE_URL_SAFE[TABLE_ENCODE_URL_SAFE[i]] = i;
        }
    }

    /**
     * Base64-encode the given data and return a newly allocated String with the result.
     *
     * @param input the data to encode
     */
    public static String encode(byte[] input) {
        return Encoder.encode(input, TABLE_ENCODE);
    }

    /**
     * Base64 url and filename safe encode the given data and return a newly allocated String with the result.
     *
     * @param input the data to encode
     */
    public static String encodeUrlSafe(byte[] input) {
        return Encoder.encode(input, TABLE_ENCODE_URL_SAFE);
    }

    /**
     * Decode the Base64-encoded data in input and return the data in a new byte array.
     *
     * @param input the data to decode
     */
    public static byte[] decode(String input) {
        return Decoder.decode(input, TABLE_DECODE);
    }

    /**
     * Decode the Base64 url and filename safe encoded data in input and return the data in a new byte array.
     *
     * @param input the data to decode
     */
    public static byte[] decodeUrlSafe(String input) {
        return Decoder.decode(input, TABLE_DECODE_URL_SAFE);
    }

    private static class Encoder {
        private static final int BIT_MASK = 0x3F; // = 00111111

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
            byte[] encodedData = new byte[ENCODED_DATA_LENGTH_IN_BYTES];

            int len;
            while ((len = byteArrayInputStream.read(plainData, 0, PLAIN_DATA_LENGTH_IN_BYTES)) > 0) {
//                int encodedBucketLengthInBit = len * 8;
//                int resultBucketLengthInBytes = encodedBucketLengthInBit / BIT_WIDTH
//                        + (encodedBucketLengthInBit % BIT_WIDTH == 0 ? 0 : 1);
//                int padLength = DECODED_BUCKET_LENGTH_IN_BYTES - resultBucketLengthInBytes;

                // Simplified for Base64
                int padLength = PLAIN_DATA_LENGTH_IN_BYTES - len;

                int value = getIntFromBucket(plainData);

                encodedData[0] = (byte) tableEncode[getIndex(value, 18)];
                encodedData[1] = (byte) tableEncode[getIndex(value, 12)];
                encodedData[2] = (byte) tableEncode[getIndex(value, 6)];
                encodedData[3] = (byte) tableEncode[getIndex(value, 0)];

                byteArrayOutputStream.write(encodedData, 0, ENCODED_DATA_LENGTH_IN_BYTES - padLength);

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

        public static byte[] decode(String input, int[] tableDecode) {
            if (input == null) {
                throw new IllegalArgumentException("Input string must not be null.");
            }
            if (input.length() == 0) {
                return new byte[0];
            }
            if (input.length() % 4 != 0) {
                throw new IllegalArgumentException("Input string length must be divisible by 4.");
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
            byte[] encodedData = new byte[ENCODED_DATA_LENGTH_IN_BYTES];

            int len;
            while ((len = byteArrayInputStream.read(encodedData, 0, ENCODED_DATA_LENGTH_IN_BYTES)) > 0) {
//                int decodedBucketLengthInBit = len * BIT_WIDTH;
//                int resultBucketLengthInBytes = decodedBucketLengthInBit / BIT_WIDTH
//                        + (decodedBucketLengthInBit % BIT_WIDTH == 0 ? 0 : 1);
//                int padLength = DECODED_BUCKET_LENGTH_IN_BYTES - resultBucketLengthInBytes;

                // Simplified for Base64
                int padLength = ENCODED_DATA_LENGTH_IN_BYTES - len;

                int bucketValue0 = getTableValue(tableDecode, encodedData[0]);
                int bucketValue1 = getTableValue(tableDecode, encodedData[1]);
                int bucketValue2 = getTableValue(tableDecode, encodedData[2]);
                int bucketValue3 = getTableValue(tableDecode, encodedData[3]);

                int value = (
                        (bucketValue0 << 18)
                                + (bucketValue1 << 12)
                                + (bucketValue2 << 6)
                                + (bucketValue3)
                );

                readFromValue(value, plainData);

                int bucketSize = PLAIN_DATA_LENGTH_IN_BYTES - padLength;
                byteArrayOutputStream.write(plainData, 0, bucketSize);

                Arrays.fill(encodedData, (byte) 0);
            }
        }

        private static int getTableValue(int[] tableDecode, byte value) {
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

        private static void readFromValue(int value, byte[] bucket) {
            bucket[0] = (byte) ((value & (0XFF << 16)) >>> 16);
            bucket[1] = (byte) ((value & (0XFF << 8)) >>> 8);
            bucket[2] = (byte) ((value & 0XFF));
        }
    }
}
