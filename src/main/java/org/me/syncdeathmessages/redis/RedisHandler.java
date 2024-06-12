package org.me.syncdeathmessages.redis;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.me.syncdeathmessages.SyncDeathMessages;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class RedisHandler {
    private final SyncDeathMessages plugin;
    private final JedisPool jedisPool;

    public RedisHandler(SyncDeathMessages plugin, String host, int port, String password) {
        this.plugin = plugin;
        plugin.info("Connecting to Redis...");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
        plugin.info("Connected to Redis!");
    }

    public void publish(String channel, String message) {
        plugin.getFoliaLib().getImpl().runAsync((WrappedTask task) -> {
            try (Jedis jedis = getJedis()) {
                jedis.publish(channel, message);
            }
        });
    }

    public void subscribe(JedisPubSub pubSub, String channel) {
        plugin.getFoliaLib().getImpl().runAsync((WrappedTask task) -> {
            try (Jedis jedis = getJedis()) {
                jedis.subscribe(pubSub, channel);
            }
        });
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void disconnect() {
        if (jedisPool != null) {
            jedisPool.close();
        }
        plugin.info("Disconnected from Redis!");
    }
}
