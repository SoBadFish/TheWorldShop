package org.badfish.theworldshop;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.commands.TheWorldCommand;
import org.badfish.theworldshop.configs.TheWorldShopConfig;
import org.badfish.theworldshop.manager.CustomItemManager;
import org.badfish.theworldshop.manager.MoneyItemManager;
import org.badfish.theworldshop.manager.PlayerSellItemManager;
import org.badfish.theworldshop.manager.SellItemManager;
import org.badfish.theworldshop.panel.ChestInventoryPanel;
import org.badfish.theworldshop.panel.lib.AbstractFakeInventory;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author BadFish
 */
public class TheWorldShopMainClass extends PluginBase {

    public static String TITLE;

    public static TheWorldShopMainClass MAIN_INSTANCE;

    public static SellItemManager SELL_MANAGER;

    public static TheWorldShopConfig WORLD_CONFIG;

    public static CustomItemManager CUSTOM_ITEM;

    public static MoneyItemManager MONEY_ITEM;

    public static ArrayList<PlayerSellItemManager> PLAYER_SELL = new ArrayList<>();

    public static LinkedHashMap<Player, ChestInventoryPanel> CLICK_PANEL= new LinkedHashMap<>();


    @Override
    public void onEnable() {
        MAIN_INSTANCE = this;
        this.getLogger().info("正在加载市场");
        checkServer();
        loadConfig();
        TITLE = TextFormat.colorize('&',getConfig().getString("title","交易行"));
        this.getLogger().info("全球市场加载完成 by 某吃瓜咸鱼");
        this.getLogger().info("本插件完全免费 如果你是购买的，那你就被坑了");
        this.getServer().getCommandMap().register("theworldshop",new TheWorldCommand("tw","全球市场"));
        this.getServer().getPluginManager().registerEvents(new ListenerEvent(),this);

    }

    public void loadConfig(){
        this.saveDefaultConfig();
        this.reloadConfig();
        this.saveResource("items.yml",false);
        WORLD_CONFIG = TheWorldShopConfig.load(getConfig());
        SELL_MANAGER = SellItemManager.loadManager(new Config(getDataFolder()+"/items.yml",Config.YAML));
        CUSTOM_ITEM = CustomItemManager.initCustomItem(new Config(getDataFolder()+"/takeItems.yml",Config.YAML));
        MONEY_ITEM = MoneyItemManager.initManager(new Config(getDataFolder()+"/sellmoney.yml",Config.YAML));
    }

    public void save(){
        SELL_MANAGER.save();
        CUSTOM_ITEM.save();
        MONEY_ITEM.save();
    }

    private void checkServer(){
        boolean ver = false;
        //双核心兼容
        try {
            Class c = Class.forName("cn.nukkit.Nukkit");
            c.getField("NUKKIT_PM1E");
            ver = true;

        } catch (ClassNotFoundException | NoSuchFieldException ignore) {

        }
        AbstractFakeInventory.IS_PM1E = ver;
        if(ver){
            this.getLogger().info("当前插件运行在: Nukkit PM1E 核心上");
        }else{
            this.getLogger().info("当前插件运行在: Nukkit 核心上");
        }
    }

    @Override
    public void onDisable() {
        save();
    }
}
