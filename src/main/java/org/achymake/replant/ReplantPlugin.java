package org.achymake.replant;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.achymake.replant.commands.ReplantCommand;
import org.achymake.replant.config.ConfigManager;
import org.achymake.replant.hooks.RegionHook;
import org.achymake.replant.hooks.worldguard.WorldGuardRegionHook;
import org.achymake.replant.listeners.ReplantListener;

// legacy imports so old code compiles
import org.achymake.replant.data.Message;
import org.achymake.replant.handlers.BlockHandler;
import org.achymake.replant.handlers.GameModeHandler;
import org.achymake.replant.handlers.MaterialHandler;
import org.achymake.replant.handlers.RandomHandler;
import org.achymake.replant.handlers.ScheduleHandler;
import org.achymake.replant.handlers.WorldHandler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ReplantPlugin extends JavaPlugin {

    private static ReplantPlugin instance;

    // 2.0 core
    private ConfigManager configManager;
    private RegionHook regionHook;

    // WorldGuard custom flag (optional)
    private StateFlag REPLANT_FLAG;

    // ===== Legacy fields (so your old classes still compile) =====
    private Message message;
    private BlockHandler blockHandler;
    private GameModeHandler gameModeHandler;
    private MaterialHandler materialHandler;
    private RandomHandler randomHandler;
    private ScheduleHandler scheduleHandler;
    private WorldHandler worldHandler;
    private UpdateChecker updateChecker;
    private BukkitScheduler bukkitScheduler;
    private PluginManager pluginManager;

    @Override
    public void onLoad() {
        instance = this;

        // Register custom WG flag safely
        try {
            if (isPluginEnabled("WorldGuard")) {
                REPLANT_FLAG = new StateFlag("replant", true);
                WorldGuard.getInstance().getFlagRegistry().register(REPLANT_FLAG);
            }
        } catch (Throwable t) {
            getLogger().warning("Could not register WorldGuard flag 'replant' (continuing anyway).");
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        // IMPORTANT: plugin.yml main must be org.achymake.replant.Replant (which extends ReplantPlugin)
        if (!(this instanceof Replant replant)) {
            getLogger().severe("plugin.yml main MUST be: org.achymake.replant.Replant");
            getLogger().severe("Replant must extend ReplantPlugin. Disabling to prevent crashes.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // ===== Legacy init (so existing code doesn't NPE if still used) =====
        this.message = new Message();
        this.blockHandler = new BlockHandler();
        this.gameModeHandler = new GameModeHandler();
        this.materialHandler = new MaterialHandler();
        this.randomHandler = new RandomHandler();
        this.scheduleHandler = new ScheduleHandler();
        this.worldHandler = new WorldHandler();
        this.updateChecker = new UpdateChecker();
        this.bukkitScheduler = getServer().getScheduler();
        this.pluginManager = getServer().getPluginManager();

        // ===== 2.0 init =====
        this.configManager = new ConfigManager(this);
        this.configManager.loadAll();

        this.regionHook = setupRegionHook();

        // Register ONLY the new optimized listener (pass Replant instance, not ReplantPlugin)
        Bukkit.getPluginManager().registerEvents(new ReplantListener(replant), this);

        // Register the new command (pass Replant instance, not ReplantPlugin)
        var cmd = getCommand("replant");
        if (cmd != null) {
            var executor = new ReplantCommand(replant);
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }

        sendInfo("Enabled for " + getMinecraftProvider() + " " + getMinecraftVersion());

        // Optional: keep your update checker behavior
        try {
            getUpdateChecker().getUpdate();
        } catch (Throwable ignored) {}
    }

    @Override
    public void onDisable() {
        try {
            getScheduleHandler().disable();
        } catch (Throwable ignored) {}

        sendInfo("Disabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
    }

    // ===== New 2.0 accessors =====
    public ConfigManager config() {
        return configManager;
    }

    public RegionHook regions() {
        return regionHook;
    }

    public void reloadPlugin() {
        if (configManager == null) configManager = new ConfigManager(this);
        configManager.loadAll();
        regionHook = setupRegionHook();
    }

    // Keep legacy name "reload()" in case old code calls it
    public void reload() {
        reloadPlugin();
    }

    // ===== WorldGuard flag / instance =====
    public StateFlag getFlag() {
        return REPLANT_FLAG;
    }

    public static ReplantPlugin plugin() {
        return instance;
    }

    public static Replant getInstance() {
        return (Replant) instance;
    }

    public WorldGuard getWorldGuard() {
        return WorldGuard.getInstance();
    }

    private RegionHook setupRegionHook() {
        if (configManager == null) return RegionHook.allowAll();
        if (!configManager.regions().isEnabled()) return RegionHook.allowAll();

        if (configManager.regions().worldGuardProvider() && isPluginEnabled("WorldGuard")) {
            return new WorldGuardRegionHook(this, configManager.regions());
        }
        return RegionHook.allowAll();
    }

    private boolean isPluginEnabled(String name) {
        Plugin p = getServer().getPluginManager().getPlugin(name);
        return p != null && p.isEnabled();
    }

    // ===== Legacy getters (your old classes require these) =====
    public PluginManager getPluginManager() { return pluginManager; }
    public BukkitScheduler getBukkitScheduler() { return bukkitScheduler; }
    public UpdateChecker getUpdateChecker() { return updateChecker; }
    public WorldHandler getWorldHandler() { return worldHandler; }
    public ScheduleHandler getScheduleHandler() { return scheduleHandler; }
    public RandomHandler getRandomHandler() { return randomHandler; }
    public MaterialHandler getMaterialHandler() { return materialHandler; }
    public GameModeHandler getGameModeHandler() { return gameModeHandler; }
    public BlockHandler getBlockHandler() { return blockHandler; }
    public Message getMessage() { return message; }

    public void sendInfo(String msg) { getLogger().info(msg); }
    public void sendWarning(String msg) { getLogger().warning(msg); }

    public String name() { return getDescription().getName(); }
    public String version() { return getDescription().getVersion(); }
    public String getMinecraftVersion() { return getServer().getBukkitVersion(); }
    public String getMinecraftProvider() { return getServer().getName(); }
}