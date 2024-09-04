package org.redcraft.extensions.block;

import org.bukkit.block.Block;
import org.redcraft.extensions.material.ExtensionMaterialData;

public class UnifiedBlock {
    Block bukkit;
    ExtensionMaterialData extension;

    public UnifiedBlock(Block bukkit, ExtensionMaterialData extension) {
        this.bukkit = bukkit;
        this.extension = extension;
    }

    public boolean isBukkitBlock() {
        return bukkit != null && extension == null;
    }
    public boolean isExtensionBlock() {
        return extension != null && bukkit == null;
    }

    public Block bukkit() {
        return bukkit;
    }
    public ExtensionMaterialData extension() {
        return extension;
    }

    public static UnifiedBlock bukkit(Block bukkit) {
        return new UnifiedBlock(bukkit,null);
    }
    public static UnifiedBlock extension(ExtensionMaterialData extension) {
        return new UnifiedBlock(null,extension);
    }
}
