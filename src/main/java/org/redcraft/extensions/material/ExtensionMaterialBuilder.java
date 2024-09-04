package org.redcraft.extensions.material;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.redcraft.extensions.Extensions;
import org.redcraft.extensions.block.BlockBreakTime;
import org.redcraft.extensions.block.BlockDrops;
import org.redcraft.extensions.item.ExtensionItemAction;

import java.util.*;
import java.util.function.Consumer;

public class ExtensionMaterialBuilder {

    private final String name;
    private final Extensions extensions;

    private BlockData blockParent;
    private PistonMoveReaction pistonMoveReaction = PistonMoveReaction.BLOCK;
    private BlockBreakTime blockBreakTime = BlockBreakTime.CONSTANT(1.0);
    private BlockDrops blockDrops = BlockDrops.ALWAYS_MYSELF;
    private ItemStack itemParent;
    private boolean doVanillaCrafts = false;
    private final Set<ExtensionItemAction> itemActions;



    protected ExtensionMaterialBuilder(String name, Extensions extensions) {
        this.name = name;
        this.extensions = extensions;
        itemActions = new HashSet<>();
    }

    public ExtensionMaterialBuilder blockParent(BlockData blockParent) {
        this.blockParent = blockParent;
        return this;
    }

    public ExtensionMaterialBuilder pistonMoveReaction(PistonMoveReaction pistonMoveReaction) {
        this.pistonMoveReaction = pistonMoveReaction;
        return this;
    }

    public ExtensionMaterialBuilder blockBreakTime(BlockBreakTime blockBreakTime) {
        this.blockBreakTime = blockBreakTime;
        return this;
    }

    public ExtensionMaterialBuilder blockDrops(BlockDrops blockDrops) {
        this.blockDrops = blockDrops;
        return this;
    }

    public ExtensionMaterialBuilder itemParent(ItemStack itemParent) {
        this.itemParent = itemParent;
        return this;
    }

    public ExtensionMaterialBuilder parent(ItemStack parent) {
        this.itemParent = parent;
        this.blockParent = Bukkit.createBlockData(parent.getType());
        return this;
    }

    public ExtensionMaterialBuilder parent(BlockData data) {
        this.itemParent = ItemStack.of(data.getMaterial());
        this.blockParent = data;
        return this;
    }
    public ExtensionMaterialBuilder doVanillaCrafts(boolean flag) {
        this.doVanillaCrafts = flag;
        return this;
    }
    public ExtensionMaterialBuilder addAction(ExtensionItemAction action) {
        this.itemActions.add(action);
        return this;
    }

    public ExtensionMaterialBuilder name(String name) {
        modifyItemMeta(meta -> meta.itemName(Component.text(name)));
        return this;
    }

    public ExtensionMaterialBuilder glint(boolean glint) {
        modifyItemMeta(meta -> meta.setEnchantmentGlintOverride(glint));
        return this;
    }

    public ExtensionMaterialBuilder lore(String[] lore) {
        modifyItemMeta(meta -> {
            List<Component> components = new LinkedList<>();
            for(String line : lore) components.add(Component.text(line));
            meta.lore(components);
        });

        return this;
    }
    public ExtensionMaterialBuilder maxStackSize(int stackSize) {
        modifyItemMeta(meta -> meta.setMaxStackSize(stackSize));
        return this;
    }
    public ExtensionMaterialBuilder customModelData(int cmd) {
        modifyItemMeta(meta -> meta.setCustomModelData(cmd));
        return this;
    }
    public ExtensionMaterialBuilder rarity(ItemRarity rarity) {
        modifyItemMeta(meta -> meta.setRarity(rarity));
        return this;
    }

    private void modifyItemMeta(Consumer<ItemMeta> modifier) {
        checkItemPresent();
        ItemMeta meta = itemParent.getItemMeta();
        modifier.accept(meta);
        itemParent.setItemMeta(meta);
    }


    private void checkItemPresent() {
        if(itemParent == null) throw new RuntimeException("Call parent(...) or itemParent(...) first!");
    }

    public ExtensionMaterial build() {
        return new ExtensionMaterial(
                name,
                blockParent,
                pistonMoveReaction,
                blockBreakTime,
                blockDrops,
                itemParent,
                doVanillaCrafts,
                itemActions
        );
    }
    public void register() {
        ExtensionMaterial material = build();
        extensions.newMaterial(material);
    }
}
