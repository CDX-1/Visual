package rip.cdx.virtual.ui.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.ui.rendering.Renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ComponentInitializeEvent {
    private final Renderer renderer;
    @Getter
    private final List<Integer> reservedSlots = new ArrayList<>();

    public void reserve(Integer... slots) {
        reservedSlots.addAll(Arrays.stream(slots).toList());
    }

    public int nextSlot() {
        return renderer.nextSlot();
    }

    public int nextSlotIfNull(@Nullable Integer slot) {
        return Objects.requireNonNullElse(slot, nextSlot());
    }
}
