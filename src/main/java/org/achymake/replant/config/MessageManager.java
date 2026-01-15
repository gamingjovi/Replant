package org.achymake.replant.config;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public final class MessageManager {

    private final ConfigManager config;

    private String prefix;
    private String noPermission;
    private String reloaded;
    private List<String> info;

    public MessageManager(ConfigManager config) {
        this.config = config;
    }

    public void reload() {
        var yml = config.rawMessages();
        this.prefix = color(yml.getString("prefix", "&b&lReplant &7» "));
        this.noPermission = color(replacePrefix(yml.getString("no-permission", "{prefix}&cNo permission.")));
        this.reloaded = color(replacePrefix(yml.getString("reloaded", "{prefix}&aReloaded.")));
        this.info = yml.getStringList("info");
    }

    public void send(CommandSender sender, String message) {
        sender.sendMessage(color(replacePrefix(message)));
    }

    public void sendNoPermission(CommandSender sender) {
        sender.sendMessage(noPermission);
    }

    public void sendReloaded(CommandSender sender, long ms) {
        sender.sendMessage(reloaded.replace("{ms}", String.valueOf(ms)));
    }

    public void sendInfo(CommandSender sender, Map<String, String> placeholders) {
        for (String line : info) {
            String out = replacePrefix(line);
            for (var e : placeholders.entrySet()) {
                out = out.replace("{" + e.getKey() + "}", e.getValue());
            }
            sender.sendMessage(color(out));
        }
    }

    public String prefix() {
        return prefix;
    }

    private String replacePrefix(String in) {
        return in == null ? "" : in.replace("{prefix}", prefix);
    }

    private String color(String in) {
        return ChatColor.translateAlternateColorCodes('&', in == null ? "" : in);
    }
}
