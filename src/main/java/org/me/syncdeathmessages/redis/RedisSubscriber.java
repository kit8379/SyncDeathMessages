package org.me.syncdeathmessages.redis;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.me.syncdeathmessages.SyncDeathMessages;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber {

    private final RedisHandler redisHandler;
    private final SyncDeathMessages plugin;
    private JedisPubSub pubSub;

    public RedisSubscriber(SyncDeathMessages plugin, RedisHandler redisHandler) {
        this.plugin = plugin;
        this.redisHandler = redisHandler;
    }

    public void subscribeToChannel(String channel) {
        pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                BaseComponent[] components = ComponentSerializer.parse(message);

                for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                    onlinePlayer.spigot().sendMessage(components);
                }
            }
        };

        redisHandler.subscribe(pubSub, channel);
        plugin.info("Subscribed to Redis channel: " + channel);
    }

    public void unsubscribeFromChannel(String channel) {
        if (pubSub != null) {
            pubSub.unsubscribe(channel);
        }
        plugin.info("Unsubscribed from Redis channel: " + channel);
    }
}
