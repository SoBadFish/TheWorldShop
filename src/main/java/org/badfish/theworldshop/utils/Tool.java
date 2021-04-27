package org.badfish.theworldshop.utils;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.ShopItem;



/**
 * @author BadFish
 */
public class Tool {


    public static ItemType getItemType(Item item){
        if(item.hasCompoundTag() && item.getNamedTag().contains(ShopItem.TAG + "tag")){
            String type = item.getNamedTag().getString(ShopItem.TAG+"type");
            return ItemType.get(type);
        }
        return null;
    }

    public static int getItemCount(Item item, Inventory inventory){
        int count = 0;
        for(Item i: inventory.getContents().values()){
            if(i.equals(item,true,true)){
                count += i.getCount();
            }
        }
        return count;
    }


    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }





}
