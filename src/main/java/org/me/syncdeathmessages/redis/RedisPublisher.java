package org.me.syncdeathmessages.redis;

public class RedisPublisher {

    private final RedisHandler redisHandler;
    private final String channel;

    public RedisPublisher(RedisHandler redisHandler, String channel) {
        this.redisHandler = redisHandler;
        this.channel = channel;
    }

    public void publishToChannel(String message) {
        redisHandler.publish(channel, message);
    }
}