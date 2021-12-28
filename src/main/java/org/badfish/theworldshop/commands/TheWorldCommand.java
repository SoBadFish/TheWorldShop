package org.badfish.theworldshop.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.commands.base.BaseCommand;
import org.badfish.theworldshop.commands.subcommand.*;
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
        this.addSubCommand(new ReloadSubCommand("reload"));
        this.addSubCommand(new SaveSubCommand("save"));
        this.loadCommandBase();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {

                DisplayPanel displayPanel = new DisplayPanel();
                displayPanel.displayPlayer((Player) sender);
                return true;
            }else{
                sender.sendMessage(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.sendCommandinConsole));
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
        sender.sendMessage("/tw add "+ TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.commandHelpAdd));
        sender.sendMessage("/tw sell "+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.commandHelpSell));
        if(sender.isOp()){
            sender.sendMessage("/tw set <money> <type:EconomyAPI|econ;PlayerPoints|point;Money|money> [name] "+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.commandHelpSet));
            sender.sendMessage("/tw reload "+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.commandReload));
            sender.sendMessage("/tw save "+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.commandSave));
        }
        sender.sendMessage("/tw "+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.commandMain));
        sender.sendMessage("---------------------------");
    }
}
