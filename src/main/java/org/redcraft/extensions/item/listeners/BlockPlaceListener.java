package org.redcraft.extensions.item.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.block.UnifiedBlock;
import org.redcraft.extensions.persistent.DataSaver;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!ExtensionsPlugin.reference().isExtensionItem(event.getItemInHand())) return;

        event.setCancelled(true);
        ItemStack stack = event.getItemInHand();

        ExtensionMaterialData data = ExtensionsPlugin.reference().getItemMaterialData(stack);
        if(data.isEmpty()) return;
        if(!data.material().isBlock()) return;

        stack.subtract();
        if(stack.getAmount() <= 0) stack = new ItemStack(Material.AIR);
        event.getPlayer().getInventory().setItem(event.getHand(),stack);

        Block block = event.getBlockPlaced();

        Bukkit.getScheduler().scheduleSyncDelayedTask(ExtensionsPlugin.reference(),
            ()-> ExtensionsPlugin.reference().setBlock(
                    block.getX(),block.getY(),block.getZ(),block.getWorld(),
                    UnifiedBlock.extension(data)
                 )
        );
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(event.isBlockInHand()) return;
        if(!event.hasItem()) return;
        if(!ExtensionsPlugin.reference().isExtensionItem(event.getItem())) return;

        ItemStack stack = event.getItem();
        ExtensionMaterialData data = ExtensionsPlugin.reference().getItemMaterialData(stack);
        if(data.isEmpty()) return;
        if(!data.material().isBlock()) return;

        event.setCancelled(true);

        //TODO handle placement
    }
}
