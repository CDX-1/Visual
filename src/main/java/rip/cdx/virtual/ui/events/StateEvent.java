package rip.cdx.virtual.ui.events;

import rip.cdx.virtual.ui.state.State;

public interface StateEvent {
    <T> T getState(State<T> state);
    <T> void setState(State<T> state, T value);
}
