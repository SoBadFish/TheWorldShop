package org.badfish.theworldshop.language;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;


import java.lang.reflect.Field;

/**
 * @author BadFish
 */

public abstract class BaseLanguage {

    public String loadInfo = "正在加载市场";
    public String loadInfo1 = "全球市场加载完成 by 某吃瓜咸鱼";
    public String loadInfo2 = "本插件完全免费 如果你是购买的，那你就被坑了";
    public String loadInfo3 = "当前插件运行在: Nukkit PM1E 核心上";
    public String loadInfo4 = "当前插件运行在: Nukkit 核心上";
    public String commandDescription = "全球市场";
    //指令
    public String addCommandPanelTitle = "上架物品";
    public String sellCommandPanelTitle = "物品回收";

    public String sendCommandinConsole = "请不要在控制台执行此指令";

    public String reloadCommandMessage = "配置文件已重载";
    public String saveCommandMessage = "配置文件已保存";

    public String setItemCommandLackMoney = "请输入大于0的金钱";
    public String setItemCommandHandAir = "请不要手持空气";
    public String setItemCommandSetName = "nbt物品必须定义名称";
    public String setItemCommandAddSuccess = "添加成功";
    public String setItemCommandMoneyError = "请输入正确的金钱";

    public String commandHelpAdd = "上架物品";
    public String commandHelpSell = "背包物品出售";
    public String commandHelpSet = "设置手持物品的回收金钱";
    public String commandReload = "重载配置文件";
    public String commandSave = "保存数据";
    public String commandMain = "显示市场";
    //item

    public String moneyItemLore1 = "回收价格";
    public String moneyItemLore2 = "双击出售";

    public String itemHasChose = "已选择 √";

    public String shopItemLore1 = "出售者 | &a[1]";

    public String shopItemLore2 = "官方出售";

    public String shopItemLore3 = "价格 |  &a[1] ";

    public String shopItemLore3NoTax = "价格 | &a[1] [2]";


    public String shopItemLore5 = "限购 | &a[1]";

    public String shopItemLore6 = "双击购买";

    public String panelStringExceptSell = "预计出售";

    public String playerPanelTitleUp = "请选择上架物品";

    public String itemPanelChoseUp = "请选择筛选物品";

    public String itemPanelBack = "返回";

    public String itemPanelNext = "下一页";

    public String itemPanelEndPage = "尾页";

    public String itemPanelChosePlayer = "请选择玩家";

    public String itemPanelChoseMoney = "请选择货币类型";

    public String itemPanelLast = "上一页";

    public String itemPanelFirstPage = "首页";

    public String itemPanelSettingByMoney = "根据价格排序";

    public String itemPanelSettingByPlayer = "根据玩家筛选";


    public String itemPanelSettingByItemCount = "根据物品数量排序";

    public String itemPanelSettingByServerShop = "筛选系统商店";

    public String itemPanelSettingByPlayerShop = "筛选玩家商店";

    public String itemPanelSettingByHiddenItem = "隐藏限购物品";

    public String itemPanelSettingByOnlyDisplayItem = "只显示限购物品";


    public String itemPanelSettingByItem = "根据物品筛选";

    public String itemPanelSettingByMoneyType = "根据货币类型筛选";


    public String itemPanelPlaceSetting = "请设置筛选条件";

    public String itemPanelDataInfo = "数据信息";

    //显示变量信息

    public String showItemCountInfo = "当前拥有 &e[1] &a件物品";

    public String showItemCountMax = "最大物品上限 &e[1] &a件\n";

    public String showItemTax = "当前税收 &e[1] ％\n";


    public String showPublicItemCountInfo = "共计 &e[1] &a件物品\n";

    public String showPublicPlayerSize = "共计 &e[1] &a位店家\n";


    public String tipInfoClickTwo = "点击两次购买";

    public String tipSeeMySell = "查看我的出售";

    public String tipRefresh = "刷新";

    public String tipInfoChose = "筛选";

    //event info

    public String sellSuccess = "&a出售物品成功 价格&e[1]";

    public String sellMaxError = "你无法上架更多的物品";

    public String sellMoneyNotInArray = "请不要超过 [1] 或 小于 [2] ";

    public String sellItemError = "您背包内物品不足";

    public String sellMoneyInputError = "请重新选择物品并输入正确的价格";

    //window panel

    public String sellItemWindowsTitle = "请输入购买价格";

    public String sellItemWindowsInputInfo = "请输入你要出售的金钱数";

    public String sellItemWindowsDropDownInfo = "请选择支付的货币类型";

    public String sellItemWindowsToggleInfo = "是否为系统商店(不消耗数量)";

    public String sellItemWindowsLimit = "商品限购数量(-1为不限制)";

    //sell info

    public String sellItemClickAgain = "再点一次出售物品";

    public String sellItemClickRemoveAgain = "再点一次撤销物品";

    public String sellItemClickBuyAgain = "再点击一次购买";


    public String sellItemSuccess = "&a出售成功 获得 &e[1] [2]";

    public String buyItemSuccess = "&a购买成功! &e花费 &f[1] [2]";

    public String playerInventoryError = "您的背包没有空间";

    public String playerMoneyError = "&c您的 [1] 不足";

    public String playerLimitError = "&c此物品只允许购买 [1] 次";




    public BaseLanguage(){}

    public void languageLoadByConfig(Config config){
        for(Field field: this.getClass().getFields()){
            if(config.exists(field.getName())) {
                try {
                    field.set(this,config.getString(field.getName()));
                } catch (IllegalAccessException ignore) {
                }
            }
        }
    }

    public String getLang(String input,Object... transferVariables){
        TransferVariable[] transferVariables1 = null;
        if(transferVariables != null) {
            transferVariables1 = new TransferVariable[transferVariables.length];
            int index = 0;
            for (Object var : transferVariables) {
                transferVariables1[index] = new TransferVariable(index + 1, var);
                index++;
            }
        }
        return getLang(input,transferVariables1);
    }

    public String getLang(String input,TransferVariable... transferVariables){
        if(transferVariables != null) {
            for (TransferVariable var : transferVariables) {
                input = input.replace("["+var.getVar()+"]", var.getValue());
            }
        }
        return TextFormat.colorize('&',input);
    }

}
