package org.badfish.theworldshop.items.paneitem.settingpanelitem;

import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class MoneySubItem extends BasePanelItem {
    public static Item toItem(String name, boolean isChose) {
        ArrayList<String> lore = new ArrayList<>();
        if(isChose){
            lore.add(TextFormat.colorize('&',"&r&l&a"+ TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemHasChose)));
        }
        return toItem(Item.get(175), name, lore, ItemType.MONEY_SUB_ITEM);

    }

}