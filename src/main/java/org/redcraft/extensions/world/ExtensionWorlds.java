package org.redcraft.extensions.world;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ExtensionWorlds implements Listener {
    private final Map<World,ExtensionWorld> worlds;

    public ExtensionWorlds() {
        worlds = new HashMap<>();
    }

    public ExtensionWorld load(World bukkit) {
        ExtensionWorld world = new ExtensionWorld(bukkit);
        worlds.put(bukkit,world);
        return world;
    }

    public boolean unload(World bukkit) {
        ExtensionWorld unloaded = worlds.remove(bukkit);
        if(unloaded == null) return false;
        unloaded.save();
        return true;
    }

    public void unloadAll() {
        for(ExtensionWorld world : worlds.values()) world.save();
        worlds.clear();
    }

    public void save(World bukkit) {
        ExtensionWorld world = worlds.get(bukkit);
        if(world == null) return;
        world.save();
    }

    @Nullable
    public ExtensionWorld get(World bukkit) {
        return worlds.get(bukkit);
    }

    public ExtensionWorld getOrLoad(World bukkit) {
        if(worlds.containsKey(bukkit)) return get(bukkit);
        else return load(bukkit);
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        save(event.getWorld());
    }
    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        unload(event.getWorld());
    }


}
