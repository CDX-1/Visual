package rip.cdx.virtual.ui.component;

import lombok.Getter;
import rip.cdx.virtual.ui.virtual.Renderer;

import java.util.UUID;

@Getter
public abstract class UIComponent {
    private final UUID uuid = UUID.randomUUID();
    public abstract void render(Renderer renderer);
}
