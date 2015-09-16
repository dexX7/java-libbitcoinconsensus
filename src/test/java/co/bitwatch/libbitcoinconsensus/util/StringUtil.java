package co.bitwatch.libbitcoinconsensus.util;

import javax.xml.bind.DatatypeConverter;

public class StringUtil {

    /**
     * Converts a hex-encoded string to a byte array.
     *
     * @param hexString The string to convert
     * @return The byte array
     */
    public static byte[] hexToBinary(String hexString) {
        return DatatypeConverter.parseHexBinary(hexString);
    }

}
