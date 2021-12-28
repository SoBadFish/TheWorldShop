package org.badfish.theworldshop.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import me.onebone.economyapi.EconomyAPI;
import money.Money;
import net.player.api.Point;
import org.badfish.theworldshop.items.MoneySellItem;


/**
 * @author BadFish
 */
public class LoadMoney {


    private MoneySellItem.MoneyType type;

    public LoadMoney(MoneySellItem.MoneyType type){
        this.type = type;

    }



    public String getMonetaryUnit(){
        if (this.type == MoneySellItem.MoneyType.EconomyAPI) {
            return EconomyAPI.getInstance().getMonetaryUnit();
        }
        return "$";
    }

    public double myMoney(Player player){
        return myMoney(player.getName());
    }

    public double myMoney(String player){
        switch (this.type){
            case Money:
                if(Money.getInstance().getPlayers().contains(player)){
                    return Money.getInstance().getMoney(player);
                }
                break;
            case EconomyAPI:
                return EconomyAPI.getInstance().myMoney(player) ;
            case PlayerPoints:
                return Point.myPoint(player);
            default:break;
        }
        return 0;
    }

    public void addMoney(Player player, double money){
        addMoney(player.getName(), money);
    }

    public void addMoney(String player, double money){
        switch (this.type){
            case Money:
                if(Money.getInstance().getPlayers().contains(player)){
                    Money.getInstance().addMoney(player, (float) money);
                    return;
                }
                break;
            case EconomyAPI:
                EconomyAPI.getInstance().addMoney(player, money, true);
                return;
            case PlayerPoints:
                Point.addPoint(player, money);
                return;
            default:break;
        }
    }
    public void reduceMoney(Player player, double money){
        reduceMoney(player.getName(), money);
    }

    public void reduceMoney(String player, double money){
        switch (this.type){
            case Money:
                if(Money.getInstance().getPlayers().contains(player)){
                    Money.getInstance().reduceMoney(player, (float) money);
                    return;
                }
                break;
            case EconomyAPI:
                EconomyAPI.getInstance().reduceMoney(player, money, true);
                return;
            case PlayerPoints:
                Point.reducePoint(player, money);
                return;
            default:break;
        }
    }

    public static boolean isEnable(MoneySellItem.MoneyType type){
        switch (type) {
            case Money:
                return Server.getInstance().getPluginManager().getPlugin("Money") != null;
            case PlayerPoints:
                return Server.getInstance().getPluginManager().getPlugin("playerPoints") != null;
            case EconomyAPI:
                return Server.getInstance().getPluginManager().getPlugin("EconomyAPI") != null;
            default:break;
        }
        return false;
    }



}
