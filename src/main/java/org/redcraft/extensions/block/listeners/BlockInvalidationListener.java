package org.redcraft.extensions.block.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.world.ExtensionWorld;

public class BlockInvalidationListener implements Listener {

    @EventHandler
    public void onBlockDestroy(BlockDestroyEvent event) {
        removeExtensionTag(event.getBlock());
    }
    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        removeExtensionTag(event.getBlock());
    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        removeExtensionTag(event.getBlock());
    }
    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        removeExtensionTag(event.getBlock());
    }
    @EventHandler
    public void onLeaveDecay(LeavesDecayEvent event) {
        removeExtensionTag(event.getBlock());
    }
    @EventHandler
    public void onTNTPrime(TNTPrimeEvent event) {
        removeExtensionTag(event.getBlock());
    }

    private void removeExtensionTag(Block block) {
        ExtensionWorld world = ExtensionsPlugin.reference().getWorld(block.getWorld());
        if(world.isExtensionBlock(block)) world.removeExtension(block);
    }
}
