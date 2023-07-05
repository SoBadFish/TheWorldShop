package org.badfish.theworldshop.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.ShopItem;

/**
 * @author BadFish
 */
public class PlayerSellItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private ShopItem item;

    private double money;

    private boolean isRemove;

    public PlayerSellItemEvent(Player player, ShopItem item, double money, boolean isRemove){
        this.player = player;
        this.item = item;
        this.money = money;
        this.isRemove = isRemove;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public ShopItem getItem() {
        return item;
    }

    public double getMoney() {
        return money;
    }
}
