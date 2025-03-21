package rip.cdx.virtual.ui.events;

import net.minestom.server.event.inventory.InventoryOpenEvent;

public interface OpenHandler {
    void onOpen(VirtualEvent<InventoryOpenEvent> virtualEvent);
}
