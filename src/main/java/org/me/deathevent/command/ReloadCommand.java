package org.me.deathevent.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.me.deathevent.DeathEvent;

public class ReloadCommand implements CommandExecutor {

    private final DeathEvent plugin;

    public ReloadCommand(DeathEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("deathevent.admin")) {
            commandSender.sendMessage("You do not have permission to execute this command.");
            return true;
        }

        plugin.reload();
        commandSender.sendMessage("DeathEvent configuration reloaded.");
        return true;
    }
}
