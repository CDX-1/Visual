package rip.cdx.virtual.ui.events;

import net.minestom.server.event.inventory.InventoryCloseEvent;

public interface CloseHandler {
    void onClose(VirtualEvent<InventoryCloseEvent> virtualEvent);
}
