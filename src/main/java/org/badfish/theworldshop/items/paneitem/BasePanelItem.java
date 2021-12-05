package org.badfish.theworldshop.items.paneitem;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.ShopItem;
import org.badfish.theworldshop.utils.Tool;

import java.util.ArrayList;

/**
 * @author BadFish
 */
public abstract class BasePanelItem {

    protected static Item toItem(Item item, String name, ArrayList<String> lore, ItemType type){
        if(name != null) {
            item.setCustomName(TextFormat.colorize('&', name));
        }
        CompoundTag tag = item.getNamedTag();
        if(tag == null){
            tag = new CompoundTag();
        }
        tag.putString(ShopItem.TAG + "tag", "ShopItem");
        tag.putString(ShopItem.TAG + "type", type.getName());
        item.setNamedTag(tag);
        if(lore.size() > 0) {
            item.setLore(lore.toArray(new String[0]));
        }

        return item;
    }

    protected static Item getItemByTag(Item item) {
        CompoundTag tag = item.getNamedTag();
        String ss = tag.getString("defaultItem");
        String[] li = ss.split(":");
        Item item1 = Item.get(Integer.parseInt(li[0]),Integer.parseInt(li[1]),Integer.parseInt(li[2]));
        if(!"not".equalsIgnoreCase(li[3])){
            byte[] b = Tool.hexStringToBytes(li[3]);
            if(b != null) {
                CompoundTag compoundTag = Item.parseCompoundTag(b);
                item1.setNamedTag(compoundTag);
            }
        }
        return item1;


//        return NBTIO.getItemHelper(tag.getCompound("defaultItem"));

    }
    protected static Item saveItem(Item saveItem, Item i) {
        Item s = saveItem.clone();
        CompoundTag tag = i.getNamedTag();
        String sa = "not";
        if(s.hasCompoundTag()){
            sa = Tool.bytesToHexString(s.getCompoundTag());
        }
        sa = s.getId() + ":" + s.getDamage() + ":" + s.getCount() + ":" + sa;
        tag.putString("defaultItem",sa);
        i.setNamedTag(tag);
        return i;
    }


}
