package org.badfish.theworldshop.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.commands.base.BaseSubCommand;
import org.badfish.theworldshop.items.CustomItem;

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
                            sender.sendMessage("请输入大于0的金钱");
                            return true;
                        }
                        Item i = ((Player) sender).getInventory().getItemInHand().clone();
                        if(i.getId() == 0){
                            sender.sendMessage("请不要手持空气");
                            return true;
                        }
                        String name = null;
                        if(args.length > 2){
                            name = args[2];
                        }
                        CustomItem customItem = new CustomItem(i);
                        if(i.hasCompoundTag()){
                            if(name == null){
                                sender.sendMessage("nbt物品必须定义名称");
                                return true;
                            }
                            customItem = new CustomItem(name,i);
                            TheWorldShopMainClass.CUSTOM_ITEM.addCustomItem(customItem);
                            TheWorldShopMainClass.CUSTOM_ITEM.save();
                        }
                        TheWorldShopMainClass.MONEY_ITEM.addMoneyItem(customItem,m);
                        TheWorldShopMainClass.MONEY_ITEM.save();
                        sender.sendMessage("添加成功");
                    }catch (Exception e){
                        sender.sendMessage("请输入正确的金钱");
                        return false;
                    }
                }

            }
        }

        return false;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
