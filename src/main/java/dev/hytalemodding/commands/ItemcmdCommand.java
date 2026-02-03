package dev.hytalemodding.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import jakarta.annotation.Nonnull;
import lombok.NonNull;



public class ItemcmdCommand extends AbstractPlayerCommand {


    public ItemcmdCommand(String name, String description) {
        super(name, description);
        addAliases("ic" , "icmd");
    }

    @NonNull
    @Override
    protected void execute(@Nonnull CommandContext context, Store<EntityStore> store, Ref<EntityStore> ref,PlayerRef pr, World world) {

    }

}