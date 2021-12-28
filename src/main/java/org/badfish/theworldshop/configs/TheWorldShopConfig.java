package org.badfish.theworldshop.configs;

import cn.nukkit.utils.Config;
import org.badfish.theworldshop.items.MoneySellItem;
import org.badfish.theworldshop.language.BaseLanguage;
import org.badfish.theworldshop.language.LanguageManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BadFish
 */
public class TheWorldShopConfig {

    private double tax;

    private BaseLanguage language;

    private String title;

    private double moneyMin, moneyMax;

    private int playerSellMax;

    private Map<String, String> moneyName;

    private TheWorldShopConfig(String title,double tax, int playerSellMax, double moneyMin, double moneyMax, Map<String, String> moneyName,String lang){
        this.title = title;
        this.moneyMax = moneyMax;
        this.moneyMin = moneyMin;
        this.playerSellMax = playerSellMax;
        this.tax = tax;
        this.moneyName = moneyName;
        this.language = LanguageManager.getLanguage(lang);
    }


    public double getMoneyMax() {
        return moneyMax;
    }

    public double getMoneyMin() {
        return moneyMin;
    }

    public static TheWorldShopConfig load(Config config){
        return new TheWorldShopConfig(config.getString("title","交易行"),config.getDouble("tax",0.2)
                ,config.getInt("player-sell-max",10)
                ,config.getDouble("sell-money-min",0.1)
                ,config.getDouble("sell-money-max",10000)
                ,config.get("money-name",new LinkedHashMap<String, String>(){{
                    put("EconomyAPI","&e金币");
                    put("PlayerPoints","&d点券");
                    put("Money","&2金钱");

        }})
                ,config.getString("lang","chs"));
    }

    public String getTitle() {
        return title;
    }

    public double getTax() {
        return tax;
    }

    public String getMoneyTypeName(MoneySellItem.MoneyType moneyType){
        if(moneyName.containsKey(moneyType.name())){
            return moneyName.get(moneyType.name());
        }
        return "&e金币";
    }

    public BaseLanguage getLanguage() {
        return language;
    }

    public int getPlayerSellMax() {
        return playerSellMax;
    }
}
