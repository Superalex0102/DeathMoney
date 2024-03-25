package me.superalex0102.deathmoney.listeners;

import me.superalex0102.deathmoney.DeathMoney;
import me.superalex0102.deathmoney.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.text.DecimalFormat;

public class PlayerDeathEventListener implements Listener {

    private final DeathMoney plugin;

    public PlayerDeathEventListener(DeathMoney plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (this.plugin.getEconomy().getBalance(e.getEntity()) > 0) {
            double amount = this.plugin.getConfig().getDouble("settings.amount");
            Player p = e.getEntity();

            if (this.plugin.getConfig().getString("settings.mode").equalsIgnoreCase("DYNAMIC")) {
                DecimalFormat df = new DecimalFormat("#.##");
                amount = Double.parseDouble(df.format(this.plugin.getEconomy().getBalance(e.getEntity()) * amount));
            }
            else if (!this.plugin.getConfig().getString("settings.mode").equalsIgnoreCase("STATIC")) {
                Bukkit.getLogger().warning("Mode is invalid! Use either DYNAMIC or STATIC!");
                return;
            }

            this.plugin.getEconomy().withdrawPlayer(p, amount);
            p.sendMessage(TextUtils.applyColor(this.plugin.getConfig().getString("messages.deathMessage")
                    .replaceAll("%amount%", String.valueOf(amount))));
            if (this.plugin.getConfig().getBoolean("settings.consoleLogging")) {
                this.plugin.getServer().getLogger().info(this.plugin.getConfig().getString("messages.deathMessageConsole")
                        .replaceAll("%player%", p.getName())
                        .replaceAll("%amount%", String.valueOf(amount)));
            }
        }
    }
}
