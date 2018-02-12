package us._donut_.litecoin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

class LitecoinCommand implements CommandExecutor, Listener {

    private Litecoin plugin;
    private Util util;
    private LitecoinManager litecoinManager;
    private LitecoinMenu litecoinMenu;
    private Mining mining;
    private Messages messages;
    private Sounds sounds;

    LitecoinCommand(Litecoin pluginInstance) {
        plugin = pluginInstance;
        util = plugin.getUtil();
        litecoinManager = plugin.getLitecoinManager();
        litecoinMenu = plugin.getLitecoinMenu();
        mining = plugin.getMining();
        messages = plugin.getMessages();
        sounds = plugin.getSounds();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("litecoin")) {

            if (args.length == 0) {
                if (sender instanceof ConsoleCommandSender) { sender.sendMessage(messages.getMessage("cannot_use_from_console")); return true; }
                Player player = (Player) sender;
                if (!player.hasPermission("litecoin.main")) { player.sendMessage(messages.getMessage("no_permission")); return true; }
                plugin.getLitecoinMenu().open(player);
            }

            else if (args[0].equalsIgnoreCase("cancel")) {
                if (sender instanceof ConsoleCommandSender) { sender.sendMessage(messages.getMessage("cannot_use_from_console")); return true; }
                Player player = (Player) sender;
                if (litecoinMenu.getPlayersExchanging().contains(player)) {
                    litecoinMenu.getPlayersExchanging().remove(player);
                    player.sendMessage(messages.getMessage("cancelled_exchange"));
                    player.playSound(player.getLocation(), sounds.getSound("cancelled_exchange"), 1, 1);
                } else if (litecoinMenu.getPlayersTransferring().contains(player)) {
                    litecoinMenu.getPlayersTransferring().remove(player);
                    player.sendMessage(messages.getMessage("cancelled_transfer"));
                    player.playSound(player.getLocation(), sounds.getSound("cancelled_transfer"), 1, 1);
                } else if (litecoinMenu.getPlayersBuying().contains(player)) {
                    litecoinMenu.getPlayersBuying().remove(player);
                    player.sendMessage(messages.getMessage("cancelled_transfer"));
                    player.playSound(player.getLocation(), sounds.getSound("cancelled_transfer"), 1, 1);
                } else {
                    player.sendMessage(messages.getMessage("nothing_to_cancel"));
                }
            }

            else if (args[0].equalsIgnoreCase("help")) {
                if (!sender.hasPermission("litecoin.help")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                sender.sendMessage(messages.getMessage("help_command"));
            }

            else if (args[0].equalsIgnoreCase("value")) {
                if (!sender.hasPermission("litecoin.value")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                sender.sendMessage(messages.getMessage("value_command").replace("{VALUE}", litecoinManager.getExchangeCurrencySymbol() + litecoinManager.getLitecoinValue()));
            }

            else if (args[0].equalsIgnoreCase("mine")) {
                if (sender instanceof ConsoleCommandSender) { sender.sendMessage(messages.getMessage("cannot_use_from_console")); return true; }
                Player player = (Player) sender;
                if (!player.hasPermission("litecoin.mine")) { player.sendMessage(messages.getMessage("no_permission")); return true; }
                mining.openInterface(player);

            }

            else if (args[0].equalsIgnoreCase("stats")) {
                if (sender instanceof ConsoleCommandSender) { sender.sendMessage(messages.getMessage("cannot_use_from_console")); return true; }
                Player player = (Player) sender;
                if (!player.hasPermission("litecoin.stats")) { player.sendMessage(messages.getMessage("no_permission")); return true; }
                if (args.length == 1) {
                    player.sendMessage(messages.getMessage("statistic_command_self").replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(player.getUniqueId())))).replace("{AMOUNT_SOLVED}", String.valueOf(litecoinManager.getPuzzlesSolved(player.getUniqueId()))).replace("{AMOUNT_MINED}", String.valueOf(litecoinManager.getLitecoinsMined(player.getUniqueId()))));
                } else {
                    OfflinePlayer statPlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (!litecoinManager.getPlayerFileConfigs().containsKey(statPlayer.getUniqueId())) { sender.sendMessage(messages.getMessage("never_joined").replace("{PLAYER}", args[1])); return true; }
                    player.sendMessage(messages.getMessage("statistic_command_other").replace("{PLAYER}", statPlayer.getName()).replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(statPlayer.getUniqueId())))).replace("{AMOUNT_SOLVED}", String.valueOf(litecoinManager.getPuzzlesSolved(statPlayer.getUniqueId()))).replace("{AMOUNT_MINED}", String.valueOf(litecoinManager.getLitecoinsMined(statPlayer.getUniqueId()))));
                }
            }

            else if (args[0].equalsIgnoreCase("sell")) {
                if (sender instanceof ConsoleCommandSender) { sender.sendMessage(messages.getMessage("cannot_use_from_console")); return true; }
                Player player = (Player) sender;
                if (!player.hasPermission("litecoin.sell")) { player.sendMessage(messages.getMessage("no_permission")); return true; }
                if (!plugin.getEconomy().hasEconomy()) { player.sendMessage(messages.getMessage("no_economy")); }
                if (args.length < 2) { player.sendMessage(messages.getMessage("sell_command_invalid_arg")); return true; }
                try {
                    double exchangeAmount = Double.valueOf(args[1]);
                    if (exchangeAmount > litecoinManager.getBalance(player.getUniqueId())) { player.sendMessage(messages.getMessage("not_enough_litecoins").replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(player.getUniqueId()))))); return true; }
                    if (exchangeAmount <= 0) { player.sendMessage(messages.getMessage("invalid_number")); return true; }
                    litecoinManager.withdraw(player.getUniqueId(), exchangeAmount);
                    litecoinManager.addToBank(exchangeAmount);
                    player.sendMessage(messages.getMessage("complete_exchange").replace("{AMOUNT}", String.valueOf(exchangeAmount)).replace("{NEW_AMOUNT}", litecoinManager.getExchangeCurrencySymbol() + util.round(2, litecoinManager.getLitecoinValue() * exchangeAmount)));
                    plugin.getEconomy().depositPlayer(player, player.getWorld().getName(), litecoinManager.getLitecoinValue() * exchangeAmount);
                } catch (NumberFormatException e) {
                    player.sendMessage(messages.getMessage("invalid_number"));
                }
            }

            else if (args[0].equalsIgnoreCase("transfer")) {
                if (sender instanceof ConsoleCommandSender) { sender.sendMessage(messages.getMessage("cannot_use_from_console")); return true; }
                Player player = (Player) sender;
                if (!player.hasPermission("litecoin.transfer")) { player.sendMessage(messages.getMessage("no_permission")); return true; }
                if (args.length < 3) { player.sendMessage(messages.getMessage("transfer_command_invalid_arg")); return true; }
                Player recipient = Bukkit.getPlayer(args[1]);
                if (recipient == null) { player.sendMessage(messages.getMessage("not_online").replace("{PLAYER}", args[1])); return true; }
                if (recipient.equals(player)) { player.sendMessage(messages.getMessage("cannot_transfer_to_self")); return true; }
                try {
                    double transferAmount = Double.valueOf(args[2]);
                    if (transferAmount > litecoinManager.getBalance(player.getUniqueId())) { player.sendMessage(messages.getMessage("not_enough_litecoins").replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(player.getUniqueId()))))); return true; }
                    if (transferAmount <= 0) { player.sendMessage(messages.getMessage("invalid_number")); return true; }
                    litecoinManager.withdraw(player.getUniqueId(), transferAmount);
                    litecoinManager.deposit(recipient.getUniqueId(), transferAmount);
                    player.sendMessage(messages.getMessage("complete_transfer").replace("{AMOUNT}", String.valueOf(transferAmount)).replace("{RECIPIENT}", recipient.getName()));
                    recipient.sendMessage(messages.getMessage("receive_litecoins").replace("{AMOUNT}", String.valueOf(transferAmount)).replace("{SENDER}", player.getName()));
                } catch (NumberFormatException e) {
                    player.sendMessage(messages.getMessage("invalid_number"));
                }
            }

            else if (args[0].equalsIgnoreCase("buy")) {
                if (sender instanceof ConsoleCommandSender) { sender.sendMessage(messages.getMessage("cannot_use_from_console")); return true; }
                Player player = (Player) sender;
                if (!player.hasPermission("litecoin.buy")) { player.sendMessage(messages.getMessage("no_permission")); return true; }
                if (!plugin.getEconomy().hasEconomy()) { player.sendMessage(messages.getMessage("no_economy")); }
                if (args.length < 2) { player.sendMessage(messages.getMessage("buy_command_invalid_arg")); return true; }
                try {
                    double buyAmount = Double.valueOf(args[1]);
                    if (buyAmount > litecoinManager.getAmountInBank()) { player.sendMessage(messages.getMessage("not_enough_in_bank").replace("{AMOUNT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getAmountInBank())))); return true; }
                    if (buyAmount <= 0) { player.sendMessage(messages.getMessage("invalid_number")); return true; }
                    double cost = (buyAmount * litecoinManager.getLitecoinValue()) * (1 + litecoinManager.getPurchaseTaxPercentage() / 100);
                    if (cost > plugin.getEconomy().getBalance(player)) { player.sendMessage(messages.getMessage("not_enough_money")); return true; }
                    litecoinManager.deposit(player.getUniqueId(), buyAmount);
                    litecoinManager.removeFromBank(buyAmount);
                    player.sendMessage(messages.getMessage("complete_purchase").replace("{AMOUNT}", String.valueOf(buyAmount)).replace("{COST}", litecoinManager.getExchangeCurrencySymbol() + util.round(2, litecoinManager.getLitecoinValue() * buyAmount)).replace("{TAX}", litecoinManager.getExchangeCurrencySymbol() + util.round(2, litecoinManager.getPurchaseTaxPercentage() / 100 * cost)));
                    plugin.getEconomy().withdrawPlayer(player, player.getWorld().getName(), cost);
                } catch (NumberFormatException e) {
                    player.sendMessage(messages.getMessage("invalid_number"));
                }
            }

            else if (args[0].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("litecoin.give")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                if (args.length < 3) { sender.sendMessage(messages.getMessage("give_command_invalid_arg")); return true; }
                OfflinePlayer recipient = Bukkit.getOfflinePlayer(args[1]);
                if (!litecoinManager.getPlayerFileConfigs().containsKey(recipient.getUniqueId())) { sender.sendMessage(messages.getMessage("never_joined").replace("{PLAYER}", args[1])); return true; }
                try {
                    double giveAmount = Double.valueOf(args[2]);
                    if (giveAmount <= 0) { sender.sendMessage(messages.getMessage("invalid_number")); return true; }
                    if (litecoinManager.getCirculationLimit() > 0 && litecoinManager.getLitecoinsInCirculation() + giveAmount >= litecoinManager.getCirculationLimit()) { sender.sendMessage(messages.getMessage("exceeds_limit").replace("{LIMIT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getCirculationLimit())))); return true; }
                    litecoinManager.deposit(recipient.getUniqueId(), giveAmount);
                    sender.sendMessage(messages.getMessage("give_command").replace("{AMOUNT}", String.valueOf(giveAmount)).replace("{PLAYER}", recipient.getName()));
                } catch (NumberFormatException e) {
                    sender.sendMessage(messages.getMessage("invalid_number"));
                }
            }

            else if (args[0].equalsIgnoreCase("remove")) {
                if (!sender.hasPermission("litecoin.remove")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                if (args.length < 3) { sender.sendMessage(messages.getMessage("remove_command_invalid_arg")); return true; }
                OfflinePlayer recipient = Bukkit.getOfflinePlayer(args[1]);
                if (!litecoinManager.getPlayerFileConfigs().containsKey(recipient.getUniqueId())) { sender.sendMessage(messages.getMessage("never_joined").replace("{PLAYER}", args[1])); return true; }
                try {
                    double removeAmount = Double.valueOf(args[2]);
                    if (removeAmount > litecoinManager.getBalance(recipient.getUniqueId())) { sender.sendMessage(messages.getMessage("other_player_not_enough_litecoins").replace("{PLAYER}", recipient.getName()).replace("{BALANCE}", String.valueOf(litecoinManager.getBalance(recipient.getUniqueId())))); return true; }
                    if (removeAmount <= 0) { sender.sendMessage(messages.getMessage("invalid_number")); return true; }
                    litecoinManager.withdraw(recipient.getUniqueId(), removeAmount);
                    sender.sendMessage(messages.getMessage("remove_command").replace("{AMOUNT}", String.valueOf(removeAmount)).replace("{PLAYER}", recipient.getName()));
                } catch (NumberFormatException e) {
                    sender.sendMessage(messages.getMessage("invalid_number"));
                }
            }

            else if (args[0].equalsIgnoreCase("set")) {
                if (!sender.hasPermission("litecoin.set")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                if (args.length < 3) { sender.sendMessage(messages.getMessage("set_command_invalid_arg")); return true; }
                OfflinePlayer recipient = Bukkit.getOfflinePlayer(args[1]);
                if (!litecoinManager.getPlayerFileConfigs().containsKey(recipient.getUniqueId())) { sender.sendMessage(messages.getMessage("never_joined").replace("{PLAYER}", args[1])); return true; }
                try {
                    double newBalance = Double.valueOf(args[2]);
                    if (newBalance < 0) { sender.sendMessage(messages.getMessage("invalid_number")); return true; }
                    if (litecoinManager.getCirculationLimit() > 0 && litecoinManager.getLitecoinsInCirculation() + (newBalance - litecoinManager.getBalance(recipient.getUniqueId())) >= litecoinManager.getCirculationLimit()) { sender.sendMessage(messages.getMessage("exceeds_limit").replace("{LIMIT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getCirculationLimit())))); return true; }
                    litecoinManager.setBalance(recipient.getUniqueId(), newBalance);
                    sender.sendMessage(messages.getMessage("set_command").replace("{AMOUNT}", String.valueOf(newBalance)).replace("{PLAYER}", recipient.getName()));
                } catch (NumberFormatException e) {
                    sender.sendMessage(messages.getMessage("invalid_number"));
                }
            }

            else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("litecoin.reload")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                plugin.reload();
                sender.sendMessage(messages.getMessage("reload_command"));
            }

            else if (args[0].equalsIgnoreCase("top")) {
                if (!sender.hasPermission("litecoin.top")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                List<OfflinePlayer> topPlayers = litecoinManager.getTopPlayers();
                StringBuilder top5 = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    if (i < topPlayers.size()) {
                        top5.append(messages.getMessage("top_command_format").replace("{PLACE}", String.valueOf(i + 1)).replace("{PLAYER}", topPlayers.get(i).getName()).replace("{BALANCE}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getBalance(topPlayers.get(i).getUniqueId())))));
                    } else {
                        top5.append(messages.getMessage("top_command_format").replace("{PLACE}", String.valueOf(i + 1)).replace("{PLAYER}", "N/A").replace("{BALANCE}", "0.0"));
                    }
                    if (i != 4) { top5.append("\n"); }
                }
                sender.sendMessage(messages.getMessage("top_command_header"));
                sender.sendMessage(top5.toString());
            }

            else if (args[0].equalsIgnoreCase("bank")) {
                if (!sender.hasPermission("litecoin.bank")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                sender.sendMessage(messages.getMessage("bank_command").replace("{AMOUNT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getAmountInBank()))));
            }

            else if (args[0].equalsIgnoreCase("tax")) {
                if (!sender.hasPermission("litecoin.tax")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                sender.sendMessage(messages.getMessage("tax_command").replace("{TAX}", litecoinManager.getPurchaseTaxPercentage() + "%"));
            }

            else if (args[0].equalsIgnoreCase("circulation")) {
                if (!sender.hasPermission("litecoin.circulation")) { sender.sendMessage(messages.getMessage("no_permission")); return true; }
                if (litecoinManager.getCirculationLimit() > 0) {
                    sender.sendMessage(messages.getMessage("circulation_command").replace("{AMOUNT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getLitecoinsInCirculation()))).replace("{LIMIT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getCirculationLimit()))));
                } else {
                    sender.sendMessage(messages.getMessage("circulation_command").replace("{AMOUNT}", String.valueOf(util.round(litecoinManager.getDisplayRoundAmount(), litecoinManager.getLitecoinsInCirculation()))).replace("{LIMIT}", "none"));
                }
            }

            else {
                sender.sendMessage(messages.getMessage("invalid_command"));
            }

            return true;
        }
        return false;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!messages.getMessage("command_name").equalsIgnoreCase("/litecoin") && event.getMessage().split(" ")[0].equalsIgnoreCase(messages.getMessage("command_name"))) {
            event.getPlayer().performCommand(event.getMessage().replace(messages.getMessage("command_name"), "/litecoin"));
            event.setCancelled(true);
        }
        if (event.getMessage().split(" ")[0].equalsIgnoreCase("/litecoin") && !messages.getMessage("command_name").equalsIgnoreCase("/litecoin")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
        }
    }
}
