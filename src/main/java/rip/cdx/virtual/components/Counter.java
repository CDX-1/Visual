package rip.cdx.virtual.components;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.ComponentInitializeEvent;
import rip.cdx.virtual.ui.events.ComponentUpdateEvent;
import rip.cdx.virtual.ui.rendering.Renderer;
import rip.cdx.virtual.ui.state.State;

public class Counter extends UIComponent {
    private final State<Integer> counter = new State<>(0);

    public Counter(int slot) {
        super(slot);
    }

    public Counter() {}

    @Override
    public void onInitialize(ComponentInitializeEvent event) {
        event.reserve(event.nextSlotIfNull(slot));
    }

    @Override
    public void onUpdate(ComponentUpdateEvent event) {
        int count = counter.get(event);
        event.setItem(
                event.getReservedSlots().getFirst(),
                ItemStack.builder(Material.CLOCK)
                        .customName(Component.text("Counter: " + count))
                        .build()
        );
    }

    @Override
    public void onPreClick(@Nullable UIComponent component, Renderer renderer, InventoryPreClickEvent event) {
        if (component != this) return;
        event.setCancelled(true);
        counter.modify(renderer, count -> count + 1);
    }
}
