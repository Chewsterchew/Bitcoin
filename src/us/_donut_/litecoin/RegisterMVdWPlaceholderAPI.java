package us._donut_.litecoin;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

public class RegisterMVdWPlaceholderAPI {

    RegisterMVdWPlaceholderAPI(Litecoin pluginInstance) {
        LitecoinManager litecoinManager = pluginInstance.getLitecoinManager();

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_value", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                return litecoinManager.getExchangeCurrencySymbol() + litecoinManager.getLitecoinValue();
            }
        });

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_bank", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                return String.valueOf(litecoinManager.getAmountInBank());
            }
        });

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_tax", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                return litecoinManager.getPurchaseTaxPercentage() + "%";
            }
        });

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_circulation", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                return String.valueOf(litecoinManager.getLitecoinsInCirculation());
            }
        });

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_circulation_limit", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                return String.valueOf(litecoinManager.getCirculationLimit());
            }
        });

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_balance", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                if (placeholderReplaceEvent.getOfflinePlayer() == null) {
                    return "";
                } else {
                    return String.valueOf(litecoinManager.getBalance(placeholderReplaceEvent.getOfflinePlayer().getUniqueId()));
                }
            }
        });

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_amount_mined", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                if (placeholderReplaceEvent.getOfflinePlayer() == null) {
                    return "";
                } else {
                    return String.valueOf(litecoinManager.getLitecoinsMined(placeholderReplaceEvent.getOfflinePlayer().getUniqueId()));
                }
            }
        });

        PlaceholderAPI.registerPlaceholder(pluginInstance, "litecoin_puzzles_solved", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                if (placeholderReplaceEvent.getOfflinePlayer() == null) {
                    return "";
                } else {
                    return String.valueOf(litecoinManager.getPuzzlesSolved(placeholderReplaceEvent.getOfflinePlayer().getUniqueId()));
                }
            }
        });
    }
}
