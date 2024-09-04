package org.redcraft.extensions.data;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.persistent.DataSaver;
import org.redcraft.extensions.util.ByteStringBuilder;
import org.redcraft.extensions.util.ByteStringReader;

import java.util.Map;

public class PersistentMap implements ExtensionData, DataSaver {

    Map<ExtensionData,ExtensionData> map;

    @Override
    public @NotNull DataSaver saver() {
        return this;
    }

    @Override
    public String name() {
        return "persistent_map";
    }

    @Override
    public ExtensionData newInstance() {
        return new PersistentMap();
    }

    @Override
    public byte[] write() {
        ByteStringBuilder builder = new ByteStringBuilder().begin(4,true);
        builder.append(map.size());
        for(Map.Entry<ExtensionData,ExtensionData> entry : map.entrySet()) {
            builder.append(entry.getKey().getHashedName());
            builder.append(entry.getKey());
            builder.append(entry.getValue().getHashedName());
            builder.append(entry.getValue());
        }
        return builder.end();
    }

    @Override
    public void read(ByteStringReader reader) {
        int length = reader.readInteger();
        for (int i = 0; i < length; i++) {
            ExtensionData key = ExtensionsPlugin.reference().getEmptyDataByHash(reader.readInteger());
            if(key == null) return;
            key.saver().read(reader);
            ExtensionData value = ExtensionsPlugin.reference().getEmptyDataByHash(reader.readInteger());
            if(value == null) return;
            value.saver().read(reader);
            map.put(key,value);
        }
    }
}