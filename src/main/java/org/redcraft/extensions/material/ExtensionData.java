package org.redcraft.extensions.material;

import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.persistent.DataSaver;

public interface ExtensionData {
    @NotNull DataSaver saver();
    String name();
    default int getHashedName() {
        return name().hashCode();
    }
    ExtensionData newInstance();
}
