package dev.hytalemodding.commands.subcommands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.FlagArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;

import dev.hytalemodding.config.AreaCmdConfig;
import jakarta.annotation.Nonnull;
import lombok.NonNull;

public class AreaCmdDelete extends AbstractPlayerCommand {
    private final Config<AreaCmdConfig> config;
    private final RequiredArg<String> region;
    private final FlagArg debug;


    public AreaCmdDelete(Config<AreaCmdConfig> config){
        super("delete","delete a cmd from an area");
        this.region = this.withRequiredArg("region", "the name of the area you want to remove the command from", ArgTypes.STRING);
        this.debug = this.withFlagArg("debug", "enable the debug option");
        this.config = config;
    }

    @NonNull
    @Override
    public void execute(@Nonnull CommandContext context, Store<EntityStore> store, Ref<EntityStore> ref,PlayerRef pr, World world){
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        if (debug.get(context)) {
            player.sendMessage(Message.raw("Region: " + region.get(context)));
            return;
        }
        AreaCmdConfig settings = config.get();
        settings.deleteMap(region.get(context));
        config.save();

    }
}
