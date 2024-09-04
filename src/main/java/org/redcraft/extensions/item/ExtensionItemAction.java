package org.redcraft.extensions.item;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.material.ExtensionMaterialData;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public interface ExtensionItemAction {
    boolean accept(Action action, ItemStack stack, ExtensionMaterialData data);
    void run(@NotNull Action action,
             @NotNull Player player,
             @Nullable ItemStack item,
             @Nullable Block block,
             @NotNull PlayerInteractEvent event);


    interface ItemActionRunnable {
        void run(@NotNull Action action,
                 @NotNull Player player,
                 @Nullable ItemStack item,
                 @Nullable Block block,
                 @NotNull PlayerInteractEvent event);
    }

    @Contract("_, _ -> new")
    static @NotNull ExtensionItemAction build(@NotNull Action type, @NotNull ItemActionRunnable action) {
        return new DefaultItemAction(Set.of(type),(item)->true,action);
    }
    @Contract("_, _, _ -> new")
    static @NotNull ExtensionItemAction build(@NotNull Action type, final @NotNull Predicate<ExtensionMaterialData> predicate, @NotNull ItemActionRunnable action) {
        return new DefaultItemAction(Set.of(type),(item) -> {
            if(!ExtensionsPlugin.reference().isExtensionItem(item)) return false;
            ExtensionMaterialData data = ExtensionsPlugin.reference().getItemMaterialData(item);
            if(data.isEmpty()) return false;
            return predicate.test(data);
        },action);
    }

    class DefaultItemAction implements ExtensionItemAction {

        private final Set<Action> type;
        private final Predicate<ItemStack> item;
        private final ItemActionRunnable runnable;

        private DefaultItemAction(Set<Action> type, Predicate<ItemStack> item, ItemActionRunnable runnable) {
            this.runnable = runnable;
            this.type = type;
            this.item = item;
        }

        @Override
        public boolean accept(Action action, ItemStack stack, ExtensionMaterialData data) {
            return type.contains(action) && item.test(stack);
        }

        @Override
        public void run(@NotNull Action action, @NotNull Player player, @Nullable ItemStack item, @Nullable Block block, @NotNull PlayerInteractEvent event) {
            runnable.run(action,player,item,block,event);
        }
    }

    static Set<ExtensionItemAction> filter(Action action, ItemStack stack, ExtensionMaterialData data) {
        Set<ExtensionItemAction> building = new HashSet<>();
        for(ExtensionItemAction a : data.material().getActions()) {
            if(a.accept(action,stack,data)) building.add(a);
        }
        return building;
    }
}
