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
        event.setDeathMessage(null);

        Entity killer = event.getEntity().getKiller();
        List<String> commands;
        if (killer instanceof Player) {
            commands = getConfig().getStringList("commands.PLAYER_KILL");
        } else {
            String causeOfDeath = event.getEntity().getLastDamageCause().getCause().name();
            commands = getConfig().getStringList("commands." + causeOfDeath);
        }

        for (String command : commands) {
            command = command.replace("{player}", event.getEntity().getName())
                    .replace("{killer}", killer.getName());
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
        }
    }
}
