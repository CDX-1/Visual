package rip.cdx.virtual;

import net.kyori.adventure.text.Component;
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
import rip.cdx.virtual.components.Counter;
import rip.cdx.virtual.components.Item;
import rip.cdx.virtual.components.Toggle;
import rip.cdx.virtual.ui.UI;

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
            UI ui = new UI("My UI", 6)
                    .addComponents(
                            new Counter(Material.DIRT),
                            new Button(
                                    ItemStack.builder(Material.DARK_OAK_BUTTON)
                                            .customName(Component.text("Suicide Button"))
                                            .build(),
                                    virtualEvent -> virtualEvent.getEvent().getPlayer().kill()
                            ),
                            new Item(
                                    ItemStack.builder(Material.ACACIA_HANGING_SIGN)
                                            .customName(Component.text("This is a cool item"))
                                            .lore(
                                                    Component.text("With some fancy lore"),
                                                    Component.text("and some more lore")
                                            )
                                            .build(),
                                    false
                            ),
                            new Toggle(
                                    ItemStack.builder(Material.GREEN_STAINED_GLASS_PANE)
                                            .customName(Component.text("On"))
                                            .build(),
                                    ItemStack.builder(Material.RED_STAINED_GLASS_PANE)
                                            .customName(Component.text("Off"))
                                            .build()
                            )
                    );
            ui.show(event.getPlayer());
        });

        minecraftServer.start("0.0.0.0", 25565);
    }
}
