package org.redcraft.extensions.util;

public class ByteStringBuilder {
    private byte[] building;
    private int length;
    private int position;
    private boolean dynamic;

    public ByteStringBuilder() {
        this.building = new byte[0];
        this.length = 0;
        this.position = 0;
        this.dynamic = false;
    }

    public ByteStringBuilder begin(int length, boolean dynamic) {
        this.building = new byte[length];
        this.length = length;
        this.dynamic = dynamic;
        return this;
    }
    public void end(byte[] b) {
        if(b.length<building.length) throw new OutOfMemoryError("Given byte array can't hold built data!");
        System.arraycopy(building,0,b,0,building.length);
        dynamic = false;
        length = 0;
        building = new byte[0];
    }
    public byte[] end() {
        byte[] out = new byte[length];
        end(out);
        return out;
    }
    public int length() {
        return length;
    }
    public int position() {
        return position;
    }
    public boolean dynamic() {
        return dynamic;
    }

    public void resize(int size) {
        byte[] newArray = new byte[size];
        System.arraycopy(building,0,newArray,0,Math.min(length,size));
        building = newArray;
        length = size;
    }
    public void ensureCapacity(int additionBytes) {
        if(position+additionBytes<=length) return;
        if(dynamic) resize(position+additionBytes);
        else throw new IndexOutOfBoundsException();
    }

    public ByteStringBuilder append(boolean b) {
        ensureCapacity(1);
        building[position] = TypeConversions.booleanToByte(b);
        position++;
        return this;
    }

    public ByteStringBuilder append(byte b) {
        ensureCapacity(1);
        building[position] = b;
        position++;
        return this;
    }

    public ByteStringBuilder append(short s) {
        ensureCapacity(2);
        System.arraycopy(TypeConversions.shortToBytes(s),0,building,position,2);
        position += 2;
        return this;
    }

    public ByteStringBuilder append(int i) {
        ensureCapacity(4);
        System.arraycopy(TypeConversions.intToBytes(i),0,building,position,4);
        position += 4;
        return this;
    }

    public ByteStringBuilder append(long l) {
        ensureCapacity(8);
        System.arraycopy(TypeConversions.longToBytes(l),0,building,position,8);
        position += 8;
        return this;
    }

    public ByteStringBuilder append(float f) {
        ensureCapacity(4);
        System.arraycopy(TypeConversions.floatToBytes(f),0,building,position,4);
        position += 4;
        return this;
    }

    public ByteStringBuilder append(double d) {
        ensureCapacity(8);
        System.arraycopy(TypeConversions.doubleToBytes(d),0,building,position,8);
        position += 8;
        return this;
    }

    public ByteStringBuilder append(char c) {
        ensureCapacity(2);
        System.arraycopy(TypeConversions.charToBytes(c),0,building,position,2);
        position += 2;
        return this;
    }
    public ByteStringBuilder append(String str) {
        byte[] bytes = str.getBytes();
        append(bytes.length);
        append(bytes);
        return this;
    }

    public ByteStringBuilder append(byte[] bytes, int length, int offset) {
        if(length == 0) return this;
        ensureCapacity(length);
        System.arraycopy(bytes,offset,building,position,length);
        position += length;
        return this;
    }
    public ByteStringBuilder append(byte[] bytes) {
        append(bytes,bytes.length,0);
        return this;
    }
    public ByteStringBuilder append(Object obj) {
        if(obj.getClass()==Boolean.class) append((boolean) obj);
        if(obj.getClass()==Byte.class) append((byte) obj);
        if(obj.getClass()==Short.class) append((short) obj);
        if(obj.getClass()==Integer.class) append((int) obj);
        if(obj.getClass()==Long.class) append((long) obj);
        if(obj.getClass()==Float.class) append((float) obj);
        if(obj.getClass()==Double.class) append((double) obj);
        if(obj.getClass()==Character.class) append((char) obj);
        return this;
    }
    public ByteStringBuilder append(Object[] array) {
        for(int i = 0; i < array.length; i++) {
            append(array[i]);
        }
        return this;
    }
    public ByteStringBuilder appendSizedArray(Object[] array) {
        append(array.length);
        append(array);
        return this;
    }

    public ByteStringBuilder position(int index) {
        position = index;
        return this;
    }

    public ByteStringBuilder position(int index, boolean resize) {
        position = index;
        if(resize && position>length) resize(position);
        return this;
    }
    public ByteStringBuilder toEnd() {
        position = length-1;
        return this;
    }

}