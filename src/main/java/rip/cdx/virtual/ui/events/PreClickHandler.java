package rip.cdx.virtual.ui.events;

import net.minestom.server.event.inventory.InventoryPreClickEvent;

public interface PreClickHandler {
    void onPreClick(VirtualEvent<InventoryPreClickEvent> virtualEvent);
    interface Global {}
}
