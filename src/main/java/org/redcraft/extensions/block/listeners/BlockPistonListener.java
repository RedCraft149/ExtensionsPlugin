package org.redcraft.extensions.block.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.world.ExtensionWorld;

public class BlockPistonListener implements Listener {
    @EventHandler
    public void onBlockPushed(BlockPistonExtendEvent event) {
        ExtensionWorld world = ExtensionsPlugin.reference().getWorld(event.getBlock().getWorld());
        for(Block block : event.getBlocks()) {
            if(world.isExtensionBlock(block)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPulled(BlockPistonRetractEvent event) {
        ExtensionWorld world = ExtensionsPlugin.reference().getWorld(event.getBlock().getWorld());
        for(Block block : event.getBlocks()) {
            if(world.isExtensionBlock(block)) {
                event.setCancelled(true);
            }
        }
    }
}
