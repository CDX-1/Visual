package rip.cdx.virtual.ui.virtual;

import rip.cdx.virtual.ui.state.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class SubRenderer {
    private final Virtual virtual;
    private final Renderer renderer;
    private final List<State<?>> stateReferences = new ArrayList<>();

    public <T> T getState(State<T> state) {
        if (!stateReferences.contains(state))
            stateReferences.add(state);
        return virtual.getState(state);
    }

    public <T> void setState(State<T> state, T value) {
        virtual.setState(state, value);
    }
}
