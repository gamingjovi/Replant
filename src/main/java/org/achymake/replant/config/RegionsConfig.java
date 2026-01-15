package org.achymake.replant.config;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class RegionsConfig {

    public enum Mode { WHITELIST, BLACKLIST }
    public enum Provider { WORLDGUARD }

    private final ConfigManager config;

    private boolean enabled;
    private Provider provider;
    private Mode mode;
    private Set<String> regionsLower;
    private boolean wgRespectBuildFlag;

    public RegionsConfig(ConfigManager config) {
        this.config = config;
        this.regionsLower = new HashSet<>();
    }

    public void reload() {
        var yml = config.rawRegions();

        this.enabled = yml.getBoolean("regions.enabled", false);

        String providerStr = yml.getString("regions.provider", "WORLDGUARD").toUpperCase(Locale.ROOT);
        try {
            this.provider = Provider.valueOf(providerStr);
        } catch (IllegalArgumentException ex) {
            this.provider = Provider.WORLDGUARD;
        }

        String modeStr = yml.getString("regions.mode", "WHITELIST").toUpperCase(Locale.ROOT);
        try {
            this.mode = Mode.valueOf(modeStr);
        } catch (IllegalArgumentException ex) {
            this.mode = Mode.WHITELIST;
        }

        this.regionsLower = new HashSet<>();
        for (String r : yml.getStringList("regions.list")) {
            if (r != null && !r.isBlank()) regionsLower.add(r.toLowerCase(Locale.ROOT));
        }

        this.wgRespectBuildFlag = yml.getBoolean("regions.worldguard.respect-build-flag", true);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Provider provider() {
        return provider;
    }

    public Mode mode() {
        return mode;
    }

    public Set<String> regionIdsLowercase() {
        return regionsLower;
    }

    public boolean worldGuardRespectBuildFlag() {
        return wgRespectBuildFlag;
    }

    public boolean worldGuardProvider() {
        return enabled && provider == Provider.WORLDGUARD;
    }
}
