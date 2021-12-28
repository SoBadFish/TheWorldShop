package org.badfish.theworldshop.items;

/**
 * @author BadFish
 */
public enum  ItemType {
    /**无* */
    NOT("not"),
    /**上一页*/
    LAST("last"),
    /**下一页*/
    NEXT("next"),
    /**我的*/
    MYSELF("myself"),
    /**退出*/
    QUIT("quit"),
    /**刷新*/
    REFRESH("refresh"),
    /**筛选条件*/
    SETTING("setting"),
    /**顺序*/
    ORDER("orderMoney"),
    /**顺序*/
    ORDER_COUNT("orderCount"),
    /**系统出售*/
    SYSTEM_SELL("systemSell"),
    /**玩家出售*/
    PLAYER_SELL("playerSell"),
    /**筛选条件 金钱类型*/

    MONEY_TYPE("moneyType"),
    /**筛选条件 金钱类型 - sub*/
    MONEY_SUB_ITEM("moneySubItem"),
    /**玩家*/
    PLAYER("player"),
    /**玩家*/
    SEEK_PLAYER("seekPlayer"),
    /**物品*/
    ITEM("item"),
    /**物品*/
    INVENTORY_ITEM("inventoryItem"),
    /**回收物品*/
    MONEY_ITEM("moneyItem"),
    /**回收物品*/
    CONFIRM_ITEM("confirmItem"),
    /**物品*/
    SEEK_ITEM("seekItem"),
    /**出售的物品*/
    SELL("sell");

    protected String name;
    ItemType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ItemType get(String name){
        for(ItemType type: ItemType.values()){
            if(type.getName().equalsIgnoreCase(name)){
                return type;
            }
        }
        return null;
    }
}
