package rip.cdx.virtual.components;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.PreClickHandler;
import rip.cdx.virtual.ui.events.VirtualEvent;
import rip.cdx.virtual.ui.state.State;
import rip.cdx.virtual.ui.virtual.Renderer;

import java.util.function.Consumer;

public class Button extends UIComponent implements PreClickHandler {
    private final State<ItemStack> itemState = new State<>();
    private final State<Consumer<VirtualEvent<InventoryPreClickEvent>>> eventHandlerState = new State<>();

    public Button(ItemStack item, Consumer<VirtualEvent<InventoryPreClickEvent>> handler) {
        itemState.set(item);
        eventHandlerState.set(handler);
    }

    public Button(Material material, Consumer<VirtualEvent<InventoryPreClickEvent>> handler) {
        this(ItemStack.builder(material).build(), handler);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setItem(renderer.nextSlot(), subRenderer -> subRenderer.getState(itemState));
    }

    @Override
    public void onPreClick(VirtualEvent<InventoryPreClickEvent> virtualEvent) {
        Consumer<VirtualEvent<InventoryPreClickEvent>> handler = virtualEvent.getVirtual().getState(eventHandlerState);
        virtualEvent.getEvent().setCancelled(true);
        handler.accept(virtualEvent);
    }
}
