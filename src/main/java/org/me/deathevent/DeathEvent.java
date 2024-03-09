package org.me.deathevent;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
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
            handlePlayerKill(playerName, killer);
        } else if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
            handleEntityAttack(playerName, damageEvent);
        } else {
            handleOtherDeaths(playerName, event);
        }
    }

    private void handlePlayerKill(String playerName, Player killer) {
        ItemStack weapon = killer.getInventory().getItemInMainHand();
        boolean hasWeaponDisplayName = weapon.hasItemMeta() && Objects.requireNonNull(weapon.getItemMeta()).hasDisplayName();
        String messageKey = "message.PLAYER_KILL." + (hasWeaponDisplayName ? "with_weapon" : "default");
        String message = Objects.requireNonNull(getConfig().getString(messageKey)).replace("{player}", playerName).replace("{killer}", killer.getDisplayName());
        if (hasWeaponDisplayName) {
            message = message.replace("{weapon}", weapon.getItemMeta().getDisplayName());
            broadcastMessage(message);
        } else {
            sendTranslatableMessage("item.minecraft." + weapon.getType().getKey().getKey(), message, "{weapon}");
        }
    }

    private void handleEntityAttack(String playerName, EntityDamageByEntityEvent damageEvent) {
        Entity damager = damageEvent.getDamager();
        String message = Objects.requireNonNull(getConfig().getString("message.ENTITY_ATTACK.default")).replace("{player}", playerName);
        if (damager.getCustomName() != null) {
            message = message.replace("{mob}", damager.getCustomName());
            broadcastMessage(message);
        } else {
            sendTranslatableMessage("entity.minecraft." + damager.getType().getKey().getKey(), message, "{mob}");
        }
    }

    private void handleOtherDeaths(String playerName, PlayerDeathEvent event) {
        String causeOfDeathKey = event.getEntity().getLastDamageCause() != null ? event.getEntity().getLastDamageCause().getCause().name() : "UNKNOWN";
        String message = Objects.requireNonNull(getConfig().getString("message." + causeOfDeathKey + ".default")).replace("{player}", playerName);
        broadcastMessage(message);
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
        getServer().broadcastMessage(message);
    }
}