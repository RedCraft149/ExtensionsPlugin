package org.redcraft.extensions.persistent;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.util.ByteStringReader;

public class EmptyData implements ExtensionData {
    @Override
    public @NotNull DataSaver saver() {
        return Saver.instance;
    }

    @Override
    public String name() {
        return "empty";
    }

    @Override
    public int getHashedName() {
        return 0;
    }

    @Override
    public ExtensionData newInstance() {
        return new EmptyData();
    }

    private static final class Saver implements DataSaver {

        public static Saver instance = new Saver();

        @Override
        public byte[] write() {
            return new byte[0];
        }

        @Override
        public void read(ByteStringReader reader) {
            //do nothing
        }
    }
}
