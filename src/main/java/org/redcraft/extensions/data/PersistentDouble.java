package org.redcraft.extensions.data;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.persistent.DataSaver;
import org.redcraft.extensions.util.ByteStringReader;
import org.redcraft.extensions.util.TypeConversions;

public class PersistentDouble implements ExtensionData, DataSaver {

    double container;

    public PersistentDouble(double d) {
        container = d;
    }
    public PersistentDouble() {
    }

    public void set(double d) {
        container = d;
    }
    public double get() {
        return container;
    }

    @Override
    public @NotNull DataSaver saver() {
        return this;
    }

    @Override
    public String name() {
        return "persistent_double";
    }

    @Override
    public ExtensionData newInstance() {
        return new PersistentDouble();
    }

    @Override
    public byte[] write() {
        return TypeConversions.doubleToBytes(container);
    }

    @Override
    public void read(ByteStringReader reader) {
        container = reader.readDouble();
    }
}
