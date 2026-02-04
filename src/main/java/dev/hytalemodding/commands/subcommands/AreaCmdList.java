package dev.hytalemodding.commands.subcommands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;

import dev.hytalemodding.config.AreaCmdConfig;
import jakarta.annotation.Nonnull;

public class AreaCmdList extends AbstractPlayerCommand {
    Config<AreaCmdConfig> config;

    public AreaCmdList(Config<AreaCmdConfig> config){
        super("list", "list all the area with commands!");
        this.config = config;

    }

    @Override
    protected void execute(@Nonnull CommandContext context,@Nonnull Store<EntityStore> store,@Nonnull Ref<EntityStore> ref,@Nonnull PlayerRef pr,@Nonnull World world){
        Player player = store.getComponent(ref, Player.getComponentType());
         AreaCmdConfig settings = config.get();
        player.sendMessage(Message.raw("---- AreaCMD list ----"));
        settings.getMap().forEach((region, innerMap) -> {
            player.sendMessage(Message.raw("Region: " + region));
            innerMap.forEach((command, active) -> {
                player.sendMessage(Message.raw(" -command: " + command));
                player.sendMessage(Message.raw(" -type: " + active));
            });
        });
    }
}
