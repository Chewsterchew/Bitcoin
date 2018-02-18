package us._donut_.litecoin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;

class Util {

    private Litecoin plugin;
    private Map<UUID, String> skullTextures = new HashMap<>();

    Util(Litecoin pluginInstance) {
        plugin = pluginInstance;
    }

    void saveYml(File file, YamlConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadConfigDefaults() {
        YamlConfiguration litecoinConfig = plugin.getLitecoinConfig();
        if (!litecoinConfig.contains("litecoin_value")) { litecoinConfig.set("litecoin_value", 1000); }
        if (!litecoinConfig.contains("litecoin_min_value")) { litecoinConfig.set("litecoin_min_value", 0); }
        if (!litecoinConfig.contains("amount_in_bank")) { litecoinConfig.set("amount_in_bank", 0); }
        if (!litecoinConfig.contains("litecoin_display_rounding")) { litecoinConfig.set("litecoin_display_rounding", 5); }
        if (!litecoinConfig.contains("purchase_tax_percentage")) { litecoinConfig.set("purchase_tax_percentage", 15); }
        if (!litecoinConfig.contains("exchange_currency_symbol")) { litecoinConfig.set("exchange_currency_symbol", "$"); }
        if (!litecoinConfig.contains("min_litecoin_value_fluctuation")) { litecoinConfig.set("min_litecoin_value_fluctuation", 0); }
        if (!litecoinConfig.contains("max_litecoin_value_fluctuation")) { litecoinConfig.set("max_litecoin_value_fluctuation", 100); }
        if (!litecoinConfig.contains("fluctuation_frequency")) { litecoinConfig.set("fluctuation_frequency", "6:00"); }
        if (!litecoinConfig.contains("min_mining_reward")) { litecoinConfig.set("min_mining_reward", 10); }
        if (!litecoinConfig.contains("max_mining_reward")) { litecoinConfig.set("max_mining_reward", 50); }
        if (!litecoinConfig.contains("circulation_limit")) { litecoinConfig.set("circulation_limit", -1); }
        if (!litecoinConfig.contains("world")) { litecoinConfig.set("world", "world"); }
        if (!litecoinConfig.contains("new_mining_puzzle_delay")) { litecoinConfig.set("new_mining_puzzle_delay", 0); }
        if (!litecoinConfig.contains("use_playerpoints")) { litecoinConfig.set("use_playerpoints", false); }
        if (!litecoinConfig.contains("use_pointsapi")) { litecoinConfig.set("use_pointsapi", false); }
        saveYml(plugin.getConfigFile(), litecoinConfig);
    }

    ItemStack createItemStack(Material item, Short dataValue, String name, String lore) {
        ItemStack itemStack = new ItemStack(item, 1, dataValue);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) { itemMeta.setLore(Arrays.asList(lore.split("\n"))); }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @SuppressWarnings("deprecation")
    ItemStack getSkull(UUID playerUUID, String playerName, String displayName, String lore) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        if (plugin.getServer().getOnlineMode()) {
            if (!skullTextures.containsKey(playerUUID)) {
                try {
                    URL address = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + playerUUID.toString().replace("-", ""));
                    InputStreamReader pageInput = new InputStreamReader(address.openStream());
                    BufferedReader source = new BufferedReader(pageInput);
                    String sourceLine = source.readLine();
                    skullTextures.put(playerUUID, sourceLine.split("\"")[17]);
                    UUID hashAsId = new UUID(skullTextures.get(playerUUID).hashCode(), skullTextures.get(playerUUID).hashCode());
                    skull = Bukkit.getUnsafe().modifyItemStack(skull, "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + skullTextures.get(playerUUID) + "\"}]}}}");
                } catch (IOException e) {
                    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                    skullMeta.setOwner(playerName);
                }
            }
        } else {
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwner(playerName);
        }
        ItemMeta skullMeta = skull.getItemMeta();
        skullMeta.setDisplayName(displayName);
        String[] multiLineLore = lore.split("\n");
        skullMeta.setLore(Arrays.asList(multiLineLore));
        skull.setItemMeta(skullMeta);
        return skull;
    }

    String colorMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    double round(int places, double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    Long getTicksFromTime(String time) {
        int hours;
        int minutes;
        try {
            hours = Integer.valueOf(time.split(":")[0]);
            minutes = Integer.valueOf(time.split(":")[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        if (hours > 24 || hours < 0 || minutes > 59 || minutes < 0) { return null; }
        return (long) (((18001 + (hours * 1000) + ((minutes / 60.0) * 1000))) % 24000);
    }
}
