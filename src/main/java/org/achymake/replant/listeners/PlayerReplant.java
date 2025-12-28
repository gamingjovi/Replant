package org.achymake.replant.listeners;

import org.achymake.replant.Replant;
import org.achymake.replant.events.PlayerReplantEvent;
import org.achymake.replant.handlers.BlockHandler;
import org.achymake.replant.handlers.MaterialHandler;
import org.achymake.replant.handlers.WorldHandler;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class PlayerReplant implements Listener {
    private Replant getInstance() {
        return Replant.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private BlockHandler getBlockHandler() {
        return getInstance().getBlockHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerReplant() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerReplant(PlayerReplantEvent event) {
        if (event.isCancelled())return;
        var block = event.getClickedBlock();
        if (!getBlockHandler().isEnable(block))return;
        var player = event.getPlayer();
        var heldItem = player.getInventory().getItemInMainHand();
        var damage = getConfig().getInt("blocks." + block.getType() + ".damage");
        if (getMaterials().isHoe(heldItem)) {
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                getMaterials().addDamage(heldItem, damage);
            }
        }
        player.swingMainHand();
        getBlockHandler().playSound(block);
        getWorldHandler().dropItems(block.getLocation().add(0.5, 0.3, 0.5), event.getDrops());
        getBlockHandler().resetAge(block);
        if (getMaterials().isHoe(heldItem)) {
            if (getMaterials().isDestroyed(heldItem)) {
                getMaterials().breakItem(player.getLocation(), heldItem);
            }
        }
    }
}