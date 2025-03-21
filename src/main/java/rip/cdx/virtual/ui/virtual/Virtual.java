package rip.cdx.virtual.ui.virtual;

import net.minestom.server.event.inventory.*;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import rip.cdx.virtual.exceptions.UIOverflowException;
import rip.cdx.virtual.ui.UI;
import rip.cdx.virtual.ui.UIEventListener;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.*;
import rip.cdx.virtual.ui.state.State;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class Virtual {
    private final Inventory inventory;
    private final UI ui;
    private boolean hasRendered = false;
    private final Map<UUID, Object> states = new HashMap<>();
    private final Map<UUID, List<Consumer<Object>>> stateListeners = new HashMap<>();
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Class<? extends InventoryEvent>, List<Consumer<InventoryEvent>>> eventHandlers = new HashMap<>();

    public Virtual(Inventory inventory, UI ui) {
        this.inventory = inventory;
        this.ui = ui;

        final UIEventListener listener = getEventListener(ui);

        listener.onClose(event -> eventHandlers.computeIfAbsent(InventoryCloseEvent.class, (key) -> new ArrayList<>())
                .forEach(handler -> handler.accept(event)));

        listener.onItemChange(event -> eventHandlers.computeIfAbsent(InventoryItemChangeEvent.class, (key) -> new ArrayList<>())
                .forEach(handler -> handler.accept(event)));
    }

    private @NotNull UIEventListener getEventListener(UI ui) {
        UIEventListener listener = ui.getListener();

        listener.onClick(event -> eventHandlers.computeIfAbsent(InventoryClickEvent.class, (key) -> new ArrayList<>())
                .forEach(handler -> handler.accept(event)));

        listener.onPreClick(event -> eventHandlers.computeIfAbsent(InventoryPreClickEvent.class, (key) -> new ArrayList<>())
                .forEach(handler -> handler.accept(event)));

        listener.onOpen(event -> eventHandlers.computeIfAbsent(InventoryOpenEvent.class, (key) -> new ArrayList<>())
                .forEach(handler -> handler.accept(event)));

        return listener;
    }

    public boolean hasRendered() {
        return hasRendered;
    }

    public void render() {
        if (hasRendered) return;
        hasRendered = true;
        for (UIComponent component : ui.getComponents()) {
            Renderer renderer = new Renderer(this, component);
            component.render(renderer);
            loadComponentHandlers(component);
        }
        update();
    }

    public void update() {
        for (int i = 0; i < inventory.getSize() - 1; i++) {
            ItemStack virtualItem = items.get(i);
            ItemStack currentItem = inventory.getItemStack(i);

            if (virtualItem == null) {
                inventory.setItemStack(i, ItemStack.AIR);
            } else if (!currentItem.equals(virtualItem)) {
                inventory.setItemStack(i, virtualItem);
            }
        }
    }

    public void setItem(int slot, ItemStack itemStack) {
        items.put(slot, itemStack);
    }

    public <T> void setState(State<T> state, T value) {
        states.put(state.getUuid(), value);
        for (Consumer<Object> consumer : stateListeners.get(state.getUuid())) {
            consumer.accept(value);
        }
        update();
    }

    public <T> T getState(State<T> state) {
        return (T) states.getOrDefault(state.getUuid(), state.get());
    }

    public <T> void onStateChange(State<T> state, Consumer<T> consumer) {
        stateListeners.computeIfAbsent(state.getUuid(), (key) -> new ArrayList<>()).add((Consumer<Object>) consumer);
    }

    public Integer nextSlot() {
        for (int i = 0; i < inventory.getSize() - 1; i++) {
            if (!items.containsKey(i)) return i;
        }
        throw new UIOverflowException();
    }

    private <T> VirtualEvent<T> wrapEvent(T event) {
        return new VirtualEvent<>(event, this);
    }

    private void loadComponentHandlers(UIComponent component) {
        if (component instanceof ClickHandler) {
            List<Consumer<InventoryEvent>> handlers = eventHandlers.computeIfAbsent(InventoryClickEvent.class, (key) -> new ArrayList<>());
            if (component instanceof ClickHandler.Global) {
                handlers.add((event) -> ((ClickHandler) component).onClick(wrapEvent((InventoryClickEvent) event)));
            } else {
                handlers.add((event) -> {
                    if (!checkItemComponentIdentifier(((InventoryClickEvent) event).getClickedItem(), component.getUuid()))
                        return;
                    ((ClickHandler) component).onClick(wrapEvent((InventoryClickEvent) event));
                });
            }
        }

        if (component instanceof PreClickHandler) {
            List<Consumer<InventoryEvent>> handlers = eventHandlers.computeIfAbsent(InventoryPreClickEvent.class, (key) -> new ArrayList<>());
            if (component instanceof PreClickHandler.Global) {
                handlers.add((event) -> ((PreClickHandler) component).onPreClick(wrapEvent((InventoryPreClickEvent) event)));
            } else {
                handlers.add((event) -> {
                    if (!checkItemComponentIdentifier(((InventoryPreClickEvent) event).getClickedItem(), component.getUuid()))
                        return;
                    ((PreClickHandler) component).onPreClick(wrapEvent((InventoryPreClickEvent) event));
                });
            }
        }

        if (component instanceof OpenHandler) {
            List<Consumer<InventoryEvent>> handlers = eventHandlers.computeIfAbsent(InventoryOpenEvent.class, (key) -> new ArrayList<>());
            handlers.add((event) -> ((OpenHandler) component).onOpen((VirtualEvent<InventoryOpenEvent>) event));
        }

        if (component instanceof CloseHandler) {
            List<Consumer<InventoryEvent>> handlers = eventHandlers.computeIfAbsent(InventoryCloseEvent.class, (key) -> new ArrayList<>());
            handlers.add((event) -> ((CloseHandler) component).onClose((VirtualEvent<InventoryCloseEvent>) event));
        }

        if (component instanceof ItemChangeHandler) {
            List<Consumer<InventoryEvent>> handlers = eventHandlers.computeIfAbsent(InventoryItemChangeEvent.class, (key) -> new ArrayList<>());
            if (component instanceof ItemChangeHandler.Global) {
                handlers.add((event) -> ((ItemChangeHandler) component).onItemChange(wrapEvent((InventoryItemChangeEvent) event)));
            } else {
                handlers.add((event) -> {
                    if (!checkItemComponentIdentifier(((InventoryItemChangeEvent) event).getPreviousItem(), component.getUuid()))
                        return;
                    ((ItemChangeHandler) component).onItemChange(wrapEvent((InventoryItemChangeEvent) event));
                });
            }
        }
    }

    private boolean checkItemComponentIdentifier(ItemStack item, UUID id) {
        if (!item.hasTag(Tag.UUID("component-id"))) return false;
        UUID itemId = item.getTag(Tag.UUID("component-id"));
        return itemId.equals(id);
    }
}
