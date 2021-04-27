package org.badfish.theworldshop.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

import org.badfish.theworldshop.commands.base.BaseCommand;
import org.badfish.theworldshop.commands.subcommand.AddSubCommand;
import org.badfish.theworldshop.commands.subcommand.SellItemSubCommand;
import org.badfish.theworldshop.commands.subcommand.SetItemMoneySubCommand;
import org.badfish.theworldshop.panel.DisplayPanel;

/**
 * @author BadFish
 */
public class TheWorldCommand extends BaseCommand {
    public TheWorldCommand(String name, String description) {
        super(name, description);
        this.setPermission("TheWorld.worldshop");
        this.addSubCommand(new AddSubCommand("add"));
        this.addSubCommand(new SellItemSubCommand("sell"));
        this.addSubCommand(new SetItemMoneySubCommand("set"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(sender instanceof Player) {
            if (args.length == 0) {
                DisplayPanel displayPanel = new DisplayPanel();
                displayPanel.displayPlayer((Player) sender);
                return true;
            }
        }
        return super.execute(sender, s, args);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage("---------------------------");
        sender.sendMessage("/tw add 将手持物品出售");
        sender.sendMessage("/tw sell 将手持物品出售");
        if(sender.isOp()){
            sender.sendMessage("/tw set <金钱> [名称:NBT物品必须设置] 设置手持物品的回收金钱");
        }
        sender.sendMessage("/tw 显示市场");
        sender.sendMessage("---------------------------");
    }
}
