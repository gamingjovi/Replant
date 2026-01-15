package org.achymake.replant.commands;

import org.achymake.replant.Replant;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Map;

public final class ReplantCommand implements CommandExecutor, TabCompleter {

    private final Replant plugin;

    public ReplantCommand(Replant plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("replant.command")) {
            plugin.config().messages().sendNoPermission(sender);
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("info")) {
            plugin.config().messages().sendInfo(sender, Map.of(
                    "version", plugin.getDescription().getVersion(),
                    "worldguard", String.valueOf(plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard"))
            ));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("replant.command.reload")) {
                plugin.config().messages().sendNoPermission(sender);
                return true;
            }

            long start = System.nanoTime();
            plugin.reloadPlugin();
            long ms = (System.nanoTime() - start) / 1_000_000L;

            plugin.config().messages().sendReloaded(sender, ms);
            return true;
        }

        plugin.config().messages().send(sender, plugin.config().messages().prefix() + "&cUsage: /replant <info|reload>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return List.of("info", "reload");
        return List.of();
    }
}