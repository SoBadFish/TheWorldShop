package org.badfish.theworldshop.configs;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import org.badfish.theworldshop.items.MoneySellItem;
import org.badfish.theworldshop.items.ShopItem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * 数据库读取的
 * */
public class ShopItemSQLData {


    //唯一ID
    public String uuid;

    public String master;

    public double money;

    public String item;

    //金钱类型
    public String money_type;


    //限购
    public int limit_buy;


    //是否不消耗库存
    public boolean is_remove;

    public static ShopItemSQLData shopItem2SQLData(ShopItem shopItem) {
        ShopItemSQLData shopItemSQLData = new ShopItemSQLData();
        shopItemSQLData.uuid = shopItem.uuid.toString();
        shopItemSQLData.master = shopItem.getSellPlayer();
        shopItemSQLData.money = shopItem.getSellMoney();
        String item = "";

        try {
            item = new String(Base64.getEncoder().encode(NBTIO.write(NBTIO.putItemHelper(shopItem.getDefaultItem()))),StandardCharsets.UTF_8);
        } catch (IOException ignore) {
        }
        shopItemSQLData.item = item;
        shopItemSQLData.money_type = shopItem.getMoneyType().name();
        shopItemSQLData.limit_buy = shopItem.limit;
        shopItemSQLData.is_remove = shopItem.isRemove();

        return shopItemSQLData;

    }

    public ShopItem asShopItem(){
        Item dataItem = null;
        try {
            dataItem = NBTIO.getItemHelper(NBTIO.read(Base64.getDecoder().decode(item.getBytes(StandardCharsets.UTF_8))));
        } catch (IOException e) {
            return null;
        }
        if(dataItem == null){
            return null;
        }

        MoneySellItem.MoneyType moneyType = MoneySellItem.MoneyType.get(money_type);
        if(moneyType == null){
            moneyType = MoneySellItem.MoneyType.EconomyAPI;
        }
        return ShopItem.cloneTo(
                UUID.fromString(uuid),
                dataItem,
                master,
                moneyType,
                money,
                is_remove,
                limit_buy
                );


    }

}
