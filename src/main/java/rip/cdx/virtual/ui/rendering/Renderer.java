package rip.cdx.virtual.ui.rendering;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.*;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.exceptions.InventoryOverflowException;
import rip.cdx.virtual.exceptions.SlotReservedException;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.events.component.ComponentReservationEvent;
import rip.cdx.virtual.ui.events.component.ComponentUpdateEvent;
import rip.cdx.virtual.ui.state.State;
import rip.cdx.virtual.utils.Canceller;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Renderer {
    @Getter
    private final UUID uuid = UUID.randomUUID();
    private final List<UIComponent> components;
    private final Inventory inventory;
    private final Map<Integer, VirtualSlot> virtualSlots = new HashMap<>();
    private final int size;
    private final Map<UUID, Object> states = new HashMap<>();
    private final Map<UUID, List<Consumer<Object>>> stateListeners = new HashMap<>();

    public Renderer(List<UIComponent> components, Inventory inventory) {
        this.components = components;
        this.inventory = inventory;
        this.size = inventory.getSize();

        for (UIComponent component : components) {
            ComponentReservationEvent event = new ComponentReservationEvent(this);
            component.onReservation(event);
            List<Integer> reservedSlots = event.getReservedSlots();
            for (int reservedSlot : reservedSlots) {
                virtualSlots.put(reservedSlot, new VirtualSlot(component, ItemStack.AIR));
            }
        }

        EventNode<InventoryEvent> node = EventNode.type(uuid.toString(), EventFilter.INVENTORY, (event, inv) -> inventory == inv);
        MinecraftServer.getGlobalEventHandler().addChild(node);

        node.addListener(InventoryClickEvent.class, event -> {
            VirtualSlot virtualSlot = virtualSlots.get(event.getSlot());
            @Nullable UIComponent component = null;
            if (virtualSlot != null) {
                component = virtualSlot.component();
            }
            for (UIComponent uiComponent : components) {
                uiComponent.onClick(component, this, event);
            }
        });

        node.addListener(InventoryPreClickEvent.class, event -> {
            VirtualSlot virtualSlot = virtualSlots.get(event.getSlot());
            @Nullable UIComponent component = null;
            if (virtualSlot != null) {
                component = virtualSlot.component();
            }
            for (UIComponent uiComponent : components) {
                uiComponent.onPreClick(component, this, event);
            }
        });

        node.addListener(InventoryItemChangeEvent.class, event -> {
            VirtualSlot virtualSlot = virtualSlots.get(event.getSlot());
            @Nullable UIComponent component = null;
            if (virtualSlot != null) {
                component = virtualSlot.component();
            }
            for (UIComponent uiComponent : components) {
                uiComponent.onItemChange(component, this, event);
            }
        });

        node.addListener(InventoryOpenEvent.class, event -> {
            for (UIComponent uiComponent : components) {
                uiComponent.onOpen(this, event);
            }
        });

        node.addListener(InventoryCloseEvent.class, event -> {
            for (UIComponent uiComponent : components) {
                uiComponent.onClose(this, event);
            }
        });
    }

    public void update() {
        for (UIComponent component : components) {
            updateComponent(component);
        }
    }

    public void render() {
        update();
        for (Map.Entry<Integer, VirtualSlot> slot : virtualSlots.entrySet()) {
            inventory.setItemStack(slot.getKey(), slot.getValue().item());
        }
    }

    @Nullable
    public VirtualSlot getSlot(int slot) {
        return virtualSlots.get(slot);
    }

    public void updateComponent(UIComponent component) {
        ComponentUpdateEvent event = new ComponentUpdateEvent(this, component);
        component.onUpdate(event);
    }

    public void setSlot(UIComponent component, int slot, ItemStack item) {
        VirtualSlot currentHolder = virtualSlots.get(slot);
        if (currentHolder != null && !currentHolder.component().equals(component))
            throw new SlotReservedException(currentHolder.component(), component);
        virtualSlots.put(slot, new VirtualSlot(component, item));
    }

    public Map<Integer, VirtualSlot> getAllSlotsByComponent(UIComponent component) {
        return virtualSlots.entrySet().stream()
                .filter(entry -> entry.getValue().component().equals(component))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Integer> getSlotsByComponent(UIComponent component) {
        return virtualSlots.entrySet().stream()
                .filter(entry -> entry.getValue().component().equals(component))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<VirtualSlot> getVirtualSlotsByComponent(UIComponent component) {
        return virtualSlots.values().stream()
                .filter(virtualSlot -> virtualSlot.component().equals(component))
                .collect(Collectors.toList());
    }

    public List<UIComponent> getComponentsByClass(Class<? extends UIComponent> clazz) {
        return virtualSlots.values().stream()
                .map(VirtualSlot::component)
                .filter(component -> component.getClass().equals(clazz))
                .collect(Collectors.toList());
    }

    public <T> T getState(State<T> state) {
        return (T) states.getOrDefault(state.getUuid(), state.getDefaultValue());
    }

    public <T> void setState(State<T> state, T value) {
        states.put(state.getUuid(), value);
        List<Consumer<Object>> listenersCopy = new ArrayList<>(stateListeners.getOrDefault(state.getUuid(), List.of()));
        listenersCopy.forEach(consumer -> consumer.accept(value));
    }

    public <T> Canceller onStateChange(State<T> state, Consumer<T> consumer) {
        List<Consumer<Object>> listeners = stateListeners.computeIfAbsent(state.getUuid(), k -> new ArrayList<>());
        listeners.add((Consumer<Object>) consumer);
        return () -> {
            listeners.remove(consumer);
        };
    }

    public int nextSlot() {
        for (int i = 0; i < size; i++) {
            if (!virtualSlots.containsKey(i)) {
                return i;
            }
        }
        throw new InventoryOverflowException("Failed to find next slot in inventory");
    }
}
