package org.badfish.theworldshop.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.events.PlayerSellItemEvent;
import org.badfish.theworldshop.items.MoneySellItem;
import org.badfish.theworldshop.items.ShopItem;
import org.badfish.theworldshop.utils.Tool;

import java.util.*;

/**
 * @author BadFish
 */
public class SellItemManager {

    private static final int ITEM_SIZE = 27;

    private ArrayList<ShopItem> sellItems;

    private SellItemManager(ArrayList<ShopItem> shopItems){
        this.sellItems = shopItems;
    }

    public int maxSize(){
        return sellItems.size();
    }

    private ArrayList<String> getPlayerNames(ArrayList<ShopItem> sellItems){
        ArrayList<String> playerName = new ArrayList<>();
        for(ShopItem shopItem: sellItems){
            if(!playerName.contains(shopItem.getSellPlayer())){
                playerName.add(shopItem.getSellPlayer());
            }
        }
        return playerName;
    }

    public ArrayList<String> getPlayerNames(){
        return getPlayerNames(sellItems);
    }



    private int playerSize(ArrayList<ShopItem> sellItems){
        return getPlayerNames(sellItems).size();
    }

    public int playerSize(){
        return playerSize(sellItems);
    }

    public ArrayList<ShopItem> getPlayerAllShopItem(String playerName){
        return getPlayerAllShopItem(playerName,sellItems);
    }
    /**
     * 筛选货币类型
     * @param sellItems 物品列表
     * */
    public ArrayList<ShopItem> getMoneyTypeItem(ArrayList<ShopItem> sellItems, MoneySellItem.MoneyType moneyType){
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        for(ShopItem shopItem:sellItems){
            if(shopItem.getMoneyType() == moneyType){
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }
    /**
     * 筛选系统商店
     * @param sellItems 物品列表
     * */
    public ArrayList<ShopItem> getSystemItemShopItem(ArrayList<ShopItem> sellItems){
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        for(ShopItem shopItem:sellItems){
            if(shopItem.isRemove()){
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }
    /**
     * 筛选非系统商店
     * @param sellItems 物品列表
     * */
    public ArrayList<ShopItem> getPlayerItemShopItem(ArrayList<ShopItem> sellItems){
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        for(ShopItem shopItem:sellItems){
            if(!shopItem.isRemove()){
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }

    public ArrayList<ShopItem> getPlayerAllShopItem(String playerName,ArrayList<ShopItem> sellItems){
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        for(ShopItem shopItem:sellItems){
            if(shopItem.getSellPlayer().equalsIgnoreCase(playerName)){
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }

    public ArrayList<ShopItem> getPlayerShopItem(String playerName,ArrayList<ShopItem> shopItems,int page){
        return getArrayListByPage(page,getPlayerAllShopItem(playerName,shopItems));
    }

    public ArrayList<ShopItem> getPlayerShopItem(String playerName,int page){
        return getArrayListByPage(page,getPlayerAllShopItem(playerName,sellItems));
    }

    public int getPlayerSellCount(String playerName){
        return getPlayerSellCount(playerName,sellItems);
    }


    private int getPlayerSellCount(String playerName, ArrayList<ShopItem> shopItems){
        int i = 0;
        for(ShopItem shopItem: shopItems){
            if(shopItem.getSellPlayer().equalsIgnoreCase(playerName)){
                i++;
            }
        }
        return i;
    }


    public ArrayList<ShopItem> getShopItemsLikeItemByPage(Item item,ArrayList<ShopItem> shopItems,int page){
        return getArrayListByPage(page,getShopItemsLikeItem(item,shopItems));
    }

    public ArrayList<ShopItem> getShopItemsLikeItemByPage(Item item,int page){
        return getArrayListByPage(page,getShopItemsLikeItem(item));
    }

    /**
     * 根据物品筛选
     * */
    private ArrayList<ShopItem> getShopItemsLikeItem(Item item){
        return getShopItemsLikeItem(item,sellItems);
    }
    /**
     * 根据物品筛选
     * */
    public ArrayList<ShopItem> getShopItemsLikeItem(Item item,ArrayList<ShopItem> sellItems){
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        for(ShopItem shopItem: sellItems){
            if(shopItem.getDefaultItem().equals(item,true,false)){
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }

    /**
     * 只显示限购
     * */
    public ArrayList<ShopItem> onlyDisplayLimitItems(ArrayList<ShopItem> shopItems){
        shopItems.removeIf(item->item.limit <= 0);
        return shopItems;
    }

    /**
     * 隐藏限购
     * */
    public ArrayList<ShopItem> hiddenLimitItems(ArrayList<ShopItem> shopItems){
        shopItems.removeIf(item->item.limit > 0);
        return shopItems;
    }



    public ArrayList<ShopItem> orderByPage(int page,ArrayList<ShopItem> shopItems){
        return getArrayListByPage(page,orderItems(shopItems));
    }

    public ArrayList<ShopItem> orderByPage(int page){
        return getArrayListByPage(page,order());
    }
    /**
     * 排序
     * */
    private ArrayList<ShopItem> order(){
        return orderItems(sellItems);
    }

    public ArrayList<ShopItem> orderItems(ArrayList<ShopItem> shopItems){
        Comparator<ShopItem> comp = Comparator.comparingDouble(ShopItem::getSellMoney);
        shopItems.sort(comp);
        return shopItems;
    }



    public ArrayList<ShopItem> orderCountItems(ArrayList<ShopItem> shopItems){
        Comparator<ShopItem> comp = Comparator.comparingInt(ShopItem::getId);
        shopItems.sort(comp);
        return shopItems;
    }

    public ArrayList<ShopItem> getSellItems() {
        return sellItems;
    }

    public void removeItem(ShopItem shopItem){
        sellItems.remove(shopItem);
    }

    public void addSellItem(Player player, Item item, MoneySellItem.MoneyType moneyType, double money, boolean isRemove,int limit){
        PlayerSellItemEvent event = new PlayerSellItemEvent(player, item, money,isRemove);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        ShopItem shopItem = ShopItem.cloneTo(UUID.randomUUID(),item,player.getName(),moneyType,money,isRemove,limit);
        this.addItem(shopItem);
    }

    private void addItem(ShopItem shopItem){
        this.sellItems.add(shopItem);
    }

    public ArrayList<ShopItem> getShopItemByPage(int page){
        return getArrayListByPage(page,sellItems);
    }

    public ArrayList<Item> getAllItem(){
        ArrayList<Item> arrayList = new ArrayList<>();
        for(ShopItem shopItem: sellItems){
            if(arrayList.contains(Item.get(shopItem.getDefaultItem().getId(),shopItem.getDefaultItem().getDamage()))){
                continue;
            }
            arrayList.add(Item.get(shopItem.getDefaultItem().getId(),shopItem.getDefaultItem().getDamage()));
        }
        return arrayList;
    }

    public <T> ArrayList<T> getArrayListByPage(int page,ArrayList<T> list){
        ArrayList<T> shopItems = new ArrayList<>();
        for(int i = (page - 1) * ITEM_SIZE; i < ITEM_SIZE + ((page - 1) * ITEM_SIZE);i++){
            if(list.size() > i){
                shopItems.add(list.get(i));
            }
        }
        return shopItems;
    }

    public static int mathShopItemPage(ArrayList<?> sellItems){
        if(sellItems.size() == 0){
            return 1;
        }
        return (int) Math.ceil(sellItems.size() / (double)ITEM_SIZE);
    }

    public int getMaxPage(){
        return mathShopItemPage(sellItems);
    }

    public static SellItemManager loadManager(Config config){
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        List<Map> arrays = config.getMapList("sell");
        ShopItem shopItem;
        for(Map map : arrays){
            shopItem = ShopItem.formMap(map);
            if(shopItem != null){
                shopItems.add(shopItem);
            }
        }
        return new SellItemManager(shopItems);
    }



    public void save(){
        Config config = new Config(TheWorldShopMainClass.MAIN_INSTANCE.getDataFolder()+"/items.yml",Config.YAML);
        List<Map<?,?>> list = new ArrayList<>();
        LinkedHashMap<String, Object> map;
        for(ShopItem shopItem: sellItems){
            map = new LinkedHashMap<>();
            map.put("id",shopItem.getDefaultItem().getId()+":"+shopItem.getDefaultItem().getDamage());
            map.put("count",shopItem.getDefaultItem().getCount());
            if(shopItem.getDefaultItem().hasCompoundTag()) {
                map.put("tag", Tool.bytesToHexString(shopItem.getDefaultItem().getCompoundTag()));
            }else{
                map.put("tag","not");
            }
            map.put("uuid",shopItem.uuid.toString());
            map.put("limit",shopItem.limit);
            map.put("moneyType",shopItem.getMoneyType().toString());
            map.put("sellPlayer",shopItem.getSellPlayer());
            map.put("sellMoney",shopItem.getSellMoney());
            map.put("isRemove",shopItem.isRemove());
            list.add(map);
        }
        config.set("sell",list);
        config.save();
    }

}
