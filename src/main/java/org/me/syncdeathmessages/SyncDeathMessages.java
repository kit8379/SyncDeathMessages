package org.me.syncdeathmessages;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.me.syncdeathmessages.command.ReloadCommand;
import org.me.syncdeathmessages.listener.DeathEventListener;
import org.me.syncdeathmessages.redis.RedisHandler;
import org.me.syncdeathmessages.redis.RedisPublisher;
import org.me.syncdeathmessages.redis.RedisSubscriber;

import java.util.Objects;

public class SyncDeathMessages extends JavaPlugin {

    private FoliaLib foliaLib;
    private RedisHandler redisHandler;
    private RedisSubscriber redisSubscriber;
    private String channel;


    @Override
    public void onEnable() {
        info("SyncDeathMessages is starting up...");
        initalize();
        info("SyncDeathMessages has started successfully!");
    }

    public void initalize() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        foliaLib = new FoliaLib(this);

        // Initialize RedisHandler
        redisHandler = new RedisHandler(this, config.getString("redis.host"), config.getInt("redis.port"), config.getString("redis.password"));

        // Get the server-group from the config
        channel = "deathmessages-" + config.getString("server-group");

        // Initialize RedisPublisher and RedisSubscriber
        RedisPublisher redisPublisher = new RedisPublisher(redisHandler, channel);
        this.redisSubscriber = new RedisSubscriber(this, redisHandler);
        // Subscribe to the deathMessages channel
        redisSubscriber.subscribeToChannel(channel);

        this.getServer().getPluginManager().registerEvents(new DeathEventListener(this, redisPublisher), this);
        Objects.requireNonNull(this.getCommand("sdmreload")).setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        info("SyncDeathMessages is shutting down...");
        shutdown();
        info("SyncDeathMessages has shut down successfully!");
    }

    public void shutdown() {
        // Unsubscribe from the deathMessages channel
        if (redisSubscriber != null) {
            redisSubscriber.unsubscribeFromChannel(channel);
        }

        // Disconnect from Redis
        if (redisHandler != null) {
            redisHandler.disconnect();
        }
    }

    public void reload() {
        info("SyncDeathMessages config is reloading...");
        reloadConfig();
        info("SyncDeathMessages config has reloaded successfully!");
    }

    public void info(String message) {
        getLogger().info(message);
    }

    public FoliaLib getFoliaLib() {
        return foliaLib;
    }
}
