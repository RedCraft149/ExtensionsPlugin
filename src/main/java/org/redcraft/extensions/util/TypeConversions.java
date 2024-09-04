package org.redcraft.extensions.util;

public final class TypeConversions {
    private TypeConversions() {
        throw new RuntimeException("Type Conversions cannot be initialized.");
    }

    public static byte[] booleanToBytes(boolean b) {
        return b ? new byte[]{1} : new byte[]{0};
    }
    public static byte[] byteToBytes(byte b) {
        return new byte[]{b};
    }
    public static byte[] shortToBytes(short s) {
        return new byte[]{
                (byte) (s>>8),
                (byte) s
        };
    }
    public static byte[] intToBytes(int i) {
        return new byte[]{
                (byte) (i>>24),
                (byte) (i>>16),
                (byte) (i>>8),
                (byte) i
        };
    }
    public static byte[] longToBytes(long l) {
        return new byte[]{
                (byte) (l>>56),
                (byte) (l>>48),
                (byte) (l>>40),
                (byte) (l>>32),
                (byte) (l>>24),
                (byte) (l>>16),
                (byte) (l>>8),
                (byte) l
        };
    }
    public static byte[] floatToBytes(float f) {
        return intToBytes(Float.floatToRawIntBits(f));
    }
    public static byte[] doubleToBytes(double d) {
        return longToBytes(Double.doubleToRawLongBits(d));
    }
    public static byte[] charToBytes(char c) {
        final short s = (short) c;
        return shortToBytes(s);
    }

    public static boolean bytesToBoolean(byte[] b) {
        return b[0] != 0;
    }
    public static byte bytesToByte(byte[] b) {
        return b[0];
    }
    public static short bytesToShort(byte[] b) {
        return (short) (((b[0] & 0xFF) << 8 ) |
                        ((b[1] & 0xFF)));
    }
    public static int bytesToInt(byte[] b) {
        return  ((b[0] & 0xFF) << 24) |
                ((b[1] & 0xFF) << 16) |
                ((b[2] & 0xFF) << 8 ) |
                ((b[3] & 0xFF));
    }
    public static long bytesToLong(byte[] b) {
        return  ((b[0] & 0xFFL) << 56L) |
                ((b[1] & 0xFFL) << 48L) |
                ((b[2] & 0xFFL) << 40L) |
                ((b[3] & 0xFFL) << 32L) |
                ((b[4] & 0xFFL) << 24L) |
                ((b[5] & 0xFFL) << 16L) |
                ((b[6] & 0xFFL) <<  8L) |
                ((b[7] & 0xFFL)       ) ;
    }
    public static float bytesToFloat(byte[] b) {
        return Float.intBitsToFloat(bytesToInt(b));
    }
    public static double bytesToDouble(byte[] b) {
        return Double.longBitsToDouble(bytesToLong(b));
    }
    public static char bytesToChar(byte[] b) {
        return (char) bytesToShort(b);
    }

    public static boolean bytesToBoolean(byte[] b, int offset) {
        return b[offset] != 0;
    }
    public static byte bytesToByte(byte[] b, int offset) {
        return b[offset];
    }
    public static short bytesToShort(byte[] b, int offset) {
        return (short) (((b[offset  ] & 0xFF) << 8 ) |
                        ((b[offset+1] & 0xFF)));
    }
    public static int bytesToInt(byte[] b, int offset) {
        return  ((b[offset  ] & 0xFF) << 24) |
                ((b[offset+1] & 0xFF) << 16) |
                ((b[offset+2] & 0xFF) << 8 ) |
                ((b[offset+3] & 0xFF));
    }
    public static long bytesToLong(byte[] b, int offset) {
        return  ((b[offset  ] & 0xFFL) << 56L) |
                ((b[offset+1] & 0xFFL) << 48L) |
                ((b[offset+2] & 0xFFL) << 40L) |
                ((b[offset+3] & 0xFFL) << 32L) |
                ((b[offset+4] & 0xFFL) << 24L) |
                ((b[offset+5] & 0xFFL) << 16L) |
                ((b[offset+6] & 0xFFL) <<  8L) |
                ((b[offset+7] & 0xFFL)       ) ;
    }
    public static float bytesToFloat(byte[] b, int offset) {
        return Float.intBitsToFloat(bytesToInt(b,offset));
    }
    public static double bytesToDouble(byte[] b, int offset) {
        return Double.longBitsToDouble(bytesToLong(b,offset));
    }
    public static char bytesToChar(byte[] b, int offset) {
        return (char) bytesToShort(b,offset);
    }

    public static byte booleanToByte(boolean b) {
        return b ? (byte) 1 : (byte) 0;
    }
    public static boolean byteToBoolean(byte b) {
        return b != 0;
    }
}
