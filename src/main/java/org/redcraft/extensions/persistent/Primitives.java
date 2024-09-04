package org.redcraft.extensions.persistent;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.*;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.material.ExtensionMaterial;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.util.ByteStringBuilder;
import org.redcraft.extensions.util.ByteStringReader;
import org.redcraft.extensions.util.IntegerLocation;
import org.redcraft.extensions.util.TypeConversions;

public class Primitives {
    public static byte[] toPrimitive(IntegerLocation location) {
        byte[] primitive = new byte[4];
        primitive[0] = (byte) location.x;
        primitive[1] = (byte) location.z;
        System.arraycopy(TypeConversions.shortToBytes((short) location.y),0,primitive,2,2);
        return primitive;
    }

    public static IntegerLocation location(ByteStringReader reader) {
        byte[] d = reader.readByteArray(4);
        IntegerLocation location = new IntegerLocation();
        location.x = d[0];
        location.z = d[1];
        location.y = TypeConversions.bytesToShort(d,2);
        return location;
    }

    public static byte[] toPrimitive(ExtensionMaterialData materialData) {
        ByteStringBuilder builder = new ByteStringBuilder().begin(8,true);
        builder.append(materialData.material().getHashedName());
        builder.append(materialData.data().getHashedName());
        builder.append(materialData.data().saver().write());
        return builder.end();
    }

    public static ExtensionMaterialData extensionMaterialData(@NotNull ByteStringReader reader) {
        ExtensionMaterial material = ExtensionsPlugin.reference().getMaterialByHash(reader.readInteger());
        if(material == null) return ExtensionMaterialData.EMPTY;

        ExtensionData data = ExtensionsPlugin.reference().getEmptyDataByHash(reader.readInteger());
        if(data != null) data.saver().read(reader);
        else data = new EmptyData();

        return new ExtensionMaterialData(material,data);
    }

}
