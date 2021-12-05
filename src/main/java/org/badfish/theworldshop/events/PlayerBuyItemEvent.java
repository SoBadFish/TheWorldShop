package org.badfish.theworldshop.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

/**
 * @author BadFish
 */
public class PlayerBuyItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private double money;

    private Item item;

    public PlayerBuyItemEvent(Player player, double money, Item item){
        this.player = player;
        this.money = money;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getMoney() {
        return money;
    }
}
