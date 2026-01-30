package dev.hytalemodding.commands;

import java.util.HashMap;

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

import jakarta.annotation.Nonnull;
import lombok.NonNull;



public class ItemcmdCommand extends AbstractPlayerCommand {
    
    public HashMap<String, String> listItemCmd = new HashMap<>();

    private final RequiredArg<String> cmd;
    private final FlagArg debug;

    public ItemcmdCommand(String name, String description) {
        super(name, description);
        this.cmd = withRequiredArg("cmd", "the command in quotations ex. '/tp 0 0 0' ", ArgTypes.STRING);
        this.debug = withFlagArg("debug", "debug toggle");
        addAliases("ic" , "icmd");
    }

    @NonNull
    @Override
    protected void execute(@Nonnull CommandContext context, Store<EntityStore> store, Ref<EntityStore> ref,PlayerRef pr, World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        String itemId = player.getInventory().getActiveHotbarItem().getItemId();
        String command = cmd.get(context);
        listItemCmd.put(itemId , command);
        context.sendMessage(Message.raw("Item: " + itemId + ", Cmd: " + command));
    }

}