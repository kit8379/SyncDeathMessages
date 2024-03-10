package org.me.deathevent;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.me.deathevent.command.ReloadCommand;
import org.me.deathevent.listener.DeathEventListener;
import org.me.deathevent.redis.RedisHandler;
import org.me.deathevent.redis.RedisPublisher;
import org.me.deathevent.redis.RedisSubscriber;

import java.util.Objects;

public class DeathEvent extends JavaPlugin {

    private RedisHandler redisHandler;

    @Override
    public void onEnable() {
        info("DeathEvent is starting up...");
        initalize();
        info("DeathEvent has started successfully!");
    }

    public void initalize() {
        FileConfiguration config = getConfig();

        // Initialize RedisHandler
        redisHandler = new RedisHandler(this, config.getString("redis.host"), config.getInt("redis.port"), config.getString("redis.password"));

        // Get the server-group from the config
        String channel = "deathmessages-" + config.getString("server-group");

        // Initialize RedisPublisher and RedisSubscriber
        RedisPublisher redisPublisher = new RedisPublisher(this, redisHandler, channel);
        RedisSubscriber redisSubscriber = new RedisSubscriber(this, redisHandler);

        // Subscribe to the deathMessages channel
        redisSubscriber.subscribeToChannel(channel);

        // Register the DeathEventListener with the RedisPublisher
        this.getServer().getPluginManager().registerEvents(new DeathEventListener(this, redisPublisher), this);

        // Register the /deathevent reload command
        Objects.requireNonNull(this.getCommand("deatheventreload")).setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        info("DeathEvent is shutting down...");
        shutdown();
        info("DeathEvent has shut down successfully!");
    }

    public void shutdown() {
        // Disconnect from Redis
        if (redisHandler != null) {
            redisHandler.disconnect();
        }
    }

    public void reload() {
        info("DeathEvent is reloading...");
        shutdown();
        reloadConfig();
        initalize();
        info("DeathEvent has reloaded successfully!");
    }

    public void info(String message) {
        getLogger().info(message);
    }

    public void debug(String message) {
        getLogger().info(message);
    }
}
