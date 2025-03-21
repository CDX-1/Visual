package rip.cdx.virtual.ui.events;

import net.minestom.server.event.inventory.InventoryItemChangeEvent;

public interface ItemChangeHandler {
    void onItemChange(VirtualEvent<InventoryItemChangeEvent> virtualEvent);
    interface Global {}
}
