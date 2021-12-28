package org.badfish.theworldshop.items;

/**
 * @author BadFish
 */
public class MoneySellItem {

    private MoneyType moneyType;

    private CustomItem customItem;

    private double money;

    public MoneySellItem(CustomItem customItem,MoneyType type,double money){
        this.customItem = customItem;
        this.moneyType = type;
        this.money = money;
    }

    public static MoneySellItem get(CustomItem customItem){
        return new MoneySellItem(customItem,MoneyType.EconomyAPI,0.0d);
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMoney() {
        return money;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    public MoneyType getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MoneySellItem){
            return customItem.equals(((MoneySellItem) obj).customItem);
        }
        return false;
    }

    public enum MoneyType{
        /**Economy PlayerPoints Money*/
        EconomyAPI,PlayerPoints,Money
    }
}
