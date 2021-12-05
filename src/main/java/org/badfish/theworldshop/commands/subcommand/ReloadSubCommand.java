package org.badfish.theworldshop.commands.subcommand;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.commands.base.BaseSubCommand;

/**
 * @author BadFish
 */
public class ReloadSubCommand extends BaseSubCommand {
    public ReloadSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender.isOp()){
            TheWorldShopMainClass.MAIN_INSTANCE.loadConfig();
            sender.sendMessage("配置文件已重载");
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
