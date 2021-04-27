package org.badfish.theworldshop.manager;


import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.CustomItem;
import org.badfish.theworldshop.items.MoneySellItem;


import java.util.ArrayList;
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
        CustomItem customItem = TheWorldShopMainClass.CUSTOM_ITEM.formItem(item);
        if(infoManager.contains(MoneySellItem.get(customItem))){
            return infoManager.get(infoManager.indexOf(MoneySellItem.get(customItem))).getMoney();
        }
        return 0.0d;
    }

    public void addMoneyItem(CustomItem customItem,double money){
        this.infoManager.add(new MoneySellItem(customItem,money));
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
        for(MoneySellItem itemDoubleEntry: infoManager){
             item = itemDoubleEntry.getCustomItem();
             if(item.getName() != null){
                 config.set(item.getName(),itemDoubleEntry.getMoney());
             }else{
                 config.set(item.getItem().getId()+":"+item.getItem().getDamage(),itemDoubleEntry.getMoney());
             }
        }
        config.save();
    }

    public static MoneyItemManager initManager(Config config){
        ArrayList<MoneySellItem> infoManager = new ArrayList<>();
        CustomItem customItem;
        for(Map.Entry<String, Object> configMap:config.getAll().entrySet()){
            customItem = TheWorldShopMainClass.CUSTOM_ITEM.formItem(configMap.getKey());
            if(customItem != null){
                infoManager.add(new MoneySellItem(customItem, (Double) configMap.getValue()));
            }
        }
        return new MoneyItemManager(infoManager);
    }


}
