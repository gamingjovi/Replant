package org.achymake.replant.listeners;

import org.achymake.replant.Replant;
import org.achymake.replant.events.PlayerReplantEvent;
import org.achymake.replant.handlers.BlockHandler;
import org.achymake.replant.handlers.MaterialHandler;
import org.achymake.replant.handlers.WorldHandler;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginManager;

public class PlayerInteract implements Listener {
    private Replant getInstance() {
        return Replant.getInstance();
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
    public PlayerInteract() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        var block = event.getClickedBlock();
        if (block == null)return;
        if (event.getHand() != EquipmentSlot.HAND)return;
        if (!getBlockHandler().isEnable(block))return;
        if (!getBlockHandler().isRightAge(block))return;
        if (!getWorldHandler().isReplantAllowed(block))return;
        var player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.SPECTATOR))return;
        var heldItem = player.getInventory().getItemInMainHand();
        var fortune = heldItem.getEnchantmentLevel(getMaterials().getEnchantment("fortune"));
        if (getMaterials().isHoe(heldItem)) {
            if (!player.hasPermission("replant.event.replant.hoe"))return;
            if (!getMaterials().hasEnchantment(heldItem))return;
        } else if (!player.hasPermission("replant.event.replant.hand"))return;
        event.setUseItemInHand(Event.Result.DENY);
        event.setUseInteractedBlock(Event.Result.ALLOW);
        var drops = getBlockHandler().getDrops(block.getType(), fortune);
        getPluginManager().callEvent(new PlayerReplantEvent(player, block, drops));
    }
}