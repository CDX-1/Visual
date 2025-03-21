package rip.cdx.virtual.ui.virtual;

import net.minestom.server.tag.Tag;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.state.State;
import lombok.RequiredArgsConstructor;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class Renderer {
    private final Virtual virtual;
    private final UIComponent component;

    public void setItem(int slot, ItemStack item) {
        virtual.setItem(slot, item);
    }

    public void setItem(int slot, Function<SubRenderer, ItemStack> itemRenderer) {
        SubRenderer renderer = new SubRenderer(virtual, this);
        setItem(slot, attachComponentIdentifier(itemRenderer.apply(renderer)));
        List<State<?>> states = new ArrayList<>(renderer.getStateReferences());

        for (State<?> state : states) {
            virtual.onStateChange(state, value -> {
                setItem(slot, attachComponentIdentifier(itemRenderer.apply(renderer)));
                states.addAll(renderer.getStateReferences());
            });
        }
    }

    private ItemStack attachComponentIdentifier(ItemStack item) {
        return item.withTag(Tag.UUID("component-id"), component.getUuid());
    }

    public Integer nextSlot() {
        return virtual.nextSlot();
    }
}
