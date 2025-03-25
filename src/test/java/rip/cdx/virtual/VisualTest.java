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
import rip.cdx.virtual.components.Counter;
import rip.cdx.virtual.components.Pagination;
import rip.cdx.virtual.ui.UI;
import rip.cdx.virtual.ui.UIViewer;

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

        globalEventHandler.addListener(PlayerChatEvent.class, event -> {
            UIViewer viewer = new UI(6, "My UI")
                    .addComponents(
                            new Counter(13),
                            new Pagination(5, List.of(
                                    ItemStack.builder(Material.ACACIA_BUTTON).build(),
                                    ItemStack.builder(Material.BIRCH_DOOR).build(),
                                    ItemStack.builder(Material.OAK_FENCE_GATE).build(),
                                    ItemStack.builder(Material.DARK_OAK_LEAVES).build(),
                                    ItemStack.builder(Material.WARPED_PLANKS).build(),
                                    ItemStack.builder(Material.ARROW).build(),
                                    ItemStack.builder(Material.ENDER_PEARL).build(),
                                    ItemStack.builder(Material.EGG).build(),
                                    ItemStack.builder(Material.SNOWBALL).build(),
                                    ItemStack.builder(Material.FIRE_CHARGE).build(),
                                    ItemStack.builder(Material.ENDER_DRAGON_SPAWN_EGG).build(),
                                    ItemStack.builder(Material.ZOMBIE_HEAD).build()
                            )).onClick(e -> e.setCancelled(true)),
                            new Pagination.PreviousButton(
                                    34,
                                    ItemStack.builder(Material.ENDER_PEARL).build()
                            ),
                            new Pagination.NextButton(
                                    45,
                                    ItemStack.builder(Material.ARROW).build()
                            )
                    )
                    .createViewer();
            viewer.show(event.getPlayer());
        });

        minecraftServer.start("0.0.0.0", 25565);
    }
}
