package org.badfish.theworldshop.items.paneitem.settingpanelitem;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public class PlayerSeekItem extends BasePanelItem {

    public static Item toItem(String name,boolean isChose){
        Item i = toItem(Item.get(397,3),name,new ArrayList<>(), ItemType.SEEK_PLAYER);
        if(isChose){
            i.addEnchantment(Enchantment.get(1));
        }
        return i;
    }

}
