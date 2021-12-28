package org.badfish.theworldshop.manager;


import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.CustomItem;
import org.badfish.theworldshop.items.MoneySellItem;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BadFish
 */
public class MoneyItemManager {

    private ArrayList<MoneySellItem> infoManager;

    private MoneyItemManager(ArrayList<MoneySellItem> manager){
        this.infoManager = manager;
    }

    private double getMoney(Item item){
        MoneySellItem sellItem = getMoneySellItem(item);
        if(sellItem == null){
            return 0.0d;
        }
        return sellItem.getMoney();
//        CustomItem customItem = TheWorldShopMainClass.CUSTOM_ITEM.formItem(item);
//        if(infoManager.contains(MoneySellItem.get(customItem))){
//            return infoManager.get(infoManager.indexOf(MoneySellItem.get(customItem))).getMoney();
//        }
//        return 0.0d;
    }

    public MoneySellItem.MoneyType getMoneyTypeName(Item item){
        MoneySellItem sellItem = getMoneySellItem(item);
        if(sellItem == null){
            return MoneySellItem.MoneyType.EconomyAPI;
        }
        return sellItem.getMoneyType();
    }


    public MoneySellItem getMoneySellItem(Item item){
        CustomItem customItem = TheWorldShopMainClass.CUSTOM_ITEM.formItem(item);
        if(infoManager.contains(MoneySellItem.get(customItem))){
            return infoManager.get(infoManager.indexOf(MoneySellItem.get(customItem)));
        }
        return null;

    }

    public void addMoneyItem(CustomItem customItem, MoneySellItem.MoneyType moneyType, double money){
        MoneySellItem moneySellItem = new MoneySellItem(customItem,moneyType,money);
        if(infoManager.contains(moneySellItem)){
            moneySellItem = infoManager.get(infoManager.indexOf(moneySellItem));
            moneySellItem.setMoneyType(moneyType);
            moneySellItem.setMoney(money);
        }else{
            infoManager.add(moneySellItem);
        }

    }

    public ArrayList<Item> getCanSellItemByInventory(Inventory inventory){
        ArrayList<Item> items = new ArrayList<>();
        for(Item item: inventory.getContents().values()){
            if(getMoney(item) > 0){
                items.add(item);
            }
        }
        return items;
    }

    public double mathMoney(Item... item){
        double money = 0.0d;
        for(Item i: item){
            money += getMoney(i) * i.getCount();
        }
        return money;
    }

    public void save(){
        CustomItem item;
        Config config = new Config(TheWorldShopMainClass.MAIN_INSTANCE.getDataFolder()+"/sellmoney.yml",Config.YAML);
        LinkedHashMap<String, Object> map;
        String name;
        for(MoneySellItem itemDoubleEntry: infoManager){
            map = new LinkedHashMap<>();
             item = itemDoubleEntry.getCustomItem();

             if(item.getName() != null){
                 name = item.getName();
             }else{
                 name = item.getItem().getId()+":"+item.getItem().getDamage();
             }
             map.put("type",itemDoubleEntry.getMoneyType().name());
             map.put("money",itemDoubleEntry.getMoney());
             config.set(name,map);
        }
        config.save();
    }

    public static MoneyItemManager initManager(Config config){
        ArrayList<MoneySellItem> infoManager = new ArrayList<>();
        CustomItem customItem;
        Object o1;
        MoneySellItem.MoneyType type;
        double m;
        for(Map.Entry<String, Object> configMap:config.getAll().entrySet()){
            customItem = TheWorldShopMainClass.CUSTOM_ITEM.formItem(configMap.getKey());
            o1 = configMap.getValue();
            if(customItem != null) {
                if (o1 instanceof Map) {
                   type = MoneySellItem.MoneyType.valueOf(((Map) o1).get("type").toString());
                   m = Double.parseDouble(((Map) o1).get("money").toString());
                    infoManager.add(new MoneySellItem(customItem, type, m));
                } else {
                    infoManager.add(new MoneySellItem(customItem, MoneySellItem.MoneyType.EconomyAPI, (Double) configMap.getValue()));
                }
            }
//
        }
        return new MoneyItemManager(infoManager);
    }


}
