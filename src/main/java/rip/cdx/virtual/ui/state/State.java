package rip.cdx.virtual.ui.state;

import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class State<T> {
    @Getter
    private final UUID uuid = UUID.randomUUID();
    private final AtomicReference<T> reference = new AtomicReference<>();

    public State(T value) {
        reference.set(value);
    }

    public State() {}

    public void set(T value) {
        reference.set(value);
    }

    public T get() {
        return reference.get();
    }
}
