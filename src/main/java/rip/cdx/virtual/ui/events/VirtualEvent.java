package rip.cdx.virtual.ui.events;

import rip.cdx.virtual.ui.virtual.Virtual;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VirtualEvent<T> {
    private final T event;
    private final Virtual virtual;
}
