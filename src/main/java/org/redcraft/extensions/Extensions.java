package org.redcraft.extensions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redcraft.extensions.block.UnifiedBlock;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.material.ExtensionMaterial;
import org.redcraft.extensions.material.ExtensionMaterialBuilder;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.world.ExtensionChunk;
import org.redcraft.extensions.world.ExtensionWorld;

import java.util.function.Predicate;

public interface Extensions {
    /**
     * Acquire an instance of Extensions.
     * @param plugin The plugin trying to acquire an instance.
     * @return An instance of Extensions
     * @throws RuntimeException if the same plugin tries to acquire Extensions multiple times.
     */
    static @NotNull Extensions acquire(Plugin plugin) {
        if(plugin == null) throw new RuntimeException("A plugin is required to get Extensions!");
        return ExtensionsProvider.getExtensionsFor(plugin);
    }

    //EXTENSIONS API - BLOCKS
    /**
     * Get the block at a position in a world.
     * @param x x position of the block (world-space)
     * @param y y position of the block (world-space)
     * @param z z position of the block (world-space)
     * @param world the world the block is in
     * @return The {@link UnifiedBlock} at the specified position in the given world.
     */
    @NotNull UnifiedBlock getBlock(int x, int y, int z, @NotNull World world);

    /**
     * Set the block at a position in a world.
     * @param x x position of the block (world-space)
     * @param y y position of the block (world-space)
     * @param z z position of the block (world-space)
     * @param world the world the block is in
     * @param block The {@link UnifiedBlock} to set at the specified position in the given world.
     *              In case of {@link UnifiedBlock#isBukkitBlock()} {@code = true}, this will have
     *              the same effect as calling {@link World#setBlockData(Location, BlockData)}, but
     *              with clearing the {@code Extension Tag}s for this block.
     */
    void setBlock(int x, int y, int z, @NotNull World world, @NotNull UnifiedBlock block);

    /**
     * Tests if a block at a position in a world is an extension block.
     * @param x x position of the block (world-space)
     * @param y y position of the block (world-space)
     * @param z z position of the block (world-space)
     * @param world the world the block is in
     * @return true if the block at the specified position in the given world is an extension block.
     *         See {@link ExtensionChunk#hasExtensionEntry(int, int, int)}.
     */
    boolean isExtensionBlock(int x, int y, int z, @NotNull World world);

    /**
     * Tests if the extension tag on a block is still valid, meaning the extension's 'parent'
     * material type is the same as the present material type.
     * @param x x position of the block (world-space)
     * @param y y position of the block (world-space)
     * @param z z position of the block (world-space)
     * @param world the world the block is in
     * @return true if the extension's 'parent' material type is the same as the present material type,
     *         otherwise false.
     */
    boolean isExtensionValid(int x, int y, int z, @NotNull World world);

    /**
     * Get the chunk at a position in a world.
     * @param x x position of the chunk (world-space)
     * @param z z position of the chunk (world-space)
     * @param world the world the chunk is in
     * @return The {@link ExtensionChunk} at the given position.
     */
    @NotNull ExtensionChunk getChunkAtWorldPosition(int x, int z, @NotNull World world);

    /**
     * Get the chunk at a position in a world.
     * @param x x position of the chunk (chunk-grid-space)
     * @param z z position of the chunk (chunk-grid-space)
     * @param world the world the chunk is in
     * @return The {@link ExtensionChunk} at the given position.
     */
    @NotNull ExtensionChunk getChunkAt(int x, int z, @NotNull World world);

    /**
     * Tries to correct errors in the chunk at the specified position.
     * An 'error' is detected if an extension is present, but the present material doesn't
     * match its material type, or the extension material doesn't exist anymore. All
     * detected blocks will be replaced with air and the extension tags will get removed.
     * @param x the chunks x position (chunk-grid space)
     * @param z the chunks z position (chunk-grid space)
     * @param world the world the chunk is in
     */
    void correctChunk(int x, int z, @NotNull World world);

    /**
     * Gets the ExtensionWorld corresponding to the given bukkit world.
     * If the ExtensionWorld is not already loaded, it will be loaded by calling this method.
     * @param bukkit the ExtensionWorld's bukkit parent
     * @return The ExtensionWorld corresponding to the given bukkit world.
     */
    @NotNull ExtensionWorld getWorld(@NotNull World bukkit);

    /**
     * Adds a new block translations. If a block of type {@code 'from'} is placed by the player,
     * the event will be canceled and instead an extension block of type {@code 'to'} is placed.
     * @param from Block to translate
     * @param to translation
     */
    void addBlockTranslation(Material from, ExtensionMaterialData to);

    /**
     * Remove a block translation. See {@link Extensions#addBlockTranslation(Material, ExtensionMaterialData)}.
     * @param from Translation to remove.
     */
    void removeBlockTranslation(Material from);

    //EXTENSIONS API - ITEMS

    /**
     * Creates a new {@link ItemStack} with the given {@link ExtensionMaterialData}.
     * @param data The ExtensionMaterialData the ItemStack will have.
     * @return A newly created item stack.
     */
    @Contract(pure = true)
    @NotNull ItemStack createItemStack(@NotNull ExtensionMaterialData data);

    /**
     * Sets the extension data of the given ItemStack. This data will be persistent.
     * @param stack The target item stack to set the data on
     * @param data The data to set
     */
    void setItemData(@NotNull ItemStack stack, @NotNull ExtensionData data);

    /**
     * Sets the ExtensionMaterialData for a given item stack.
     * <p style="color:FF0000"> Warning: This method will only change the
     * ExtensionMaterialData, not the actual {@link org.bukkit.Material} of the item.</p>
     * @param stack The stack to set the ExtensionMaterial on
     * @param materialData The ExtensionMaterialData to set
     */
    void setItemMaterialData(@NotNull ItemStack stack, @NotNull ExtensionMaterialData materialData);

    /**
     * Get the ExtensionMaterialData of a given ItemStack. If no data is present,
     * {@link ExtensionMaterialData#EMPTY} will be returned.
     * @param stack The ItemStack to get the ExtensionMaterialData from.
     * @return The ExtensionMaterialData of the given ItemStack.
     */
    @NotNull ExtensionMaterialData getItemMaterialData(@NotNull ItemStack stack);

    /**
     * Test if the given ItemStack has an ExtensionMaterialData and is therefor considered an
     * 'Extension Item'.
     * @param stack the ItemStack to test
     * @return true if the ItemStack has an ExtensionMaterialData, false otherwise.
     */
    boolean isExtensionItem(@NotNull ItemStack stack);

    //EXTENSIONS API - REGISTRY

    /**
     * Get an ExtensionMaterial by the hash code of its name.
     * @param hashCode The ExtensionMaterial name's hash code.
     * @return a found ExtensionMaterial or null.
     */
    @Nullable ExtensionMaterial getMaterialByHash(int hashCode);

    /**
     * Get an ExtensionMaterial by its name.
     * @param name The name of the ExtensionMaterial
     * @return The ExtensionMaterial with the given name, or null if none exists.
     */
     @Nullable ExtensionMaterial getMaterial(@NotNull String name);

    /**
     * Get an instance of an ExtensionData by its hashedName without any values set.
     * @param hashedName The hash code of the ExtensionData's name.
     * @return The ExtensionData with the given hashedName.
     */
     @Nullable ExtensionData getEmptyDataByHash(int hashedName);

    /**
     * Register a new ExtensionMaterial
     * @param material ExtensionMaterial to register.
     */
     void newMaterial(@NotNull ExtensionMaterial material);

    /**
     * Register a new ExtensionData type
     * @param data an instance of the type to register
     */
     void newDataType(@NotNull ExtensionData data);

     /**
      * @return A builder for creating a new ExtensionMaterial.
      * @param name the name of the ExtensionMaterial to build
     */
     ExtensionMaterialBuilder newMaterial(@NotNull String name);

    /**
     * If this method returns true for a recipe, the result can be crafted, also using extension items.
     * Else, Extensions will perform a series of checks to determine if the result should be craft-able
     * with Extension items by regarding the crafting namespace and ExtensionMaterial.
     * See {@link org.redcraft.extensions.item.listeners.CraftingListener#allowForExtensionItems(Recipe)}.
     * @param recipe The recipe to check
     * @return true if extension items should be allowed for crafting, false otherwise. If the recipe
     *         does not check for NBT data of an item, returning true might lead to unwanted behaviour,
     *         as only the ExtensionMaterials item parent type is considered.
     */
     boolean isRecipeAllowedWithExtensionItem(Recipe recipe);

    /**
     * Add a rule to the Extensions crafting system.
     * If any of the registered predicates return true, {@link Extensions#isRecipeAllowedWithExtensionItem(Recipe)}
     * will also return true.
     * The predicate should default to {@code false}.
     * @param predicate predicate, true if the recipe should be craft-able with extension items.
     */
     void addRecipeRule(Predicate<Recipe> predicate);
}
