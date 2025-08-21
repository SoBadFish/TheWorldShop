package org.badfish.theworldshop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.commands.TheWorldCommand;
import org.badfish.theworldshop.configs.ShopItemSQLData;
import org.badfish.theworldshop.configs.TheWorldShopConfig;
import org.badfish.theworldshop.db.SqliteHelper;
import org.badfish.theworldshop.language.BaseLanguage;
import org.badfish.theworldshop.language.LanguageManager;
import org.badfish.theworldshop.language.langs.ChineseLanguage;
import org.badfish.theworldshop.language.langs.EnglishLanguage;
import org.badfish.theworldshop.manager.*;
import org.badfish.theworldshop.panel.ChestInventoryPanel;
import org.badfish.theworldshop.panel.lib.AbstractFakeInventory;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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


    public static final String DB_TABLE = "theworldshop";

    public SqliteHelper sqliteHelper;

    @Override
    public void onEnable() {
        MAIN_INSTANCE = this;

        initLanguage();
        loadLanguage();

        this.getLogger().info(language.getLang(language.loadInfo));
        checkServer();
        //初始化数据库信息
        saveResource("data.db",false);
        try {
            sqliteHelper = new SqliteHelper(getDataFolder()+"/data.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        if(sqliteHelper != null){
            if(!sqliteHelper.exists(DB_TABLE)){
                sqliteHelper.addTable(DB_TABLE, SqliteHelper.DBTable.asDbTable(ShopItemSQLData.class));
            }
        }

        chunkDb();

        loadConfig();




        TITLE = TextFormat.colorize('&',getConfig().getString("title","交易行"));
        this.getLogger().info(language.getLang(language.loadInfo1));
        this.getLogger().info(language.getLang(language.loadInfo2));
        this.getServer().getCommandMap().register("theworldshop",new TheWorldCommand("tw",language.getLang(language.commandDescription)));
        this.getServer().getPluginManager().registerEvents(new ListenerEvent(),this);

    }

    private void chunkDb(){
        //检查DB
        if(sqliteHelper != null){
            List<String> columns = sqliteHelper.getColumns(DB_TABLE);
            Field[] fd = ShopItemSQLData.class.getFields();
            for (Field field : fd){
                if(!columns.contains(field.getName())){
                    //新增...
                    sqliteHelper.addColumns(DB_TABLE,field.getName().toLowerCase(),field);
                    getLogger().info("检测到新字段 "+field.getName()+" 正在写入数据库...");

                }
            }
        }

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
        if(WORLD_CONFIG == null) {
            WORLD_CONFIG = TheWorldShopConfig.load(getConfig());
            CUSTOM_ITEM = CustomItemManager.initCustomItem(new Config(getDataFolder() + "/takeItems.yml", Config.YAML));
            MONEY_ITEM = MoneyItemManager.initManager(new Config(getDataFolder() + "/sellmoney.yml", Config.YAML));
            PLAYER_DATA = PlayerDataManager.initManager(new Config(getDataFolder() + "/playerData.yml", Config.YAML));
            SELL_MANAGER = SellItemManager.loadManager(sqliteHelper);
        }else{
            WORLD_CONFIG.reload(getConfig());
            CUSTOM_ITEM.reload(new Config(getDataFolder() + "/takeItems.yml", Config.YAML));
            MONEY_ITEM.reload(new Config(getDataFolder() + "/sellmoney.yml", Config.YAML));
            PLAYER_DATA.reload(new Config(getDataFolder() + "/playerData.yml", Config.YAML));
        }
    }

    public void save(){
//        if(SELL_MANAGER != null) {
//            SELL_MANAGER.save();
//        }
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

    public static String CORE_NAME = "";
    private void checkServer(){
        boolean ver = false;
        //双核心兼容
        CORE_NAME = "Nukkit";
        try {
            Class<?> c = Class.forName("cn.nukkit.Nukkit");
            c.getField("NUKKIT_PM1E");
            ver = true;
            CORE_NAME = "Nukkit PM1E";


        } catch (ClassNotFoundException | NoSuchFieldException ignore) { }
        try {
            Class<?> c = Class.forName("cn.nukkit.Nukkit");
            CORE_NAME = c.getField("NUKKIT").get(c).toString();

            ver = true;

        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignore) {
        }


        AbstractFakeInventory.IS_PM1E = ver;
        if(ver){
            Server.getInstance().enableExperimentMode = true;
            Server.getInstance().forceResources = true;
        }
        sendMessageToConsole("&e当前核心为 "+CORE_NAME);
    }

    public static void sendMessageToConsole(String msg){
        sendMessageToObject(msg,null);
    }
    public static void sendMessageToObject(String msg, Object o){
        String message = TextFormat.colorize('&',TITLE+" &r"+msg);
        if(o != null){
            if(o instanceof Player){
                if(((Player) o).isOnline()) {
                    ((Player) o).sendMessage(message);
                    return;
                }
            }
            if(o instanceof EntityHuman){
                message = ((EntityHuman) o).getName()+"->"+message;
            }
        }
        MAIN_INSTANCE.getLogger().info(message);

    }



    @Override
    public void onDisable() {
        save();
    }
}
