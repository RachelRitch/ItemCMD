package dev.hytalemodding;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.orbisguard.api.OrbisGuardAPI;

import dev.hytalemodding.commands.AreaCmdCommand;
import dev.hytalemodding.commands.ItemcmdCommand;
import dev.hytalemodding.config.AreaCmdConfig;
import dev.hytalemodding.events.ItemcmdEvent;
import dev.hytalemodding.interactions.ItemcmdInteraction;
import dev.hytalemodding.interactions.blockcmdInteraction;
import dev.hytalemodding.systems.AreaCmdSystem;


public class ItemcmdPlugin extends JavaPlugin {

    private final Config<AreaCmdConfig> config;

    public ItemcmdPlugin(@Nonnull JavaPluginInit init) {
        super(init);

        this.config = this.withConfig("AreaCmdConfig", AreaCmdConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();
        //var
        OrbisGuardAPI api = OrbisGuardAPI.getInstance();
        //commands
        this.getCommandRegistry().registerCommand(new ItemcmdCommand("itemcmd", "A command that allow you to create items that run commands!!"));
        this.getCommandRegistry().registerCommand(new AreaCmdCommand("areacmd","A command that allow areas to run commands!!", this.config));
        //events
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ItemcmdEvent::onPlayerReady);

    
        //Codec for interaction
        this.getCodecRegistry(Interaction.CODEC).register(
            "ItemCMD_itemCommand_Interaction",
            ItemcmdInteraction.class,
            ItemcmdInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register(
            "ItemCMD_blockCommand_Interaction",
            blockcmdInteraction.class,
            blockcmdInteraction.CODEC);
        //Systems
        AreaCmdSystem areaSystem = new AreaCmdSystem();
        if (api != null) this.getEntityStoreRegistry().registerSystem(new AreaCmdSystem());
        config.save();

    

    }
    

}