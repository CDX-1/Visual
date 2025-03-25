package rip.cdx.virtual.ui.component;

import net.minestom.server.event.inventory.*;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.ui.events.component.ComponentReservationEvent;
import rip.cdx.virtual.ui.events.component.ComponentUpdateEvent;
import rip.cdx.virtual.ui.rendering.Renderer;

public abstract class UIComponent {
    @Nullable
    protected final Integer slot;

    public UIComponent(int slot) {
        this.slot = slot;
    }
    public UIComponent() {
        this.slot = null;
    }

    public void onReservation(ComponentReservationEvent event) {}
    public void onUpdate(ComponentUpdateEvent event) {}

    public void onClick(@Nullable UIComponent component, Renderer renderer, InventoryClickEvent event) {}
    public void onPreClick(@Nullable UIComponent component, Renderer renderer, InventoryPreClickEvent event) {}
    public void onItemChange(@Nullable UIComponent component, Renderer renderer, InventoryItemChangeEvent event) {}
    public void onOpen(Renderer renderer, InventoryOpenEvent event) {}
    public void onClose(Renderer renderer, InventoryCloseEvent event) {}
}
