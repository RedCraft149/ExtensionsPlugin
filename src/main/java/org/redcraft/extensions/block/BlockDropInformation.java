package org.redcraft.extensions.block;

import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockDropInformation {
    public Supplier<ItemStack> loot;
    public int exp;

    public BlockDropInformation(Supplier<ItemStack> loot, int exp) {
        this.loot = loot;
        this.exp = exp;
    }

    public void generateAll(Consumer<ItemStack> function) {
        ItemStack next;
        while ((next = loot.get()) != null) function.accept(next);
    }
}
