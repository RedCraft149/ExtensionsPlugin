package org.redcraft.extensions.util;

public class IntegerLocation {
    public int x, y, z;

    public IntegerLocation() {
    }

    public IntegerLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }

    public boolean equals(Object other) {
        if(other == null) return false;
        if(!(other instanceof IntegerLocation loc)) return false;
        return loc.x == x && loc.y == y && loc.z == z;
    }

    public IntegerLocation fromPrimitive(byte[] d) {
        x = d[0];
        z = d[1];
        y = TypeConversions.bytesToShort(d,2);
        return this;
    }

    public static IntegerLocation createFromPrimitive(ByteStringReader reader) {
        return new IntegerLocation().fromPrimitive(reader.readByteArray(4));
    }

    public static IntegerLocation of(int x, int y, int z) {
        return new IntegerLocation(x,y,z);
    }
}
