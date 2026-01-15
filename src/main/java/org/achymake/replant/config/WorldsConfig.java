package org.achymake.replant.config;

import org.bukkit.World;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class WorldsConfig {

    public enum Mode { ALL, WHITELIST, BLACKLIST }

    private final ConfigManager config;

    private Mode mode = Mode.ALL;
    private Set<String> worldsLower = new HashSet<>();

    public WorldsConfig(ConfigManager config) {
        this.config = config;
    }

    public void reload() {
        String modeStr = config.rawConfig().getString("worlds.mode", "ALL").toUpperCase(Locale.ROOT);
        try {
            this.mode = Mode.valueOf(modeStr);
        } catch (IllegalArgumentException ex) {
            this.mode = Mode.ALL;
        }

        worldsLower = new HashSet<>();
        for (String w : config.rawConfig().getStringList("worlds.list")) {
            if (w != null && !w.isBlank()) worldsLower.add(w.toLowerCase(Locale.ROOT));
        }
    }

    public boolean isAllowed(World world) {
        if (mode == Mode.ALL) return true;

        boolean listed = worldsLower.contains(world.getName().toLowerCase(Locale.ROOT));

        return switch (mode) {
            case WHITELIST -> listed;
            case BLACKLIST -> !listed;
            default -> true;
        };
    }
}
