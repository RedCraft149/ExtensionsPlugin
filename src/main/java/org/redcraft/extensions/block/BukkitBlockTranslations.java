package org.redcraft.extensions.block;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.material.ExtensionMaterialData;

import java.util.HashMap;
import java.util.Map;

public class BukkitBlockTranslations implements Listener {
    Map<Material, ExtensionMaterialData> translations;

    public BukkitBlockTranslations() {
        translations = new HashMap<>();
    }

    public void addTranslation(Material from, ExtensionMaterialData to) {
        translations.put(from,to);
    }
    public void removeTranslation(Material from) {
        translations.remove(from);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Material material = block.getType();
        if(!translations.containsKey(material)) return;
        ExtensionMaterialData data = translations.get(material);
        if(data == null) return;

        event.setCancelled(true);

        ItemStack stack = event.getItemInHand();
        stack.subtract();
        if(stack.getAmount() <= 0) stack = new ItemStack(Material.AIR);
        event.getPlayer().getInventory().setItem(event.getHand(),stack);

        Bukkit.getScheduler().scheduleSyncDelayedTask(ExtensionsPlugin.reference(),
                ()-> ExtensionsPlugin.reference().setBlock(
                        block.getX(),block.getY(),block.getZ(),block.getWorld(),
                        UnifiedBlock.extension(data)
                )
        );
    }
}
