package rip.cdx.virtual.ui.state;

public interface StateResolver {
    <T> T getState(State<T> state);
    <T> void setState(State<T> state, T value);
}
