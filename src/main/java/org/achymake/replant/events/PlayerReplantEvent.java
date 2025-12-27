package org.achymake.replant.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class PlayerReplantEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Block clickedBlock;
    private List<ItemStack> drops;
    private boolean cancelled;
    public PlayerReplantEvent(Player player, Block clickedBlock, List<ItemStack> drops) {
        this.player = player;
        this.clickedBlock = clickedBlock;
        this.drops = drops;
    }
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public Player getPlayer() {
        return player;
    }
    public Block getClickedBlock() {
        return clickedBlock;
    }
    public List<ItemStack> getDrops() {
        return drops;
    }
    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}