package org.badfish.theworldshop.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import org.badfish.theworldshop.items.ShopItem;

/**
 * @author BadFish
 */
public class ChoseByItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLER_LIST;
    }


    private ShopItem item;
    public ChoseByItemEvent(Player player, ShopItem item){

        this.item = item;
        this.player = player;
    }



    public ShopItem getItem() {
        return item;
    }
}
