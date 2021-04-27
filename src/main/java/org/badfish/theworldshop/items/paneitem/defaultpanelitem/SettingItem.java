package org.badfish.theworldshop.items.paneitem.defaultpanelitem;

import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class SettingItem extends BasePanelItem {

    public static Item toItem(String name){
        return toItem(Item.get(356),name,new ArrayList<>(), ItemType.SETTING);
    }

    public static int getIndex(){
        return 53;
    }

}
