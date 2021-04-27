package org.badfish.theworldshop.configs;

import cn.nukkit.utils.Config;

/**
 * @author BadFish
 */
public class TheWorldShopConfig {

    private double tax;

    private double moneyMin, moneyMax;

    private int playerSellMax;

    private TheWorldShopConfig(double tax, int playerSellMax, double moneyMin, double moneyMax){
        this.moneyMax = moneyMax;
        this.moneyMin = moneyMin;
        this.playerSellMax = playerSellMax;
        this.tax = tax;
    }

    public double getMoneyMax() {
        return moneyMax;
    }

    public double getMoneyMin() {
        return moneyMin;
    }

    public static TheWorldShopConfig load(Config config){
        return new TheWorldShopConfig(config.getDouble("tax",0.2)
                ,config.getInt("player-sell-max",10),config.getDouble("sell-money-min",0.1),config.getDouble("sell-money-max",10000));
    }

    public double getTax() {
        return tax;
    }

    public int getPlayerSellMax() {
        return playerSellMax;
    }
}
