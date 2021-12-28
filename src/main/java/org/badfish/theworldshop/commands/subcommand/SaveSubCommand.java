package org.badfish.theworldshop.commands.subcommand;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.commands.base.BaseSubCommand;

/**
 * @author BadFish
 */
public class SaveSubCommand extends BaseSubCommand {
    public SaveSubCommand(String name) {
        super(name);
    }

    @Override
    protected boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        TheWorldShopMainClass.MAIN_INSTANCE.save();
        sender.sendMessage(TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.saveCommandMessage));

        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
