package org.badfish.theworldshop.manager;

import cn.nukkit.item.Item;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class PlayerSellItemManager {

    private String playerName;

    private ArrayList<Item> sellItems;

    public PlayerSellItemManager(String playerName,ArrayList<Item> items){
        this.playerName = playerName;
        this.sellItems = items;
    }

    private PlayerSellItemManager(String playerName){
        this.playerName = playerName;
    }

    public ArrayList<Item> getSellItems() {
        return sellItems;
    }

    public static PlayerSellItemManager getInstance(String playerName){
        return new PlayerSellItemManager(playerName);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerSellItemManager){
            return ((PlayerSellItemManager) obj).playerName.equals(playerName);
        }
        return false;
    }
}
