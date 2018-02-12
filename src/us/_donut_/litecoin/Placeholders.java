package us._donut_.litecoin;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class Placeholders extends EZPlaceholderHook {

    private LitecoinManager litecoinManager;

    public Placeholders(Litecoin pluginInstance) {
        super(pluginInstance, "litecoin");
        litecoinManager = pluginInstance.getLitecoinManager();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("value")) {
            return litecoinManager.getExchangeCurrencySymbol() + litecoinManager.getLitecoinValue();
        }

        if (identifier.equals("bank")) {
            return String.valueOf(litecoinManager.getAmountInBank());
        }

        if (identifier.equals("tax")) {
            return litecoinManager.getPurchaseTaxPercentage() + "%";
        }

        if (identifier.equals("circulation")) {
            return String.valueOf(litecoinManager.getLitecoinsInCirculation());
        }

        if (identifier.equals("circulation_limit")) {
            return String.valueOf(litecoinManager.getCirculationLimit());
        }

        if (player == null) {
            return "";
        }

        if (identifier.equals("balance")) {
            return String.valueOf(litecoinManager.getBalance(player.getUniqueId()));
        }

        if (identifier.equals("amount_mined")) {
            return String.valueOf(litecoinManager.getLitecoinsMined(player.getUniqueId()));
        }

        if (identifier.equals("puzzles_solved")) {
            return String.valueOf(litecoinManager.getPuzzlesSolved(player.getUniqueId()));
        }
        return null;
    }
}
