package org.achymake.replant.config;

import org.achymake.replant.crops.CropRegistry;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class ConfigManager {

    private final JavaPlugin plugin;

    private YamlConfiguration config;
    private YamlConfiguration messages;
    private YamlConfiguration crops;
    private YamlConfiguration regions;

    private final MessageManager messageManager;
    private final CropRegistry cropRegistry;
    private final RegionsConfig regionsConfig;
    private final WorldsConfig worldsConfig;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messageManager = new MessageManager(this);
        this.cropRegistry = new CropRegistry(this);
        this.regionsConfig = new RegionsConfig(this);
        this.worldsConfig = new WorldsConfig(this);
    }

    public void loadAll() {
        saveDefault("config.yml");
        saveDefault("messages.yml");
        saveDefault("crops.yml");
        saveDefault("regions.yml");

        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        this.messages = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "messages.yml"));
        this.crops = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "crops.yml"));
        this.regions = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "regions.yml"));

        this.messageManager.reload();
        this.cropRegistry.reload();
        this.regionsConfig.reload();
        this.worldsConfig.reload();
    }

    private void saveDefault(String name) {
        File f = new File(plugin.getDataFolder(), name);
        if (!f.exists()) {
            plugin.saveResource(name, false);
        }
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    public YamlConfiguration rawConfig() {
        return Objects.requireNonNull(config);
    }

    public YamlConfiguration rawMessages() {
        return Objects.requireNonNull(messages);
    }

    public YamlConfiguration rawCrops() {
        return Objects.requireNonNull(crops);
    }

    public YamlConfiguration rawRegions() {
        return Objects.requireNonNull(regions);
    }

    public MessageManager messages() {
        return messageManager;
    }

    public CropRegistry crops() {
        return cropRegistry;
    }

    public RegionsConfig regions() {
        return regionsConfig;
    }

    public WorldsConfig worlds() {
        return worldsConfig;
    }

    // Convenience getters
    public boolean debug() {
        return rawConfig().getBoolean("general.debug", false);
    }

    public boolean enabled() {
        return rawConfig().getBoolean("replant.enabled", true);
    }

    public int delayTicks() {
        return Math.max(0, rawConfig().getInt("replant.delay-ticks", 1));
    }

    public boolean onlyFullyGrown() {
        return rawConfig().getBoolean("replant.only-fully-grown", true);
    }

    public boolean requireFarmlandBelow() {
        return rawConfig().getBoolean("replant.require-farmland-below", true);
    }

    public boolean freeReplant() {
        return rawConfig().getBoolean("replant.free-replant", false);
    }

    public boolean consumeSeedFromDrops() {
        return rawConfig().getBoolean("replant.consume-seed.from-drops", true);
    }

    public boolean consumeSeedFromInventoryIfNeeded() {
        return rawConfig().getBoolean("replant.consume-seed.from-inventory-if-needed", false);
    }

    public boolean allowHand() {
        return rawConfig().getBoolean("replant.tool.allow-hand", true);
    }

    public boolean allowHoe() {
        return rawConfig().getBoolean("replant.tool.allow-hoe", true);
    }

    public boolean actionbarOnReplant() {
        return rawConfig().getBoolean("messages.actionbar-on-replant", false);
    }

    public boolean worldAllowed(World world) {
        return worldsConfig.isAllowed(world);
    }
}
