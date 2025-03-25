package rip.cdx.virtual.ui.state;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Setter
public class State<T> {
    private final UUID uuid = UUID.randomUUID();
    private @Nullable T defaultValue = null;

    public State(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public State() {}

    public T get(StateResolver resolver) {
        return resolver.getState(this);
    }

    public void set(StateResolver resolver, T value) {
        resolver.setState(this, value);
    }

    public void modify(StateResolver resolver, Function<T, T> function) {
        set(resolver, function.apply(get(resolver)));
    }
}
