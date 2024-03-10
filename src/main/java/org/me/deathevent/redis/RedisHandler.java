package org.me.deathevent.redis;

import org.me.deathevent.DeathEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CompletableFuture;

public class RedisHandler {

    private final JedisPool jedisPool;

    public RedisHandler(DeathEvent plugin, String host, int port, String password) {
        plugin.info("Connecting to Redis...");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
        plugin.info("Connected to Redis!");

    }

    public void publish(String channel, String message) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }

    public void subscribe(JedisPubSub pubSub, String channel) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(pubSub, channel);
            }
        });
    }

    public void disconnect() {
        jedisPool.close();
    }
}