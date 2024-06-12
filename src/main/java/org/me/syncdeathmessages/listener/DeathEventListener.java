package org.me.syncdeathmessages.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.me.syncdeathmessages.SyncDeathMessages;
import org.me.syncdeathmessages.handler.DeathMessageHandler;
import org.me.syncdeathmessages.redis.RedisPublisher;

public class DeathEventListener implements Listener {

    private final DeathMessageHandler messageHandler;

    public DeathEventListener(SyncDeathMessages plugin, RedisPublisher redisPublisher) {
        this.messageHandler = new DeathMessageHandler(plugin, redisPublisher);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        messageHandler.handleDeathEvent(event);
    }
}
