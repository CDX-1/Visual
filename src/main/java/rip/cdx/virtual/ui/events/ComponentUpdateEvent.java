package rip.cdx.virtual.ui.events;

import lombok.RequiredArgsConstructor;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.exceptions.SlotReservedException;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.rendering.Renderer;
import rip.cdx.virtual.ui.rendering.VirtualSlot;
import rip.cdx.virtual.ui.state.State;
import rip.cdx.virtual.ui.state.StateResolver;
import rip.cdx.virtual.utils.Canceller;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class ComponentUpdateEvent implements StateResolver {
    private final Renderer renderer;
    private final UIComponent component;

    public void setItem(int slot, ItemStack item) {
        @Nullable VirtualSlot currentHolder = renderer.getSlot(slot);
        if (currentHolder != null && !currentHolder.component().equals(component))
            throw new SlotReservedException(currentHolder.component(), component);
        renderer.setSlot(component, slot, item);
    }

    public List<Integer> getReservedSlots() {
        return renderer.getSlotsByComponent(component);
    }

    @Override
    public <T> T getState(State<T> state) {
        T value = state.get(renderer);
        AtomicReference<Canceller> canceller = new AtomicReference<>();
        canceller.set(
                renderer.onStateChange(state, _ -> {
                    renderer.render();
                    canceller.get().cancel();
                })
        );
        return value;
    }

    @Override
    public <T> void setState(State<T> state, T value) {
        state.set(renderer, value);
    }
}
