package org.badfish.theworldshop;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.commands.TheWorldCommand;
import org.badfish.theworldshop.configs.TheWorldShopConfig;
import org.badfish.theworldshop.language.BaseLanguage;
import org.badfish.theworldshop.language.LanguageManager;
import org.badfish.theworldshop.language.langs.ChineseLanguage;
import org.badfish.theworldshop.language.langs.EnglishLanguage;
import org.badfish.theworldshop.manager.*;
import org.badfish.theworldshop.panel.ChestInventoryPanel;
import org.badfish.theworldshop.panel.lib.AbstractFakeInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author BadFish
 */
public class TheWorldShopMainClass extends PluginBase {

    public static String TITLE;

    public static String DEFAULT_LANGUAGE = "chs";

    public static BaseLanguage language;

    public static TheWorldShopMainClass MAIN_INSTANCE;

    public static SellItemManager SELL_MANAGER;

    public static TheWorldShopConfig WORLD_CONFIG;

    public static CustomItemManager CUSTOM_ITEM;

    public static PlayerDataManager PLAYER_DATA;

    public static MoneyItemManager MONEY_ITEM;

    public static ArrayList<PlayerSellItemManager> PLAYER_SELL = new ArrayList<>();

    public static LinkedHashMap<Player, ChestInventoryPanel> CLICK_PANEL= new LinkedHashMap<>();


    @Override
    public void onEnable() {
        MAIN_INSTANCE = this;

        initLanguage();
        loadLanguage();

        this.getLogger().info(language.getLang(language.loadInfo));
        checkServer();

        loadConfig();

        TITLE = TextFormat.colorize('&',getConfig().getString("title","交易行"));
        this.getLogger().info(language.getLang(language.loadInfo1));
        this.getLogger().info(language.getLang(language.loadInfo2));
        this.getServer().getCommandMap().register("theworldshop",new TheWorldCommand("tw",language.getLang(language.commandDescription)));
        this.getServer().getPluginManager().registerEvents(new ListenerEvent(),this);

    }

    public void loadLanguage(){
        File file = new File(this.getDataFolder()+"/config.yml");
        File file1 = new File(this.getDataFolder()+"/language.yml");
        if(!file.exists()){
            this.saveResource("lang/"+DEFAULT_LANGUAGE+"/config.yml","config.yml",false);
        }else{

            if(!getConfig().getString("lang","chs").equalsIgnoreCase(DEFAULT_LANGUAGE)){
                DEFAULT_LANGUAGE = getConfig().getString("lang","chs");
                this.saveResource("lang/"+DEFAULT_LANGUAGE+"/config.yml","config.yml",true);
                this.saveResource("lang/"+DEFAULT_LANGUAGE+"/language.yml","language.yml",true);
                reloadConfig();
            }
        }
        if(!file1.exists()){
            this.saveResource("lang/"+DEFAULT_LANGUAGE+"/language.yml","language.yml",false);
        }

        LanguageManager.getLanguage(DEFAULT_LANGUAGE).languageLoadByConfig(new Config(file1,Config.YAML));
        language = LanguageManager.getLanguage(DEFAULT_LANGUAGE);
    }

    private void initLanguage(){
        LanguageManager.register("chs",new ChineseLanguage());
        LanguageManager.register("eng",new EnglishLanguage());
    }

    public static BaseLanguage getLanguage() {
        return language;
    }

    public void loadConfig(){

        this.saveResource("items.yml",false);
        WORLD_CONFIG = TheWorldShopConfig.load(getConfig());
        SELL_MANAGER = SellItemManager.loadManager(new Config(getDataFolder()+"/items.yml",Config.YAML));
        CUSTOM_ITEM = CustomItemManager.initCustomItem(new Config(getDataFolder()+"/takeItems.yml",Config.YAML));
        MONEY_ITEM = MoneyItemManager.initManager(new Config(getDataFolder()+"/sellmoney.yml",Config.YAML));
        PLAYER_DATA = PlayerDataManager.initManager(new Config(getDataFolder()+"/playerData.yml",Config.YAML));
    }

    public void save(){
        if(SELL_MANAGER != null) {
            SELL_MANAGER.save();
        }
        if(SELL_MANAGER != null) {
            CUSTOM_ITEM.save();
        }
        if(SELL_MANAGER != null) {
            MONEY_ITEM.save();
        }
        if(PLAYER_DATA != null){
            PLAYER_DATA.save();
        }
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
            this.getLogger().info(language.getLang(language.loadInfo3));
        }else{
            this.getLogger().info(language.getLang(language.loadInfo4));
        }
    }

    @Override
    public void onDisable() {
        save();
    }
}
