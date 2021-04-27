package org.badfish.theworldshop.manager;

import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.ItemType;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class PlayerInfoManager {

    private static ArrayList<PlayerInfoManager> INFO_MANAGER = new ArrayList<>();

    private String playerName;

    private String chosePlayer;

    private ArrayList<ItemType> settings = new ArrayList<>();

    private Item choseItem;

    private int page = 1;

    private PlayerInfoManager(String playerName){
        this.playerName = playerName;
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

    public int getPage() {
        return page;
    }

    public static PlayerInfoManager getInstance(String playerName){
        PlayerInfoManager infoManager = new PlayerInfoManager(playerName);
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
