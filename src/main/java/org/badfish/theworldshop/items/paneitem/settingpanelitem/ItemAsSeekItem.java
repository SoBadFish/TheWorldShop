package org.badfish.theworldshop.items.paneitem.settingpanelitem;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class ItemAsSeekItem extends BasePanelItem {

    public static Item toItem(Item item,boolean isSetting){
        String name = null;
        if(isSetting){
            name = "&r&l&a"+ TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemHasChose);
        }
        Item i = toItem(item.clone(),name,new ArrayList<>(), ItemType.SEEK_ITEM);
        if(isSetting){
            i.addEnchantment(Enchantment.get(1));
        }
        return i;
    }
}
