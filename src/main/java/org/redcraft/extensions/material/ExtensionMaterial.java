package org.redcraft.extensions.material;

import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.redcraft.extensions.Extensions;
import org.redcraft.extensions.block.BlockBreakTime;
import org.redcraft.extensions.block.BlockDropInformation;
import org.redcraft.extensions.block.BlockDrops;
import org.redcraft.extensions.block.UnifiedBlock;
import org.redcraft.extensions.item.ExtensionItemAction;

import java.util.HashSet;
import java.util.Set;

public class ExtensionMaterial {
    String name;

    //Block
    private final BlockData blockParent;
    private final PistonMoveReaction pistonMoveReaction;
    private final BlockBreakTime blockBreakTime;
    private final BlockDrops blockDrops;

    //Item
    private final ItemStack itemParent;
    private final boolean doVanillaCrafts;
    private final Set<ExtensionItemAction> actions;

    public ExtensionMaterial(String name, BlockData blockParent, PistonMoveReaction pistonMoveReaction, BlockBreakTime blockBreakTime, BlockDrops blockDrops, ItemStack itemParent, boolean doVanillaCrafts, Set<ExtensionItemAction> actions) {
        this.name = name;
        this.blockParent = blockParent;
        this.pistonMoveReaction = pistonMoveReaction;
        this.blockBreakTime = blockBreakTime;
        this.blockDrops = blockDrops;
        this.itemParent = itemParent;
        this.doVanillaCrafts = doVanillaCrafts;
        this.actions = new HashSet<>(actions);
    }

    public boolean isBlock() {
        return blockParent != null;
    }
    public boolean isItem() {
        return itemParent != null;
    }

    public BlockData blockParent() {
        return blockParent;
    }
    public @Nullable PistonMoveReaction pistonMoveReaction() {
        return pistonMoveReaction;
    }
    public @Nullable BlockBreakTime blockBreakTime() {
        return blockBreakTime;
    }
    public @Nullable BlockDrops blockDrops() {
        return blockDrops;
    }
    public @Nullable BlockDropInformation getDrops(Player player, UnifiedBlock block) {
        if(blockDrops == null) return null;
        return blockDrops.drop(player,block);
    }
    public double getBreakTime(Player player, UnifiedBlock block, double standard) {
        if(blockBreakTime == null) return BlockBreakTime.UNSET;
        return blockBreakTime.breakTime(player,block,standard);
    }

    public ItemStack itemParent() {
        return itemParent;
    }
    public boolean doVanillaCrafts() {
        return doVanillaCrafts;
    }
    public Set<ExtensionItemAction> getActions() {
        return actions;
    }

    public int getHashedName() {
        return name.hashCode();
    }

    public static ExtensionMaterialBuilder build(String name, Extensions extensions) {
        return new ExtensionMaterialBuilder(name, extensions);
    }

    public int hashCode() {
        return name.hashCode();
    }
    public boolean equals(Object other) {
        if(other instanceof ExtensionMaterial mat) return mat.name.equals(name);
        return false;
    }
}
