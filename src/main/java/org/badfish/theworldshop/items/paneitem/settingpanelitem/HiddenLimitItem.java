package org.badfish.theworldshop.items.paneitem.settingpanelitem;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author Sobadfish
 * @date 2022/11/19
 */
public class HiddenLimitItem extends BasePanelItem {
    //289

    public static Item toItem(String name, boolean isSetting) {
        Item i = toItem(Item.get(289), name, new ArrayList<>(), ItemType.HIDE_LIMIT);
        if (isSetting) {
            i.addEnchantment(Enchantment.get(1));
        }
        return i;
    }

    public static int getIndex(){
        return 37;
    }
}
