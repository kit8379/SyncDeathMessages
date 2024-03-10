package org.me.deathevent.redis;

import org.me.deathevent.DeathEvent;

public class RedisPublisher {

    private final DeathEvent plugin;
    private final RedisHandler redisHandler;
    private final String channel;

    public RedisPublisher(DeathEvent plugin, RedisHandler redisHandler, String channel) {
        this.plugin = plugin;
        this.redisHandler = redisHandler;
        this.channel = channel;
    }

    public void publishToChannel(String message) {
        redisHandler.publish(channel, message);
        plugin.debug("Published message to Redis: " + message);
    }
}