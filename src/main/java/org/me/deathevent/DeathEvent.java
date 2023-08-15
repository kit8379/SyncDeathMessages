package org.me.deathevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DeathEvent extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Entity killer = event.getEntity().getKiller();
        List<String> commands = getCommandsForDeathEvent(killer, event);

        for (String command : commands) {
            command = processCommandPlaceholders(command, event, killer);
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
        }
    }

    private List<String> getCommandsForDeathEvent(Entity killer, PlayerDeathEvent event) {
        if (killer instanceof Player) {
            return getConfig().getStringList("commands.PLAYER_KILL");
        } else {
            String causeOfDeath = event.getEntity().getLastDamageCause().getCause().name();
            return getConfig().getStringList("commands." + causeOfDeath);
        }
    }

    private String processCommandPlaceholders(String command, PlayerDeathEvent event, Entity killer) {
        String playerName = event.getEntity().getName();
        String killerName = killer != null ? killer.getName() : "unknown";
        String mobDisplayName = killer != null ? killer.getCustomName() : "unknown";
        if (mobDisplayName == null) {
            mobDisplayName = killer.getType().name();
        }

        return command.replace("{player}", playerName)
                .replace("{killer}", killerName)
                .replace("{mobdisplayname}", mobDisplayName);
    }
}
