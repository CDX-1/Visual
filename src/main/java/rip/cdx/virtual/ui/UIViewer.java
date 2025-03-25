package rip.cdx.virtual.ui;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import rip.cdx.virtual.ui.component.UIComponent;
import rip.cdx.virtual.ui.rendering.Renderer;

import java.util.List;

public class UIViewer {
    private final Inventory inventory;
    private final Renderer renderer;
    private boolean hasRendered = false;

    public UIViewer(int rows, Component title, List<UIComponent> components) {
        this.inventory = new Inventory(getInventoryTypeFromRows(rows), title);
        this.renderer = new Renderer(components, inventory);
    }

    public void show(Player... players) {
        if (!hasRendered) {
            renderer.render();
            hasRendered = true;
        }
        for (Player player : players) {
            player.openInventory(inventory);
        }
    }

    private InventoryType getInventoryTypeFromRows(int rows) {
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
