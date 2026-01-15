package org.achymake.replant.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public final class PlayerReplantEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Block clickedBlock;
    private final List<ItemStack> drops;

    private boolean cancelled;

    public PlayerReplantEvent(Player player, Block clickedBlock) {
        this(player, clickedBlock, Collections.emptyList());
    }

    public PlayerReplantEvent(Player player, Block clickedBlock, List<ItemStack> drops) {
        this.player = player;
        this.clickedBlock = clickedBlock;
        this.drops = (drops == null) ? Collections.emptyList() : drops;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    // Legacy support
    public List<ItemStack> getDrops() {
        return drops;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}