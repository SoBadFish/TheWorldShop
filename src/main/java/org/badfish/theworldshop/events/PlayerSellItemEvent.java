package org.badfish.theworldshop.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

/**
 * @author BadFish
 */
public class PlayerSellItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private Item item;

    private double money;

    public PlayerSellItemEvent(Player player, Item item,double money){
        this.player = player;
        this.item = item;
        this.money = money;
    }

    public Item getItem() {
        return item;
    }

    public double getMoney() {
        return money;
    }
}
