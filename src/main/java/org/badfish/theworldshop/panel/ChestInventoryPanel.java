package org.badfish.theworldshop.panel;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import org.badfish.theworldshop.ListenerEvent;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.manager.PlayerSellItemManager;
import org.badfish.theworldshop.panel.lib.DoubleChestFakeInventory;

/**
 * @author BadFish
 */
public class ChestInventoryPanel extends DoubleChestFakeInventory implements InventoryHolder{

    long id;

    ChestInventoryPanel(InventoryHolder holder, String name) {
        super(holder);
        this.setName(name);
    }



    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        super.onSlotChange(index, before, send);
    }


    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = who.getWindowId(this);
        pk.entityId = id;
        pk.type = InventoryType.DOUBLE_CHEST.getNetworkType();
        who.dataPacket(pk);
    }

    @Override
    public void onClose(Player who) {
        clearAll();
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = id;
        who.dataPacket(pk);
        super.onClose(who);
        TheWorldShopMainClass.CLICK_PANEL.remove(who);
        ListenerEvent.BUY_LOCK.remove(who);
        ListenerEvent.LOOK_PLAYERS.remove(who);
        ListenerEvent.LOOK_ITEMS.remove(who);
        TheWorldShopMainClass.PLAYER_SELL.remove(PlayerSellItemManager.getInstance(who.getName()));

    }

    @Override
    public Inventory getInventory() {
        return this;
    }



}
