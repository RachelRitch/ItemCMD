package dev.hytalemodding.commands.subcommands;

import java.util.HashMap;
import java.util.Map;

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

public class AreaCmdCreate extends AbstractPlayerCommand  {
    private final Config<AreaCmdConfig> config;
    private final RequiredArg<String> region;
    private final RequiredArg<String> command;
    private final RequiredArg<String> active;
    private final FlagArg debug;

    public AreaCmdCreate(Config<AreaCmdConfig> config){
        super("create","create a cmd for a area");
        this.region = this.withRequiredArg("region", "the name of the area you want to add the command too", ArgTypes.STRING);
        this.command = this.withRequiredArg("command", "the command you want to use in quotes", ArgTypes.STRING);
        this.active = this.withRequiredArg("type", "when the command is activated", ArgTypes.STRING);
        this.debug = this.withFlagArg("debug", "enable debug option");
        this.config = config;
    }

    @NonNull
    @Override
    public void execute(@Nonnull CommandContext context, Store<EntityStore> store, Ref<EntityStore> ref,PlayerRef pr, World world){
        AreaCmdConfig settings = config.get();
        Map<String, Map<String, String>> areaCMD = settings.getMap();
        Map<String, String> innerAreaCmd = new HashMap<>();
        String commandcmd = command.get(context).substring(1, command.get(context).length() - 1);
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        if (debug.get(context)) {
            player.sendMessage(Message.raw("Region: " + region.get(context) + ", command: " + command.get(context))); 
            return;
        }
        innerAreaCmd.put(commandcmd, active.get(context));
        areaCMD.put(region.get(context), innerAreaCmd);
        settings.setMap(areaCMD);
        config.save();

    }
}
