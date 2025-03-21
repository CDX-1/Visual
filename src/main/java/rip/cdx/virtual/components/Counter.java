package rip.cdx.virtual.components;

import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.PreClickHandler;
import rip.cdx.virtual.ui.events.VirtualEvent;
import rip.cdx.virtual.ui.state.State;
import rip.cdx.virtual.ui.virtual.Renderer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class Counter extends UIComponent implements PreClickHandler {
    private final State<Material> materialState = new State<>(Material.OAK_BUTTON);
    private final State<Integer> counterState = new State<>(0);

    public Counter() {}

    public Counter(Material material) {
        materialState.set(material);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setItem(renderer.nextSlot(), subRenderer -> {
            int counter = subRenderer.getState(counterState);
            return ItemStack.builder(materialState.get())
                    .customName(Component.text("Counter: " + counter))
                    .build();
        });
    }

    @Override
    public void onPreClick(VirtualEvent<InventoryPreClickEvent> virtualEvent) {
        int counter = virtualEvent.getVirtual().getState(counterState);
        virtualEvent.getVirtual().setState(counterState, counter + 1);
        virtualEvent.getEvent().setCancelled(true);
    }
}
