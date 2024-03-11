package org.me.syncdeathmessages.redis;

import org.me.syncdeathmessages.SyncDeathMessages;

public class RedisPublisher {

    private final SyncDeathMessages plugin;
    private final RedisHandler redisHandler;
    private final String channel;

    public RedisPublisher(SyncDeathMessages plugin, RedisHandler redisHandler, String channel) {
        this.plugin = plugin;
        this.redisHandler = redisHandler;
        this.channel = channel;
    }

    public void publishToChannel(String message) {
        redisHandler.publish(channel, message);
        plugin.debug("Published message to Redis: " + message);
    }
}