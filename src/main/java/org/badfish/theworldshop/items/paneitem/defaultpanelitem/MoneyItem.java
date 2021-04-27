package org.badfish.theworldshop.items.paneitem.defaultpanelitem;

import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author BadFish
 */
public class MoneyItem extends BasePanelItem {

    public static Item formItem(Item item){
        return getItemByTag(item);
    }


    public static Item toItem(Item defaultItem) {
        Item saveItem = defaultItem.clone();
        defaultItem = defaultItem.clone();
        ArrayList<String> lore = new ArrayList<>(Arrays.asList(defaultItem.getLore()));
        double money = TheWorldShopMainClass.MONEY_ITEM.mathMoney(defaultItem);
        lore.add(TextFormat.colorize('&',"&r&7■■■■■■■■■■■■■■■■■■■■"));
        lore.add(TextFormat.colorize('&',"&r&e回收价格    |   &a"+(money) ));
        lore.add(TextFormat.colorize('&',"&r&l&d      双击出售"));
        lore.add(TextFormat.colorize('&',"&r&7■■■■■■■■■■■■■■■■■■■■"));
        Item i =  toItem(defaultItem,null,new ArrayList<>(), ItemType.MONEY_ITEM);
        i.setLore(lore.toArray(new String[0]));
        return saveItem(saveItem, i);
    }


}
