package us._donut_.litecoin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LitecoinMenu implements Listener {

    private Litecoin plugin;
    private Util util;
    private LitecoinManager litecoinManager;
    private Messages messages;
    private Sounds sounds;
    private Map<Player, Inventory> menus = new HashMap<>();
    private int[] evenSlots = {0, 2, 4, 6, 8, 18, 20, 22, 24, 26};
    private int[] oddSlots = {1, 3, 5, 7, 9, 17, 19, 21, 23, 25};
    private ItemStack darkBlueGlass;
    private ItemStack lightBlueGlass;
    private ItemStack transferLitecoinItem;
    private ItemStack exchangeLitecoinItem;
    private ItemStack buyLitecoinItem;
    private ItemStack miningLitecoinItem;
    private List<Player> playersExchanging = new ArrayList<>();
    private List<Player> playersTransferring = new ArrayList<>();
    private List<Player> playersBuying = new ArrayList<>();

    LitecoinMenu(Litecoin pluginInstance) {
        plugin = pluginInstance;
        util = plugin.getUtil();
        litecoinManager = plugin.getLitecoinManager();
        messages = plugin.getMessages();
        sounds = plugin.getSounds();
        reload();
        updateGlassInMenus();
    }

    void reload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTitle().equalsIgnoreCase(messages.getMessage("menu_title"))) { player.closeInventory(); }
        }
        menus.clear();
        darkBlueGlass = util.createItemStack(Material.STAINED_GLASS_PANE, (short) 11, " ", null);
        lightBlueGlass = util.createItemStack(Material.STAINED_GLASS_PANE, (short) 3, " ", null);
        transferLitecoinItem = util.createItemStack(Material.BOOK_AND_QUILL, (short) 0, messages.getMessage("transfer_item_name"), messages.getMessage("transfer_item_lore"));
        exchangeLitecoinItem = util.createItemStack(Material.GOLD_INGOT, (short) 0, messages.getMessage("exchange_item_name"), messages.getMessage("exchange_item_lore"));
        buyLitecoinItem = util.createItemStack(Material.EMERALD, (short) 0, messages.getMessage("buy_item_name"), messages.getMessage("buy_item_lore"));
        miningLitecoinItem = util.createItemStack(Material.DIAMOND_PICKAXE, (short) 0, messages.getMessage("mining_item_name"), messages.getMessage("mining_item_lore"));
    }

    List<Player> getPlayersExchanging() { return playersExchanging; }
    List<Player> getPlayersTransferring() { return playersTransferring; }
    List<Player> getPlayersBuying() { return playersBuying; }

    void open(Player player) {
        if (menus.containsKey(player)) {
            menus.get(player).setItem(11, util.getSkull(player.getUniqueId(), player.getName(), messages.getMessage("statistic_item_name"), messages.getMessage("statistic_item_lore").replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(player.getUniqueId())))).replace("{AMOUNT_SOLVED}", String.valueOf(litecoinManager.getPuzzlesSolved(player.getUniqueId()))).replace("{AMOUNT_MINED}", String.valueOf(litecoinManager.getLitecoinsMined(player.getUniqueId())))));
        } else {
            createMenu(player);
        }
        player.openInventory(menus.get(player));
    }

    private void createMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, messages.getMessage("menu_title"));
        menu.setItem(11, util.getSkull(player.getUniqueId(), player.getName(), messages.getMessage("statistic_item_name"), messages.getMessage("statistic_item_lore").replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(player.getUniqueId())))).replace("{AMOUNT_SOLVED}", String.valueOf(litecoinManager.getPuzzlesSolved(player.getUniqueId()))).replace("{AMOUNT_MINED}", String.valueOf(litecoinManager.getLitecoinsMined(player.getUniqueId())))));
        menu.setItem(12, transferLitecoinItem);
        menu.setItem(13, buyLitecoinItem);
        menu.setItem(14, exchangeLitecoinItem);
        menu.setItem(15, miningLitecoinItem);
        for (int slot : evenSlots) { menu.setItem(slot, darkBlueGlass); }
        for (int slot : oddSlots) { menu.setItem(slot, lightBlueGlass); }
        menus.put(player, menu);
    }

    private void updateGlassInMenus() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Inventory menu : menus.values()) {
                    if (menu.getItem(0).getDurability() == (short) 11) {
                        for (int slot : evenSlots) { menu.setItem(slot, lightBlueGlass); }
                        for (int slot : oddSlots) { menu.setItem(slot, darkBlueGlass); }
                    } else {
                        for (int slot : evenSlots) { menu.setItem(slot, darkBlueGlass); }
                        for (int slot : oddSlots) { menu.setItem(slot, lightBlueGlass); }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    private void sendCancelButton(Player player) {
        TextComponent cancelButton = new TextComponent(TextComponent.fromLegacyText(messages.getMessage("cancel_button")));
        cancelButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/litecoin cancel"));
        cancelButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getMessage("cancel_button_hover")).create()));
        player.spigot().sendMessage(cancelButton);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onDragInGUI(InventoryDragEvent event) {
        if (event.getInventory().getName() != null && event.getInventory().getName().equalsIgnoreCase(messages.getMessage("menu_title"))) { event.setCancelled(true); }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onMoveInGUI(InventoryMoveItemEvent event) {
        if (event.getDestination().getName() != null && event.getDestination().getName().equalsIgnoreCase(messages.getMessage("menu_title"))) { event.setCancelled(true); }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(messages.getMessage("menu_title"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getSlot() == 12) {
                if (!player.hasPermission("litecoin.gui.transfer")) { player.sendMessage(messages.getMessage("no_permission")); }
                player.closeInventory();
                player.playSound(player.getLocation(), sounds.getSound("click_transfer_item"), 1, 1);
                playersTransferring.add(player);
                player.sendMessage(messages.getMessage("begin_transfer").replace("{BALANCE}", String.valueOf(litecoinManager.getBalance(player.getUniqueId()))));
                sendCancelButton(player);
            } else if (event.getSlot() == 13) {
                if (!player.hasPermission("litecoin.gui.buy")) { player.sendMessage(messages.getMessage("no_permission")); }
                if (!plugin.getEconomy().hasEconomy()) {
                    player.playSound(player.getLocation(), sounds.getSound("no_economy"), 1, 1);
                    player.sendMessage(messages.getMessage("no_economy"));
                } else {
                    player.closeInventory();
                    player.playSound(player.getLocation(), sounds.getSound("click_buy_item"), 1, 1);
                    playersBuying.add(player);
                    player.sendMessage(messages.getMessage("begin_purchase").replace("{BANK}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(),litecoinManager.getAmountInBank()))).replace("{VALUE}", litecoinManager.getExchangeCurrencySymbol() + litecoinManager.getLitecoinValue()).replace("{TAX}", litecoinManager.getPurchaseTaxPercentage() + "%"));
                    sendCancelButton(player);
                }
            } else if (event.getSlot() == 14) {
                if (!player.hasPermission("litecoin.gui.sell")) { player.sendMessage(messages.getMessage("no_permission")); }
                player.closeInventory();
                if (!plugin.getEconomy().hasEconomy()) {
                    player.playSound(player.getLocation(), sounds.getSound("no_economy"), 1, 1);
                    player.sendMessage(messages.getMessage("no_economy"));
                } else {
                    player.playSound(player.getLocation(), sounds.getSound("click_exchange_item"), 1, 1);
                    playersExchanging.add(player);
                    player.sendMessage(messages.getMessage("begin_exchange").replace("{BALANCE}", String.valueOf(litecoinManager.getBalance(player.getUniqueId()))).replace("{VALUE}", litecoinManager.getExchangeCurrencySymbol() + litecoinManager.getLitecoinValue()));
                    sendCancelButton(player);
                }
            } else if (event.getSlot() == 15) {
                if (!player.hasPermission("litecoin.gui.mine")) { player.sendMessage(messages.getMessage("no_permission")); }
                player.playSound(player.getLocation(), sounds.getSound("click_mining_item"), 1, 1);
                plugin.getMining().openInterface(player);
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onQuit(PlayerQuitEvent event) {
        if (menus.containsKey(event.getPlayer())) { menus.remove(event.getPlayer()); }
        if (playersExchanging.contains(event.getPlayer())) { playersExchanging.remove(event.getPlayer()); }
        if (playersTransferring.contains(event.getPlayer())) { playersTransferring.remove(event.getPlayer()); }
        if (playersBuying.contains(event.getPlayer())) { playersBuying.remove(event.getPlayer()); }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().equalsIgnoreCase("/litecoin cancel")) {
            if (playersExchanging.contains(event.getPlayer()) || playersTransferring.contains(event.getPlayer()) || playersBuying.contains(event.getPlayer())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(messages.getMessage("cannot_use_commands"));
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (playersExchanging.contains(player)) {
            event.setCancelled(true);
            try {
                double exchangeAmount = Double.valueOf(event.getMessage());
                if (exchangeAmount > litecoinManager.getBalance(player.getUniqueId())) { player.sendMessage(messages.getMessage("not_enough_litecoins").replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(player.getUniqueId()))))); return; }
                    if (exchangeAmount <= 0) { player.sendMessage(messages.getMessage("invalid_number")); return; }
                    litecoinManager.withdraw(player.getUniqueId(), exchangeAmount);
                    litecoinManager.addToBank(exchangeAmount);
                    player.playSound(player.getLocation(), sounds.getSound("complete_exchange"), 1, 1);
                    player.sendMessage(messages.getMessage("complete_exchange").replace("{AMOUNT}", String.valueOf(exchangeAmount)).replace("{NEW_AMOUNT}", litecoinManager.getExchangeCurrencySymbol() + util.round(2, litecoinManager.getLitecoinValue() * exchangeAmount)));
                    plugin.getEconomy().depositPlayer(player, player.getWorld().getName(), litecoinManager.getLitecoinValue() * exchangeAmount);
                    playersExchanging.remove(player);
            } catch (NumberFormatException e) {
                player.sendMessage(messages.getMessage("invalid_number"));
            }

        } else if (playersTransferring.contains(player)) {
            event.setCancelled(true);
            String[] message = event.getMessage().split(" ");
            if (message.length != 2) { player.sendMessage(messages.getMessage("invalid_entry")); return; }
            Player recipient = Bukkit.getPlayer(message[0]);
            if (recipient == null) { player.sendMessage(messages.getMessage("not_online").replace("{PLAYER}", message[0])); return; }
            if (recipient.equals(player)) { player.sendMessage(messages.getMessage("cannot_transfer_to_self")); return; }
            try {
                double transferAmount = Double.valueOf(message[1]);
                if (transferAmount > litecoinManager.getBalance(player.getUniqueId())) { player.sendMessage(messages.getMessage("not_enough_litecoins").replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(player.getUniqueId()))))); return; }
                if (transferAmount <= 0) { player.sendMessage(messages.getMessage("invalid_number")); return; }
                litecoinManager.withdraw(player.getUniqueId(), transferAmount);
                litecoinManager.deposit(recipient.getUniqueId(), transferAmount);
                player.sendMessage(messages.getMessage("complete_transfer").replace("{AMOUNT}", String.valueOf(transferAmount)).replace("{RECIPIENT}", recipient.getName()));
                recipient.sendMessage(messages.getMessage("receive_litecoins").replace("{AMOUNT}", String.valueOf(transferAmount)).replace("{SENDER}", player.getName()));
                player.playSound(player.getLocation(), sounds.getSound("complete_transfer"), 1, 1);
                recipient.playSound(player.getLocation(), sounds.getSound("complete_transfer"), 1, 1);
                playersTransferring.remove(player);
            } catch (NumberFormatException e) {
                player.sendMessage(messages.getMessage("invalid_number"));
            }

        } else if (playersBuying.contains(player)) {
            event.setCancelled(true);
            try {
                double buyAmount = Double.valueOf(event.getMessage());
                if (buyAmount > litecoinManager.getAmountInBank()) { player.sendMessage(messages.getMessage("not_enough_in_bank").replace("{AMOUNT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getAmountInBank())))); return; }
                if (buyAmount <= 0) { player.sendMessage(messages.getMessage("invalid_number")); return; }
                double cost = (buyAmount * litecoinManager.getLitecoinValue()) * (1 + litecoinManager.getPurchaseTaxPercentage() / 100);
                if (cost > plugin.getEconomy().getBalance(player)) { player.sendMessage(messages.getMessage("not_enough_money")); return; }
                litecoinManager.deposit(player.getUniqueId(), buyAmount);
                litecoinManager.removeFromBank(buyAmount);
                player.playSound(player.getLocation(), sounds.getSound("complete_purchase"), 1, 1);
                player.sendMessage(messages.getMessage("complete_purchase").replace("{AMOUNT}", String.valueOf(buyAmount)).replace("{COST}", litecoinManager.getExchangeCurrencySymbol() + util.round(2, litecoinManager.getLitecoinValue() * buyAmount)).replace("{TAX}", litecoinManager.getExchangeCurrencySymbol() + util.round(2, litecoinManager.getPurchaseTaxPercentage() / 100 * cost)));
                plugin.getEconomy().withdrawPlayer(player, player.getWorld().getName(), cost);
                playersBuying.remove(player);
            } catch (NumberFormatException e) {
                player.sendMessage(messages.getMessage("invalid_number"));
            }
        }
    }
}
