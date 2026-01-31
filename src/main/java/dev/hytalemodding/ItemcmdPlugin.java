package dev.hytalemodding;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import dev.hytalemodding.events.ItemcmdEvent;
import dev.hytalemodding.interactions.ItemcmdInteraction;
import dev.hytalemodding.interactions.blockcmdInteraction;

public class ItemcmdPlugin extends JavaPlugin {

    public ItemcmdPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        //this.getCommandRegistry().registerCommand(new ItemcmdCommand("itemcmd", "A command that allow you to create items that run commands!!"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ItemcmdEvent::onPlayerReady);
        this.getCodecRegistry(Interaction.CODEC).register(
            "ItemCMD_itemCommand_Interaction",
            ItemcmdInteraction.class,
            ItemcmdInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register(
            "ItemCMD_blockCommand_Interaction",
            blockcmdInteraction.class,
            blockcmdInteraction.CODEC);
    }
}