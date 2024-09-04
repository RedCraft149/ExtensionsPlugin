package org.redcraft.extensions;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ExtensionsProvider {
    private static final HashSet<Plugin> alreadyAcquired = new HashSet<>();
    /*package-private*/ static @NotNull Extensions getExtensionsFor(@NotNull Plugin plugin) {
        if(alreadyAcquired.contains(plugin)) {
            throw new RuntimeException("Tried to get Extensions more then once by the same plugin!");
        }
        alreadyAcquired.add(plugin);
        return ExtensionsPlugin.getPlugin(ExtensionsPlugin.class);
    }
}
