package rip.cdx.virtual;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import rip.cdx.virtual.components.Button;
import rip.cdx.virtual.components.Pagination;
import rip.cdx.virtual.ui.UI;
import rip.cdx.virtual.ui.UIViewer;

import java.util.ArrayList;
import java.util.List;

public class VisualTest {
    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instance = instanceManager.createInstanceContainer();

        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        instance.setChunkSupplier(LightingChunk::new);

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            Player player = event.getPlayer();
            event.setSpawningInstance(instance);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(ItemStack.builder(Material.fromId(i + 1)).build());
        }

        globalEventHandler.addListener(PlayerChatEvent.class, event -> {
            UIViewer viewer = new UI(6, "My UI")
                    .addComponents(
                            new Pagination(27, items)
                                    .onClick(e -> e.setCancelled(true)),
                            new Pagination.PreviousButton(
                                    29,
                                    ItemStack.builder(Material.ENDER_PEARL).build()
                            ),
                            new Pagination.NextButton(
                                    33,
                                    ItemStack.builder(Material.ARROW).build()
                            ),
                            new Button(ItemStack.builder(Material.DIAMOND_SWORD).build(), e -> e.getPlayer().sendMessage("hi"))
                    )
                    .createViewer();
            viewer.show(event.getPlayer());
        });

        minecraftServer.start("0.0.0.0", 25565);
    }
}
