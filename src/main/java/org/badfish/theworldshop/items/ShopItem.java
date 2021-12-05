package org.badfish.theworldshop.items;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.utils.Tool;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author BadFish
 */
public class ShopItem  {

    private Item shopItem;

    private String sellPlayer;

    public final static String TAG = "Shop_Item_";

    private double sellMoney;

    private Item defaultItem;


    private boolean isRemove = false;


    private ShopItem(Item defaultItem,String sellPlayer,double sellMoney) {
        this.defaultItem = defaultItem.clone();
        this.shopItem = defaultItem.clone();
        this.sellPlayer = sellPlayer;
        this.sellMoney = sellMoney;
    }

    private static ArrayList<String> asList(String[] s){
        ArrayList<String> strings = new ArrayList<>();
        if(s.length > 0){
            for(String s1: s){
                strings.add(s1+"");
            }
        }
        return strings;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public static ShopItem formMap(Map map){
        try {
            Item i = Item.fromString(map.get("id").toString());
            //过滤空气
            if(i.getId() == 0){
                return null;
            }
            i.setCount(Integer.parseInt(map.get("count").toString()));
            String tag = map.get("tag").toString();

            if (!"not".equalsIgnoreCase(tag)) {
                byte[] tagForm = Tool.hexStringToBytes(tag);
                if(tagForm != null){
                    i.setCompoundTag(Item.parseCompoundTag(tagForm));
                }
            }
            String sellPlayer = map.get("sellPlayer").toString();
            boolean remove = false;
            if(map.containsKey("isRemove")){
                remove = (Boolean) map.get("isRemove");
            }
            double sellMoney = Double.parseDouble(map.get("sellMoney").toString());

            return cloneTo(i, sellPlayer, sellMoney,remove);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static ShopItem getShopItemByItem(Item item){
        CompoundTag tag = item.getNamedTag();
        if(tag != null){
            if(tag.contains(TAG+"tag") && tag.getString(TAG+"type").equalsIgnoreCase(ItemType.SELL.getName())){
                Item def = Item.get(item.getId(),item.getDamage());
                def.setCount(item.getCount());
                if(tag.contains(TAG+"defaultItem")){
                    byte[] tagForm = Tool.hexStringToBytes(tag.getString(TAG+"defaultItem"));
                    if(tagForm != null){
                        def.setNamedTag(Item.parseCompoundTag(tagForm));
                    }
                }

                ShopItem item1 = new ShopItem(def,tag.getString(TAG+"player"),tag.getDouble(TAG+"money"));
                item1.setRemove(tag.getBoolean(TAG+"isRemove"));
                return item1;
            }
        }
        return null;
    }

    public Item toItem(){
        return shopItem;
    }

    public static ShopItem cloneTo(Item defaultItem,String sellPlayer,double sellMoney,boolean isRemove){
        ShopItem item = new ShopItem(defaultItem,null,0);
        item.setSellMoney(sellMoney);
        item.setSellPlayer(sellPlayer);
        item.setRemove(isRemove);
        String[] loreItem =  item.shopItem.getLore();
        ArrayList<String> lore = new ArrayList<>();
        if(loreItem.length > 0){
            lore = asList(loreItem);
        }
        double m = 0;
        if(TheWorldShopMainClass.WORLD_CONFIG.getTax() > 0) {
           m = TheWorldShopMainClass.
                    WORLD_CONFIG.getTax() * sellMoney;
        }

        String m1 = String.format("%.2f",m);
        lore.add(TextFormat.colorize('&',"&r&7■■■■■■■■■■■■■■■■■■■■"));

        lore.add(TextFormat.colorize('&',"&r&e出售者     |   &a"+sellPlayer));
        if(isRemove){
            lore.add(TextFormat.colorize('&',"&r&d官方出售"));
            lore.add(TextFormat.colorize('&',"&r&e价格       |   &a"+(sellMoney) ));
        }else{
            if(TheWorldShopMainClass.WORLD_CONFIG.getTax() > 0){
                lore.add(TextFormat.colorize('&',"&r&e价格       |   &a"+(sellMoney + m) + " &r(&e↑"+m1+"&r)"));
                lore.add(TextFormat.colorize('&',"&r&e当前税收   |   &a"+ (TheWorldShopMainClass.
                        WORLD_CONFIG.getTax() * 100) + "％"));
            }else{
                lore.add(TextFormat.colorize('&',"&r&e价格       |   &a"+(sellMoney )));
            }

        }

        lore.add(TextFormat.colorize('&',""));
        lore.add(TextFormat.colorize('&',"&r&l&a      双击购买"));
        lore.add(TextFormat.colorize('&',"&r&7■■■■■■■■■■■■■■■■■■■■"));

        item.shopItem.setLore(lore.toArray(new String[0]));
        CompoundTag tag =  item.shopItem.getNamedTag();

        tag.putString(TAG+"tag","ShopItem");
        tag.putString(TAG+"type","sell");
        tag.putString(TAG+"player",sellPlayer);
        tag.putDouble(TAG+"money",sellMoney);
        tag.putBoolean(TAG+"isRemove",isRemove);
        if(defaultItem.hasCompoundTag()) {
            String b = Tool.bytesToHexString(defaultItem.getCompoundTag());
            if(b != null) {
                tag.putString(TAG + "defaultItem", b);
            }
        }
        item.shopItem.setCompoundTag(tag);
        return item;
    }

    private void setSellPlayer(String sellPlayer) {
        this.sellPlayer = sellPlayer;
    }

    private void setSellMoney(double sellMoney) {
        this.sellMoney = sellMoney;
    }

    public String getSellPlayer() {
        return sellPlayer;
    }

    public double getSellMoney() {
        return sellMoney;
    }

    public Item getDefaultItem() {
        return defaultItem;
    }

    public int getId(){
        return defaultItem.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ShopItem){
            if(((ShopItem) obj).defaultItem.equals(defaultItem,true,true)){
                if(sellPlayer.equalsIgnoreCase(((ShopItem) obj).sellPlayer)){
                    return (int)Math.ceil(sellMoney) == (int)Math.ceil(((ShopItem) obj).sellMoney);
                }
            }
        }
        return false;
    }
}
