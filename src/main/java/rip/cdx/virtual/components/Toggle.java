package rip.cdx.virtual.components;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.PreClickHandler;
import rip.cdx.virtual.ui.events.VirtualEvent;
import rip.cdx.virtual.ui.state.State;
import rip.cdx.virtual.ui.virtual.Renderer;

public class Toggle extends UIComponent implements PreClickHandler {
    private final State<ItemStack> onState = new State<>();
    private final State<ItemStack> offState = new State<>();
    private final State<Boolean> toggleState = new State<>();

    public Toggle(ItemStack onItem, ItemStack offItem, boolean initialToggle) {
        this.onState.set(onItem);
        this.offState.set(offItem);
        this.toggleState.set(initialToggle);
    }

    public Toggle(ItemStack onItem, ItemStack offItem) {
        this(onItem, offItem, false);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setItem(renderer.nextSlot(), subRenderer -> {
            ItemStack onItem = subRenderer.getState(onState);
            ItemStack offItem = subRenderer.getState(offState);
            boolean toggled = subRenderer.getState(toggleState);

            if (toggled)
                return onItem;
            else
                return offItem;
        });
    }

    @Override
    public void onPreClick(VirtualEvent<InventoryPreClickEvent> virtualEvent) {
        virtualEvent.getEvent().setCancelled(true);
        boolean toggled = virtualEvent.getVirtual().getState(toggleState);
        virtualEvent.getVirtual().setState(toggleState, !toggled);
    }
}
