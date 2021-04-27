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
        item.setLore(lore.toArray(new String[0]));
        return item;
    }

    protected static Item getItemByTag(Item item) {
        Item i = Item.get(item.getId(),item.getDamage(),item.getCount());
        if(item.hasCompoundTag()){
            CompoundTag tag = item.getNamedTag();
            if(tag.contains("defaultItem")){
                byte[] tagForm = Tool.hexStringToBytes(tag.getString("defaultItem"));
                if(tagForm != null){
                    i.setNamedTag(Item.parseCompoundTag(tagForm));
                }
            }
        }
        return i;
    }
    protected static Item saveItem(Item saveItem, Item i) {
        if(saveItem.hasCompoundTag()) {
            CompoundTag tag = saveItem.getNamedTag();
            String b = Tool.bytesToHexString(saveItem.getCompoundTag());
            if(b != null) {
                tag.putString("defaultItem", b);
            }
            i.setNamedTag(tag);
        }
        return i;
    }

}
