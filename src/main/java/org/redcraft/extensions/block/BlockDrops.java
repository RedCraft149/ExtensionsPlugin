package org.redcraft.extensions.block;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.redcraft.extensions.ExtensionsPlugin;

import java.util.function.Supplier;

public interface BlockDrops {
    BlockDropInformation drop(Player player, UnifiedBlock block);

    BlockDrops ALWAYS_MYSELF = (player, block) -> new BlockDropInformation(SingletonSupplier.of(block),0);
    BlockDrops.Reduced NEVER = () -> new BlockDropInformation(()->null,0);

    interface Reduced extends BlockDrops {
        BlockDropInformation drop();
        default BlockDropInformation drop(Player player, UnifiedBlock block) {
            return drop();
        }
    }

    class SingletonSupplier implements Supplier<ItemStack> {

        boolean supplied = false;
        ItemStack stack;

        private SingletonSupplier(ItemStack stack) {
            this.stack = stack;
        }
        @Override
        public ItemStack get() {
            if(supplied) return null;
            supplied = true;
            return stack;
        }

        public static SingletonSupplier of(ItemStack stack) {
            return new SingletonSupplier(stack);
        }
        public static SingletonSupplier of(UnifiedBlock block) {
            return new SingletonSupplier(ExtensionsPlugin.reference().createItemStack(block.extension()));
        }
    }
}
