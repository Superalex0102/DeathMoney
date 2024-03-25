package me.superalex0102.deathmoney;

import me.superalex0102.deathmoney.listeners.PlayerDeathEventListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DeathMoney extends JavaPlugin {

    private static DeathMoney instance;
    private Economy econ = null;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        this.createConfigFiles();

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerDeathEventListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.econ = rsp.getProvider();
        return this.econ != null;
    }

    private void createConfigFiles() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static DeathMoney getInstance() {
        return instance;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public Economy getEconomy() {
        return this.econ;
    }
}
