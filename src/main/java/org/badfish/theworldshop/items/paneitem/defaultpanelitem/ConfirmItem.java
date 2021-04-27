package org.badfish.theworldshop.items.paneitem.defaultpanelitem;

import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class ConfirmItem extends BasePanelItem {
    public static Item toItem(ArrayList<String> lore){

        return toItem(Item.get(262,0),"&r&a确认",lore, ItemType.CONFIRM_ITEM);
    }


    public static int getIndex() {
        return 51;
    }
}
