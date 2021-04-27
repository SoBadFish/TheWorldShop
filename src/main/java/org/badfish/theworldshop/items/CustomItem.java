package org.badfish.theworldshop.items;

import cn.nukkit.item.Item;

/**
 * @author BadFish
 */
public class CustomItem {

    private String name = null;

    private Item item;

    public CustomItem(Item item){
        this.item = item;
    }

    public CustomItem(String name, Item item){
        this.name = name;
        this.item = item;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name+" item: "+item.getId()+":"+item.getDamage();
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CustomItem){
            if(name != null){
                return name.equalsIgnoreCase(((CustomItem) obj).name)
                        && ((CustomItem) obj).item.equals(item);
            }
            return  ((CustomItem) obj).item.equals(item);
        }
        return false;
    }
}
