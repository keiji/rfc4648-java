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
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * Utilities for encoding and decoding the Base64 representation of binary data.
 * <p>
 * See RFC https://datatracker.ietf.org/doc/html/rfc4648
 * Special thanks to http://www5d.biglobe.ne.jp/stssk/rfc/rfc4648j.html
 */
public class Base64 {

    private Base64() {
    }

    private static final int PLAIN_DATA_BLOCK_SIZE = 3;
    private static final int ENCODED_DATA_BLOCK_SIZE = 4;

    private static final char PAD = '=';

    private static final byte[] TABLE_ENCODE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/',
    };

    private static final byte[] TABLE_ENCODE_URL_SAFE = {
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

        TABLE_DECODE[PAD] = 0;
        TABLE_DECODE_URL_SAFE[PAD] = 0;
    }

    /**
     * Base64-encode the given data and return a newly allocated String with the result.
     *
     * @param input the data to encode
     * @return a newly allocated String with the result
     */
    public static String encode(byte[] input) {
        return Encoder.encode(input, TABLE_ENCODE);
    }

    /**
     * Base64-encode the given stream data and output encoded data as stream.
     *
     * @param inputStream  the data stream to encode
     * @param outputStream the output stream of the result
     */
    public static void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
        Encoder.encode(inputStream, outputStream, TABLE_ENCODE);
    }

    /**
     * Base64 url and filename safe encode the given data and return a newly allocated String with the result.
     *
     * @param input the data to encode
     * @return a newly allocated String with the result
     */
    public static String encodeUrlSafe(byte[] input) {
        return Encoder.encode(input, TABLE_ENCODE_URL_SAFE);
    }

    /**
     * Base64 url and filename safe encode the given stream data and output encoded data as stream.
     *
     * @param inputStream  the data stream to encode
     * @param outputStream the output stream of the result
     */
    public static void encodeUrlSafe(InputStream inputStream, OutputStream outputStream) throws IOException {
        Encoder.encode(inputStream, outputStream, TABLE_ENCODE_URL_SAFE);
    }

    /**
     * Decode the Base64-encoded data in input and return the data in a new byte array.
     *
     * @param input the data to decode
     * @return the data in a new byte array
     */
    public static byte[] decode(String input) {
        return Decoder.decode(input, TABLE_DECODE, true);
    }

    /**
     * Decode the Base64-encoded data in input and output decoded data as stream.
     *
     * @param inputStream  the data stream to encode
     * @param outputStream the output stream of the result
     */
    public static void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        Decoder.decode(inputStream, outputStream, TABLE_DECODE, true);
    }

    /**
     * Decode the Base64 url and filename safe encoded data in input and return the data in a new byte array.
     *
     * @param input the data to decode
     * @return the data in a new byte array
     */
    public static byte[] decodeUrlSafe(String input) {
        return Decoder.decode(input, TABLE_DECODE_URL_SAFE, false);
    }

    /**
     * Decode the Base64 url and filename safe encoded data in input output decoded data as stream.
     *
     * @param inputStream  the data stream to encode
     * @param outputStream the output stream of the result
     */
    public static void decodeUrlSafe(InputStream inputStream, OutputStream outputStream) throws IOException {
        Decoder.decode(inputStream, outputStream, TABLE_DECODE_URL_SAFE, false);
    }

    private static class Encoder {
        private static final int BIT_MASK = 0x3F; // = 00111111

        public static String encode(byte[] input, byte[] tableEncode) {
            if (input == null) {
                throw new IllegalArgumentException("Input data must not be null.");
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(input);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                encode(bais, baos, tableEncode);
                return baos.toString(StandardCharsets.US_ASCII.name());
            } catch (UnsupportedEncodingException e) {
                return baos.toString();
            } catch (IOException e) {
                // ByteArray[Input|Output]Stream never throws IOException
                return null;
            }
        }

        private static void encode(
                InputStream inputStream,
                OutputStream outputStream,
                byte[] tableEncode
        ) throws IOException {
            if (inputStream == null) {
                throw new InvalidParameterException("inputStream must not be null.");
            }
            if (outputStream == null) {
                throw new InvalidParameterException("outputStream must not be null.");
            }

            byte[] plainDataBlock = new byte[PLAIN_DATA_BLOCK_SIZE];
            byte[] encodedDataBlock = new byte[ENCODED_DATA_BLOCK_SIZE];

            int len;
            while ((len = inputStream.read(plainDataBlock, 0, PLAIN_DATA_BLOCK_SIZE)) > 0) {
                int padSize = PLAIN_DATA_BLOCK_SIZE - len;

                int value = getIntFromBlock(plainDataBlock);

                encodedDataBlock[0] = tableEncode[getIndex(value, 18)];
                encodedDataBlock[1] = tableEncode[getIndex(value, 12)];
                encodedDataBlock[2] = tableEncode[getIndex(value, 6)];
                encodedDataBlock[3] = tableEncode[getIndex(value, 0)];

                outputStream.write(encodedDataBlock, 0, ENCODED_DATA_BLOCK_SIZE - padSize);

                for (int i = 0; i < padSize; i++) {
                    outputStream.write((byte) PAD);
                }

                // Clear plainDataBlock.
                Arrays.fill(plainDataBlock, (byte) 0);
            }
        }

        private static byte getIndex(long value, int shift) {
            return (byte) ((value & BIT_MASK << shift) >>> shift);
        }

        private static int getIntFromBlock(byte[] block) {
            return (
                    byteToInt(block[0]) << 16)
                    + (byteToInt(block[1]) << 8)
                    + (byteToInt(block[2])
            );
        }

        private static int byteToInt(byte bucketValue) {
            return ((int) bucketValue) & 0xFF;
        }
    }

    private static class Decoder {

        public static byte[] decode(String input, int[] tableDecode, boolean expectPadding) {
            if (input == null) {
                throw new IllegalArgumentException("Input string must not be null.");
            }
            if (input.length() == 0) {
                return new byte[0];
            }
            if (expectPadding && input.length() % 4 != 0) {
                throw new IllegalArgumentException("Input string length must be divisible by 4.");
            }

            int padLength = 0;
            if (input.indexOf(PAD) >= 0) {
                padLength = input.length() - input.indexOf(PAD);
            }

            String padStrippedStr = input.substring(0, input.length() - padLength);

            ByteArrayInputStream bais = new ByteArrayInputStream(padStrippedStr.getBytes(StandardCharsets.US_ASCII));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                decode(bais, baos, tableDecode, expectPadding);
                return baos.toByteArray();
            } catch (IOException exception) {
                // ByteArray[Input|Output]Stream never throws IOException
                return null;
            }
        }

        private static void decode(
                InputStream inputStream,
                OutputStream outputStream,
                int[] tableDecode,
                boolean expectPadding
        ) throws IOException {
            if (inputStream == null) {
                throw new IllegalArgumentException("inputStream must be not null.");
            }
            if (outputStream == null) {
                throw new IllegalArgumentException("outputStream must be not null.");
            }

            byte[] plainDataBlock = new byte[PLAIN_DATA_BLOCK_SIZE];
            byte[] encodedDataBlock = new byte[ENCODED_DATA_BLOCK_SIZE];

            int len;
            while ((len = inputStream.read(encodedDataBlock, 0, ENCODED_DATA_BLOCK_SIZE)) > 0) {
                int padSize = ENCODED_DATA_BLOCK_SIZE - len;
                if (expectPadding) {
                    if (encodedDataBlock[0] == PAD) {
//                        padSize = 4;
                        continue;
                    } else if (encodedDataBlock[1] == PAD) {
                        padSize = 3;
                    } else if (encodedDataBlock[2] == PAD) {
                        padSize = 2;
                    } else if (encodedDataBlock[3] == PAD) {
                        padSize = 1;
                    }
                }

                int bucketValue0 = getTableValue(tableDecode, encodedDataBlock[0]);
                int bucketValue1 = getTableValue(tableDecode, encodedDataBlock[1]);
                int bucketValue2 = getTableValue(tableDecode, encodedDataBlock[2]);
                int bucketValue3 = getTableValue(tableDecode, encodedDataBlock[3]);

                int value = (bucketValue0 << 18)
                        + (bucketValue1 << 12)
                        + (bucketValue2 << 6)
                        + (bucketValue3);

                convertToBlock(value, plainDataBlock);

                outputStream.write(plainDataBlock, 0, PLAIN_DATA_BLOCK_SIZE - padSize);

                Arrays.fill(encodedDataBlock, (byte) 0);
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

        private static void convertToBlock(int value, byte[] block) {
            block[0] = (byte) ((value & (0XFF << 16)) >>> 16);
            block[1] = (byte) ((value & (0XFF << 8)) >>> 8);
            block[2] = (byte) ((value & 0XFF));
        }
    }
}
