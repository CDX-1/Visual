package rip.cdx.virtual.components;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.ComponentReservationEvent;
import rip.cdx.virtual.ui.events.ComponentUpdateEvent;
import rip.cdx.virtual.ui.rendering.Renderer;
import rip.cdx.virtual.ui.state.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Pagination extends UIComponent {
    private final int pageSize;
    private final State<List<ItemStack>> itemsState = new State<>(new ArrayList<>());
    private final State<Integer> pageState = new State<>(0);
    private final State<Consumer<InventoryPreClickEvent>> clickHandler = new State<>();

    public Pagination(int slot, int pageSize, List<ItemStack> items) {
        super(slot);
        this.pageSize = pageSize;
        this.itemsState.setDefaultValue(items);
    }

    public Pagination(int pageSize, List<ItemStack> items) {
        this.pageSize = pageSize;
        this.itemsState.setDefaultValue(items);
    }

    @Override
    public void onReservation(ComponentReservationEvent event) {
        int offset = event.nextSlotIfNull(slot);
        for (int i = 0; i < pageSize; i++) {
            event.reserve(offset + i);
        }
    }

    @Override
    public void onUpdate(ComponentUpdateEvent event) {
        int page = pageState.get(event);
        List<ItemStack> items = itemsState.get(event);
        int offset = page * pageSize;
        Iterator<Integer> reservedSlotIterator = event.getReservedSlots().iterator();
        for (int i = 0; i < pageSize; i++) {
            if (reservedSlotIterator.hasNext()) {
                ItemStack item;
                if (items.size() > offset + i) {
                    item = items.get(offset + i);
                } else {
                    item = ItemStack.AIR;
                }

                event.setItem(reservedSlotIterator.next(), item);
            }
        }
    }

    public int getMaxPages(Renderer renderer) {
        return (int) Math.ceil((double) itemsState.get(renderer).size() / pageSize) - 1;
    }

    public void previousPage(Renderer renderer) {
        pageState.modify(renderer, page -> (page == 0) ? getMaxPages(renderer) : page - 1);
    }

    public void nextPage(Renderer renderer) {
        pageState.modify(renderer, page -> (page + 1 > getMaxPages(renderer)) ? 0 : page + 1);
    }

    public Pagination onClick(Consumer<InventoryPreClickEvent> handler) {
        clickHandler.setDefaultValue(handler);
        return this;
    }

    @Override
    public void onPreClick(@Nullable UIComponent component, Renderer renderer, InventoryPreClickEvent event) {
        if (component != this) return;
        Consumer<InventoryPreClickEvent> handler = clickHandler.get(renderer);
        if (handler != null) {
            handler.accept(event);
        }
    }

    private static abstract class NavButton extends UIComponent {
        private final State<ItemStack> itemState = new State<>();
        private Pagination pagination;

        public NavButton(int slot, ItemStack item) {
            super(slot);
            itemState.setDefaultValue(item);
        }

        public NavButton(ItemStack item) {
            itemState.setDefaultValue(item);
        }

        @Override
        public void onReservation(ComponentReservationEvent event) {
            event.reserve(event.nextSlotIfNull(slot));
        }

        @Override
        public void onUpdate(ComponentUpdateEvent event) {
            event.setItem(
                    event.getReservedSlots().getFirst(),
                    itemState.get(event)
            );
        }

        @Override
        public void onPreClick(@Nullable UIComponent component, Renderer renderer, InventoryPreClickEvent event) {
            if (component != this) return;
            event.setCancelled(true);

            if (pagination == null) {
                List<UIComponent> components = renderer.getComponentsByClass(Pagination.class);
                assert !components.isEmpty() : "No pagination component could be found";
                pagination = (Pagination) components.getFirst();
            }

            click(renderer, pagination);
        }

        public abstract void click(Renderer renderer, Pagination pagination);
    }

    public static class PreviousButton extends NavButton {
        public PreviousButton(int slot, ItemStack item) {
            super(slot, item);
        }

        public PreviousButton(ItemStack item) {
            super(item);
        }

        @Override
        public void click(Renderer renderer, Pagination pagination) {
            pagination.previousPage(renderer);
        }
    }

    public static class NextButton extends NavButton {
        public NextButton(int slot, ItemStack item) {
            super(slot, item);
        }

        public NextButton(ItemStack item) {
            super(item);
        }

        @Override
        public void click(Renderer renderer, Pagination pagination) {
            pagination.nextPage(renderer);
        }
    }
}
