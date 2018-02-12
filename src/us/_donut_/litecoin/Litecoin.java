package us._donut_.litecoin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Litecoin extends JavaPlugin {

    private Util util;
    private LitecoinManager litecoinManager;
    private Mining mining;
    private LitecoinMenu litecoinMenu;
    private File configFile;
    private YamlConfiguration litecoinConfig;
    private ServerEconomy economy;
    private Messages messages;
    private Sounds sounds;
    private static LitecoinAPI api;

    @Override
    public void onEnable() {
        util = new Util(this);

        configFile = new File(getDataFolder(), "config.yml");
        litecoinConfig = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) { getLogger().info("Generated config.yml!"); }
        if (new File(getDataFolder(), "Player Data").mkdirs()) { getLogger().info("Generated player data folder!"); }
        util.loadConfigDefaults();

        economy = new ServerEconomy(this);
        messages = new Messages(this);
        sounds = new Sounds(this);
        getServer().getPluginManager().registerEvents(litecoinManager = new LitecoinManager(this), this);
        getServer().getPluginManager().registerEvents(mining = new Mining(this), this);
        getServer().getPluginManager().registerEvents(litecoinMenu = new LitecoinMenu(this), this);
        LitecoinCommand litecoinCommand;
        getServer().getPluginManager().registerEvents(litecoinCommand = new LitecoinCommand(this), this);
        getCommand("litecoin").setExecutor(litecoinCommand);
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) { new RegisterPlaceholderAPI(this).hook(); }
        if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) { new RegisterMVdWPlaceholderAPI(this); }
        api = new LitecoinAPI(this);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

    void reload() {
        configFile = new File(getDataFolder(), "config.yml");
        litecoinConfig = YamlConfiguration.loadConfiguration(configFile);
        messages.reload();
        sounds.reload();
        economy.reload();
        litecoinMenu.reload();
        litecoinManager.reload();
        mining.reload();
    }

    Util getUtil() { return util; }
    LitecoinManager getLitecoinManager() { return litecoinManager; }
    Mining getMining() { return mining; }
    LitecoinMenu getLitecoinMenu() { return litecoinMenu; }
    ServerEconomy getEconomy() { return economy; }
    File getConfigFile() { return configFile; }
    YamlConfiguration getLitecoinConfig() { return litecoinConfig; }
    Messages getMessages() { return messages; }
    Sounds getSounds() { return sounds; }

    public static LitecoinAPI getAPI() { return api; }
}
