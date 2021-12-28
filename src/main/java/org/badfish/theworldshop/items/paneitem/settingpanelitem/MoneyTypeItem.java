package org.badfish.theworldshop.items.paneitem.settingpanelitem;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class MoneyTypeItem extends BasePanelItem {
    public static Item toItem(String name, boolean isSetting){
        Item i = toItem(Item.get(175),name,new ArrayList<>(), ItemType.MONEY_TYPE);
        if(isSetting){
            i.addEnchantment(Enchantment.get(1));
        }
        return i;
    }

    public static int getIndex(){
        return 25;
    }
}
