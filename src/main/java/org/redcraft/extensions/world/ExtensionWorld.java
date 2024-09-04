package org.redcraft.extensions.world;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.redcraft.extensions.block.UnifiedBlock;
import org.redcraft.extensions.util.IntegerLocation;
import org.redcraft.extensions.util.RMath;

import java.util.HashMap;
import java.util.Map;

public class ExtensionWorld {
    Map<Long, ExtensionChunk> loadedChunks;
    World parent;

    public ExtensionWorld(World parent) {
        this.parent = parent;
        this.loadedChunks = new HashMap<>();
    }

    public World parent() {
        return parent;
    }

    @NotNull
    public ExtensionChunk load(@NotNull Chunk chunk) {
        long key = chunk.getChunkKey();
        if(loadedChunks.containsKey(key)) return loadedChunks.get(key);

        ExtensionChunk extensionChunk = new ExtensionChunk(chunk,false,true);
        loadedChunks.put(key,extensionChunk);
        return extensionChunk;
    }

    public void unload(@NotNull Chunk chunk) {
        ExtensionChunk unloaded = loadedChunks.remove(chunk.getChunkKey());
        if(unloaded != null && unloaded.isDirty()) unloaded.save();
    }

    public ExtensionChunk loadAtWorldPosition(int x, int z) {
        return load(parent.getChunkAt(chunkKey(x,z)));
    }


    public UnifiedBlock getBlock(int x, int y, int z) {
        ExtensionChunk chunk = loadAtWorldPosition(x,z);
        IntegerLocation inChunk = locationInChunk(x,y,z);
        return chunk.getBlock(inChunk.x,inChunk.y,inChunk.z);
    }
    public void setBlock(int x, int y, int z, UnifiedBlock block) {
        ExtensionChunk chunk = loadAtWorldPosition(x,z);
        IntegerLocation inChunk = locationInChunk(x,y,z);
        chunk.setBlock(inChunk.x,inChunk.y,inChunk.z,block);
    }
    public boolean isExtensionBlock(int x, int y, int z) {
        ExtensionChunk chunk = loadAtWorldPosition(x,z);
        IntegerLocation inChunk = locationInChunk(x,y,z);
        return chunk.hasExtensionEntry(inChunk.x,inChunk.y,inChunk.z);
    }
    public boolean isExtensionValid(int x, int y, int z) {
        ExtensionChunk chunk = loadAtWorldPosition(x,z);
        IntegerLocation inChunk = locationInChunk(x,y,z);
        return chunk.isExtensionEntryValid(inChunk.x,inChunk.y,inChunk.z);
    }
    public void removeExtension(int x, int y, int z) {
        ExtensionChunk chunk = loadAtWorldPosition(x,z);
        IntegerLocation inChunk = locationInChunk(x,y,z);
        chunk.clearExtensionEntry(inChunk.x,inChunk.y,inChunk.z);
    }

    public boolean isExtensionBlock(Block block) {
        return isExtensionBlock(block.getX(),block.getY(),block.getZ());
    }
    public boolean isExtensionValid(Block block) {
        return isExtensionValid(block.getX(),block.getY(),block.getZ());
    }
    public UnifiedBlock getUnifiedBlock(Block block) {
        return getBlock(block.getX(),block.getY(),block.getZ());
    }
    public void removeExtension(Block block) {
        removeExtension(block.getX(),block.getY(),block.getZ());
    }

    private IntegerLocation locationInChunk(int x, int y, int z) {
        return new IntegerLocation(RMath.mod(x,16),y,RMath.mod(z,16));
    }
    private long chunkKey(int x, int z) {
        return Chunk.getChunkKey(x >> 4, z >> 4);
    }

    public void save() {
        for(ExtensionChunk loaded : loadedChunks.values()) loaded.saveIfDirty();
    }
}
