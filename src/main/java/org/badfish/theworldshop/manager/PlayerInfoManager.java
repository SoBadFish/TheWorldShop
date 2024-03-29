package org.badfish.theworldshop.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.MoneySellItem;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class PlayerInfoManager {

    private boolean isWindows = true;

    private static ArrayList<PlayerInfoManager> INFO_MANAGER = new ArrayList<>();

    private String playerName;

    private String chosePlayer;

    private ArrayList<ItemType> settings = new ArrayList<>();

    private MoneySellItem.MoneyType moneyType;

    private Item choseItem;

    private int page = 1;

    private PlayerInfoManager(String playerName){
        this.playerName = playerName;
    }

    public void setWindows(boolean windows) {
        isWindows = windows;
    }

    public boolean isWindows() {
        return isWindows;
    }

    public void setPage(int page) {
        if(page < 1){
            this.page = 1;
            return;
        }
        this.page = page;
    }

    public void setChosePlayer(String chosePlayer) {
        this.chosePlayer = chosePlayer;
    }

    public String getChosePlayer() {
        return chosePlayer;
    }

    public void setChoseItem(Item choseItem) {
        if(choseItem != null){
            this.choseItem = Item.get(choseItem.getId(),choseItem.getDamage());
        }else{
            this.choseItem = null;
        }

    }

    public ArrayList<ItemType> getSettings() {
        return settings;
    }

    public Item getChoseItem() {
        return choseItem;
    }

    public boolean isSetting(ItemType itemType){
        return (settings.contains(itemType));
    }

    public void addSettings(ItemType itemType) {
        if(isSetting(itemType)){
            settings.remove(itemType);
            return;
        }
        settings.add(itemType);
    }



    public MoneySellItem.MoneyType getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneySellItem.MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    public int getPage() {
        return page;
    }

    public static PlayerInfoManager getInstance(String playerName){
        PlayerInfoManager infoManager = new PlayerInfoManager(playerName);
        Player player = Server.getInstance().getPlayer(playerName);
        if(player != null){
            infoManager.setWindows(player.getLoginChainData().getDeviceOS() == 7);
        }
        if(!PlayerInfoManager.INFO_MANAGER.contains(infoManager)){
            PlayerInfoManager.INFO_MANAGER.add(infoManager);
        }
        return PlayerInfoManager.INFO_MANAGER.get(PlayerInfoManager.INFO_MANAGER.indexOf(infoManager));
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerInfoManager){
            return ((PlayerInfoManager) obj).playerName.equalsIgnoreCase(playerName);
        }
        return false;
    }
}
