package dev.hytalemodding.interactions;


import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class blockcmdInteraction extends SimpleBlockInteraction {
    protected String command;
    protected boolean debug;

    public static final BuilderCodec<blockcmdInteraction> CODEC = BuilderCodec.builder(blockcmdInteraction.class, blockcmdInteraction::new, SimpleInstantInteraction.ABSTRACT_CODEC)                .documentation("execute cmd on player with item!")
                .append(new KeyedCodec<>("Command", Codec.STRING),
                        (executeCommandInteraction, o) -> executeCommandInteraction.command=(String) o,
                        (executeCommandInteraction) -> executeCommandInteraction.command)
                .documentation("Command that will be executed when used! placeholder: {player}")
                .add()
                .append(new KeyedCodec<Boolean>("EnableDebug", Codec.BOOLEAN),
                    (debugInteraction, o) -> debugInteraction.debug=(boolean) o,
                    (debugInteraction) -> debugInteraction.debug)
                .documentation("enable and disable debug infomation for when using a item")
                .add()
                .build();

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
        Ref<EntityStore> ref = interactionContext.getEntity();
        Player player = commandBuffer.getComponent(ref, Player.getComponentType());
        if (player == null){
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("player is null");
            return;
        }

        if (command == null){
            if(debug){
                player.sendMessage(Message.raw("No command given!"));
            }
            return;
        }
        String resolved = command;
        if (command.contains("{player}")){
         resolved = resolved.replace("{player}", player.getDisplayName());
        }
        if (resolved != null){
            if (debug){
                player.sendMessage(Message.raw("You have used the " + interactionContext.getHeldItem().getItemId() + " with: " + resolved));
            }
            CommandManager.get().handleCommand(player, resolved);
        } else if (debug) {
            player.sendMessage(Message.raw("block not Configured!"));
        }
   }

    @Override
    protected void simulateInteractWithBlock(InteractionType it, InteractionContext ic, ItemStack is, World world, Vector3i vctr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
