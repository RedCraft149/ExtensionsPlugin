package org.redcraft.extensions.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.util.ByteStringReader;

public class ExtensionMaterialDataPersistentType implements PersistentDataType<byte[], ExtensionMaterialData> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ExtensionMaterialData> getComplexType() {
        return ExtensionMaterialData.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ExtensionMaterialData complex, @NotNull PersistentDataAdapterContext context) {
        return Primitives.toPrimitive(complex);
    }

    @Override
    public @NotNull ExtensionMaterialData fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteStringReader reader = new ByteStringReader().use(primitive);
        ExtensionMaterialData data = Primitives.extensionMaterialData(reader);
        if(data == null) return ExtensionMaterialData.EMPTY;
        return data;
    }
}
