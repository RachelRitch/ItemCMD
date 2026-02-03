package dev.hytalemodding.commands.subcommands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import jakarta.annotation.Nonnull;
import lombok.NonNull;

public class AreaCmdDelete extends AbstractPlayerCommand {
    public AreaCmdDelete(){
        super("delete","delete a command for a area");
    }
    @NonNull
    @Override
    public void execute(@Nonnull CommandContext context, Store<EntityStore> store, Ref<EntityStore> ref,PlayerRef pr, World world){

    }
}
