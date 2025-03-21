package rip.cdx.virtual.components;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.PreClickHandler;
import rip.cdx.virtual.ui.events.VirtualEvent;
import rip.cdx.virtual.ui.state.State;
import rip.cdx.virtual.ui.virtual.Renderer;

public class Item extends UIComponent implements PreClickHandler {
    private final State<ItemStack> itemState = new State<>();
    private final State<Boolean> allowWithdrawState = new State<>();

    public Item(ItemStack item, boolean allowWithdraw) {
        itemState.set(item);
        this.allowWithdrawState.set(allowWithdraw);
    }

    public Item(Material material, boolean allowWithdraw) {
        this(ItemStack.builder(material).build(), allowWithdraw);
    }

    public Item(ItemStack item) {
        this(item, true);
    }

    public Item(Material material) {
        this(ItemStack.builder(material).build(), true);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setItem(renderer.nextSlot(), subRenderer -> subRenderer.getState(itemState));
    }

    @Override
    public void onPreClick(VirtualEvent<InventoryPreClickEvent> virtualEvent) {
        boolean allowWithdraw = virtualEvent.getVirtual().getState(allowWithdrawState);
        if (!allowWithdraw) virtualEvent.getEvent().setCancelled(true);
    }
}
