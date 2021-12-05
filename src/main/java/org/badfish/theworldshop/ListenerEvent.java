package org.badfish.theworldshop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.TextFormat;
import me.onebone.economyapi.EconomyAPI;
import org.badfish.theworldshop.events.ChoseByItemEvent;
import org.badfish.theworldshop.events.PlayerBuyItemEvent;
import org.badfish.theworldshop.events.PlayerSellItemEvent;
import org.badfish.theworldshop.events.SellItemEvent;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.ShopItem;
import org.badfish.theworldshop.items.paneitem.defaultpanelitem.LastInventoryItem;
import org.badfish.theworldshop.items.paneitem.defaultpanelitem.MoneyItem;
import org.badfish.theworldshop.manager.PlayerInfoManager;
import org.badfish.theworldshop.manager.PlayerSellItemManager;
import org.badfish.theworldshop.panel.ChestInventoryPanel;
import org.badfish.theworldshop.panel.DisplayPanel;
import org.badfish.theworldshop.utils.Tool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BadFish
 */
public class ListenerEvent implements Listener {

    private static final int MONEY_UI_NUMBER = 0x5014;

    public static LinkedHashMap<Player,ShopItem> BUY_LOCK = new LinkedHashMap<>();

    private static LinkedHashMap<Player,Item> SELL_LOCK = new LinkedHashMap<>();

    private static ArrayList<Player> LOOK_MYSELF = new ArrayList<>();

    public static ArrayList<Player> LOOK_ITEMS = new ArrayList<>();

    public static ArrayList<Player> LOOK_PLAYERS = new ArrayList<>();

    private static LinkedHashMap<Player,Item> SELL_ITEM = new LinkedHashMap<>();

    @EventHandler
    public void onUp(PlayerSellItemEvent event){
        Item item = event.getItem();
        Player player = event.getPlayer();
        if(TheWorldShopMainClass.SELL_MANAGER.getPlayerSellCount(player.getName()) <
                TheWorldShopMainClass.WORLD_CONFIG.getPlayerSellMax()) {
            player.getInventory().removeItem(item);
            player.sendMessage(TextFormat.colorize('&',"&a出售物品成功 价格&e"+event.getMoney()));
        }else{
            player.sendMessage(TextFormat.colorize('&',"&c你无法上架更多的物品"));
            event.setCancelled();
        }
    }

    @EventHandler
    public void onPlayerFormRespondedEvent(PlayerFormRespondedEvent event){
        Player player = event.getPlayer();
        if(event.getFormID() == MONEY_UI_NUMBER){
            if(event.wasClosed()){
                return;
            }
            if(SELL_ITEM.containsKey(player)){
                Item i = LastInventoryItem.formItem(SELL_ITEM.get(player));
                SELL_ITEM.remove(player);
                String number = ((FormResponseCustom)event.getResponse()).getInputResponse(0);
                boolean isRemove = false;
                if(player.isOp()){
                    isRemove = ((FormResponseCustom)event.getResponse()).getToggleResponse(1);
                }
                double d;
                try {
                    d = Double.parseDouble(number);
                    if(d > 0){
                        if(d < TheWorldShopMainClass.WORLD_CONFIG.getMoneyMin() ||
                                d > TheWorldShopMainClass.WORLD_CONFIG.getMoneyMax()){
                            player.sendMessage(TextFormat.colorize('&',
                                    "&c请不要超过 "+TheWorldShopMainClass.WORLD_CONFIG.getMoneyMax()+
                                    " 或 小于 "+TheWorldShopMainClass.WORLD_CONFIG.getMoneyMin()));
                            return;
                        }
                        if(i.getCount() <= Tool.getItemCount(i,player.getInventory())){
                            TheWorldShopMainClass.SELL_MANAGER.addSellItem(player, i,d,isRemove);

                        }else{
                            player.sendMessage(TextFormat.colorize('&',"&c您背包内物品不足"));
                        }
                    }else{
                        player.sendMessage(TextFormat.colorize('&',"&c请重新选择物品并输入正确的价格"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    player.sendMessage(TextFormat.colorize('&',"&c请重新选择物品并输入正确的价格"));
                }

            }
        }

    }

    private void displaySetting(Player player,Item item,PlayerInfoManager playerInfoManager,ItemType type,Inventory inventory){
        if(type != null) {
            switch (type) {
                case SEEK_PLAYER:
                    player.getLevel().addSound(player.getPosition(), Sound.RANDOM_ORB,1,1,player);
                    if(playerInfoManager.getChosePlayer() == null) {
                        playerInfoManager.setChosePlayer(item.getCustomName());
                        playerInfoManager.addSettings(type);
                    }else{
                        if(item.getCustomName().equalsIgnoreCase(playerInfoManager.getChosePlayer())) {
                            playerInfoManager.setChosePlayer(null);
                            playerInfoManager.addSettings(type);
                        }else{
                            playerInfoManager.setChosePlayer(item.getCustomName());
                        }
                    }
                    inventory.setContents(DisplayPanel.getPlayerPanel(playerInfoManager));
                    break;
                case SEEK_ITEM:
                    player.getLevel().addSound(player.getPosition(), Sound.RANDOM_ORB,1,1,player);
                    if(playerInfoManager.getChoseItem() == null){
                        playerInfoManager.setChoseItem(item);
                        playerInfoManager.addSettings(type);
                    }else{
                        if(playerInfoManager.getChoseItem().equals(item,true,false)){
                            playerInfoManager.setChoseItem(null);
                            playerInfoManager.addSettings(type);
                        }else{
                            playerInfoManager.setChoseItem(item);
                        }
                    }
                    inventory.setContents(DisplayPanel.getItemPanel(playerInfoManager));
                    break;
                case PLAYER_SELL:
                case ORDER_COUNT:
                case ORDER:
                case SYSTEM_SELL:
                    playerInfoManager.addSettings(type);
                    player.getLevel().addSound(player.getPosition(), Sound.RANDOM_ORB,1,1,player);
                case SETTING:
                    inventory.setContents(DisplayPanel.getSettingPanel(playerInfoManager));
                    break;

                default:break;
            }
        }
    }
    private void reset(){
        for(Map.Entry<Player,ChestInventoryPanel> panelEntry:TheWorldShopMainClass.CLICK_PANEL.entrySet()){
            PlayerInfoManager manager = PlayerInfoManager.getInstance(panelEntry.getKey().getName());
            if(LOOK_MYSELF.contains(panelEntry.getKey())){
                panelEntry.getValue().setContents(DisplayPanel.getPlayerItems(manager));
            }else{
                panelEntry.getValue().setContents(DisplayPanel.getItems(manager));
            }

        }
    }

    private void display(Player player,Item item,PlayerInfoManager playerInfoManager,ItemType type,Inventory inventory){
        if(type != null){
            switch (type){
                case LAST:
                    player.level.addSound(player,Sound.ITEM_BOOK_PAGE_TURN,1,1,player);
                    if(item.getCount() == 2){
                        playerInfoManager.setPage(1);
                        displayLookPanel(player, playerInfoManager, inventory);
                        return;
                    }
                    playerInfoManager.setPage(playerInfoManager.getPage() - 1);
                    if(LOOK_PLAYERS.contains(player)){
                        inventory.setContents(DisplayPanel.getPlayerPanel(playerInfoManager));
                        break;
                    }
                    if(LOOK_ITEMS.contains(player)){
                        inventory.setContents(DisplayPanel.getItemPanel(playerInfoManager));
                        break;
                    }
                    if(LOOK_MYSELF.contains(player)){
                        inventory.setContents(DisplayPanel.getPlayerItems(playerInfoManager));
                        break;
                    }
                    inventory.setContents(DisplayPanel.getItems(playerInfoManager));
                    break;
                case NEXT:
                    player.level.addSound(player,Sound.ITEM_BOOK_PAGE_TURN,1,1,player);
                    if(item.getCount() == 2){
                        playerInfoManager.setPage(TheWorldShopMainClass.SELL_MANAGER.getMaxPage());
                        displayLookPanel(player, playerInfoManager, inventory);
                        break;
                    }
                    playerInfoManager.setPage(playerInfoManager.getPage() + 1);
                    if(LOOK_PLAYERS.contains(player)){
                        inventory.setContents(DisplayPanel.getPlayerPanel(playerInfoManager));
                        break;
                    }
                    if(LOOK_ITEMS.contains(player)){
                        inventory.setContents(DisplayPanel.getItemPanel(playerInfoManager));
                        break;
                    }
                    if(LOOK_MYSELF.contains(player)){
                        inventory.setContents(DisplayPanel.getPlayerItems(playerInfoManager));
                        break;
                    }
                    inventory.setContents(DisplayPanel.getItems(playerInfoManager));
                    break;

                case QUIT:
                    playerInfoManager.setPage(1);
                    LOOK_MYSELF.remove(player);
                    LOOK_PLAYERS.remove(player);
                    LOOK_ITEMS.remove(player);
                case REFRESH:
                    if(LOOK_MYSELF.contains(player)){
                        inventory.setContents(DisplayPanel.getPlayerItems(playerInfoManager));
                        break;
                    }
                    inventory.setContents(DisplayPanel.getItems(playerInfoManager));
                    break;
                case MYSELF:
                    LOOK_MYSELF.add(player);
                    inventory.setContents(DisplayPanel.getPlayerItems(playerInfoManager));
                    break;
                case SEEK_PLAYER:
                case SEEK_ITEM:
                case ORDER:
                case SETTING:
                case ORDER_COUNT:
                case SYSTEM_SELL:
                case PLAYER_SELL:
                    displaySetting(player, item, playerInfoManager, type, inventory);
                    break;
                case PLAYER:
                    LOOK_PLAYERS.add(player);
                    inventory.setContents(DisplayPanel.getPlayerPanel(playerInfoManager));
                    break;
                case INVENTORY_ITEM:
                    player.getLevel().addSound(player.getPosition(), Sound.RANDOM_ORB,1,1,player);
                    inventory.close(player);
                    SELL_ITEM.put(player,item.clone());
                    FormWindowCustom custom = new FormWindowCustom("请输入购买价格");
                    custom.addElement(new ElementInput("请输入你要出售的金钱数"));
                    if(player.isOp()){
                        custom.addElement(new ElementToggle("是否为系统商店(不消耗数量)"));
                    }
                    player.showFormWindow(custom,MONEY_UI_NUMBER);
                    break;
                case ITEM:
                    LOOK_ITEMS.add(player);
                    inventory.setContents(DisplayPanel.getItemPanel(playerInfoManager));
                    break;
                case SELL:
                    ChoseByItemEvent event1 = new ChoseByItemEvent(player,ShopItem.getShopItemByItem(item));
                    Server.getInstance().getPluginManager().callEvent(event1);
                    if(event1.isCancelled()){
                        return;
                    }
                    //刷新所有玩家界面
                    reset();
                    break;
                case MONEY_ITEM:
                    SellItemEvent event = new SellItemEvent(player,item);
                    Server.getInstance().getPluginManager().callEvent(event);
                    if(event.isCancelled()){
                        return;
                    }
                    inventory.setContents(DisplayPanel.inventorySellItemPanel(player));
                    break;
                default:break;
            }
        }
    }

    @EventHandler
    public void onItemSell(SellItemEvent event){
        Player player = event.getPlayer();
        Item sellItem = event.getItem();
        if(!SELL_LOCK.containsKey(player)){
            SELL_LOCK.put(player, sellItem);
            event.setCancelled();
            player.getLevel().addSound(player.getPosition(),Sound
                    .MOB_VILLAGER_NO,1,1,player);
            player.sendActionBar("再点一次出售物品");
            return;
        }
        Item i = SELL_LOCK.get(player);
        if(i.equals(sellItem)){
            SELL_LOCK.remove(player);
            PlayerSellItemManager manager = TheWorldShopMainClass.PLAYER_SELL.
                    get(TheWorldShopMainClass.PLAYER_SELL.
                            indexOf(PlayerSellItemManager.getInstance(player.getName())));
            Item last = MoneyItem.formItem(i);
            if(Tool.getItemCount(last,player.getInventory()) >= last.getCount()){
                player.getInventory().removeItem(last);
                double m = TheWorldShopMainClass.MONEY_ITEM.mathMoney(last);
                EconomyAPI.getInstance().addMoney(player,m);
                player.sendMessage("出售成功 获得 "+m);
            }
            manager.getSellItems().remove(last);
        }else{
            SELL_LOCK.remove(player);
            event.setCancelled();
        }

    }


    private void displayLookPanel(Player player, PlayerInfoManager playerInfoManager, Inventory inventory) {
        if(LOOK_PLAYERS.contains(player)){
            inventory.setContents(DisplayPanel.getPlayerPanel(playerInfoManager));
            return;
        }
        if(LOOK_ITEMS.contains(player)){
            inventory.setContents(DisplayPanel.getItemPanel(playerInfoManager));
            return;
        }
        if(LOOK_MYSELF.contains(player)){
            inventory.setContents(DisplayPanel.getPlayerItems(playerInfoManager));
            return;
        }
        inventory.setContents(DisplayPanel.getItems(playerInfoManager));
    }

    @EventHandler
    public void onItemChange(InventoryTransactionEvent event) {
        InventoryTransaction transaction = event.getTransaction();
        for (InventoryAction action : transaction.getActions()) {
            for (Inventory inventory : transaction.getInventories()) {
                if(inventory instanceof ChestInventoryPanel) {
                    event.setCancelled();
                    for(Player player: inventory.getViewers()) {
                        Item item = action.getSourceItem();
                        if (item.hasCompoundTag() && item.getNamedTag().contains(ShopItem.TAG + "tag")) {
                            PlayerInfoManager playerInfoManager = PlayerInfoManager.getInstance(player.getName());
                            ItemType type = Tool.getItemType(item);
                            display(player,item,playerInfoManager,type,inventory);
                        }
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onBuy(ChoseByItemEvent event){
        ShopItem shopItem = event.getItem();
        Player player = event.getPlayer();
        if(player.getName().equalsIgnoreCase(shopItem.getSellPlayer())){
            if(!BUY_LOCK.containsKey(player)) {
                BUY_LOCK.put(player,shopItem);
                event.setCancelled();
                player.getLevel().addSound(player.getPosition(),Sound
                .MOB_VILLAGER_NO,1,1,player);
                player.sendActionBar("再点一次撤销物品");
                return;
            }
        }
        if(!BUY_LOCK.containsKey(player)){
            BUY_LOCK.put(player, shopItem);
            event.setCancelled();
            player.getLevel().addSound(player.getPosition(),Sound
                    .MOB_VILLAGER_NO,1,1,player);
            player.sendActionBar("再点击一次购买");
            return;
        }

        ShopItem click = BUY_LOCK.get(player);
        if(click.equals(shopItem)){
            BUY_LOCK.remove(player);
            double sellMoney = shopItem.getSellMoney();
            if(!shopItem.isRemove()){
                if(TheWorldShopMainClass.WORLD_CONFIG.getTax() > 0) {
                    sellMoney = (sellMoney * TheWorldShopMainClass.WORLD_CONFIG.getTax()) + sellMoney;
                }
            }
            PlayerBuyItemEvent event1 = new PlayerBuyItemEvent(player,sellMoney,shopItem.getDefaultItem());
            Server.getInstance().getPluginManager().callEvent(event1);
            if(event1.isCancelled()){
                event.setCancelled();
                return;
            }
            sellMoney = event1.getMoney();
            if(player.getName().equalsIgnoreCase(shopItem.getSellPlayer())){
                if(player.getInventory().canAddItem(event1.getItem())) {
                    TheWorldShopMainClass.SELL_MANAGER.removeItem(shopItem);
                }else{
                    player.sendActionBar("您的背包没有空间");
                    player.getLevel().addSound(player.getPosition(),Sound
                            .MOB_VILLAGER_NO,1,1,player);
                    event.setCancelled();
                    return;
                }
                player.getLevel().addSound(player.getPosition(),Sound
                        .MOB_VILLAGER_YES,1,1,player);
                player.getInventory().addItem(event1.getItem());
                return;
            }


            if(EconomyAPI.getInstance().myMoney(player) >= sellMoney){
                if(player.getInventory().canAddItem(shopItem.getDefaultItem())){
                    player.getInventory().addItem(shopItem.getDefaultItem());
                    if(!shopItem.isRemove()) {
                        TheWorldShopMainClass.SELL_MANAGER.removeItem(shopItem);
                    }
                    player.getLevel().addSound(player.getPosition(),Sound
                            .MOB_VILLAGER_YES,1,1,player);
                    player.sendMessage(TextFormat.colorize('&',"&a购买成功! &e花费: &f"+sellMoney));
                }else{
                    player.getLevel().addSound(player.getPosition(),Sound
                            .MOB_VILLAGER_NO,1,1,player);
                    player.sendActionBar(TextFormat.colorize('&',"&c您的背包没有空间"));
                    event.setCancelled();
                    return;
                }
                String target = shopItem.getSellPlayer();
                if(!shopItem.isRemove()) {
                    EconomyAPI.getInstance().addMoney(target, shopItem.getSellMoney());
                }
                EconomyAPI.getInstance().reduceMoney(player,sellMoney);
            }else{
                player.getLevel().addSound(player.getPosition(),Sound
                        .MOB_VILLAGER_NO,1,1,player);
                player.sendActionBar("您的金钱不足");
                event.setCancelled();
            }
        }else{
            BUY_LOCK.remove(player);
            player.getLevel().addSound(player.getPosition(),Sound
                    .MOB_VILLAGER_NO,1,1,player);
            event.setCancelled();
        }

    }

}