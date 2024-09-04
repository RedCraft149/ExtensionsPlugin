package org.redcraft.extensions.data;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.persistent.DataSaver;
import org.redcraft.extensions.util.ByteStringBuilder;
import org.redcraft.extensions.util.ByteStringReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Same as PersistentMap, but without complete data loss on a single fail.
 * This map saves the data with additional data size information, so that
 * a missing or corrupted entry in the map leads to only this entry to
 * be unreadable instead of the whole map along with other data stored in the
 * same byte string.
 */
public class SavePersistentMap extends HashMap<ExtensionData,ExtensionData> implements ExtensionData, DataSaver {

    @Override
    public @NotNull DataSaver saver() {
        return this;
    }

    @Override
    public String name() {
        return "save_persistent_map";
    }

    @Override
    public ExtensionData newInstance() {
        return new SavePersistentMap();
    }

    @Override
    public byte[] write() {
        //format: [length in bytes] [map size] { [entry length in bytes] [entry bytes] }
        ByteStringBuilder builder = new ByteStringBuilder().begin(8,true);
        builder.append(size());

        for(Map.Entry<ExtensionData,ExtensionData> entry : entrySet()) {
            ByteStringBuilder entryBuilder = new ByteStringBuilder().begin(0,true);
            entryBuilder.append(entry.getKey().getHashedName());
            entryBuilder.append(entry.getKey().saver().write());
            entryBuilder.append(entry.getValue().getHashedName());
            entryBuilder.append(entry.getValue().saver().write());
            byte[] bytes = entryBuilder.end();
            builder.append(bytes.length).append(bytes);
        }

        byte[] b = builder.end();
        ByteStringBuilder finalBuilder = new ByteStringBuilder().begin(b.length+4,false);
        finalBuilder.append(b.length).append(b);
        return finalBuilder.end();
    }

    @Override
    public void read(ByteStringReader reader) {
        //unlink from reader
        int length = reader.readInteger();
        byte[] b = reader.readByteArray(length);

        ByteStringReader mapReader = new ByteStringReader().use(b);
        int entries = mapReader.readInteger();
        for (int i = 0; i < entries; i++) {
            int entryLength = reader.readInteger();
            byte[] entryData = reader.readByteArray(entryLength);

            ByteStringReader entryReader = new ByteStringReader().use(entryData);

            int keyName = entryReader.readInteger();
            ExtensionData key = ExtensionsPlugin.reference().getEmptyDataByHash(keyName);
            if(key == null) continue;
            key.saver().read(entryReader);

            int valueName = entryReader.readInteger();
            ExtensionData value = ExtensionsPlugin.reference().getEmptyDataByHash(valueName);
            if(value == null) continue;
            value.saver().read(entryReader);

            put(key,value);
        }
    }
}
