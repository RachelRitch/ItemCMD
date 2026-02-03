package dev.hytalemodding.commands;

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
import com.orbisguard.api.OrbisGuardAPI;

import dev.hytalemodding.commands.subcommands.AreaCmdCreate;
import dev.hytalemodding.commands.subcommands.AreaCmdDelete;
import dev.hytalemodding.commands.subcommands.AreaCmdList;
import dev.hytalemodding.config.AreaCmdConfig;
import jakarta.annotation.Nonnull;
import lombok.NonNull;




public class AreaCmdCommand extends AbstractPlayerCommand  {
    private final Config<AreaCmdConfig> config;
    private final OrbisGuardAPI api = OrbisGuardAPI.getInstance();
    public AreaCmdCommand(String name, String description, Config<AreaCmdConfig> config){
        super(name, description);
        this.config = config;
        if (api != null){
            addSubCommand(new AreaCmdDelete(this.config));
            addSubCommand(new AreaCmdCreate(this.config));
            addSubCommand(new AreaCmdList(this.config));
        }
        addAliases("ac");
    }

    @NonNull
    @Override
    protected void execute(@Nonnull CommandContext context, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef pr, World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (api == null) {
            player.sendMessage(Message.raw("Need to install orbisguard 0.7.0+"));
        }
    }

}
