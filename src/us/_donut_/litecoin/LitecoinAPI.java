package us._donut_.litecoin;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class LitecoinAPI {

    private LitecoinManager litecoinManager;
    private LitecoinMenu litecoinMenu;
    private Mining mining;

    LitecoinAPI(Litecoin pluginInstance) {
        litecoinManager = pluginInstance.getLitecoinManager();
        litecoinMenu = pluginInstance.getLitecoinMenu();
        mining = pluginInstance.getMining();
    }

    public Double getAmountInCirculation() { return litecoinManager.getLitecoinsInCirculation(); }
    public List<OfflinePlayer> getTopPlayers() { return litecoinManager.getTopPlayers(); }
    public Double getAmountInBank() { return litecoinManager.getAmountInBank(); }
    public Double getPurchaseTaxPercentage() { return litecoinManager.getPurchaseTaxPercentage(); }
    public Double getBalance(UUID playerUUID) { return litecoinManager.getBalance(playerUUID); }
    public Integer getPuzzlesSolved(UUID playerUUID) { return litecoinManager.getPuzzlesSolved(playerUUID); }
    public Double getLitecoinsMined(UUID playerUUID) { return litecoinManager.getLitecoinsMined(playerUUID); }
    public Double getLitecoinValue() { return litecoinManager.getLitecoinValue(); }
    public Double getCirculationLimit() { return litecoinManager.getCirculationLimit(); }
    public String getExchangeCurrencySymbol() { return litecoinManager.getExchangeCurrencySymbol(); }
    public void setBalance(UUID playerUUID, double balance) { litecoinManager.setBalance(playerUUID, balance); }
    public void withdraw(UUID playerUUID, double amount) { litecoinManager.withdraw(playerUUID, amount); }
    public void deposit(UUID playerUUID, double amount) { litecoinManager.deposit(playerUUID, amount); }
    public void setPuzzlesSolved(UUID playerUUID, int amount) { litecoinManager.setPuzzlesSolved(playerUUID, amount); }
    public void setLitecoinsMined(UUID playerUUID, double amount) { litecoinManager.setLitecoinsMined(playerUUID, amount); }
    public void addToBank(double amount) { litecoinManager.addToBank(amount); }
    public void removeFromBank(double amount) { litecoinManager.removeFromBank(amount); }
    public void makeValueFluctuate() { litecoinManager.fluctuate(); }
    public void openMainMenu(Player player) { litecoinMenu.open(player); }
    public void openMiningInterface(Player player) { mining.openInterface(player); }
}
