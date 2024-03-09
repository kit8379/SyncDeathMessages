package org.me.deathevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public class DeathEvent extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Entity killer = player.getKiller();
        String playerName = player.getName();
        String messageKey = "default";

        if (killer instanceof Player killerPlayer) {
            ItemStack weapon = killerPlayer.getInventory().getItemInMainHand();
            String weaponName = weapon.hasItemMeta() && Objects.requireNonNull(weapon.getItemMeta()).hasDisplayName() ? weapon.getItemMeta().getDisplayName() : weapon.getType().name();
            messageKey = !weaponName.isEmpty() ? "with_weapon" : "default";
            List<String> commands = getConfig().getStringList("commands.PLAYER_KILL." + messageKey);
            executeCommands(commands, playerName, killerPlayer.getName(), weaponName, "");
        } else if (killer != null) {
            String mobName = killer.getCustomName() != null ? killer.getCustomName() : killer.getType().name();
            List<String> commands = getConfig().getStringList("commands.ENTITY_ATTACK." + messageKey);
            executeCommands(commands, playerName, "", "", mobName);
        } else {
            String causeOfDeath = player.getLastDamageCause() != null ? player.getLastDamageCause().getCause().name() : "UNKNOWN";
            List<String> commands = getConfig().getStringList("commands." + causeOfDeath + "." + messageKey);
            executeCommands(commands, playerName, "", "", "");
        }
    }

    private void executeCommands(List<String> commands, String playerName, String killerName, String weaponName, String mobName) {
        for (String command : commands) {
            command = command.replace("{player}", playerName).replace("{killer}", killerName).replace("{weapon}", weaponName).replace("{mob}", mobName);
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
        }
    }
}
