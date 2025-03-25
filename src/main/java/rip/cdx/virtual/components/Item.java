package rip.cdx.virtual.components;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.ComponentInitializeEvent;
import rip.cdx.virtual.ui.events.ComponentUpdateEvent;
import rip.cdx.virtual.ui.rendering.Renderer;
import rip.cdx.virtual.ui.state.State;

public class Item extends UIComponent {
    private final State<ItemStack> item = new State<>();

    public Item(int slot, ItemStack item) {
        super(slot);
        this.item.setDefaultValue(item);
    }

    public Item(ItemStack item) {
        super();
        this.item.setDefaultValue(item);
    }

    @Override
    public void onInitialize(ComponentInitializeEvent event) {
        event.reserve(event.nextSlotIfNull(slot));
    }

    @Override
    public void onUpdate(ComponentUpdateEvent event) {
        event.setItem(
                event.getReservedSlots().getFirst(),
                item.get(event)
        );
    }

    @Override
    public void onPreClick(@Nullable UIComponent component, Renderer renderer, InventoryPreClickEvent event) {
        if (component != this) return;
        event.setCancelled(true);
    }
}
