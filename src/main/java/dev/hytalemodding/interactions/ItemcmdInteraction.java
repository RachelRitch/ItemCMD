package dev.hytalemodding.interactions;

import javax.annotation.Nonnull;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;




public class ItemcmdInteraction extends SimpleInstantInteraction {
    protected String command;
    protected boolean debug;

    public static final BuilderCodec<ItemcmdInteraction> CODEC = BuilderCodec.builder(ItemcmdInteraction.class, ItemcmdInteraction::new, SimpleInstantInteraction.ABSTRACT_CODEC)                .documentation("execute cmd on player with item!")
                .append(new KeyedCodec<>("Command", Codec.STRING),
                        (executeCommandInteraction, o) -> executeCommandInteraction.command=(String) o,
                        (executeCommandInteraction) -> executeCommandInteraction.command)
                .documentation("Command that will be executed when used placeholder: {player}")
                .add()
                .append(new KeyedCodec<Boolean>("EnableDebug", Codec.BOOLEAN),
                    (debugInteraction, o) -> debugInteraction.debug=(boolean) o,
                    (debugInteraction) -> debugInteraction.debug)
                .documentation("enable and disable debug infomation for when using a item")
                .add()
                .build();

    public HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();


    @Override
    protected void firstRun(@Nonnull InteractionType it,@Nonnull InteractionContext ic,@Nonnull CooldownHandler cooldown){
        CommandBuffer<EntityStore> commandBuffer = ic.getCommandBuffer();
        LOGGER = HytaleLogger.get("<ItemCMD>");
        if (commandBuffer == null && debug){
            ic.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("CommandBuffer is null");
            return;
        }

        World world = commandBuffer.getExternalData().getWorld();
        Store<EntityStore> store = commandBuffer.getExternalData().getStore();
        Ref<EntityStore> ref = ic.getEntity();
        Player player = commandBuffer.getComponent(ref, Player.getComponentType());
        if (player == null && debug){
            ic.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("player is null");
            return;
        }

        ItemStack itemstack = ic.getHeldItem();
          if (itemstack == null && debug){
            ic.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("itemstack is null");
            return;
        }

        String resolved = command.replace("{player}", player.getDisplayName());


        if (command == null){
            if(debug){
                player.sendMessage(Message.raw("No command given!"));
            }
            return;
        }
        if (resolved != null){
            if (debug){
                player.sendMessage(Message.raw("You have used the " + ic.getHeldItem().getItemId() + " with: " + resolved));
            }
            CommandManager.get().handleCommand(player, resolved);
        } else if (debug) {
            player.sendMessage(Message.raw("Item not Configured!"));
        }



    }

}
