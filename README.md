# SyncDeathMessages - Cross-Server Death Announcements

SyncDeathMessages is a Minecraft plugin that enables cross-server death message announcements using Redis. It is designed to have zero performance impact on the server, ensuring a seamless experience for players.

## Features

- 🌐 **Cross-Server Announcements:** Death messages are synchronized across multiple servers using Redis, providing a consistent experience for players in a networked environment.
- ⚡ **Fully Asynchronous:** The plugin operates asynchronously, ensuring that server performance is not impacted, even during high player activity.
- 🎨 **Customizable Messages:** Death messages can be customized to suit your server's theme and language.
- 🛠️ **Hover Event Support:** Weapon and mob names in death messages can display additional information when hovered over with the mouse pointer.
- 🌍 **Client Locale Item Names:** Item type names in death messages automatically adapt to the client's locale, eliminating the need for manual translation.

## Installation

1. Ensure you have a Redis server set up and accessible by your Minecraft servers.
2. Download the SyncDeathMessages plugin JAR file.
3. Place the JAR file in the `plugins` folder of your Minecraft server.
4. Restart the server to generate the default configuration files.
5. Restart the server again for the changes to take effect.
6. If you want to disable the original local death messages, use `/gamerule showDeathMessages false` in your server console.

## Usage

Once installed and configured, the plugin works automatically. When a player dies, their death message is broadcasted across all servers connected to the same Redis channel.

## Command
- `/sdmreload` - Reloads the plugin's configuration file. (Permission: `syncdeathmessages.admin`)

## Configuration

The plugin's configuration file allows you to set up the Redis connection details, customize death messages, and more. 

You can also visit here: https://github.com/kit8379/SyncDeathMessages/tree/master/languages to find your prefered languages and replace it with your existing messages section. 

The default config.yml messages also contain some emoji. If you want to use them, you need to have a plugin that supports emoji. You can use install our resource pack to see the emoji in the chat. You can download it from here:
https://github.com/kit8379/SyncDeathMessages/tree/master/resourcepack

Here's an example of the config.yml file:
```yaml
# It is also called a cluster id sometimes.
# If you have different set of servers and you want to have different death message channel for each server,
# you can set the server-group to the server name.
server-group: default

# Redis configuration
redis:
  host: localhost
  port: 6379
  password: ""

# Death Messages
messages:
  PLAYER_KILL:
    default:
      - "&c{player} &e was killed by &a{killer}&r 😵"
    with_weapon:
      - "&c{player} &e was killed by &a{killer} &e using &r{weapon}&r 🔪"
  ENTITY_ATTACK:
    default:
      - "&c{player} &e was killed by &a{mob}&r 💀"
  PROJECTILE:
    default:
      - "&c{player} &e was shot to death by a projectile&r 🏹"
  SUFFOCATION:
    default:
      - "&c{player} &e suffocated to death&r 😵"
  FALL:
    default:
      - "&c{player} &e fell to death&r 🕳️"
  FIRE:
    default:
      - "&c{player} &e was burnt to death&r 🔥"
  FIRE_TICK:
    default:
      - "&c{player} &e was burnt to death&r 🔥"
  MELTING:
    default:
      - "&c{player} &e melted&r 🌞"
  LAVA:
    default:
      - "&c{player} &e was burnt by lava&r 🌋"
  DROWNING:
    default:
      - "&c{player} &e drowned&r 🌊"
  BLOCK_EXPLOSION:
    default:
      - "&c{player} &e was blown up by an explosion&r 💣"
  ENTITY_EXPLOSION:
    default:
      - "&c{player} &e was blown up by an explosion&r 💣"
  VOID:
    default:
      - "&c{player} &e fell out of the world&r 🕳️"
  LIGHTNING:
    default:
      - "&c{player} &e was struck by lightning&r ⚡"
  SUICIDE:
    default:
      - "&c{player} &e committed suicide&r 😔"
  STARVATION:
    default:
      - "&c{player} &e starved to death&r 🍽️"
  POISON:
    default:
      - "&c{player} &e was poisoned to death&r ☠️"
  MAGIC:
    default:
      - "&c{player} &e was killed by magic&r ✨"
  WITHER:
    default:
      - "&c{player} &e withered away&r 💀"
  FALLING_BLOCK:
    default:
      - "&c{player} &e was squashed by a falling block&r 🧱"
  THORNS:
    default:
      - "&c{player} &e was killed by thorns&r 🌵"
  UNKNOWN:
    default:
      - "&c{player} &e died for unknown reasons&r ❓"

```

### Contributing:

🌟 Contributions are welcome! Whether you're a developer, a writer, or just a Minecraft enthusiast, you can help by:

- Submitting pull requests
- Reporting bugs and suggesting features
- Helping with documentation and the wiki
- Sharing NewSky with others

### Support:
👮‍♂️ You can join our Discord to receive community support!
Discord: https://discord.gg/UgHrFpQeEP