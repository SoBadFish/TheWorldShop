package org.badfish.theworldshop.manager;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.CustomItem;
import org.badfish.theworldshop.utils.Tool;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author BadFish
 */
public class CustomItemManager {

    private ArrayList<CustomItem> items;

    private CustomItemManager(ArrayList<CustomItem> customItems){
        this.items = customItems;
    }

    public static CustomItemManager initCustomItem(Config config){
        ArrayList<CustomItem> items = new ArrayList<>();
        String name;
        for(Map.Entry<String, Object> map: config.getAll().entrySet()){
            name = map.getKey();
            String msg = map.getValue().toString();
            Item item = toItem(msg);
            if(item != null){
                items.add(new CustomItem(name,item));
            }
        }
        return new CustomItemManager(items);
    }

    public void addCustomItem(CustomItem customItem){
        if(!items.contains(customItem)){
            items.add(customItem);
        }
    }
    public CustomItem formItem(Item i){
        CustomItem item = null;
        for(CustomItem customItem: items){
           if(customItem.getItem().equals(i)){
               item = customItem;
               break;
           }
        }
        if(item == null){
            item = new CustomItem(i);
        }
        return item;
    }


    public CustomItem formItem(String name){
        CustomItem item = null;
        for(CustomItem customItem: items){
            if(customItem.getName().equalsIgnoreCase(name)){
                item = customItem;
                break;
            }
        }
        if(item == null){
            Item i = toItem(name);
            if(i != null){
                return formItem(i);

            }
        }
        return item;
    }

    private static Item toItem(String str){
        if(!"".equals(str)){
            String[] strings = str.split(":");
            Item item = new Item(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]));
            if(strings.length > 2) {
                if (!"not".equals(strings[2])) {
                    byte[] bytes = Tool.hexStringToBytes(strings[2]);
                    if (bytes != null) {
                        CompoundTag tag = Item.parseCompoundTag(bytes);
                        item.setNamedTag(tag);
                    }
                }
            }
            return item;
        }
        return null;
    }

    public void save(){
        Config config = new Config(TheWorldShopMainClass.MAIN_INSTANCE.getDataFolder()+"/takeItems.yml",Config.YAML);
        for(CustomItem customItem: items){
            if(customItem.getName() != null){
                config.set(customItem.getName(),toStringItem(customItem.getItem()));
            }
        }
        config.save();
    }

    private String toStringItem(Item item){
        String tag = "not";
        if(item.hasCompoundTag()){
            tag = Tool.bytesToHexString(item.getCompoundTag());
        }
        return item.getId()+":"+item.getDamage()+":"+tag;

    }
}
