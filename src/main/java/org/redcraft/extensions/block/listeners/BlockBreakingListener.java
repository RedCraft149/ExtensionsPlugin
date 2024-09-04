package org.redcraft.extensions.block.listeners;


import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.redcraft.extensions.block.BlockBreakTime;
import org.redcraft.extensions.material.ExtensionMaterial;
import org.redcraft.extensions.ExtensionsPlugin;
import org.redcraft.extensions.block.BlockDropInformation;
import org.redcraft.extensions.block.UnifiedBlock;
import org.redcraft.extensions.world.ExtensionWorld;

public class BlockBreakingListener implements Listener {
    //TODO InteractEvent is not necessarily fired all the time - solution: extra listener
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        //reset attributes modified by setBreakingSpeed
        setBreakingSpeed(event.getPlayer(),1.0);

        Block block = event.getBlock();
        ExtensionWorld world = ExtensionsPlugin.reference().getWorld(block.getWorld());

        if(!world.isExtensionBlock(block) || !world.isExtensionValid(block)) return;

        UnifiedBlock uBlock = world.getUnifiedBlock(block);
        if(!uBlock.isExtensionBlock()) {
            event.setDropItems(false);
            return;
        }

        ExtensionMaterial material = uBlock.extension().material();
        BlockDropInformation information = material.getDrops(event.getPlayer(),uBlock);
        if(information == null) return;
        event.setDropItems(false);

        Location dropLocation = block.getLocation().add(0.5,0.5,0.5);
        information.generateAll(item -> world.parent().dropItemNaturally(dropLocation,item));

        if(information.exp > 0) {
            ExperienceOrb orb = world.parent().spawn(dropLocation, ExperienceOrb.class);
            orb.setExperience(information.exp);
        }

        world.removeExtension(block);
    }

    @EventHandler
    public void onBlockDamageEnd(BlockDamageAbortEvent event) {
        setBreakingSpeed(event.getPlayer(),1.0);
    }
    @EventHandler
    public void onBlockInteracted(PlayerInteractEvent event) {
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if(block == null) return;

        ExtensionWorld world = ExtensionsPlugin.reference().getWorld(block.getWorld());
        if(!world.isExtensionBlock(block) || !world.isExtensionValid(block)) return;

        UnifiedBlock uBlock = world.getUnifiedBlock(block);
        if(!uBlock.isExtensionBlock()) return;
        ExtensionMaterial material = uBlock.extension().material();

        Player player = event.getPlayer();
        setBreakingSpeed(player,1.0); //ensure base is set to 1.0 before calculations of new attribute
        double destroySpeed =  block.getBreakSpeed(player);
        double playerBreakTime = material.getBreakTime(player,uBlock,destroySpeed);
        if(BlockBreakTime.isUnset(playerBreakTime)) return;
        double finalBreakingSpeed = 0.05 / destroySpeed / playerBreakTime;
        setBreakingSpeed(player,finalBreakingSpeed);
    }

    private void setBreakingSpeed(Player player, double base) {
        AttributeInstance modifier = player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED);
        if(modifier == null) return;
        modifier.setBaseValue(base);
    }

}
