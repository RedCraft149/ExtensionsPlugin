package org.redcraft.extensions.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.UUID;

public class ItemTags {
    //SETTERS
    public static void set(ItemStack stack, String key, boolean value) {
        modify(stack,key, value ? (byte) 1 : (byte) 0,PersistentDataType.BYTE);
    }
    public static void set(ItemStack stack, String key, byte value) {
        modify(stack,key,value,PersistentDataType.BYTE);
    }
    public static void set(ItemStack stack, String key, short value) {
        modify(stack,key,value,PersistentDataType.SHORT);
    }
    public static void set(ItemStack stack, String key, int value) {
        modify(stack,key,value,PersistentDataType.INTEGER);
    }
    public static void set(ItemStack stack, String key, long value) {
        modify(stack,key,value,PersistentDataType.LONG);
    }
    public static void set(ItemStack stack, String key, float value) {
        modify(stack,key,value,PersistentDataType.FLOAT);
    }
    public static void set(ItemStack stack, String key, double value) {
        modify(stack,key,value,PersistentDataType.DOUBLE);
    }
    public static void set(ItemStack stack, String key, char value) {
        modify(stack,key,(short) value,PersistentDataType.SHORT);
    }
    public static void set(ItemStack stack, String key, String value) {
        modify(stack,key,value,PersistentDataType.STRING);
    }

    /**
     * Only use this method if you want to store a tag of actual byte data.
     * If the data represents a serialized complex data type, use {@link org.redcraft.extensions.material.ExtensionData}.
     * @param stack ItemStack to modify
     * @param key Data key
     * @param value Data value
     */
    public static void set(ItemStack stack, String key, byte[] value) {
        modify(stack,key,value,PersistentDataType.BYTE_ARRAY);
    }
    public static void set(ItemStack stack, String key, int[] value) {
        modify(stack,key,value,PersistentDataType.INTEGER_ARRAY);
    }
    public static void set(ItemStack stack, String key, long[] value) {
        modify(stack,key,value,PersistentDataType.LONG_ARRAY);
    }
    public static void set(ItemStack stack, String key, UUID value) {
        modify(stack,key,new long[]{value.getMostSignificantBits(),value.getLeastSignificantBits()},PersistentDataType.LONG_ARRAY);
    }
    //GETTERS

    public static boolean getBoolean(ItemStack stack, String key) {
        return read(stack, key, PersistentDataType.BYTE) != 0;
    }
    public static byte getByte(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.BYTE);
    }
    public static short getShort(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.SHORT);
    }
    public static int getInteger(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.INTEGER);
    }
    public static long getLong(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.LONG);
    }
    public static float getFloat(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.FLOAT);
    }
    public static double getDouble(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.DOUBLE);
    }
    public static char getCharacter(ItemStack stack, String key) {
        return (char) (short) read(stack,key,PersistentDataType.SHORT);
    }
    public static String getString(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.STRING);
    }
    public static byte[] getByteArray(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.BYTE_ARRAY);
    }
    public static int[] getIntegerArray(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.INTEGER_ARRAY);
    }
    public static long[] getLongArray(ItemStack stack, String key) {
        return read(stack,key,PersistentDataType.LONG_ARRAY);
    }
    public static UUID getUUID(ItemStack stack, String key) {
        long[] array = read(stack,key,PersistentDataType.LONG_ARRAY);
        if(array.length != 2) throw new NoSuchElementException();
        return new UUID(array[0],array[1]);
    }

    public static boolean hasEntry(ItemStack stack, String key) {
        return isPresent(stack,key,null);
    }

    /**
     * @param stack stack to test
     * @param key key to test
     * @return True if the registered key is present and is interpretable as a boolean value.
     *          If the stored value is a byte, this method will also return true.
     */
    public static boolean hasBoolean(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.BYTE);
    }
    public static boolean hasByte(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.BYTE);
    }
    public static boolean hasShort(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.SHORT);
    }
    public static boolean hasInteger(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.INTEGER);
    }
    public static boolean hasLong(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.LONG);
    }
    public static boolean hasFloat(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.FLOAT);
    }
    public static boolean hasDouble(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.DOUBLE);
    }

    /**
     * @param stack stack to test
     * @param key key to test
     * @return True if the registered key is present and is interpretable as a character.
     *         If the stored value is a short, this method will also return true.
     */
    public static boolean hasCharacter(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.SHORT);
    }
    public static boolean hasString(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.STRING);
    }
    public static boolean hasByteArray(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.BYTE_ARRAY);
    }
    public static boolean hasIntegerArray(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.INTEGER_ARRAY);
    }
    public static boolean hasLongArray(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.LONG_ARRAY);
    }

    /**
     * @param stack stack to test
     * @param key key to test
     * @return True if the registered key might be interpretable as a UUID.
     *         If the stored value is a long array, this method will also return true.
     */
    public static boolean hasUUID(ItemStack stack, String key) {
        return isPresent(stack,key,PersistentDataType.LONG_ARRAY);
    }

    /**
     * @param stack stack to test
     * @param key key to test
     * @return True if the registered key <b>is</b> interpretable as a UUID.
     *          If the stored value is a long array is a long array of length 2, this method will also return true.
     *
     */
    public static boolean hasUUIDExact(ItemStack stack, String key) {
        try {
            getUUID(stack,key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private static <T> void modify(ItemStack stack, String key, T value, PersistentDataType<T,T> type) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        if(namespacedKey == null) throw new IllegalArgumentException("Key is invalid! (See: NamespacedKey.fromString(...))");
        container.set(namespacedKey,type,value);

        stack.setItemMeta(meta);
    }

    private static <T> @NotNull T read(ItemStack stack, String key, PersistentDataType<T,T> type) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        if(namespacedKey == null) throw new IllegalArgumentException("Key is invalid! (See: NamespacedKey.fromString(...))");

        T t = container.get(namespacedKey,type);
        if(t == null) throw new NoSuchElementException();
        return t;
    }

    private static <T> boolean isPresent(ItemStack stack, String key, @Nullable PersistentDataType<T,T> type) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        if(namespacedKey == null) throw new IllegalArgumentException("Key is invalid! (See: NamespacedKey.fromString(...))");
        if(type == null) return container.has(namespacedKey);
        else return container.has(namespacedKey,type);
    }
}
