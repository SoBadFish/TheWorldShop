package org.badfish.theworldshop.items.paneitem.defaultpanelitem;

import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;


import java.util.ArrayList;

/**
 * @author BadFish
 */
public class LastInventoryItem extends BasePanelItem {



    public static Item formItem(Item item){
        return getItemByTag(item);

    }

    public static Item toItem(Item defaultItem) {
        Item saveItem = defaultItem.clone();
        Item i =  toItem(saveItem,null,new ArrayList<>(), ItemType.INVENTORY_ITEM);
        return saveItem(defaultItem, i);
    }
}
