package com.maximde.fancyphysics.listeners.entity;

import com.maximde.fancyphysics.FancyPhysics;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


import java.util.concurrent.ThreadLocalRandom;

public class ExplodeListener implements Listener {

    private FancyPhysics fancyPhysics;
    public ExplodeListener(FancyPhysics fancyPhysics) {
        this.fancyPhysics = fancyPhysics;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        realisticExplosion(event);
    }

    private void realisticExplosion(EntityExplodeEvent event) {
        if(!this.fancyPhysics.config.isRealisticExplosion()) return;
        event.setYield(40);
        if(fancyPhysics.config.isPerformanceMode() && event.getLocation().getChunk().getEntities().length > 2000) {
            return;
        }
        for (Block block : event.blockList()) {
            float x = -(float)(ThreadLocalRandom.current().nextDouble() / 10) + (float) (Math.random() / 10);
            float y = -0.1F + (float) (ThreadLocalRandom.current().nextDouble() + 0.5D);
            float z = -(float)(ThreadLocalRandom.current().nextDouble() / 10) + (float) (Math.random() / 10);


            if(block.getType() == Material.TNT) {
                TNTPrimed tntPrimed = (TNTPrimed) block.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
                tntPrimed.setFuseTicks((int)(ThreadLocalRandom.current().nextFloat() * 100) + 80);
                tntPrimed.setVelocity(new Vector(x, y, z));
                block.setType(Material.AIR);
                continue;
            }
            if(fancyPhysics.config.isPerformanceMode() && event.getLocation().getChunk().getEntities().length > 500) {
                block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(block.getType()));
                block.setType(Material.AIR);
                return;
            }
            FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
            fallingBlock.setDropItem(true);
            fallingBlock.setVelocity(new Vector(x, y, z));
            block.setType(Material.AIR);
        }
    }

}
