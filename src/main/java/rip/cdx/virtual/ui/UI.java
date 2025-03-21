package rip.cdx.virtual.ui;

import rip.cdx.virtual.exceptions.TooManyRowsException;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.virtual.Virtual;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class UI {
    private static final int MAX_ROWS = 6;

    private final Component title;
    private final int rows;
    private final Inventory inventory;
    private final UIEventListener listener;
    private final Virtual virtual;
    private final List<UIComponent> components = new ArrayList<>();

    public UI(Component title, int rows) {
        if (rows > MAX_ROWS || rows <= 0) throw new TooManyRowsException(MAX_ROWS);

        this.title = title;
        this.rows = rows;
        this.inventory = new Inventory(
                rowsToInventoryType(rows),
                title
        );
        listener = UIEventListener.bind(inventory);
        virtual = new Virtual(inventory, this);
    }

    public UI(String title, int rows) {
        this(Component.text(title), rows);
    }

    public void show(Player... players) {
        if (!virtual.hasRendered()) virtual.render();
        for (Player player : players) {
            player.openInventory(inventory);
        }
    }

    public UI addComponents(UIComponent... components) {
        this.components.addAll(Arrays.stream(components).toList());
        return this;
    }

    private InventoryType rowsToInventoryType(int rows) {
        return switch (rows) {
            case 2 -> InventoryType.CHEST_2_ROW;
            case 3 -> InventoryType.CHEST_3_ROW;
            case 4 -> InventoryType.CHEST_4_ROW;
            case 5 -> InventoryType.CHEST_5_ROW;
            case 6 -> InventoryType.CHEST_6_ROW;
            default -> InventoryType.CHEST_1_ROW;
        };
    }
}
