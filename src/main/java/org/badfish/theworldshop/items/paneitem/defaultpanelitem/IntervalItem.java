package org.badfish.theworldshop.items.paneitem.defaultpanelitem;

import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;
import org.badfish.theworldshop.items.ItemType;

import java.util.ArrayList;


/**
 * @author BadFish
 */
public class IntervalItem extends BasePanelItem {


    public static Item toItem(String name, ArrayList<String> lore) {
        return toItem(Item.get(160, 14),name,lore,ItemType.NOT);
    }


}
