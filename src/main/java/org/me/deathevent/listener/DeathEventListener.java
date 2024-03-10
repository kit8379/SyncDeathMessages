package org.me.deathevent.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.me.deathevent.DeathEvent;
import org.me.deathevent.handler.DeathMessageHandler;
import org.me.deathevent.redis.RedisPublisher;

public class DeathEventListener implements Listener {

    private final DeathEvent plugin;
    private final DeathMessageHandler messageHandler;

    public DeathEventListener(DeathEvent plugin, RedisPublisher redisPublisher) {
        this.plugin = plugin;
        this.messageHandler = new DeathMessageHandler(plugin, redisPublisher);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        messageHandler.handleDeathEvent(event);
        plugin.debug("Death event handled");
    }
}
