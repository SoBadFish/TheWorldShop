package org.badfish.theworldshop.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.commands.base.BaseSubCommand;
import org.badfish.theworldshop.items.CustomItem;
import org.badfish.theworldshop.items.MoneySellItem;

/**
 * @author BadFish
 */
public class SetItemMoneySubCommand extends BaseSubCommand {
    public SetItemMoneySubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player){
            if(sender.isOp()){
                if(args.length > 1){
                    double m;
                    try {
                        m = Double.parseDouble(args[1]);
                        if(m <= 0){
                            sender.sendMessage(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.setItemCommandLackMoney));
                            return true;
                        }
                        Item i = ((Player) sender).getInventory().getItemInHand().clone();
                        if(i.getId() == 0){
                            sender.sendMessage(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.setItemCommandHandAir));
                            return true;
                        }
                        String name = null;
                        String moneyType = "";
                        if(args.length > 2){
                            moneyType = args[2];
                        }
                        if(args[2].toLowerCase().equalsIgnoreCase(MoneySellItem.MoneyType.PlayerPoints.name().toLowerCase())
                        || "点券".equals(args[2]) || args[2].toLowerCase().contains("player") || args[2].toLowerCase().contains("point")){
                            moneyType = MoneySellItem.MoneyType.PlayerPoints.name();
                        }
                        if(args[2].toLowerCase().equalsIgnoreCase(MoneySellItem.MoneyType.EconomyAPI.name().toLowerCase())
                                || "金币".equals(args[2]) || args[2].toLowerCase().contains("econ")){
                            moneyType = MoneySellItem.MoneyType.EconomyAPI.name();
                        }
                        if(args[2].toLowerCase().equalsIgnoreCase(MoneySellItem.MoneyType.Money.name().toLowerCase())
                                || "金钱".equals(args[2]) || args[2].toLowerCase().contains("money")){
                            moneyType = MoneySellItem.MoneyType.Money.name();
                        }
                        MoneySellItem.MoneyType type = MoneySellItem.MoneyType.EconomyAPI;
                        try {
                            type = MoneySellItem.MoneyType.valueOf(moneyType);
                        }catch (Exception ignore){}
                        if(args.length > 3){
                            name = args[3];
                        }
                        CustomItem customItem = new CustomItem(i);
                        if(i.hasCompoundTag()){
                            if(name == null || "".equals(name.trim())){
                                sender.sendMessage(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.setItemCommandSetName));
                                return true;
                            }
                            customItem = new CustomItem(name,i);
                            TheWorldShopMainClass.CUSTOM_ITEM.addCustomItem(customItem);
                            TheWorldShopMainClass.CUSTOM_ITEM.save();
                        }
                        TheWorldShopMainClass.MONEY_ITEM.addMoneyItem(customItem,type,m);
                        TheWorldShopMainClass.MONEY_ITEM.save();
                        sender.sendMessage(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.setItemCommandAddSuccess));
                    }catch (Exception e){
                        sender.sendMessage(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.setItemCommandMoneyError));
                        return false;
                    }
                }

            }
        }

        return false;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("money", CommandParamType.INT,false),
                new CommandParameter("moneyType",new String[]{"economyapi","playerpoints","money","金币","点券","金钱"}),
                new CommandParameter("name",CommandParamType.TEXT,true)
        };
    }
}
