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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

        public static String encode(byte[] input, char[] tableEncode) {
            StringBuilder result = new StringBuilder();

            byte[] bucket = new byte[INTEGER_LENGTH_IN_BYTES];

            ByteArrayInputStream bis = new ByteArrayInputStream(input);

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
                Arrays.fill(bucket, (byte) 0);
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

        public static byte[] decode(String input, int[] tableDecode) {
            if (input == null || input.length() == 0) {
                return new byte[0];
            }
            if (input.length() % 4 != 0) {
                throw new IllegalArgumentException("Decoded string length must be divisible by 4.");
            }

            int padLength = 0;
            if (input.indexOf(PAD) >= 0) {
                padLength = input.length() - input.indexOf(PAD);
            }

            String padStrippedStr = input.substring(0, input.length() - padLength);
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

                Arrays.fill(chars, (byte) 0);
            }

            byte[] output = bb.array();
            bb.clear();

            return output;
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
