package org.redcraft.extensions.util;

public class ByteStringReader {
    byte[] content;
    int length;
    int position;

    public ByteStringReader use(byte[] content) {
        this.content = content;
        this.length = content.length;
        this.position = 0;
        return this;
    }

    private void ensureCapacity(int additionalBytes) {
        if(position+additionalBytes>length) throw new IndexOutOfBoundsException();
    }

    public boolean readBoolean() {
        ensureCapacity(1);
        boolean b = TypeConversions.byteToBoolean(content[position]);
        position++;
        return b;
    }

    public byte readByte() {
        ensureCapacity(1);
        byte b = content[position];
        position++;
        return b;
    }

    public short readShort() {
        ensureCapacity(2);
        short s = TypeConversions.bytesToShort(content,position);
        position += 2;
        return s;
    }

    public int readInteger() {
        ensureCapacity(4);
        int i = TypeConversions.bytesToInt(content,position);
        position += 4;
        return i;
    }

    public long readLong() {
        ensureCapacity(8);
        long l = TypeConversions.bytesToLong(content,position);
        position += 8;
        return l;
    }

    public float readFloat() {
        ensureCapacity(4);
        float f = TypeConversions.bytesToFloat(content,position);
        position += 4;
        return f;
    }

    public double readDouble() {
        ensureCapacity(8);
        double d = TypeConversions.bytesToDouble(content,position);
        position += 8;
        return d;
    }

    public char readChar() {
        ensureCapacity(2);
        char c = TypeConversions.bytesToChar(content,position);
        position += 2;
        return c;
    }

    public String readString() {
        int length = readInteger();
        byte[] bytes = readByteArray(length);
        return new String(bytes);
    }

    @SuppressWarnings("unchecked")
    public <T> T read(Class<T>  t) {
        if(t==boolean.class) return (T) (Boolean) readBoolean();
        if(t==byte.class) return (T) (Byte) readByte();
        if(t==short.class) return (T) (Short) readShort();
        if(t==int.class) return (T) (Integer) readInteger();
        if(t==long.class) return (T) (Long) readLong();
        if(t==float.class) return (T) (Float) readFloat();
        if(t==double.class) return (T) (Double) readDouble();
        if(t==char.class) return (T) (Character) readChar();
        return null;
    }

    public byte[] readByteArray(int length) {
        ensureCapacity(length);
        byte[] array = new byte[length];
        System.arraycopy(content,position,array,0,length);
        position += length;
        return array;
    }

    public Object[] readSizedArray(Class<?> t) {
        int length = readInteger();
        Object[] array = new Object[length];
        for(int i = 0; i < length; i++) {
            array[i] = read(t);
        }
        return array;
    }

    public int available() {
        return length-position;
    }
    public boolean hasNext() {
        return available()>0;
    }

    public void position(int position) {
        this.position = position;
    }
}
