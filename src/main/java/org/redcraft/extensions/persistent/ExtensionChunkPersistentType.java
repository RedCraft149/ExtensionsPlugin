package org.redcraft.extensions.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.util.ByteStringBuilder;
import org.redcraft.extensions.util.ByteStringReader;
import org.redcraft.extensions.util.IntegerLocation;

import java.util.HashMap;
import java.util.Map;

public class ExtensionChunkPersistentType implements PersistentDataType<byte[],Map<IntegerLocation, ExtensionMaterialData>> {

    //<editor-fold desc="Class<...> complexType = ...;">
    @SuppressWarnings("unchecked")
    private static final Class<Map<IntegerLocation, ExtensionMaterialData>> complexType = (Class<Map<IntegerLocation, ExtensionMaterialData>>) (Class<?>) Map.class;
    //</editor-fold>

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Map<IntegerLocation, ExtensionMaterialData>> getComplexType() {
        return complexType;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Map<IntegerLocation, ExtensionMaterialData> complex, @NotNull PersistentDataAdapterContext context) {
        ByteStringBuilder builder = new ByteStringBuilder().begin(complex.size()*8+4,true);
        builder.append(complex.size());
        for(Map.Entry<IntegerLocation, ExtensionMaterialData> entry : complex.entrySet()) {
            builder.append(Primitives.toPrimitive(entry.getKey()));
            builder.append(Primitives.toPrimitive(entry.getValue()));
        }
        return builder.end();
    }

    @NotNull
    @Override
    public Map<IntegerLocation, ExtensionMaterialData> fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteStringReader reader = new ByteStringReader().use(primitive);
        int size = reader.readInteger();
        Map<IntegerLocation, ExtensionMaterialData> map = new HashMap<>(size);

        for (int i = 0; i < size; i++) {
            IntegerLocation location = Primitives.location(reader);
            ExtensionMaterialData data = Primitives.extensionMaterialData(reader);
            map.put(location,data);
        }

        return map;
    }
}
