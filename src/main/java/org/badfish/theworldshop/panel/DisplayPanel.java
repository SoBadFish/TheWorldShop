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
import org.badfish.theworldshop.items.MoneySellItem;
import org.badfish.theworldshop.items.ShopItem;
import org.badfish.theworldshop.items.paneitem.defaultpanelitem.*;
import org.badfish.theworldshop.items.paneitem.settingpanelitem.*;
import org.badfish.theworldshop.language.TransferVariable;
import org.badfish.theworldshop.manager.PlayerInfoManager;
import org.badfish.theworldshop.manager.PlayerSellItemManager;
import org.badfish.theworldshop.manager.SellItemManager;
import org.badfish.theworldshop.panel.lib.AbstractFakeInventory;
import org.badfish.theworldshop.utils.LoadMoney;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BadFish
 */
public class DisplayPanel implements InventoryHolder {

    private AbstractFakeInventory inventory;


    private static final int ITEM_INDEX = 36;

    private static final int LINE_SIZE = 9;



    public static  Map<Integer, Item> getPlayerItems(PlayerInfoManager infoManager){
        int page = infoManager.getPage();
        ArrayList<ShopItem> shopItems = TheWorldShopMainClass.SELL_MANAGER
                .getPlayerAllShopItem(infoManager.getPlayerName());
        for(ItemType itemType : infoManager.getSettings()){
            switch (itemType){
                case MONEY_SUB_ITEM:
                    if(infoManager.getMoneyType() != null) {
                        shopItems = TheWorldShopMainClass.SELL_MANAGER.getMoneyTypeItem(shopItems, infoManager.getMoneyType());
                    }
                    break;
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
                case HIDE_LIMIT:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.hiddenLimitItems(shopItems);
                    break;
                case ONLY_DISPLAY_LIMIT:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.onlyDisplayLimitItems(shopItems);
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
        return getItemsTo(infoManager,infoManager.getPage(),shopItems,maxPage,true,infoManager.isWindows());

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
        Map<Integer,Item> panel = defaultPanel("&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.panelStringExceptSell)+": $"+money);
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
        Map<Integer,Item> panel = defaultPanel("&r&c"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.playerPanelTitleUp));
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
                case MONEY_SUB_ITEM:
                    if(infoManager.getMoneyType() != null) {
                        shopItems = TheWorldShopMainClass.SELL_MANAGER.getMoneyTypeItem(shopItems, infoManager.getMoneyType());
                    }
                    break;
                case PLAYER_SELL:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.getPlayerItemShopItem(shopItems);
                    break;
                case SYSTEM_SELL:
                    shopItems = TheWorldShopMainClass.SELL_MANAGER.getSystemItemShopItem(shopItems);
                    break;
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
        return getItemsTo(infoManager,infoManager.getPage(),shopItems,maxPage,false,infoManager.isWindows());
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
            panel.put(index, IntervalItem.toItem("&r&c"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelChoseUp),new ArrayList<>()));
        }
        boolean isChose;
        for(Item i: TheWorldShopMainClass.SELL_MANAGER.getArrayListByPage(page,items)){
            isChose = infoManager.getChoseItem() != null && infoManager.getChoseItem().equals(i,true,false);
            panel.put(index, ItemAsSeekItem.toItem(i,isChose));
            index++;
        }
        for(index = ITEM_INDEX ;index < ITEM_INDEX + LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("&r&c"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelChoseUp),new ArrayList<>()));
        }
        return addPagePanel(page, maxPage, panel,infoManager.isWindows());
    }

    private static Map<Integer, Item> addPagePanel(int page, int maxPage, Map<Integer, Item> panel,boolean isWindows) {
        panel.put(45, QuitItem.toItem("&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelBack)+TheWorldShopMainClass.TITLE));
        addLastPage(page,maxPage,panel,isWindows);
        addPanelPageNext(page, maxPage, panel);

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
            panel.put(index, IntervalItem.toItem(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelChosePlayer),new ArrayList<>()));
        }
        boolean isChose;
        for(String name: TheWorldShopMainClass.SELL_MANAGER.getArrayListByPage(page,playerName)){
            isChose = infoManager.getChosePlayer() != null && infoManager.getChosePlayer().equalsIgnoreCase(name);
            panel.put(index, PlayerSeekItem.toItem(name,isChose));
            index++;
        }
        for(index = ITEM_INDEX ;index < ITEM_INDEX + LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelChosePlayer),new ArrayList<>()));
        }
        return addPagePanel(page, maxPage, panel,infoManager.isWindows());
    }

    public static Map<Integer, Item> getMoneyPanel(PlayerInfoManager infoManager){
        Map<Integer,Item> panel = new LinkedHashMap<>();
        int index = 0;
        for(;index < LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelChoseMoney),new ArrayList<>()));
        }
        boolean isChose;
        for(MoneySellItem.MoneyType moneyType: MoneySellItem.MoneyType.values()){
            if(!LoadMoney.isEnable(moneyType)){
                continue;
            }
            isChose = infoManager.getMoneyType() != null && infoManager.getMoneyType() == moneyType;
            panel.put(index,MoneySubItem.toItem(moneyType.name(),isChose));
            index++;
        }
        for(index = ITEM_INDEX ;index < ITEM_INDEX + LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelChosePlayer),new ArrayList<>()));
        }
        return addPagePanel(1, 1, panel,infoManager.isWindows());
    }

    private static void addLastPage(int page, int maxPage, Map<Integer, Item> panel,boolean isWindows) {
        if(page > 1){
            if(isWindows){
                panel.put(LastItem.getIndex(), LastItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelLast)+" ("+page+"/"+maxPage+")"));
                Item i2 = LastItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelFirstPage));
                i2.setCount(2);
                panel.put(LastItem.getIndex() - 1, i2);
            }else{
                panel.put(48, LastItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelLast)+" ("+page+"/"+maxPage+")"));
                Item i2 = LastItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelFirstPage));
                i2.setCount(2);
                panel.put(47, i2);
            }

        }
    }

    public static Map<Integer, Item> getSettingPanel(PlayerInfoManager infoManager){
        Map<Integer,Item> panel = new LinkedHashMap<>();
        if(infoManager.isWindows()){
            panel.put(OrderItem.getIndex(), OrderItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByMoney),infoManager.isSetting(ItemType.ORDER)));
            panel.put(PlayerItem.getIndex(), PlayerItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByPlayer),infoManager.isSetting(ItemType.PLAYER)));
            panel.put(OrderCountItem.getIndex(),OrderCountItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByItemCount),infoManager.isSetting(ItemType.ORDER_COUNT)));
            panel.put(SystemShopItem.getIndex(),SystemShopItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByServerShop),infoManager.isSetting(ItemType.SYSTEM_SELL)));
            panel.put(PlayerShopItem.getIndex(),PlayerShopItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByPlayerShop),infoManager.isSetting(ItemType.PLAYER_SELL)));
            panel.put(ItemSeekItem.getIndex(),ItemSeekItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByItem),infoManager.isSetting(ItemType.ITEM)));
            panel.put(MoneyTypeItem.getIndex(),MoneyTypeItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByMoneyType),infoManager.isSetting(ItemType.MONEY_TYPE)));
            panel.put(HiddenLimitItem.getIndex(),HiddenLimitItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByHiddenItem),infoManager.isSetting(ItemType.HIDE_LIMIT)));
            panel.put(DisplayLimitItem.getIndex(),DisplayLimitItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByOnlyDisplayItem),infoManager.isSetting(ItemType.ONLY_DISPLAY_LIMIT)));
        }else{
            panel.put(21 - 4, OrderItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByMoney),infoManager.isSetting(ItemType.ORDER)));
            panel.put(22 - 4, PlayerItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByPlayer),infoManager.isSetting(ItemType.PLAYER)));
            panel.put(23 - 4,OrderCountItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByItemCount),infoManager.isSetting(ItemType.ORDER_COUNT)));
            panel.put(24 - 4,SystemShopItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByServerShop),infoManager.isSetting(ItemType.SYSTEM_SELL)));
            panel.put(25 - 4,PlayerShopItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByPlayerShop),infoManager.isSetting(ItemType.PLAYER_SELL)));
            panel.put(26 - 4,ItemSeekItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByItem),infoManager.isSetting(ItemType.ITEM)));
            panel.put(27 - 4,MoneyTypeItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByMoneyType),infoManager.isSetting(ItemType.MONEY_TYPE)));
            panel.put(28- 4,HiddenLimitItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByHiddenItem),infoManager.isSetting(ItemType.HIDE_LIMIT)));
            panel.put(29 - 4,DisplayLimitItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelSettingByOnlyDisplayItem),infoManager.isSetting(ItemType.ONLY_DISPLAY_LIMIT)));
        }

        for(int index = 0;index < LINE_SIZE;index++){
            panel.put(index,IntervalItem.toItem("&c"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelPlaceSetting),new ArrayList<>()));

        }
        for(int index = 45;index < 54;index++){
            panel.put(index,IntervalItem.toItem("&c"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelPlaceSetting),new ArrayList<>()));

        }
        panel.put(49,QuitItem.toItem("&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelBack)+TheWorldShopMainClass.TITLE));

        return panel;
    }


    private static Map<Integer, Item> getItemsTo(PlayerInfoManager infoManager,int page, ArrayList<ShopItem> shopItems,int maxPage,boolean my,boolean isWindows){
        int index = 0;
        if(page > maxPage){
            page = maxPage;
        }
        Map<Integer,Item> panel = new LinkedHashMap<>();
        ArrayList<String> lore = new ArrayList<>();
        if(my) {
            lore.add(TextFormat.colorize('&',"&r&b"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelDataInfo)+"\n"));

            int size = 0;
            if(shopItems.size() > 0){
                size = TheWorldShopMainClass.SELL_MANAGER.getPlayerAllShopItem(shopItems.get(0).getSellPlayer()).size();
            }
            lore.add(TextFormat.colorize('&',"&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.showItemCountInfo,new TransferVariable(size))+"\n"));
            lore.add(TextFormat.colorize('&',"&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.showItemCountMax,new TransferVariable(TheWorldShopMainClass.WORLD_CONFIG.getPlayerSellMax()))));

        }else{
            lore.add(TextFormat.colorize('&',"&r&b"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelDataInfo)+"\n"));
            lore.add(TextFormat.colorize('&',"&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.showPublicItemCountInfo,new TransferVariable(TheWorldShopMainClass.SELL_MANAGER.maxSize()))));
            lore.add(TextFormat.colorize('&',"&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.showPublicPlayerSize,new TransferVariable(TheWorldShopMainClass.SELL_MANAGER.playerSize()))));


        }
        if(TheWorldShopMainClass.WORLD_CONFIG.getTax() > 0) {
            lore.add(TextFormat.colorize('&', "&r&a"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.showItemTax,new TransferVariable((TheWorldShopMainClass.
                    WORLD_CONFIG.getTax() * 100)))));
        }
        for(;index < LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("&r&c"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.tipInfoClickTwo),lore));
        }
        for(ShopItem shopItem: shopItems){
            panel.put(index,shopItem.toItem(infoManager.getPlayerName()));
            index++;
        }
        if(my) {
            panel.put(45, QuitItem.toItem(TheWorldShopMainClass.TITLE));
        }else{
            panel.put(MySelfItem.getIndex(), MySelfItem.toItem("&r&e"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.tipSeeMySell)));
        }
        addLastPage(page, maxPage, panel,isWindows);
        panel.put(RefreshItem.getIndex(), RefreshItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.tipRefresh)));
        panel.put(SettingItem.getIndex(),SettingItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.tipInfoChose)));
        addPanelPageNext(page, maxPage, panel);


        for(index = ITEM_INDEX ;index < ITEM_INDEX + LINE_SIZE;index++){
            panel.put(index, IntervalItem.toItem("&r&c"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.tipInfoClickTwo),lore));
        }
        return panel;

    }

    private static void addPanelPageNext(int page, int maxPage, Map<Integer, Item> panel) {
        if(maxPage > page) {
            panel.put(NextItem.getIndex(), NextItem.toItem("&r"+ TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelNext)+" ("+page+"/"+maxPage+")"));
            Item i2 = NextItem.toItem("&r"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemPanelEndPage));
            i2.setCount(2);
            panel.put(NextItem.getIndex() + 1, i2);
        }
    }


    public void displayPlayer(Player player,Map<Integer, Item> itemMap,String name){

        ChestInventoryPanel panel = new ChestInventoryPanel(this,name);
        panel.setContents(itemMap);
        panel.id = Entity.entityCount++;
        inventory = panel;
        player.addWindow(panel);

    }


    public void displayPlayer(Player player){
        PlayerInfoManager infoManager = PlayerInfoManager.getInstance(player.getName());
        ChestInventoryPanel panel = new ChestInventoryPanel(this,TheWorldShopMainClass.TITLE);
        panel.setContents(getItems(infoManager));
        panel.id = Entity.entityCount++;
        TheWorldShopMainClass.CLICK_PANEL.put(player,panel);
        inventory = panel;
        player.addWindow(panel);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
