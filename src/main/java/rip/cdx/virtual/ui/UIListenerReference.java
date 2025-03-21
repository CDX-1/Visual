package rip.cdx.virtual.ui;

import lombok.RequiredArgsConstructor;
import net.minestom.server.event.trait.InventoryEvent;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class UIListenerReference {
    private final UIEventListener eventListener;
    private final Class<? extends InventoryEvent> eventClass;
    private final Consumer<? super InventoryEvent> eventConsumer;

    public void unsubscribe() {
        eventListener.getListeners().get(eventClass).remove(eventConsumer);
    }
}
