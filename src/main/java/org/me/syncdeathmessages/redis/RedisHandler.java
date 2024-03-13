package org.me.syncdeathmessages.redis;

import org.me.syncdeathmessages.SyncDeathMessages;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CompletableFuture;

public class RedisHandler {
    private final SyncDeathMessages plugin;
    private final JedisPool jedisPool;

    public RedisHandler(SyncDeathMessages plugin, String host, int port, String password) {
        this.plugin = plugin;
        plugin.getLogger().info("Connecting to Redis...");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
        plugin.info("Connected to Redis!");
    }

    public void publish(String channel, String message) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = getJedis()) {
                jedis.publish(channel, message);
            }
        });
    }

    public void subscribe(JedisPubSub pubSub, String channel) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = getJedis()) {
                jedis.subscribe(pubSub, channel);
            }
        });
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void disconnect() {
        jedisPool.close();
    }
}
