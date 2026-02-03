package dev.hytalemodding;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.orbisguard.api.OrbisGuardAPI;

import dev.hytalemodding.config.AreaCmdConfig;
import dev.hytalemodding.interactions.ItemcmdInteraction;
import dev.hytalemodding.interactions.blockcmdInteraction;
import dev.hytalemodding.systems.AreaCmdSystem;

public class ItemcmdPlugin extends JavaPlugin {

    private final Config<AreaCmdConfig> config;

    public ItemcmdPlugin(@Nonnull JavaPluginInit init) {
        super(init);

        //Codec for interaction
        this.getCodecRegistry(Interaction.CODEC).register(
            "ItemCMD_itemCommand_Interaction",
            ItemcmdInteraction.class,
            ItemcmdInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register(
            "ItemCMD_blockCommand_Interaction",
            blockcmdInteraction.class,
            blockcmdInteraction.CODEC);
        
        this.config = this.withConfig("AreaCmdConfig", AreaCmdConfig.CODEC);
    }

    @Override
    protected void setup() {
        //setup commands
        OrbisGuardAPI api = OrbisGuardAPI.getInstance();

        //commands
        //this.getCommandRegistry().registerCommand(new ItemcmdCommand("itemcmd", "A command that allow you to create items that run commands!!"));

        //events
        //this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ItemcmdEvent::onPlayerReady);

        //Systems
        if (api != null) this.getEntityStoreRegistry().registerSystem(new AreaCmdSystem());
        config.save();
    }

}