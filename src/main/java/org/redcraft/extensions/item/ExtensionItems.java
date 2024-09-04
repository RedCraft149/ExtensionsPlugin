package org.redcraft.extensions.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.persistent.EmptyData;
import org.redcraft.extensions.persistent.ExtensionMaterialDataPersistentType;

public class ExtensionItems {

    private static final NamespacedKey key = new NamespacedKey("extensions","item_data");
    private static final ExtensionMaterialDataPersistentType type = new ExtensionMaterialDataPersistentType();

    public ItemStack createItemStack(ExtensionMaterialData materialData) {
        ItemStack item = materialData.material().itemParent().clone();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, type, materialData);
        item.setItemMeta(meta);
        return item;
    }

    public void setData(@NotNull ItemStack target, @Nullable ExtensionData data) {
        if(data == null) data = new EmptyData(); //ensure no null-data is written

        ItemMeta meta = target.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        ExtensionMaterialData present = container.get(key,type);
        if(present == null) return;
        present.data(data);
        container.set(key,type,present);
        target.setItemMeta(meta);
    }

    public void setMaterialData(@NotNull ItemStack target, @NotNull ExtensionMaterialData materialData) {
        ItemMeta meta = target.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, type, materialData);
        target.setItemMeta(meta);
    }

    public @NotNull ExtensionMaterialData getMaterialData(@NotNull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        ExtensionMaterialData data = container.get(key,type);
        if(data == null) return ExtensionMaterialData.EMPTY;
        return data;
    }

    public boolean hasExtensionTag(@NotNull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(key);
    }
}
