package org.redcraft.extensions.item.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;
import org.redcraft.extensions.Extensions;
import org.redcraft.extensions.ExtensionsPlugin;

public class CraftingListener implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        if(allowForExtensionItems(recipe)) return;

        for(ItemStack stack : event.getInventory().getMatrix()) {
            if(stack == null) continue;
            if(ExtensionsPlugin.reference().isExtensionItem(stack)){
                event.setCancelled(true);
                break;
            }
        }
    }

    /**
     * Tests if the given recipe can also be crafted with extension items.
     * @param recipe Recipe to test
     * @return False: <br>
     *          - if the recipe key was not found. <br>
     *          - if the recipe key's namespace is 'minecraft' <br>
     *         True: <br>
     *          - if {@link Extensions#isRecipeAllowedWithExtensionItem(Recipe)} is true <br>
     *          - otherwise
     */
    public boolean allowForExtensionItems(Recipe recipe) {
        if(ExtensionsPlugin.reference().isRecipeAllowedWithExtensionItem(recipe)) return true;
        NamespacedKey key = keyOf(recipe);
        if(key == null) return false;
        if(key.getNamespace().equals(NamespacedKey.MINECRAFT)) return false;
        return true;
    }

    private @Nullable NamespacedKey keyOf(Recipe recipe) {
        if(recipe instanceof CookingRecipe<?> c) return c.getKey();
        if(recipe instanceof CraftingRecipe c) return c.getKey();
        if(recipe instanceof SmithingRecipe c) return c.getKey();
        if(recipe instanceof StonecuttingRecipe c) return c.getKey();
        return null;
    }
}
