package org.badfish.theworldshop.manager;

import cn.nukkit.utils.Config;
import org.badfish.theworldshop.items.ShopItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Sobadfish
 * @date 2022/11/14
 */
public class PlayerDataManager {

    public Config config;

    public LinkedHashMap<String,PlayerData> playerData;


    public PlayerDataManager(Config config,LinkedHashMap<String,PlayerData> playerData){
        this.config = config;
        this.playerData = playerData;
    }

    public boolean chunkBuyItem(String playerName,ShopItem item){
        if(item.limit > 0){
            PlayerData data = new PlayerData();
            if(!playerData.containsKey(playerName)){
                data.buyItem.put(item.uuid,1);
                playerData.put(playerName,data);
                return true;
            }else{
               data = playerData.get(playerName);
                if(item.limit > data.buyItem.get(item.uuid)){
                    data.buyItem.put(item.uuid,data.buyItem.get(item.uuid) + 1);
                    return true;
                }
            }
        }
        return false;
    }

    private static class PlayerData{

        public LinkedHashMap<UUID,Integer> buyItem;

        public LinkedHashMap<String, Object> toConfig(){
            LinkedHashMap<String, Object> so = new LinkedHashMap<>();
            for(Map.Entry<UUID,Integer> me: buyItem.entrySet()){
                so.put(me.getKey().toString(),me.getValue());
            }
            return so;
        }

    }

    public static PlayerDataManager initManager(Config config){
        LinkedHashMap<String,PlayerData> playerDatas = new LinkedHashMap<>();
        Map<String, Object> objectMap = config.getAll();
        for(String playerName: objectMap.keySet()){
            Map<?,?> obj = (Map<?,?>) objectMap.get(playerName);
            PlayerData data = new PlayerData();
            for(Map.Entry<?,?> mo : obj.entrySet()){
                data.buyItem.put(UUID.fromString(mo.getKey().toString()),
                        Integer.parseInt(mo.getValue().toString()));
            }
            playerDatas.put(playerName,data);
        }
        return new PlayerDataManager(config,playerDatas);
    }

    public void save(){
        for(Map.Entry<String,PlayerData> dataEntry: playerData.entrySet()){
            config.set(dataEntry.getKey(),dataEntry.getValue().toConfig());
        }
        config.save();
    }




}
