package us._donut_.litecoin;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

class Messages {

    private Litecoin plugin;
    private Util util;
    private File messagesFile;
    private YamlConfiguration messagesConfig;
    private Map<String, String> messages = new HashMap<>();

    Messages(Litecoin pluginInstance) {
        plugin = pluginInstance;
        util = pluginInstance.getUtil();
        reload();
    }

    void reload() {
        messages.clear();
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        if (!messagesFile.exists()) {
            messagesConfig.options().header("Messages accept color codes." + System.lineSeparator() + "Any message can be multiple lines (see bottom messages for examples)." + System.lineSeparator() + "{VARIABLES} are filled in with their respective values, they can only be used in the messages they are in by default");
            plugin.getLogger().info("Generated messages.yml!");
        }
        loadDefaults();
        loadAllMessages();
    }

    String getMessage(String message) {
        return messages.get(message);
    }

    private void loadDefaults() {
        if (!messagesConfig.contains("command_name")) { messagesConfig.set("command_name", "/litecoin"); }
        if (!messagesConfig.contains("complete_transfer")) { messagesConfig.set("complete_transfer", "&aSuccessfully transferred {AMOUNT} litecoins to &2{RECIPIENT}."); }
        if (!messagesConfig.contains("complete_purchase")) { messagesConfig.set("complete_purchase", "&aSuccessfully bought {AMOUNT} litecoins for {COST} plus {TAX} in tax."); }
        if (!messagesConfig.contains("receive_litecoins")) { messagesConfig.set("receive_litecoins", "&aYou received {AMOUNT} litecoins from &2{SENDER}."); }
        if (!messagesConfig.contains("invalid_number")) { messagesConfig.set("invalid_number", "&cInvalid number."); }
        if (!messagesConfig.contains("cannot_transfer_to_self")) { messagesConfig.set("cannot_transfer_to_self", "&cYou cannot transfer litecoins to yourself."); }
        if (!messagesConfig.contains("not_enough_litecoins")) { messagesConfig.set("not_enough_litecoins", "&cYou only have {BALANCE} litecoins."); }
        if (!messagesConfig.contains("not_enough_in_bank")) { messagesConfig.set("not_enough_in_bank", "&cThe bank only has {AMOUNT} litecoins."); }
        if (!messagesConfig.contains("not_enough_money")) { messagesConfig.set("not_enough_money", "&cYou cannot afford this many litecoins."); }
        if (!messagesConfig.contains("not_online")) { messagesConfig.set("not_online", "&4{PLAYER} &cis not online."); }
        if (!messagesConfig.contains("invalid_entry")) { messagesConfig.set("invalid_entry", "&cInvalid entry."); }
        if (!messagesConfig.contains("complete_exchange")) { messagesConfig.set("complete_exchange", "&aSuccessfully sold {AMOUNT} litecoins for {NEW_AMOUNT}."); }
        if (!messagesConfig.contains("no_economy")) { messagesConfig.set("no_economy", "&cNo economy plugin was detected."); }
        if (!messagesConfig.contains("cannot_use_commands")) { messagesConfig.set("cannot_use_commands", "&cYou cannot use commands at this time."); }
        if (!messagesConfig.contains("cancel_button")) { messagesConfig.set("cancel_button", "&c&l[Cancel]"); }
        if (!messagesConfig.contains("cancel_button_hover")) { messagesConfig.set("cancel_button_hover", "&cClick to cancel"); }
        if (!messagesConfig.contains("cancelled_transfer")) { messagesConfig.set("cancelled_transfer", "&cCancelled transfer."); }
        if (!messagesConfig.contains("cancelled_exchange")) { messagesConfig.set("cancelled_exchange", "&cCancelled exchange."); }
        if (!messagesConfig.contains("nothing_to_cancel")) { messagesConfig.set("nothing_to_cancel", "&cNothing to cancel."); }
        if (!messagesConfig.contains("cannot_use_from_console")) { messagesConfig.set("cannot_use_from_console", "&cYou cannot use this command from console."); }
        if (!messagesConfig.contains("generating_puzzle")) { messagesConfig.set("generating_puzzle", "&aA new puzzle is being generated..."); }
        if (!messagesConfig.contains("generated_puzzle")) { messagesConfig.set("generated_puzzle", "&aPuzzle generated, be the first player to solve it to earn litecoins!"); }
        if (!messagesConfig.contains("reward")) { messagesConfig.set("reward", "&aCongrats, you were rewarded {REWARD} litecoins!"); }
        if (!messagesConfig.contains("menu_title")) { messagesConfig.set("menu_title", "&9&lLitecoin Menu"); }
        if (!messagesConfig.contains("statistic_item_name")) { messagesConfig.set("statistic_item_name", "&9&lStatistics"); }
        if (!messagesConfig.contains("transfer_item_name")) { messagesConfig.set("transfer_item_name", "&9&lTransfer Litecoins"); }
        if (!messagesConfig.contains("transfer_item_lore")) { messagesConfig.set("transfer_item_lore", "&3Transfer litecoins to another account"); }
        if (!messagesConfig.contains("exchange_item_name")) { messagesConfig.set("exchange_item_name", "&9&lSell Litecoins"); }
        if (!messagesConfig.contains("exchange_item_lore")) { messagesConfig.set("exchange_item_lore", "&3Sell litecoins to the bank"); }
        if (!messagesConfig.contains("buy_item_name")) { messagesConfig.set("buy_item_name", "&9&lBuy Litecoins"); }
        if (!messagesConfig.contains("buy_item_lore")) { messagesConfig.set("buy_item_lore", "&3Buy litecoins from the bank"); }
        if (!messagesConfig.contains("mining_item_name")) { messagesConfig.set("mining_item_name", "&9&lLitecoin Mining"); }
        if (!messagesConfig.contains("mining_item_lore")) { messagesConfig.set("mining_item_lore", "&3Solve puzzles to earn litecoins"); }
        if (!messagesConfig.contains("mining_menu_title")) { messagesConfig.set("mining_menu_title", "&9&lLitecoin Mining"); }
        if (!messagesConfig.contains("reset_item_name")) { messagesConfig.set("reset_item_name", "&4&lReset"); }
        if (!messagesConfig.contains("reset_item_lore")) { messagesConfig.set("reset_item_lore", "&cClick to reset the tiles"); }
        if (!messagesConfig.contains("solve_item_name")) { messagesConfig.set("solve_item_name", "&2&lSolve"); }
        if (!messagesConfig.contains("solve_item_lore")) { messagesConfig.set("solve_item_lore", "&aClick when you think you solved the puzzle"); }
        if (!messagesConfig.contains("exit_item_name")) { messagesConfig.set("exit_item_name", "&4&lExit"); }
        if (!messagesConfig.contains("white_tile")) { messagesConfig.set("white_tile", "&9&lWhite"); }
        if (!messagesConfig.contains("orange_tile")) { messagesConfig.set("orange_tile", "&9&lOrange"); }
        if (!messagesConfig.contains("magenta_tile")) { messagesConfig.set("magenta_tile", "&9&lMagenta"); }
        if (!messagesConfig.contains("light_blue_tile")) { messagesConfig.set("light_blue_tile", "&9&lLight Blue"); }
        if (!messagesConfig.contains("yellow_tile")) { messagesConfig.set("yellow_tile", "&9&lYellow"); }
        if (!messagesConfig.contains("lime_tile")) { messagesConfig.set("lime_tile", "&9&lLime"); }
        if (!messagesConfig.contains("pink_tile")) { messagesConfig.set("pink_tile", "&9&lPink"); }
        if (!messagesConfig.contains("gray_tile")) { messagesConfig.set("gray_tile", "&9&lGray"); }
        if (!messagesConfig.contains("light_gray_tile")) { messagesConfig.set("light_gray_tile", "&9&lLight Gray"); }
        if (!messagesConfig.contains("cyan_tile")) { messagesConfig.set("cyan_tile", "&9&lCyan"); }
        if (!messagesConfig.contains("purple_tile")) { messagesConfig.set("purple_tile", "&9&lPurple"); }
        if (!messagesConfig.contains("blue_tile")) { messagesConfig.set("blue_tile", "&9&lBlue"); }
        if (!messagesConfig.contains("brown_tile")) { messagesConfig.set("brown_tile", "&9&lBrown"); }
        if (!messagesConfig.contains("green_tile")) { messagesConfig.set("green_tile", "&9&lGreen"); }
        if (!messagesConfig.contains("red_tile")) { messagesConfig.set("red_tile", "&9&lRed"); }
        if (!messagesConfig.contains("black_tile")) { messagesConfig.set("black_tile", "&9&lBlack"); }
        if (!messagesConfig.contains("no_permission")) { messagesConfig.set("no_permission", "&cYou do not have permission to use this command."); }
        if (!messagesConfig.contains("invalid_command")) { messagesConfig.set("invalid_command", "&cInvalid command."); }
        if (!messagesConfig.contains("never_joined")) { messagesConfig.set("never_joined", "&4{PLAYER} &chas never joined the server."); }
        if (!messagesConfig.contains("give_command")) { messagesConfig.set("give_command", "&aGave {AMOUNT} litecoins to balance of &2{PLAYER}."); }
        if (!messagesConfig.contains("give_command_invalid_arg")) { messagesConfig.set("give_command_invalid_arg", "&cInvalid argument."); }
        if (!messagesConfig.contains("remove_command")) { messagesConfig.set("remove_command", "&aRemoved {AMOUNT} litecoins from balance of &2{PLAYER}."); }
        if (!messagesConfig.contains("remove_command_invalid_arg")) { messagesConfig.set("remove_command_invalid_arg", "&cInvalid argument."); }
        if (!messagesConfig.contains("other_player_not_enough_litecoins")) { messagesConfig.set("other_player_not_enough_litecoins", "&c{PLAYER} only has {BALANCE} litecoins."); }
        if (!messagesConfig.contains("set_command")) { messagesConfig.set("set_command", "&aSet balance of &2{PLAYER} &ato {AMOUNT} litecoins."); }
        if (!messagesConfig.contains("set_command_invalid_arg")) { messagesConfig.set("set_command_invalid_arg", "&cInvalid argument."); }
        if (!messagesConfig.contains("tax_command")) { messagesConfig.set("tax_command", "&3Purchase tax: &b{TAX}"); }
        if (!messagesConfig.contains("top_command_header")) { messagesConfig.set("top_command_header", "&9<<< Litecoin Top Players >>>"); }
        if (!messagesConfig.contains("top_command_format")) { messagesConfig.set("top_command_format", "&3{PLACE}. {PLAYER}: &b{BALANCE} litecoins"); }
        if (!messagesConfig.contains("reload_command")) { messagesConfig.set("reload_command", "&aSuccessfully reloaded litecoin."); }
        if (!messagesConfig.contains("bank_command")) { messagesConfig.set("bank_command", "&3Amount of litecoins in bank: &b{AMOUNT}"); }
        if (!messagesConfig.contains("value_command")) { messagesConfig.set("value_command", "&3Current value of 1 litecoin: &b{VALUE}"); }
        if (!messagesConfig.contains("buy_command_invalid_arg")) { messagesConfig.set("buy_command_invalid_arg", "&cInvalid argument."); }
        if (!messagesConfig.contains("sell_command_invalid_arg")) { messagesConfig.set("sell_command_invalid_arg", "&cInvalid argument."); }
        if (!messagesConfig.contains("transfer_command_invalid_arg")) { messagesConfig.set("transfer_command_invalid_arg", "&cInvalid argument."); }
        if (!messagesConfig.contains("exceeds_limit")) { messagesConfig.set("exceeds_limit", "&cThis amount would cause the number of litecoins in circulation to exceed the limit of {LIMIT} litecoins."); }

        if (!messagesConfig.contains("black_market_title")) { messagesConfig.set("black_market_title", "&9&lLitecoin Black Market"); }
        if (!messagesConfig.contains("black_market_command_invalid_arg")) { messagesConfig.set("black_market_command_invalid_arg", "&cInvalid argument."); }
        if (!messagesConfig.contains("black_market_set_item")) { messagesConfig.set("black_market_set_item", "&aSuccessfully set item in black market."); }
        if (!messagesConfig.contains("black_market_item_cost")) { messagesConfig.set("black_market_item_cost", "&6Cost: &a{COST} litecoins"); }
        if (!messagesConfig.contains("black_market_not_enough_litecoins")) { messagesConfig.set("black_market_not_enough_litecoins", "&cYou do not have enough litecoins to buy this."); }
        if (!messagesConfig.contains("black_market_purchase")) { messagesConfig.set("black_market_purchase", "&aSuccessfully bought item for {COST} litecoins."); }

        if (!messagesConfig.contains("value_increase")) { messagesConfig.set("value_increase", Arrays.asList(" ", "&9<<< Daily Litecoin Announcement >>>", "&3New litecoin value: &b{VALUE}", "&aValue has increased by: &2{CHANGE}")); }
        if (!messagesConfig.contains("value_decrease")) { messagesConfig.set("value_decrease", Arrays.asList(" ", "&9<<< Daily Litecoin Announcement >>>", "&3New litecoin value: &b{VALUE}", "&cValue has decreased by: &4{CHANGE}")); }
        if (!messagesConfig.contains("solved")) { messagesConfig.set("solved", Arrays.asList(" ", "&9<<< Litecoin Announcement >>>", "&3Puzzle solved by: &b{SOLVER}", "&3Reward: &b{REWARD} litecoins", " ")); }
        if (!messagesConfig.contains("begin_transfer")) { messagesConfig.set("begin_transfer", Arrays.asList(" ", "&aYour balance: &2{BALANCE} litecoins", "&aEnter the player and amount of litecoins (e.g. Notch 5):")); }
        if (!messagesConfig.contains("begin_purchase")) { messagesConfig.set("begin_purchase", Arrays.asList(" ", "&aLitecoins in bank: &2{BANK} litecoins", "&aLitecoin cost: &2{VALUE} per litecoin", "&aTax: &2{TAX}", "&aEnter the amount of litecoins you would like to buy:")); }
        if (!messagesConfig.contains("begin_exchange")) { messagesConfig.set("begin_exchange", Arrays.asList(" ", "&aYour balance: &2{BALANCE} litecoins", "&aCurrent litecoin value: &2{VALUE}", "&aEnter the amount of litecoins you would like to sell:")); }
        if (!messagesConfig.contains("exit_item_lore")) { messagesConfig.set("exit_item_lore", Arrays.asList("&cProgress will be saved", "&c(as long as you don't leave the server)")); }
        if (!messagesConfig.contains("statistic_item_lore")) { messagesConfig.set("statistic_item_lore", Arrays.asList("&3Balance: &b{BALANCE} litecoins", "&3Mining puzzles solved: &b{AMOUNT_SOLVED}", "&3Litecoins mined: &b{AMOUNT_MINED}")); }
        if (!messagesConfig.contains("statistic_command_self")) { messagesConfig.set("statistic_command_self", Arrays.asList("&9<<< Your Stats >>>", "&3Balance: &b{BALANCE} litecoins", "&3Mining puzzles solved: &b{AMOUNT_SOLVED}", "&3Litecoins mined: &b{AMOUNT_MINED}")); }
        if (!messagesConfig.contains("statistic_command_other")) { messagesConfig.set("statistic_command_other", Arrays.asList("&9<<< {PLAYER}'s Stats>>>", "&3Balance: &b{BALANCE} litecoins", "&3Mining puzzles solved: &b{AMOUNT_SOLVED}", "&3Litecoins mined: &b{AMOUNT_MINED}")); }
        if (!messagesConfig.contains("help_command")) { messagesConfig.set("help_command", Arrays.asList(" ", "&9<<< Litecoin Commands >>>", "&3/litecoin help: &bDisplay this page", "&3/litecoin value: &bView current litecoin value", "&3/litecoin stats [player]: &bView player stats", "&3/litecoin bank: &bView amount of litecoins in bank", "&3/litecoin tax: &bView the current purchase tax", "&3/litecoin circulation: &bView circulation info", "&3/litecoin top: &bView players with the most litecoins", "&3/litecoin mine: &bOpen mining interface", "&3/litecoin transfer <player> <amount>: &bTransfer litecoins", "&3/litecoin sell <amount>: &bSell litecoins", "&3/litecoin buy <amount>: &bBuy litecoins", "&3/litecoin blackmarket: &bOpen black market", "&3/litecoin blackmarket setslot <slot> <price>: &bEdit black market", "&3/litecoin give <player> <amount>: &bAdd to balance", "&3/litecoin remove <player> <amount>: &bRemove from balance", "&3/litecoin set <player> <amount>: &bSet balance", "&3/litecoin reload: &bReload plugin", "&3/litecoin cancel: &bCancel transfer/purchase/sell")); }
        if (!messagesConfig.contains("circulation_command")) { messagesConfig.set("circulation_command", Arrays.asList("&3Amount of litecoins in circulation: &b{AMOUNT}", "&3Circulation limit: &b{LIMIT}")); }

        //Change previous defaults
        if (messagesConfig.getString("cancel_button_hover").equalsIgnoreCase("&cCancelled transfer.")) { messagesConfig.set("cancel_button_hover", "&cClick to cancel"); }
        if (messagesConfig.getString("exchange_item_name").equalsIgnoreCase("&9&lExchange Litecoins")) { messagesConfig.set("exchange_item_name", "&9&lSell Litecoins"); }
        if (messagesConfig.getString("exchange_item_lore").equalsIgnoreCase("&3Exchange litecoins for other currency")) { messagesConfig.set("exchange_item_lore", "&3Sell litecoins to the bank"); }
        if (messagesConfig.getString("complete_exchange").equalsIgnoreCase("&aSuccessfully exchanged {AMOUNT} litecoins for {NEW_AMOUNT}.")) { messagesConfig.set("complete_exchange", "&aSuccessfully sold {AMOUNT} litecoins to the bank for {NEW_AMOUNT}."); }
        if (messagesConfig.getStringList("begin_exchange").get(3).equalsIgnoreCase("&aEnter the amount of litecoins you would like to exchange:")) { messagesConfig.set("begin_exchange", Arrays.asList(" ", "&aYour balance: &2{BALANCE} litecoins", "&aCurrent litecoin value: &2{VALUE}", "&aEnter the amount of litecoins you would like to sell:")); }
        if (messagesConfig.getStringList("help_command").get(13).equalsIgnoreCase("&3/litecoin give <player> <amount>: &bAdd to balance")) { messagesConfig.set("help_command", Arrays.asList(" ", "&9<<< Litecoin Commands >>>", "&3/litecoin help: &bDisplay this page", "&3/litecoin value: &bView current litecoin value", "&3/litecoin stats [player]: &bView player stats", "&3/litecoin bank: &bView amount of litecoins in bank", "&3/litecoin tax: &bView the current purchase tax", "&3/litecoin circulation: &bView circulation info",  "&3/litecoin top: &bView players with the most litecoins", "&3/litecoin mine: &bOpen mining interface", "&3/litecoin transfer <player> <amount>: &bTransfer litecoins", "&3/litecoin sell <amount>: &bSell litecoins", "&3/litecoin buy <amount>: &bBuy litecoins", "&3/litecoin blackmarket: &bOpen black market", "&3/litecoin blackmarket setslot <slot> <price>: &bEdit black market", "&3/litecoin give <player> <amount>: &bAdd to balance", "&3/litecoin remove <player> <amount>: &bRemove from balance", "&3/litecoin set <player> <amount>: &bSet balance", "&3/litecoin reload: &bReload plugin", "&3/litecoin cancel: &bCancel transfer/purchase/sell")); }
        if (messagesConfig.getString("solve_item_name").equalsIgnoreCase("&aClick when you think you solved the puzzle")) { messagesConfig.set("solve_item_name", "&2&lSolve"); }
        if (messagesConfig.getString("solve_item_lore").equalsIgnoreCase("&cClick to reset the tiles")) { messagesConfig.set("solve_item_lore", "&aClick when you think you solved the puzzle"); }
        util.saveYml(messagesFile, messagesConfig);
    }

    private void loadAllMessages() {
        for (String configurableMessage : messagesConfig.getKeys(false)) {
            List<String> multipleLineMessage = messagesConfig.getStringList(configurableMessage);
            if (multipleLineMessage.isEmpty()) {
                messages.put(configurableMessage, util.colorMessage(messagesConfig.getString(configurableMessage)));
            } else {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < multipleLineMessage.size(); i++) {
                    message.append(multipleLineMessage.get(i));
                    if (i != multipleLineMessage.size() - 1) {
                        message.append("\n");
                    }
                }
                messages.put(configurableMessage, util.colorMessage(message.toString()));
            }
        }
    }
}
