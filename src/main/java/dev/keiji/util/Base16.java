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
 * Utilities for encoding and decoding the Base16 representation of binary data.
 * <p>
 * See RFC https://datatracker.ietf.org/doc/html/rfc4648
 * Special thanks to http://www5d.biglobe.ne.jp/stssk/rfc/rfc4648j.html
 */
public class Base16 {

    private Base16() {
    }

    private static final int PLAIN_DATA_BLOCK_SIZE = 1;
    private static final int ENCODED_DATA_BLOCK_SIZE = 2;

    private static final byte[] TABLE_ENCODE = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
    };

    private static final byte[] TABLE_DECODE = new byte[1 << 7];

    static {
        // Initialize
        Arrays.fill(TABLE_DECODE, (byte) -1);

        // build reverse lookup tables
        for (int i = 0; i < TABLE_ENCODE.length; i++) {
            TABLE_DECODE[TABLE_ENCODE[i]] = (byte) i;
        }
    }

    /**
     * Base16-encode the given data and return a newly allocated String with the result.
     *
     * @param input the data to encode
     * @return a newly allocated String with the result
     */
    public static String encode(byte[] input) {
        return Encoder.encode(input);
    }

    /**
     * Decode the Base16-encoded data in input and return the data in a new byte array.
     *
     * @param input the data to decode
     * @return the data in a new byte array
     */
    public static byte[] decode(String input) {
        return Decoder.decode(input);
    }

    private static class Encoder {
        private static final int BIT_MASK = 0xF; // = 00001111

        public static String encode(byte[] input) {
            if (input == null) {
                throw new IllegalArgumentException("Input data must not be null.");
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(input);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            encode(bis, bos);

            try {
                return bos.toString(StandardCharsets.US_ASCII.name());
            } catch (UnsupportedEncodingException e) {
                return bos.toString();
            }
        }

        private static void encode(
                ByteArrayInputStream byteArrayInputStream,
                ByteArrayOutputStream byteArrayOutputStream
        ) {
            byte[] plainDataBlock = new byte[PLAIN_DATA_BLOCK_SIZE];
            byte[] encodedDataBlock = new byte[ENCODED_DATA_BLOCK_SIZE];

            while (byteArrayInputStream.read(plainDataBlock, 0, PLAIN_DATA_BLOCK_SIZE) > 0) {
                int value = byteToInt(plainDataBlock[0]);

                encodedDataBlock[0] = TABLE_ENCODE[getIndex(value, 4)];
                encodedDataBlock[1] = TABLE_ENCODE[getIndex(value, 0)];

                byteArrayOutputStream.write(encodedDataBlock, 0, ENCODED_DATA_BLOCK_SIZE);
            }
        }

        private static int byteToInt(byte bucketValue) {
            return ((int) bucketValue) & 0xFF;
        }

        private static byte getIndex(long value, int shift) {
            return (byte) ((value & BIT_MASK << shift) >>> shift);
        }
    }

    private static class Decoder {

        public static byte[] decode(String input) {
            if (input == null) {
                throw new IllegalArgumentException("Input string must not be null.");
            }
            if (input.length() == 0) {
                return new byte[0];
            }
            if (input.length() % 2 != 0) {
                throw new IllegalArgumentException("Input string length must be divisible by 2.");
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            decode(bis, bos);

            return bos.toByteArray();
        }

        private static void decode(
                ByteArrayInputStream byteArrayInputStream,
                ByteArrayOutputStream byteArrayOutputStream
        ) {
            byte[] encodedDataBlock = new byte[ENCODED_DATA_BLOCK_SIZE];
            byte[] plainDataBlock = new byte[PLAIN_DATA_BLOCK_SIZE];

            while (byteArrayInputStream.read(encodedDataBlock, 0, ENCODED_DATA_BLOCK_SIZE) > 0) {
                int valueHigh = getTableValue(TABLE_DECODE, encodedDataBlock[0]) << 4;
                int valueLow = getTableValue(TABLE_DECODE, encodedDataBlock[1]);
                plainDataBlock[0] = (byte) (valueHigh + valueLow);
                byteArrayOutputStream.write(plainDataBlock, 0, PLAIN_DATA_BLOCK_SIZE);
            }
        }

        private static int getTableValue(byte[] tableDecode, byte value) {
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
    }
}
