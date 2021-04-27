package org.badfish.theworldshop.items.paneitem.defaultpanelitem;

import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;
import org.badfish.theworldshop.items.ItemType;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class NextItem extends BasePanelItem {

    public static Item toItem(String name){
        return toItem(Item.get(262,0),name,new ArrayList<>(),ItemType.NEXT);
    }


    public static int getIndex() {
        return 51;
    }
}
