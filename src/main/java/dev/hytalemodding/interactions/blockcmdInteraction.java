package dev.hytalemodding.interactions;


import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;




public class blockcmdInteraction extends SimpleBlockInteraction {
    // Codec for adding new components to items json file
    public static final BuilderCodec<blockcmdInteraction> CODEC = BuilderCodec.builder(blockcmdInteraction.class, blockcmdInteraction::new, SimpleInstantInteraction.ABSTRACT_CODEC)                .documentation("execute cmd on player with item!")
                .append(new KeyedCodec<>("Command", Codec.STRING),
                        (executeCommandInteraction, o) -> executeCommandInteraction.command=(String) o,
                        (executeCommandInteraction) -> executeCommandInteraction.command)
                .documentation("Command that will be executed when used! placeholder: {player}, {allplayers}")
                .add() //add command slot to asset node json
                .append(new KeyedCodec<Boolean>("EnableDebug", Codec.BOOLEAN),
                    (debugInteraction, o) -> debugInteraction.debug=(boolean) o,
                    (debugInteraction) -> debugInteraction.debug)
                .documentation("enable and disable debug infomation for when using a item")
                .add() //add debug command boolean to asset node json
                .build();

   //var
    public String command;
    protected boolean debug;

    public HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    protected void interactWithBlock(
      @Nonnull World world,
      @Nonnull CommandBuffer<EntityStore> commandBuffer,
      @Nonnull InteractionType interactionType,
      @Nonnull InteractionContext interactionContext,
      @Nullable ItemStack itemStack,
      @Nonnull Vector3i vector,
      @Nonnull CooldownHandler cooldownhandler
   ){
        LOGGER = HytaleLogger.get("<ItemCMD>");
        final PlayerRef player = world.getEntityStore().getStore().getComponent(interactionContext.getEntity(), PlayerRef.getComponentType()); //player interacted with block
        final var allPlayers =  world.getPlayerRefs(); //player list

        if (player == null){
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("player is null");
            return;
        }

        //get block id ex. Example_Block
        final String block = world.getBlockType(vector).getId();

        if (block == null){
            if(debug){
                player.sendMessage(Message.raw("No commandblock! ;)"));
            }
            return;
        }

        if (command == null){
            if(debug){
                player.sendMessage(Message.raw("No command given!"));
            }
            return;
        }

        final String resolved = command;
        @Nonnull String playerResolved = command;

        if (command.contains("{player}")){
          playerResolved = resolved.replace("{player}", player.getUsername());
        }
        //if used placeholder {allplayers} then sends cmd to each player online
        if (command.contains("{allplayers}")){
            if (resolved != null){
                allPlayers.forEach(playerRef -> {
                    String playersResolved = resolved.replace("{allplayers}", playerRef.getUsername());
                    if (debug){
                        player.sendMessage(Message.raw("You have used the block: " + block + " with: " + playersResolved));
                    }
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, playersResolved);
                });
            }
        }

        //if command exist then send command
        if (playerResolved != null){
            if (debug){
                player.sendMessage(Message.raw("You have used the block: " + block + " with: " + playerResolved));
            }
            CommandManager.get().handleCommand(ConsoleSender.INSTANCE, playerResolved);
        }
   }


    @Override
    protected void simulateInteractWithBlock(InteractionType it, InteractionContext ic,@Nullable ItemStack is, World world, Vector3i vctr) {
    }


}
