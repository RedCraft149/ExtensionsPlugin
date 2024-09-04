package org.redcraft.extensions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redcraft.extensions.block.BukkitBlockTranslations;
import org.redcraft.extensions.block.UnifiedBlock;
import org.redcraft.extensions.block.listeners.BlockBreakingListener;
import org.redcraft.extensions.block.listeners.BlockInvalidationListener;
import org.redcraft.extensions.block.listeners.BlockPistonListener;
import org.redcraft.extensions.commands.GiveCommand;
import org.redcraft.extensions.item.ExtensionItems;
import org.redcraft.extensions.item.listeners.ActionListener;
import org.redcraft.extensions.item.listeners.BlockPlaceListener;
import org.redcraft.extensions.item.listeners.CraftingListener;
import org.redcraft.extensions.material.ExtensionData;
import org.redcraft.extensions.material.ExtensionMaterial;
import org.redcraft.extensions.material.ExtensionMaterialBuilder;
import org.redcraft.extensions.material.ExtensionMaterialData;
import org.redcraft.extensions.util.HashRegistry;
import org.redcraft.extensions.world.ExtensionChunk;
import org.redcraft.extensions.world.ExtensionWorld;
import org.redcraft.extensions.world.ExtensionWorlds;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public final class ExtensionsPlugin extends JavaPlugin implements Extensions {

    private static ExtensionsPlugin EXTENSIONS;

    private ExtensionWorlds worlds;
    private ExtensionItems items;
    private BukkitBlockTranslations translations;

    private HashRegistry<ExtensionMaterial> materials;
    private HashRegistry<ExtensionData> dataTypes;
    private Set<Predicate<Recipe>> recipePredicates;

    @Override
    public void onEnable() {
        // Plugin startup logic
        EXTENSIONS = this;

        materials = new HashRegistry<>(ExtensionMaterial::getHashedName);
        dataTypes = new HashRegistry<>(ExtensionData::getHashedName);
        recipePredicates = new HashSet<>();

        worlds = new ExtensionWorlds();
        items = new ExtensionItems();
        translations = new BukkitBlockTranslations();

        registerListener(worlds);
        registerListener(new BlockInvalidationListener());
        registerListener(new BlockBreakingListener());
        registerListener(new BlockPistonListener());
        registerListener(new BlockPlaceListener());
        registerListener(new GiveCommand());
        registerListener(translations);

        registerListener(new CraftingListener());
        registerListener(new ActionListener());

        newMaterial("craft")
                .parent(ItemStack.of(Material.OAK_PLANKS))
                .register();
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener,this);
    }

    @Override
    public void onDisable() {
        worlds.unloadAll();
    }

    /**
     * <p style="color:FF0000">internal use only</p>
     */
    public static ExtensionsPlugin reference() {
        return EXTENSIONS;
    }

    //API Worlds/Blocks

    public @NotNull UnifiedBlock getBlock(int x, int y, int z, @NotNull World world) {
        return worlds.getOrLoad(world).getBlock(x,y,z);
    }
    public void setBlock(int x, int y, int z, @NotNull World world, @NotNull UnifiedBlock block) {
        worlds.getOrLoad(world).setBlock(x,y,z,block);
    }

    public boolean isExtensionBlock(int x, int y, int z, @NotNull World world) {
        return worlds.getOrLoad(world).isExtensionBlock(x,y,z);
    }

    public boolean isExtensionValid(int x, int y, int z, @NotNull World world) {
        return worlds.getOrLoad(world).isExtensionValid(x,y,z);
    }
    public @NotNull ExtensionChunk getChunkAtWorldPosition(int x, int z, @NotNull World world) {
        return worlds.getOrLoad(world).loadAtWorldPosition(x,z);
    }
    public @NotNull ExtensionChunk getChunkAt(int x, int z, @NotNull World world) {
        return worlds.getOrLoad(world).load(world.getChunkAt(x,z));
    }


    public void correctChunk(int x, int z, @NotNull World world) {
        worlds.getOrLoad(world).load(world.getChunkAt(x,z)).correct();
    }
    public @NotNull ExtensionWorld getWorld(@NotNull World bukkit) {
        return worlds.getOrLoad(bukkit);
    }

    @Override
    public void addBlockTranslation(Material from, ExtensionMaterialData to) {
        translations.addTranslation(from, to);
    }

    @Override
    public void removeBlockTranslation(Material from) {
        translations.removeTranslation(from);
    }

    //API Items
    public @NotNull ItemStack createItemStack(@NotNull ExtensionMaterialData data) {
        return items.createItemStack(data);
    }
    public void setItemData(@NotNull ItemStack stack, @NotNull ExtensionData data) {
        items.setData(stack,data);
    }
    public void setItemMaterialData(@NotNull ItemStack stack, @NotNull ExtensionMaterialData materialData) {
        items.setMaterialData(stack,materialData);
    }
    public @NotNull ExtensionMaterialData getItemMaterialData(@NotNull ItemStack stack) {
        return items.getMaterialData(stack);
    }
    public boolean isExtensionItem(@NotNull ItemStack stack) {
        return items.hasExtensionTag(stack);
    }

    public @Nullable ExtensionMaterial getMaterialByHash(int hashCode) {
        return materials.get(hashCode);
    }
    public @Nullable ExtensionMaterial getMaterial(@NotNull String name) {
        return materials.get(name.hashCode());
    }
    public @Nullable ExtensionData getEmptyDataByHash(int hashedName) {
        ExtensionData data = dataTypes.get(hashedName);
        if(data != null) return data.newInstance();
        else return null;
    }
    public void newMaterial(@NotNull ExtensionMaterial material) {
        materials.register(material);
    }
    public void newDataType(@NotNull ExtensionData data) {
        dataTypes.register(data);
    }

    @Override
    public ExtensionMaterialBuilder newMaterial(@NotNull String name) {
        return ExtensionMaterial.build(name,this);
    }

    @Override
    public boolean isRecipeAllowedWithExtensionItem(Recipe recipe) {
        //any amount logical 'or'
        for(Predicate<Recipe> predicate : recipePredicates) {
            if(predicate.test(recipe)) return true;
        }
        return false;
    }

    @Override
    public void addRecipeRule(Predicate<Recipe> predicate) {
        recipePredicates.add(predicate);
    }
}
