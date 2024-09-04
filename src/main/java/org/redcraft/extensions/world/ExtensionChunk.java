package org.redcraft.extensions.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.block.UnifiedBlock;
import org.redcraft.extensions.persistent.ExtensionChunkPersistentType;
import org.redcraft.extensions.util.IntegerLocation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExtensionChunk {

    private static final NamespacedKey key = new NamespacedKey("extensions","block_data");
    private static final ExtensionChunkPersistentType type = new ExtensionChunkPersistentType();

    Chunk bukkit;
    private final Map<IntegerLocation, ExtensionMaterialData> dataMap;
    private final boolean autoSave;
    private boolean dirty;

    public ExtensionChunk(Chunk bukkit, boolean autoSave, boolean correct) {
        this.bukkit = bukkit;
        this.autoSave = autoSave;

        this.dataMap = new HashMap<>();
        this.dirty = false;

        load();
        if(correct) correct();
    }

    public UnifiedBlock getBlock(int x, int y, int z) {
        IntegerLocation key = IntegerLocation.of(x,y,z);
        if(!dataMap.containsKey(key)) {
            return UnifiedBlock.bukkit(bukkit.getBlock(x,y,z));
        } else {
            return UnifiedBlock.extension(dataMap.get(key));
        }
    }
    public void setBlock(int x, int y, int z, UnifiedBlock block) {
        clearExtensionEntry(x,y,z);
        if(block.isBukkitBlock()) {
            bukkit.getBlock(x,y,z).setBlockData(block.bukkit().getBlockData());
        } else if(block.isExtensionBlock()) {
            setExtensionBlock(x,y,z,block.extension());
        }
    }

    public void setExtensionBlock(int x, int y, int z, ExtensionMaterialData block) {
        if(!block.material().isBlock()) throw new IllegalArgumentException("ExtensionMaterial must represent a block!");
        bukkit.getBlock(x,y,z).setBlockData(block.material().blockParent());
        dataMap.put(IntegerLocation.of(x,y,z),block);
        if(autoSave) save();
        else dirty = true;
    }

    public void clearExtensionEntry(int x, int y, int z) {
        dataMap.remove(IntegerLocation.of(x,y,z));
        if(autoSave) save();
        else dirty = true;
    }

    public boolean hasExtensionEntry(int x, int y, int z) {
        return dataMap.containsKey(IntegerLocation.of(x,y,z));
    }

    public boolean isExtensionEntryValid(int x, int y, int z) {
        UnifiedBlock unifiedBlock = getBlock(x,y,z);
        if(unifiedBlock.isBukkitBlock()) return true;
        if(unifiedBlock.extension().isEmpty()) return false;
        if(!unifiedBlock.extension().material().isBlock()) return false;

        Material type = bukkit.getBlock(x,y,z).getType();
        return unifiedBlock.extension().material().blockParent().getMaterial() == type;
    }

    public boolean isDirty() {
        return dirty;
    }
    public void save() {
        bukkit.getPersistentDataContainer().set(key,type,dataMap);
        dirty = false;
    }
    public void saveIfDirty() {
        if(dirty) save();
    }
    public void load() {
        Map<IntegerLocation,ExtensionMaterialData> data = bukkit.getPersistentDataContainer().get(key,type);
        if(data == null) return;
        this.dataMap.putAll(data);
    }

    public void correct() {
        Iterator<Map.Entry<IntegerLocation, ExtensionMaterialData>> itr = dataMap.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<IntegerLocation, ExtensionMaterialData> entry = itr.next();
            IntegerLocation location = entry.getKey();
            Block block = bukkit.getBlock(location.x,location.y,location.z);
            Material type = block.getType();
            if(entry.getValue().isEmpty() || !entry.getValue().material().isBlock() || type != entry.getValue().material().blockParent().getMaterial()) {
                block.setType(Material.AIR);
                itr.remove();
            }
        }
    }
}
