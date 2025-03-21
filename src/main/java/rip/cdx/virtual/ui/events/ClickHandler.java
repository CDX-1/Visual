package rip.cdx.virtual.ui.events;

import net.minestom.server.event.inventory.InventoryClickEvent;

public interface ClickHandler {
    void onClick(VirtualEvent<InventoryClickEvent> virtualEvent);
    interface Global {}
}
