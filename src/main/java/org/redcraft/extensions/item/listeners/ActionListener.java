package org.redcraft.extensions.item.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.item.ExtensionItemAction;
import org.redcraft.extensions.material.ExtensionMaterialData;

import java.util.Set;

public class ActionListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(item == null) return;
        if(!ExtensionsPlugin.reference().isExtensionItem(item)) return;

        ExtensionMaterialData data = ExtensionsPlugin.reference().getItemMaterialData(item);
        if(data.isEmpty()) return;
        Set<ExtensionItemAction> toRun = ExtensionItemAction.filter(event.getAction(),item,data);
        for(ExtensionItemAction action : toRun) action.run(event.getAction(),event.getPlayer(),item,
                                                event.getClickedBlock(),event);

        if(!toRun.isEmpty()) event.setCancelled(true);
    }
}
