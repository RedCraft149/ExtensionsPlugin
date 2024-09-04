package org.redcraft.extensions.commands;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.material.ExtensionMaterial;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.persistent.EmptyData;

public class GiveCommand implements Listener {
    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        if(!(event.message() instanceof TextComponent)) return;
        String msg = ((TextComponent) event.message()).content();
        if(msg.startsWith("give")) {
            msg = msg.substring(5);
            event.setCancelled(true);
            ExtensionMaterial material = ExtensionsPlugin.reference().getMaterial(msg);
            ItemStack item = ExtensionsPlugin.reference().createItemStack(new ExtensionMaterialData(material,new EmptyData()));
            event.getPlayer().getInventory().addItem(item);
        }
    }
}
