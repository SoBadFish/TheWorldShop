package org.badfish.theworldshop.panel;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.ShopItem;
import org.badfish.theworldshop.items.paneitem.defaultpanelitem.*;
import org.badfish.theworldshop.items.paneitem.settingpanelitem.*;
import org.badfish.theworldshop.manager.PlayerInfoManager;
import org.badfish.theworldshop.manager.PlayerSellItemManager;
import org.badfish.theworldshop.manager.SellItemManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BadFish
 */
public class DisplayPanel implements InventoryHolder {


    private static final int ITEM_INDEX = 36;

    private static final int LINE_SIZE = 9;



    public static  Map<Integer, Item> getPlayerItems(PlayerInfoManager infoManager){
        int page = infoManager.getPage();
        ArrayList<ShopItem> shopItems = TheWorldShopMainClass.SELL_MANAGER
                .getPlayerAllShopItem(infoManager.getPlayerName());
        for(ItemType itemType : infoManager.getSettings()){
            switch (itemType){
                case ORDER_COUNT:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.orderCountItems(shopItems);
                    break;
                case ORDER:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.orderItems(shopItems);
                    break;
                case SEEK_ITEM:
                    if(infoManager.getChoseItem() != null) {
                        shopItems = TheWorldShopMainClass.SELL_MANAGER.getShopItemsLikeItem(infoManager.getChoseItem(), shopItems);
                    }
                    break;
                default:break;
            }
        }
        int maxPage = 1;
        if(shopItems.size() > 0) {
            maxPage = SellItemManager.mathShopItemPage(shopItems);
            if(page > maxPage){
                page = maxPage;
            }
        }

        shopItems = TheWorldShopMainClass.SELL_MANAGER.getPlayerShopItem(infoManager.getPlayerName(),shopItems,page);
        return getItemsTo(infoManager.getPage(),shopItems,maxPage,true);

    }


    public static Map<Integer, Item> inventorySellItemPanel(Player player){
        ArrayList<Item> items;
        if(!TheWorldShopMainClass.PLAYER_SELL.contains(PlayerSellItemManager.
                getInstance(player.getName()))){
            TheWorldShopMainClass.PLAYER_SELL.add(new PlayerSellItemManager(player.getName(),
                    TheWorldShopMainClass.
                    MONEY_ITEM.getCanSellItemByInventory(player.getInventory())));
        }
        items = TheWorldShopMainClass.PLAYER_SELL.get(TheWorldShopMainClass.PLAYER_SELL
                .indexOf(PlayerSellItemManager.getInstance(player.getName()))).getSellItems();

        double money = TheWorldShopMainClass.MONEY_ITEM.mathMoney(items.toArray(new Item[0]));
        Map<Integer,Item> panel = defaultPanel("&r&a预计出售: $"+money);
        int index = LINE_SIZE;
        for(Item item: items){
            panel.put(index,MoneyItem.toItem(item));
            index++;
        }
        return panel;
    }


    /**
     * 基础物品布局
     * */
    public static Map<Integer, Item> defaultPanel(String lineName){
        Map<Integer,Item> panel = new LinkedHashMap<>();
        int index = 0;
        for(;index < LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem(lineName,new ArrayList<>()));
        }
        for(index = ITEM_INDEX + LINE_SIZE ;index < ITEM_INDEX + (LINE_SIZE * 2);index++){
            panel.put(index, IntervalItem.toItem(lineName,new ArrayList<>()));
        }
        return panel;
    }

    /**
     * 展示玩家背包
     * */
    public static Map<Integer, Item> playerInventoryPanel(PlayerInfoManager infoManager){
        Map<Integer,Item> panel = defaultPanel("&r&c请选择上架物品");
        Player player = Server.getInstance().getPlayer(infoManager.getPlayerName());
        if(player == null){
            return new LinkedHashMap<>();
        }
        int index = LINE_SIZE;
        for(Item item: player.getInventory().getContents().values()){
            panel.put(index,LastInventoryItem.toItem(item));
            index++;
        }
        return panel;

    }

    public static Map<Integer, Item> getItems(PlayerInfoManager infoManager){
        int page = infoManager.getPage();
        int maxPage;
//
        ArrayList<ShopItem> shopItems = TheWorldShopMainClass.SELL_MANAGER.getSellItems();
        for(ItemType itemType : infoManager.getSettings()){
            switch (itemType){
                case ORDER_COUNT:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.orderCountItems(shopItems);
                    break;
                case ORDER:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.orderItems(shopItems);
                    break;
                case SEEK_ITEM:
                    if(infoManager.getChoseItem() != null) {
                        shopItems = TheWorldShopMainClass.SELL_MANAGER.getShopItemsLikeItem(infoManager.getChoseItem(), shopItems);
                    }
                    break;
                case SEEK_PLAYER:
                    if(infoManager.getChosePlayer() != null) {
                        shopItems = TheWorldShopMainClass.SELL_MANAGER.getPlayerAllShopItem(infoManager.getChosePlayer(), shopItems);
                    }
                    break;
                default:break;
            }
        }

        maxPage = SellItemManager.mathShopItemPage(shopItems);
        if(page > maxPage){
            page = maxPage;
        }
        shopItems = TheWorldShopMainClass.SELL_MANAGER.getArrayListByPage(page,shopItems);
        return getItemsTo(infoManager.getPage(),shopItems,maxPage,false);
    }

    public static Map<Integer, Item> getItemPanel(PlayerInfoManager infoManager){
        ArrayList<Item> items = TheWorldShopMainClass.SELL_MANAGER.getAllItem();
        int index = 0;
        int page = infoManager.getPage();
        int maxPage = SellItemManager.mathShopItemPage(items);
        if(page > maxPage){
            page = maxPage;
        }
        Map<Integer,Item> panel = new LinkedHashMap<>();
        for(;index < LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("&r&c请选择筛选物品",new ArrayList<>()));
        }
        boolean isChose;
        for(Item i: TheWorldShopMainClass.SELL_MANAGER.getArrayListByPage(page,items)){
            isChose = infoManager.getChoseItem() != null && infoManager.getChoseItem().equals(i,true,false);
            panel.put(index, ItemAsSeekItem.toItem(i,isChose));
            index++;
        }
        for(index = ITEM_INDEX ;index < ITEM_INDEX + LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("&r&c请选择筛选物品",new ArrayList<>()));
        }
        return addPagePanel(page, maxPage, panel);
    }

    private static Map<Integer, Item> addPagePanel(int page, int maxPage, Map<Integer, Item> panel) {
        panel.put(45, QuitItem.toItem("&r&a返回全球市场"));
        addLastPage(page,maxPage,panel);
        if(maxPage > page) {
            panel.put(NextItem.getIndex(), NextItem.toItem("&r下一页 ("+page+"/"+maxPage+")"));
            Item i2 = NextItem.toItem("&r尾页");
            i2.setCount(2);
            panel.put(NextItem.getIndex() + 1, i2);
        }

        return panel;
    }

    public static Map<Integer, Item> getPlayerPanel(PlayerInfoManager infoManager){
        ArrayList<String> playerName = TheWorldShopMainClass.SELL_MANAGER.getPlayerNames();
        int page = infoManager.getPage();
        int maxPage = SellItemManager.mathShopItemPage(playerName);
        if(page > maxPage){
            page = maxPage;
        }
        Map<Integer,Item> panel = new LinkedHashMap<>();
        int index = 0;
        for(;index < LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("请选择玩家",new ArrayList<>()));
        }
        boolean isChose;
        for(String name: TheWorldShopMainClass.SELL_MANAGER.getArrayListByPage(page,playerName)){
            isChose = infoManager.getChosePlayer() != null && infoManager.getChosePlayer().equalsIgnoreCase(name);
            panel.put(index, PlayerSeekItem.toItem(name,isChose));
            index++;
        }
        for(index = ITEM_INDEX ;index < ITEM_INDEX + LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("请选择玩家",new ArrayList<>()));
        }
        return addPagePanel(page, maxPage, panel);
    }

    private static void addLastPage(int page, int maxPage, Map<Integer, Item> panel) {
        if(page > 1){
            panel.put(LastItem.getIndex(), LastItem.toItem("&r上一页 ("+page+"/"+maxPage+")"));
            Item i2 = LastItem.toItem("&r首页");
            i2.setCount(2);
            panel.put(LastItem.getIndex() - 1, i2);
        }
    }

    public static Map<Integer, Item> getSettingPanel(PlayerInfoManager infoManager){
        Map<Integer,Item> panel = new LinkedHashMap<>();
        panel.put(OrderItem.getIndex(), OrderItem.toItem("&r&a根据价格排序",infoManager.isSetting(ItemType.ORDER)));
        panel.put(PlayerItem.getIndex(), PlayerItem.toItem("&r&a根据玩家筛选",infoManager.isSetting(ItemType.PLAYER)));
        panel.put(OrderCountItem.getIndex(),OrderCountItem.toItem("&r&a根据物品数量排序",infoManager.isSetting(ItemType.ORDER_COUNT)));
        panel.put(ItemSeekItem.getIndex(),ItemSeekItem.toItem("&r&a根据物品筛选",infoManager.isSetting(ItemType.ITEM)));
        for(int index = 0;index < LINE_SIZE;index++){
            panel.put(index,IntervalItem.toItem("&c请设置筛选条件",new ArrayList<>()));

        }
        for(int index = 45;index < 54;index++){
            panel.put(index,IntervalItem.toItem("&c请设置筛选条件",new ArrayList<>()));

        }
        panel.put(49,QuitItem.toItem("&r&a返回全球市场"));

        return panel;
    }


    private static Map<Integer, Item> getItemsTo(int page, ArrayList<ShopItem> shopItems,int maxPage,boolean my){
        int index = 0;
        if(page > maxPage){
            page = maxPage;
        }
        Map<Integer,Item> panel = new LinkedHashMap<>();
        ArrayList<String> lore = new ArrayList<>();
        if(my) {
            lore.add(TextFormat.colorize('&',"&r&b数据信息\n"));

            int size = 0;
            if(shopItems.size() > 0){
                size = TheWorldShopMainClass.SELL_MANAGER.getPlayerAllShopItem(shopItems.get(0).getSellPlayer()).size();
            }
            lore.add(TextFormat.colorize('&',"&r&a当前拥有 &e" + size + " &a件物品\n"));
            lore.add(TextFormat.colorize('&',"&r&a最大物品上限 &e" + TheWorldShopMainClass.WORLD_CONFIG.getPlayerSellMax() + " &a件\n"));
            lore.add(TextFormat.colorize('&',"&r&a当前税收 &e" + (TheWorldShopMainClass.
                    WORLD_CONFIG.getTax() * 100) + "％\n"));
        }else{
            lore.add(TextFormat.colorize('&',"&r&b数据信息\n"));
            lore.add(TextFormat.colorize('&',"&r&a共计 &e" + TheWorldShopMainClass.SELL_MANAGER.maxSize() + " &a件物品\n"));
            lore.add(TextFormat.colorize('&',"&r&a共计 &e" + TheWorldShopMainClass.SELL_MANAGER.playerSize() + " &a位店家\n"));
            lore.add(TextFormat.colorize('&',"&r&a当前税收 &e" + (TheWorldShopMainClass.
                    WORLD_CONFIG.getTax() * 100) + "％\n"));

        }
        for(;index < LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("&r&c点击两次购买",lore));
        }
        for(ShopItem shopItem: shopItems){
            panel.put(index,shopItem.toItem());
            index++;
        }
        if(my) {
            panel.put(45, QuitItem.toItem("&r&a全球市场"));
        }else{
            panel.put(MySelfItem.getIndex(), MySelfItem.toItem("&r&e查看我的出售"));
        }
        addLastPage(page, maxPage, panel);
        panel.put(RefreshItem.getIndex(), RefreshItem.toItem("&r刷新"));
        panel.put(SettingItem.getIndex(),SettingItem.toItem("&r筛选"));
        if(maxPage > page) {
            panel.put(NextItem.getIndex(), NextItem.toItem("&r下一页 ("+page+"/"+maxPage+")"));
            Item i2 = NextItem.toItem("&r尾页");
            i2.setCount(2);
            panel.put(NextItem.getIndex() + 1, i2);
        }


        for(index = ITEM_INDEX ;index < ITEM_INDEX + LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("&r&c点击两次购买",lore));
        }
        return panel;

    }





    public void displayPlayer(Player player,Map<Integer, Item> itemMap,String name){
        ChestInventoryPanel panel = new ChestInventoryPanel(this,name);
        panel.setContents(itemMap);
        panel.id = Entity.entityCount++;
        player.addWindow(panel);
    }


    public void displayPlayer(Player player){
        PlayerInfoManager infoManager = PlayerInfoManager.getInstance(player.getName());
        ChestInventoryPanel panel = new ChestInventoryPanel(this,"全球市场");
        panel.setContents(getItems(infoManager));
        panel.id = Entity.entityCount++;
        TheWorldShopMainClass.CLICK_PANEL.put(player,panel);
        player.addWindow(panel);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
