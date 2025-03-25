package rip.cdx.virtual.ui.state;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import rip.cdx.virtual.ui.events.StateEvent;
import rip.cdx.virtual.ui.rendering.Renderer;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Setter
public class State<T> {
    private final UUID uuid = UUID.randomUUID();
    private @Nullable T defaultValue = null;

    public State(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public State() {}

    public T get(Renderer renderer) {
        return renderer.getState(this);
    }

    public void set(Renderer renderer, T value) {
        renderer.setState(this, value);
    }

    public void modify(Renderer renderer, Function<T, T> function) {
        set(renderer, function.apply(get(renderer)));
    }

    public T get(StateEvent event) {
        return event.getState(this);
    }

    public void set(StateEvent event, T value) {
        event.setState(this, value);
    }

    public void modify(StateEvent event, Function<T, T> function) {
        set(event, function.apply(get(event)));
    }
}
