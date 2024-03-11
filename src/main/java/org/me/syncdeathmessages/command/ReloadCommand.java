package org.me.syncdeathmessages.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.me.syncdeathmessages.SyncDeathMessages;

public class ReloadCommand implements CommandExecutor {

    private final SyncDeathMessages plugin;

    public ReloadCommand(SyncDeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("syncdeathmessages.admin")) {
            commandSender.sendMessage("You do not have permission to execute this command.");
            return true;
        }

        plugin.reload();
        commandSender.sendMessage("SyncDeathMessages configuration reloaded.");
        return true;
    }
}
