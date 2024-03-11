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

## Usage

Once installed and configured, the plugin works automatically. When a player dies, their death message is broadcasted across all servers connected to the same Redis channel.

## Contributing

Contributions are welcome! Feel free to submit pull requests, report bugs, suggest features, or help with documentation.

## Support

If you encounter any issues or have questions, please open an issue on the plugin's GitHub repository.
