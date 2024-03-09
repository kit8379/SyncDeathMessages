package org.me.deathevent;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DeathEvent extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        String playerName = player.getDisplayName();

        if (killer != null) {
            ItemStack weapon = killer.getInventory().getItemInMainHand();
            boolean hasWeaponDisplayName = weapon.hasItemMeta() && Objects.requireNonNull(weapon.getItemMeta()).hasDisplayName();
            String messagePath = "messages.PLAYER_KILL." + (hasWeaponDisplayName ? "with_weapon" : "default");
            String message = ChatColor.translateAlternateColorCodes('&', getConfig().getString(messagePath))
                    .replace("{player}", playerName)
                    .replace("{killer}", killer.getDisplayName());
            if (hasWeaponDisplayName) {
                message = message.replace("{weapon}", weapon.getItemMeta().getDisplayName());
                broadcastMessage(message);
            } else {
                sendTranslatableMessage("item.minecraft." + weapon.getType().getKey().getKey(), message, "{weapon}");
            }
        } else if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
            Entity damager = damageEvent.getDamager();
            String message = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.ENTITY_ATTACK.default"))
                    .replace("{player}", playerName);
            if (damager.getCustomName() != null) {
                message = message.replace("{mob}", damager.getCustomName());
                broadcastMessage(message);
            } else {
                sendTranslatableMessage("entity.minecraft." + damager.getType().getKey().getKey(), message, "{mob}");
            }
        } else {
            String causeOfDeathKey = player.getLastDamageCause() != null ? player.getLastDamageCause().getCause().name() : "UNKNOWN";
            String message = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + causeOfDeathKey + ".default"))
                    .replace("{player}", playerName);
            broadcastMessage(message);
        }
    }

    private void sendTranslatableMessage(String translateKey, String messageTemplate, String placeholder) {
        String escapedPlaceholder = placeholder.replace("{", "\\{").replace("}", "\\}");
        String[] parts = messageTemplate.split(escapedPlaceholder);
        TextComponent beforePlaceholder = new TextComponent(parts[0]);
        TranslatableComponent translatableComponent = new TranslatableComponent(translateKey);
        TextComponent afterPlaceholder = parts.length > 1 ? new TextComponent(parts[1]) : new TextComponent("");
        BaseComponent[] finalMessage = new BaseComponent[]{beforePlaceholder, translatableComponent, afterPlaceholder};
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(finalMessage);
        }
    }


    private void broadcastMessage(String message) {
        String translatedMessage = ChatColor.translateAlternateColorCodes('&', message);
        getServer().broadcastMessage(translatedMessage);
    }
}