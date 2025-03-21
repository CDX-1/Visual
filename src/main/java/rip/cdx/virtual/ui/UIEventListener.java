package rip.cdx.virtual.ui;

import lombok.AccessLevel;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.*;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class UIEventListener {
    @Getter(value = AccessLevel.PROTECTED)
    private final Map<Class<? super InventoryEvent>, List<Consumer<? super InventoryEvent>>> listeners = new HashMap<>();
    private final EventNode<InventoryEvent> node;

    private UIEventListener(Inventory inventory) {
        node = EventNode.type(
                UUID.randomUUID().toString(),
                EventFilter.INVENTORY,
                (event, inv) -> inventory == inv
        );
        for (Class<? extends InventoryEvent> eventType : List.of(
                InventoryClickEvent.class,
                InventoryPreClickEvent.class,
                InventoryOpenEvent.class,
                InventoryCloseEvent.class,
                InventoryItemChangeEvent.class
        )) {
            listeners.put((Class<? super InventoryEvent>) eventType, new ArrayList<>());
            node.addListener(eventType, event -> listeners.get(eventType).forEach(consumer -> consumer.accept(event)));
        }
    }

    public static UIEventListener bind(Inventory inventory) {
        UIEventListener eventListener = new UIEventListener(inventory);
        eventListener.attach();
        return eventListener;
    }

    public void attach() {
        MinecraftServer.getGlobalEventHandler().addChild(node);
    }

    public void detach() {
        MinecraftServer.getGlobalEventHandler().removeChild(node);
    }

    public <T extends InventoryEvent> UIListenerReference onEvent(Class<T> eventType, Consumer<T> consumer) {
        Consumer<? super InventoryEvent> eventConsumer = event -> consumer.accept(eventType.cast(event));
        listeners.get(eventType).add(eventConsumer);
        return new UIListenerReference(this, eventType, eventConsumer);
    }

    public UIListenerReference onClick(Consumer<InventoryClickEvent> consumer) {
        return onEvent(InventoryClickEvent.class, consumer);
    }

    public UIListenerReference onPreClick(Consumer<InventoryPreClickEvent> consumer) {
        return onEvent(InventoryPreClickEvent.class, consumer);
    }

    public UIListenerReference onOpen(Consumer<InventoryOpenEvent> consumer) {
        return onEvent(InventoryOpenEvent.class, consumer);
    }

    public UIListenerReference onClose(Consumer<InventoryCloseEvent> consumer) {
        return onEvent(InventoryCloseEvent.class, consumer);
    }

    public UIListenerReference onItemChange(Consumer<InventoryItemChangeEvent> consumer) {
        return onEvent(InventoryItemChangeEvent.class, consumer);
    }
}
