package org.badfish.theworldshop.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.commands.base.BaseSubCommand;
import org.badfish.theworldshop.manager.PlayerInfoManager;
import org.badfish.theworldshop.panel.DisplayPanel;

/**
 * @author BadFish
 */
public class AddSubCommand extends BaseSubCommand {
    public AddSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            DisplayPanel panel = new DisplayPanel();
            panel.displayPlayer((Player) sender,DisplayPanel.playerInventoryPanel(PlayerInfoManager.getInstance(sender.getName())),
                    TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.addCommandPanelTitle));
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
