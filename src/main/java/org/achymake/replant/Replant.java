package org.achymake.replant;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.achymake.replant.commands.*;
import org.achymake.replant.data.*;
import org.achymake.replant.handlers.*;
import org.achymake.replant.listeners.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

public final class Replant extends JavaPlugin {
    private static Replant instance;
    private Message message;
    private BlockHandler blockHandler;
    private MaterialHandler materialHandler;
    private RandomHandler randomHandler;
    private ScheduleHandler scheduleHandler;
    private WorldHandler worldHandler;
    private UpdateChecker updateChecker;
    private BukkitScheduler bukkitScheduler;
    private PluginManager pluginManager;
    private StateFlag REPLANT_FLAG;
    @Override
    public void onLoad() {
        REPLANT_FLAG = new StateFlag("replant", true);
        getWorldGuard().getFlagRegistry().register(getFlag());
    }
    public StateFlag getFlag() {
        return REPLANT_FLAG;
    }
    @Override
    public void onEnable() {
        instance = this;
        message = new Message();
        blockHandler = new BlockHandler();
        materialHandler = new MaterialHandler();
        randomHandler = new RandomHandler();
        scheduleHandler = new ScheduleHandler();
        worldHandler = new WorldHandler();
        updateChecker = new UpdateChecker();
        bukkitScheduler = getServer().getScheduler();
        pluginManager = getServer().getPluginManager();
        commands();
        events();
        reload();
        sendInfo("Enabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        getScheduleHandler().disable();
        sendInfo("Disabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
    }
    private void commands() {
        new ReplantCommand();
    }
    private void events() {
        new PlayerInteract();
        new PlayerJoin();
        new PlayerReplant();
    }
    public void reload() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else reloadConfig();
    }
    public PluginManager getPluginManager() {
        return pluginManager;
    }
    public BukkitScheduler getBukkitScheduler() {
        return bukkitScheduler;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public WorldHandler getWorldHandler() {
        return worldHandler;
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public RandomHandler getRandomHandler() {
        return randomHandler;
    }
    public MaterialHandler getMaterialHandler() {
        return materialHandler;
    }
    public BlockHandler getBlockHandler() {
        return blockHandler;
    }
    public Message getMessage() {
        return message;
    }
    public static Replant getInstance() {
        return instance;
    }
    public void sendInfo(String message) {
        getLogger().info(message);
    }
    public void sendWarning(String message) {
        getLogger().warning(message);
    }
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
    public String getMinecraftVersion() {
        return getServer().getBukkitVersion();
    }
    public String getMinecraftProvider() {
        return getServer().getName();
    }
    public WorldGuard getWorldGuard() {
        return WorldGuard.getInstance();
    }
}