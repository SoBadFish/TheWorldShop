package org.badfish.theworldshop.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;


/**
 * @author BadFish
 */
public class SellItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLER_LIST;
    }


    private Item item;

    public SellItemEvent(Player player, Item item){

        this.item = item;
        this.player = player;
    }



    public Item getItem() {
        return item;
    }

}
