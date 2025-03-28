package rip.cdx.virtual.components;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.ComponentInitializeEvent;
import rip.cdx.virtual.ui.events.ComponentUpdateEvent;
import rip.cdx.virtual.ui.rendering.Renderer;
import rip.cdx.virtual.ui.state.State;

import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Filler extends UIComponent {
    private final IntStream range;
    private final State<ItemStack> itemState = new State<>();
    private final State<Consumer<InventoryPreClickEvent>> actionState = new State<>();

    public Filler(IntStream stream, ItemStack item, @Nullable Consumer<InventoryPreClickEvent> action) {
        range = stream;
        itemState.setDefaultValue(item);
        if (action != null) {
            actionState.setDefaultValue(action);
        }
    }

    public Filler(IntStream stream, ItemStack item) {
        this(stream, item, null);
    }

    @Override
    public void onInitialize(ComponentInitializeEvent event) {
        range.forEach(event::reserve);
    }

    @Override
    public void onUpdate(ComponentUpdateEvent event) {
        ItemStack item = itemState.get(event);
        for (Integer reservedSlot : event.getReservedSlots()) {
            event.setItem(reservedSlot, item);
        }
    }

    @Override
    public void onPreClick(@Nullable UIComponent component, Renderer renderer, InventoryPreClickEvent event) {
        if (component != this) return;
        event.setCancelled(true);
        @Nullable Consumer<InventoryPreClickEvent> action = actionState.get(renderer);
        if (action != null) {
            action.accept(event);
        }
    }
}
