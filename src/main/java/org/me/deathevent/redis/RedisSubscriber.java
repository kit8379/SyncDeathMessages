package org.me.deathevent.redis;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.me.deathevent.DeathEvent;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber {

    private final RedisHandler redisHandler;
    private final DeathEvent plugin;

    public RedisSubscriber(DeathEvent plugin, RedisHandler redisHandler) {
        this.plugin = plugin;
        this.redisHandler = redisHandler;
    }

    public void subscribeToChannel(String channel) {
        JedisPubSub pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                plugin.debug("Received message from Redis: " + message);
                BaseComponent[] components = ComponentSerializer.parse(message);

                for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                    onlinePlayer.spigot().sendMessage(components);
                }
                plugin.debug("Sent message to all online players");
            }
        };

        redisHandler.subscribe(pubSub, channel);
        plugin.debug("Subscribed to Redis channel: " + channel);
    }
}
