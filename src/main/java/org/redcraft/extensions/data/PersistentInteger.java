package org.redcraft.extensions.data;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.persistent.DataSaver;
import org.redcraft.extensions.util.ByteStringReader;
import org.redcraft.extensions.util.TypeConversions;

public class PersistentInteger implements ExtensionData, DataSaver {

    int container;

    public PersistentInteger(int container) {
        this.container = container;
    }

    public PersistentInteger() {
    }

    public int get() {
        return container;
    }
    public void set(int i) {
        container = i;
    }

    @Override
    public @NotNull DataSaver saver() {
        return this;
    }

    @Override
    public String name() {
        return "persistent_integer";
    }

    @Override
    public ExtensionData newInstance() {
        return new PersistentInteger();
    }

    @Override
    public byte[] write() {
        return TypeConversions.intToBytes(container);
    }

    @Override
    public void read(ByteStringReader reader) {
        container = reader.readInteger();
    }
}
