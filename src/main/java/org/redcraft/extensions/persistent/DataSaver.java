package org.redcraft.extensions.persistent;

import org.redcraft.extensions.util.ByteStringReader;

public interface DataSaver {
    byte[] write();
    void read(ByteStringReader reader);
}
