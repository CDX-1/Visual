package rip.cdx.virtual.components;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.CloseHandler;
import rip.cdx.virtual.ui.events.VirtualEvent;
import rip.cdx.virtual.ui.state.State;
import rip.cdx.virtual.ui.virtual.Renderer;

import java.util.function.Function;

public class KeepOpen extends UIComponent implements CloseHandler {
    private final State<Function<InventoryCloseEvent, Boolean>> onCloseState = new State<>();

    public KeepOpen(Function<InventoryCloseEvent, Boolean> onClose) {
        onCloseState.set(onClose);
    }

    public KeepOpen() {
        this((_unused) -> true);
    }

    @Override
    public void render(Renderer renderer) { /* NO RENDERING */ }

    @Override
    public void onClose(VirtualEvent<InventoryCloseEvent> virtualEvent) {
        boolean keepOpen = onCloseState.get().apply(virtualEvent.getEvent());
        if (keepOpen)
            virtualEvent.getEvent().setNewInventory(virtualEvent.getVirtual().getInventory());
    }
}
