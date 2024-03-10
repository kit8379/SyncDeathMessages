package org.me.deathevent;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.me.deathevent.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class DeathEvent extends JavaPlugin implements Listener {
    private final Random random = new Random();

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
        String messageKey = "messages.PLAYER_KILL." + (hasWeaponDisplayName ? "with_weapon" : "default");
        List<String> messages = getConfig().getStringList(messageKey);
        String message = getRandomMessage(messages).replace("{player}", playerName).replace("{killer}", killer.getDisplayName());
        if (hasWeaponDisplayName) {
            TextComponent weaponComponent = new TextComponent(weapon.getItemMeta().getDisplayName());
            weaponComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new Text(weapon.getItemMeta().toString())));
            message = message.replace("{weapon}", weaponComponent.toLegacyText());
            broadcastMessage(message);
        } else {
            sendTranslatableMessage("item.minecraft." + weapon.getType().getKey().getKey(), message, "{weapon}");
        }
    }

    private void handleEntityAttack(String playerName, EntityDamageByEntityEvent damageEvent) {
        Entity damager = damageEvent.getDamager();
        List<String> messages = getConfig().getStringList("messages.ENTITY_ATTACK.default");
        String message = getRandomMessage(messages).replace("{player}", playerName);
        if (damager.getCustomName() != null) {
            message = message.replace("{mob}", damager.getCustomName());
            broadcastMessage(message);
        } else {
            sendTranslatableMessage("entity.minecraft." + damager.getType().getKey().getKey(), message, "{mob}");
        }
    }

    private void handleOtherDeaths(String playerName, PlayerDeathEvent event) {
        String causeOfDeathKey = event.getEntity().getLastDamageCause() != null ? event.getEntity().getLastDamageCause().getCause().name() : "UNKNOWN";
        List<String> messages = getConfig().getStringList("messages." + causeOfDeathKey + ".default");
        String message = getRandomMessage(messages).replace("{player}", playerName);
        broadcastMessage(message);
    }

    private void sendTranslatableMessage(String translateKey, String messageTemplate, String placeholder) {
        List<BaseComponent> components = new ArrayList<>();
        String[] parts = messageTemplate.split(Pattern.quote(placeholder));
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                components.add(new TranslatableComponent(translateKey));
            }
            components.add(new TextComponent(parts[i]));
        }
        BaseComponent[] finalMessage = components.toArray(new BaseComponent[0]);
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(finalMessage);
        }
    }

    private void broadcastMessage(String message) {
        getServer().broadcastMessage(Utils.colorize(message));
    }

    private String getRandomMessage(List<String> messages) {
        return messages.get(random.nextInt(messages.size()));
    }
}
