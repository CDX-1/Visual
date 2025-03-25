package rip.cdx.virtual.ui;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import rip.cdx.virtual.ui.component.UIComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class UI {
    private final int rows;
    private final Component title;
    private final List<UIComponent> components = new ArrayList<>();

    public UI(int rows, Component title) {
        this.rows = rows;
        this.title = title;
    }

    public UI(int rows, String title) {
        this(rows, Component.text(title));
    }

    public UI addComponent(UIComponent component) {
        components.add(component);
        return this;
    }

    public UI addComponents(UIComponent... components) {
        this.components.addAll(Arrays.stream(components).toList());
        return this;
    }

    public UIViewer createViewer() {
        return new UIViewer(rows, title, new ArrayList<>(components));
    }
}
