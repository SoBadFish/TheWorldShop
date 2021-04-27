package org.badfish.theworldshop.items;

/**
 * @author BadFish
 */
public class MoneySellItem {
    private CustomItem customItem;

    private double money;

    public MoneySellItem(CustomItem customItem,double money){
        this.customItem = customItem;
        this.money = money;
    }

    public static MoneySellItem get(CustomItem customItem){
        return new MoneySellItem(customItem,0.0d);
    }

    public double getMoney() {
        return money;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MoneySellItem){
            return customItem.equals(((MoneySellItem) obj).customItem);
        }
        return false;
    }
}
