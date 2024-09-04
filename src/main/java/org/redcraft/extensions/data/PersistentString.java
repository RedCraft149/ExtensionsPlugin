package org.redcraft.extensions.data;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.persistent.DataSaver;
import org.redcraft.extensions.util.ByteStringBuilder;
import org.redcraft.extensions.util.ByteStringReader;

public class PersistentString implements ExtensionData, DataSaver {

    String container;
    public PersistentString(String str) {
        this.container = str;
    }
    public PersistentString(){}

    public String get() {
        return container;
    }
    public void set(String str) {
        container = str;
    }

    @Override
    public @NotNull DataSaver saver() {
        return this;
    }

    @Override
    public String name() {
        return "persistent_string";
    }

    @Override
    public ExtensionData newInstance() {
        return new PersistentString();
    }

    @Override
    public byte[] write() {
        ByteStringBuilder builder = new ByteStringBuilder().begin(4,true);
        byte[] b = container.getBytes();
        builder.append(b.length).append(b);
        return builder.end();
    }

    @Override
    public void read(ByteStringReader reader) {
        int length = reader.readInteger();
        container = new String(reader.readByteArray(length));
    }
}
