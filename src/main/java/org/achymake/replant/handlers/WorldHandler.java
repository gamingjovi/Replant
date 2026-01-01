package org.achymake.replant.handlers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.achymake.replant.Replant;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorldHandler {
    private Replant getInstance() {
        return Replant.getInstance();
    }
    private WorldGuard getWorldGuard() {
        return getInstance().getWorldGuard();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    public Item spawnItem(Location location, ItemStack itemStack) {
        var world = location.getWorld();
        if (world != null) {
            var item = world.createEntity(location, Item.class);
            item.setItemStack(itemStack);
            world.addEntity(item);
            return item;
        } else return null;
    }
    public void dropItems(Location location, List<ItemStack> itemStacks) {
        var world = location.getWorld();
        if (world != null) {
            for (var itemStack : itemStacks) {
                spawnItem(location, itemStack);
            }
        }
    }
    public void playSound(Location location, String soundType, double volume, double pitch) {
        var world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.valueOf(soundType), (float) volume, (float) pitch);
    }
    public boolean isReplantAllowed(Block block) {
        var world = block.getWorld();
        if (getConfig().getStringList("replant.worlds").contains(world.getName())) {
            var regionManager = getWorldGuard().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            if (regionManager != null) {
                var x = block.getX();
                var y = block.getY();
                var z = block.getZ();
                var vector = BlockVector3.at(x, y, z);
                var protectedCuboidRegion = new ProtectedCuboidRegion("_", vector, vector);
                return isAllowed(regionManager.getApplicableRegions(protectedCuboidRegion));
            } else return true;
        } else return false;
    }
    private boolean isAllowed(ApplicableRegionSet applicableRegionSet) {
        for (var regionIn : applicableRegionSet) {
            if (regionIn != null) {
                var flag = regionIn.getFlag(getInstance().getFlag());
                if (flag == StateFlag.State.ALLOW) {
                    return true;
                } else if (flag == StateFlag.State.DENY) {
                    return false;
                }
            }
        }
        return true;
    }
}